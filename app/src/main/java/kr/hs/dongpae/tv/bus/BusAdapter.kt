package kr.hs.dongpae.tv.bus

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.transitionseverywhere.ChangeText
import com.transitionseverywhere.TransitionManager
import kotlinx.android.synthetic.main.item_bus_indecator.view.*
import kotlinx.android.synthetic.main.item_bus_location.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.hs.dongpae.tv.JSManager
import kr.hs.dongpae.tv.R
import kr.hs.dongpae.tv.bus.BusData.Companion.LEFT
import kr.hs.dongpae.tv.bus.BusData.Companion.RIGHT

class BusAdapter(private val context: Context, private val bus: List<BusData>): RecyclerView.Adapter<BusAdapter.BusHolder>() {

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BusHolder(LayoutInflater.from(context).inflate(R.layout.item_bus_indecator_min, parent, false))
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BusHolder(LayoutInflater.from(context).inflate(R.layout.item_bus_indecator, parent, false))
    override fun onBindViewHolder(holder: BusHolder, position: Int) = holder.bind(bus[position])
    override fun getItemCount() = bus.size

    inner class BusHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(data: BusData) {
            with(itemView) {
                text_bus_number.text = data.name
                layout_bus_background.backgroundTintList = ColorStateList.valueOf(context.getColor(data.color))

                view_bus_left_f.backgroundTintList = ColorStateList.valueOf(context.getColor(data.color))
                view_bus_left_s.backgroundTintList = ColorStateList.valueOf(context.getColor(data.color))
                view_bus_right_f.backgroundTintList = ColorStateList.valueOf(context.getColor(data.color))
                view_bus_right_s.backgroundTintList = ColorStateList.valueOf(context.getColor(data.color))
            }

            CoroutineScope(Dispatchers.Main).launch {
                while (true) {
                    update(data)
                    delay(3000)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        private fun update(data: BusData) {
            with(itemView) {
                TransitionManager.beginDelayedTransition(layout_bus_rootview, ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN))

                data.getBusLocation(LEFT).forEachIndexed { index, locationData ->
                    when (index) {
                        0 -> {
                            view_bus_left_f.visibility = View.VISIBLE
                            text_bus_time_left.text = "${locationData.time}분"
                            view_bus_left_f.text_bus_location_name.text = locationData.name
                            view_bus_left_f.text_bus_location_name.isSelected = true
                        }
                        1 -> {
                            view_bus_left_s.visibility = View.VISIBLE
                            view_bus_left_s.text_bus_location_name.text = locationData.name
                            view_bus_left_s.text_bus_location_name.isSelected = true
                        }
                        else -> return@forEachIndexed
                    }
                }
                data.getBusLocation(RIGHT).forEachIndexed { index, locationData ->
                    when (index) {
                        0 -> {
                            view_bus_right_f.visibility = View.VISIBLE
                            text_bus_time_right.text = "${locationData.time}분"
                            view_bus_right_f.text_bus_location_name.text = locationData.name
                            view_bus_right_f.text_bus_location_name.isSelected = true
                        }
                        1 -> {
                            view_bus_right_s.visibility = View.VISIBLE
                            view_bus_right_s.text_bus_location_name.text = locationData.name
                            view_bus_right_s.text_bus_location_name.isSelected = true
                        }
                        else -> return@forEachIndexed
                    }
                }
            }
        }
    }
}