package com.kotensky.waterquality.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kotensky.waterquality.R
import com.kotensky.waterquality.interfaces.ListItemClickListener
import com.kotensky.waterquality.model.entities.StatisticDataEntity
import kotlinx.android.synthetic.main.statistic_details_item.view.*
import javax.inject.Inject

class StatisticDetailsAdapter @Inject constructor() :
        RecyclerView.Adapter<StatisticDetailsAdapter.DetailsViewHolder>() {

    var data: List<StatisticDataEntity?>? = null
    var listener: ListItemClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.statistic_details_item, parent, false);
        return DetailsViewHolder(view)
    }

    override fun getItemCount() = data?.size ?: 0

    override fun onBindViewHolder(holder: DetailsViewHolder, position: Int) {
        val dataItem = data?.getOrNull(position) ?: return
        holder.itemView.timeTxt?.text = dataItem.time
        holder.itemView.dateTxt?.text = dataItem.date
        holder.itemView.latTxt?.text = holder.itemView.context.getString(R.string.lat_tmp, dataItem.lat.toString())
        holder.itemView.lonTxt?.text = holder.itemView.context.getString(R.string.lon_tmp, dataItem.lon.toString())
        holder.itemView.temperatureTxt?.text = holder.itemView.context.getString(R.string.temperature_tmp, dataItem.temperature.toString())
        holder.itemView.phTxt?.text = holder.itemView.context.getString(R.string.ph_tmp, dataItem.ph.toString())
        holder.itemView.ppmTxt?.text = holder.itemView.context.getString(R.string.ppm_tmp, dataItem.ppm.toString())
        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }
    }

    class DetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}