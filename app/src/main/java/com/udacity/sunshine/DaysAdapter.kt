package com.udacity.sunshine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.udacity.sunshine.databinding.ItemFutureBinding
import com.udacity.sunshine.databinding.ItemTodayBinding
import kotlin.math.roundToInt

class DaysAdapter(val days: List<Day>, val listener: ClickListener) :
    RecyclerView.Adapter<ViewHolder>() {
    interface ClickListener {
        fun onClick(position: Int)
    }

    companion object {
        const val EXTRA_DAY = "EXTRA_DAY"
        const val EXTRA_POSITION = "EXTRA_POSITION"
        const val VIEW_TODAY = 0
        const val VIEW_FUTURE = 1
    }

    inner class ViewHolderToday(val binding: ItemTodayBinding) : ViewHolder(binding.root) {
        fun bind(day: Day) {
            val text = Utility.getDay(adapterPosition)
            val jour = when (adapterPosition) {
                0 -> text
                else -> text.split(",")[0]
            }
            binding.day.text = jour
            val resource = Utility.getWeatherResource(day.weather[0].id)
            binding.image.setImageResource(resource)
            binding.forecast.text = day.weather[0].main
            binding.high.text = "${day.temp.max.roundToInt().toString()}\u00B0"
            binding.low.text = "${day.temp.min.roundToInt().toString()}\u00B0"

            itemView.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }
    }

    inner class ViewHolderFuture(val binding: ItemFutureBinding) : ViewHolder(binding.root) {
        fun bind(day: Day) {
            val text = Utility.getDay(adapterPosition)
            val jour = when (adapterPosition) {
                0 -> text
                else -> text.split(",")[0]
            }
            binding.day.text = jour
            val resource = Utility.getWeatherResource(day.weather[0].id)
            binding.image.setImageResource(resource)
            binding.forecast.text = day.weather[0].main
            binding.high.text = "${day.temp.max.roundToInt().toString()}\u00B0"
            binding.low.text = "${day.temp.min.roundToInt().toString()}\u00B0"

            itemView.setOnClickListener {
                listener.onClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TODAY) {
            val holder = ItemTodayBinding.inflate(inflater, parent, false)
            ViewHolderToday(holder)
        } else {
            val holder = ItemFutureBinding.inflate(inflater, parent, false)
            ViewHolderFuture(holder)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TODAY) {
            (holder as ViewHolderToday).bind(days[position])
        } else {
            (holder as ViewHolderFuture).bind(days[position])
        }
    }

    override fun getItemCount() = days.size

    override fun getItemViewType(position: Int) = if (position == 0) VIEW_TODAY else VIEW_FUTURE
}