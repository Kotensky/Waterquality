package com.kotensky.waterquality.view.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.kotensky.waterquality.R
import com.kotensky.waterquality.di.components.DaggerScreenComponent

class SplashActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }, 4500)

    }

    override fun inject() {
        DaggerScreenComponent.builder()
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this)
    }
}