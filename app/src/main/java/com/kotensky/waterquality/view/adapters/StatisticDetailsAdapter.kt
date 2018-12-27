package com.kotensky.waterquality.view.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kotensky.waterquality.R
import com.kotensky.waterquality.interfaces.ListItemClickListener
import com.kotensky.waterquality.interfaces.StatisticDetailsItemClickListener
import com.kotensky.waterquality.model.entities.StatisticDataEntity
import kotlinx.android.synthetic.main.statistic_details_add_item.view.*
import kotlinx.android.synthetic.main.statistic_details_item.view.*
import javax.inject.Inject

class StatisticDetailsAdapter @Inject constructor() :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_ITEM = 0
    private val TYPE_ADD_ITEM = 1

    var data: List<StatisticDataEntity?>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var listener: StatisticDetailsItemClickListener? = null
    var showAddItem: Boolean = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ADD_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.statistic_details_add_item, parent, false);
            AddDataViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.statistic_details_item, parent, false);
            DetailsViewHolder(view)
        }
    }

    override fun getItemCount() = (data?.size ?: 0) + (if (showAddItem) 1 else 0)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is DetailsViewHolder) {
            holder.bindView(data?.getOrNull(position), listener, position)
        }
        if (holder is AddDataViewHolder) {
            holder.itemView.addBtn?.setOnClickListener {
                listener?.onAddItemClick()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (showAddItem && position == itemCount - 1) {
            TYPE_ADD_ITEM
        } else {
            TYPE_ITEM
        }
    }


    inner class DetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(dataItem: StatisticDataEntity?, listener: ListItemClickListener?, position: Int) {
            if (dataItem == null)
                return

            itemView.timeTxt?.text = dataItem.time
            itemView.dateTxt?.text = dataItem.date
            itemView.latTxt?.text = itemView.context.getString(R.string.lat_tmp, dataItem.lat.toString())
            itemView.lonTxt?.text = itemView.context.getString(R.string.lon_tmp, dataItem.lon.toString())
            itemView.temperatureTxt?.text = itemView.context.getString(R.string.temperature_tmp, dataItem.temperature.toString())
            itemView.phTxt?.text = itemView.context.getString(R.string.ph_tmp, dataItem.ph.toString())
            itemView.ppmTxt?.text = itemView.context.getString(R.string.ppm_tmp, dataItem.ppm.toString())

            if (listener != null) {
                itemView.setOnClickListener {
                    listener.onItemClick(position)
                }
            }
        }
    }

    inner class AddDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}