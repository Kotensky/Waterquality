package com.kotensky.waterquality.di.components


import com.kotensky.waterquality.view.activities.MainActivity
import com.kotensky.waterquality.di.ScreenScope
import dagger.Component

@ScreenScope
@Component(dependencies = [(ApplicationComponent::class)])
interface ScreenComponent {

    fun inject(mainActivity: MainActivity)

}