package com.kotensky.waterquality.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.kotensky.waterquality.R
import com.kotensky.waterquality.di.components.DaggerScreenComponent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {


    private val PICK_TXT_REQUEST_CODE = 24

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        addWaterDataFab.setOnClickListener { view ->
            val intent = Intent()
                    .setType("text/plain")
                    .setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(
                    Intent.createChooser(intent, getString(R.string.chooser_text)),
                    PICK_TXT_REQUEST_CODE)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == PICK_TXT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.e("TAG", intent?.data?.toString())
        }
    }

    override fun inject() {
        DaggerScreenComponent.builder()
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this)
    }
}
