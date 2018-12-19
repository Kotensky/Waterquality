package com.kotensky.waterquality.view.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.github.mikephil.charting.data.Entry
import com.kotensky.waterquality.R
import com.kotensky.waterquality.model.entities.StatisticMainEntity
import com.kotensky.waterquality.view.fragment.ChartItemFragment
import java.text.SimpleDateFormat
import java.util.*

class ChartPagerAdapter(private val context: Context,
                        private val mainEntity: StatisticMainEntity?,
                        supportFragmentManager: FragmentManager) : FragmentPagerAdapter(supportFragmentManager) {

    private val dateFormat = SimpleDateFormat("HH:mm:ss,dd.MM.yyyy", Locale.getDefault())

    override fun getItem(position: Int): Fragment {
        val entries = ArrayList<Entry>()
        var dateFirstInMillis = 0L
        if (mainEntity?.data?.isNotEmpty() == true) {
            dateFirstInMillis = (dateFormat.parse("${mainEntity.data!![0]!!.time},${mainEntity.data!![0]!!.date}").time / 1000L)

            loop@ for (dataEntity in mainEntity.data!!) {
                val value = when (position) {
                    0 -> dataEntity?.temperature ?: continue@loop
                    1 -> dataEntity?.ph ?: continue@loop
                    else -> dataEntity?.ppm ?: continue@loop
                }
                val dateInMillis = (dateFormat.parse("${dataEntity.time},${dataEntity.date}").time / 1000L)

                val delta = dateInMillis - dateFirstInMillis
                entries.add(Entry(delta.toFloat(), value))
            }
        }

        return ChartItemFragment.newInstance(entries, getPageTitle(position).toString(), dateFirstInMillis)
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> context.getString(R.string.page_title_0)
            1 -> context.getString(R.string.page_title_1)
            2 -> context.getString(R.string.page_title_2)
            else -> super.getPageTitle(position)
        }
    }
}