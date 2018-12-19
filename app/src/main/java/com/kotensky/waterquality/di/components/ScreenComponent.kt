package com.kotensky.waterquality.di.components


import com.kotensky.waterquality.di.ScreenScope
import com.kotensky.waterquality.view.activities.*
import dagger.Component

@ScreenScope
@Component(dependencies = [(ApplicationComponent::class)])
interface ScreenComponent {

    fun inject(splashActivity: SplashActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(importStatisticActivity: ImportStatisticActivity)
    fun inject(statisticDetailsActivity: StatisticDetailsActivity)
    fun inject(statisticChartsActivity: StatisticChartsActivity)

}