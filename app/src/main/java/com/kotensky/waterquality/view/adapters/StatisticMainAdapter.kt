package com.kotensky.waterquality.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kotensky.waterquality.R
import com.kotensky.waterquality.interfaces.MainStatisticItemClickListener
import com.kotensky.waterquality.model.entities.StatisticMainEntity
import kotlinx.android.synthetic.main.statistic_main_item.view.*
import javax.inject.Inject

class StatisticMainAdapter @Inject constructor() :
        RecyclerView.Adapter<StatisticMainAdapter.StatisticMainViewHolder>() {

    var statistics: List<StatisticMainEntity?>? = null
    var listener: MainStatisticItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticMainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.statistic_main_item, parent, false);
        return StatisticMainViewHolder(view)
    }

    override fun getItemCount() = statistics?.size ?: 0

    override fun onBindViewHolder(holder: StatisticMainViewHolder, position: Int) {
        val statisticItem = statistics?.getOrNull(position) ?: return
        holder.itemView.nameTxt?.text = statisticItem.name
        holder.itemView.firstMeasureDateTxt?.text =
                holder.itemView.context.getString(
                        R.string.first_measure_tmp,
                        statisticItem.data?.getOrNull(0)?.time ?: "",
                        statisticItem.data?.getOrNull(0)?.date ?: "")
        holder.itemView.statisticMainItemCard?.setOnClickListener {
            listener?.onItemClick(position)
        }
        holder.itemView.moreImg?.setOnClickListener {
            listener?.onItemClickMore(it, position)
        }
    }

    class StatisticMainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}