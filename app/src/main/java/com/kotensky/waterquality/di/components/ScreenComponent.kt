package com.kotensky.waterquality.di.components


import com.kotensky.waterquality.di.ScreenScope
import com.kotensky.waterquality.view.activities.AddStatisticActivity
import com.kotensky.waterquality.view.activities.MainActivity
import dagger.Component

@ScreenScope
@Component(dependencies = [(ApplicationComponent::class)])
interface ScreenComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(addStatisticActivity: AddStatisticActivity)

}