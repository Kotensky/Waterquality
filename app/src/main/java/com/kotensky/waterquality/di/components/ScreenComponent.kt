package com.kotensky.waterquality.di.components


import com.kotensky.waterquality.di.ScreenScope
import com.kotensky.waterquality.view.activities.ImportStatisticActivity
import com.kotensky.waterquality.view.activities.MainActivity
import com.kotensky.waterquality.view.activities.StatisticDetailsActivity
import dagger.Component

@ScreenScope
@Component(dependencies = [(ApplicationComponent::class)])
interface ScreenComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(importStatisticActivity: ImportStatisticActivity)
    fun inject(statisticDetailsActivity: StatisticDetailsActivity)

}