package com.udacity.sunshine

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.udacity.sunshine.databinding.FragmentMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "MainFragment-Truong"

class MainFragment : Fragment() {

    companion object {
        private const val API_KEY = "a0e4b2727858f8dc3bdc0428ef7e3712"
        private const val NUM_DAYS = 16
        private const val MODE_JSON = "json"
        private const val MODE_XML = "xml"
        private const val UNIT_METRIC = "metric"
        private const val UNIT_IMPERIAL = "imperial"
        private const val ZIPCODE_DEFAULT = "95131"
    }

    private lateinit var days: MutableList<Day>
    private lateinit var adapter: DaysAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(layoutInflater)

        days = mutableListOf<Day>()
        adapter = DaysAdapter(days, object : DaysAdapter.ClickListener {
            override fun onClick(position: Int) {
                Log.d(TAG, "clicked at $position")
                if (position != RecyclerView.NO_POSITION) {
                    val action = MainFragmentDirections.mainToDetail(days[position], position)
                    findNavController().navigate(action)
                }
            }
        })
        binding.recycler.adapter = adapter
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recycler.layoutManager = layoutManager
        val divider =
            DividerItemDecoration(binding.recycler.context, layoutManager.orientation)
        binding.recycler.addItemDecoration(divider)

        fetchWeather(ZIPCODE_DEFAULT, UNIT_IMPERIAL)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.action_refresh -> {
                        Log.d(TAG, "menu REFRESH")
                        val location = Utility.getPreference(
                            requireContext(),
                            R.string.pref_location_key,
                            R.string.pref_location_default
                        )
                        val units = Utility.getPreference(
                            requireContext(),
                            R.string.pref_units_key,
                            R.string.pref_units_imperial
                        )
                        Log.i(TAG, "location $location, $units: $units")
                        if (location != null && units != null) {
                            fetchWeather(location, units)
                        }
                    }
                    R.id.action_settings -> {
                        Log.d(TAG, "menu SETTINGS")
                        val intent = Intent(requireActivity(), SettingsActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.action_map -> {
                        Log.d(TAG, "menu MAP")
                        val location = Utility.getPreference(
                            requireContext(),
                            R.string.pref_location_key,
                            R.string.pref_location_default
                        )
                        val geo = Uri.parse("geo:0,0?")
                            .buildUpon()
                            .appendQueryParameter("q", location)
                            .build()
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = geo
                        if (intent.resolveActivity(requireActivity().packageManager) != null) {
                            startActivity(intent)
                        } else {
                            Log.w(TAG, "Couldn't call $location no receiving apps installed.")
                        }
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun fetchWeather(zipcode: String, units: String) {
        WeatherService.instance.fetchWeather(zipcode, units, NUM_DAYS, API_KEY).enqueue(object :
            Callback<Record> {
            override fun onResponse(call: Call<Record>, response: Response<Record>) {
                Log.i(TAG, "onResponse $response")
                if (response.body() == null) {
                    Log.w(TAG, "Did not receive valid response body from OpenWeatherMap API")
                } else {
                    days.clear()
                    days.addAll(response.body()!!.list)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Record>, t: Throwable) {
                Log.e(TAG, "onFailure $t")
            }
        })
    }
}