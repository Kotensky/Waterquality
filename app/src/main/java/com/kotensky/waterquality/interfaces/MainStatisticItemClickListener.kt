package com.kotensky.waterquality.interfaces

import android.view.View

interface MainStatisticItemClickListener : ListItemClickListener{

    fun onItemClickMore(view: View, position: Int)

}