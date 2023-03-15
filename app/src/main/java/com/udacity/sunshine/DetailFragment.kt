package com.udacity.sunshine

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import com.udacity.sunshine.databinding.FragmentDetailBinding
import kotlin.math.roundToInt

private const val TAG = "DetailActivity-Truong"

class DetailFragment : Fragment() {

    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(layoutInflater)

        val text = Utility.getDay(args.position).split(",")
        binding.day.text = text[0]
        binding.date.text = text[1]
        binding.high.text = "${args.day.temp.max.roundToInt().toString()}\u00B0"
        binding.low.text = "${args.day.temp.min.roundToInt().toString()}\u00B0"
        val resource = Utility.getWeatherResource(args.day.weather[0].id)
        binding.image.setImageResource(resource)
        binding.forecast.text = args.day.weather[0].main
        binding.humidity.text = "Humidity: ${args.day.humidity.toString()} %"
        binding.pressure.text = "Pressure: ${args.day.pressure.toString()} hPa"
        binding.wind.text = "Wind: ${args.day.speed.roundToInt().toString()} km/h NW"

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
                inflater.inflate(R.menu.menu_detail, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.action_settings -> {
                        val intent = Intent(requireActivity(), SettingsActivity::class.java)
                        startActivity(intent)
                    }
                    R.id.action_map -> {
                        val provider = MenuItemCompat.getActionProvider(item) as ShareActionProvider
                        if (provider != null) {
                            provider.setShareIntent(doShare())
                        } else {
                            Log.w(TAG, "Share Action Provider is null")
                        }
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun doShare(): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        intent.type = "text/plain"
        // intent.putExtra(Intent.EXTRA_TEXT, forecast)
        return intent
    }
}