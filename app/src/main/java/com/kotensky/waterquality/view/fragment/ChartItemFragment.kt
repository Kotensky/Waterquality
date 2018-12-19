package com.kotensky.waterquality.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.EntryXComparator
import com.kotensky.waterquality.R
import kotlinx.android.synthetic.main.fragment_chart_item.*
import java.text.SimpleDateFormat
import java.util.*


class ChartItemFragment : Fragment() {

    companion object {
        private const val ENTRIES_KEY = "entries_key"
        private const val VALUE_NAME_KEY = "value_name_key"
        private const val DATE_FIRST_IN_MILLIS_KEY = "date_first_in_millis_key"

        fun newInstance(entries: ArrayList<Entry>, valueName: String, dateFirstInMillis: Long): ChartItemFragment {
            val fragment = ChartItemFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(ENTRIES_KEY, entries)
            bundle.putString(VALUE_NAME_KEY, valueName)
            bundle.putLong(DATE_FIRST_IN_MILLIS_KEY, dateFirstInMillis)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var entries = ArrayList<Entry>()
    private var valueName = ""
    private var dateFirstInMillis = 0L
    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd.MM", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            entries = arguments!!.getParcelableArrayList(ENTRIES_KEY)
            valueName = arguments!!.getString(VALUE_NAME_KEY, "")
            dateFirstInMillis = arguments!!.getLong(DATE_FIRST_IN_MILLIS_KEY, 0L)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_chart_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChart()
    }


    private fun setupChart() {
        val xAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setValueFormatter { value, axis ->
            val d = Date((java.lang.Float.valueOf(value)!!.toLong() + dateFirstInMillis) * 1000L)
            timeFormat.format(d)
        }
        setChartData()
    }

    private fun setChartData() {
        Collections.sort(entries, EntryXComparator())
        val dataSet = LineDataSet(entries, valueName)
        val data = LineData(dataSet)
        lineChart.description.text = ""
        lineChart.data = data
        lineChart.invalidate()

    }

}