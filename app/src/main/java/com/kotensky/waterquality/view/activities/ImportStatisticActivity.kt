package com.kotensky.waterquality.view.activities

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.MediaColumns
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import com.kotensky.waterquality.R
import com.kotensky.waterquality.di.components.DaggerScreenComponent
import com.kotensky.waterquality.interfaces.view.DBView
import com.kotensky.waterquality.model.entities.StatisticDataEntity
import com.kotensky.waterquality.model.entities.StatisticMainEntity
import com.kotensky.waterquality.presenter.DBPresenter
import kotlinx.android.synthetic.main.activity_add_statistic.*
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import javax.inject.Inject


class ImportStatisticActivity : BaseActivity(), DBView {


    companion object {
        const val ADD_STATISTIC_REQUEST_CODE = 22
        private const val PICK_TXT_REQUEST_CODE = 23

    }

    @Inject
    lateinit var presenter: DBPresenter

    private val dataList = ArrayList<StatisticDataEntity?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_statistic)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_add_statistic_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        presenter.view = this

        attachFileContainer.setOnClickListener { view ->
            val intent = Intent()
                    .setType("text/plain")
                    .setAction(Intent.ACTION_GET_CONTENT)
            try {
                startActivityForResult(
                        Intent.createChooser(intent, getString(R.string.chooser_text)),
                        PICK_TXT_REQUEST_CODE)
            } catch (e: ActivityNotFoundException) {
                showToast(getString(R.string.install_file_manager_msg))
            }
        }
        saveFab.setOnClickListener {

            if (isDataValid()) {
                presenter.addStatistic(StatisticMainEntity(
                        name = fileNameInput?.text?.toString(),
                        data = dataList))
            }

        }
    }

    private fun isDataValid(): Boolean {
        var isValid = true
        if (fileNameInput?.text.isNullOrEmpty()) {
            isValid = false
            fileNameInputContainer.error = getString(R.string.file_name_empty_error)
        } else {
            fileNameInputContainer.error = null
        }
        if (dataList.isEmpty()) {
            isValid = false
            showToast(getString(R.string.attach_file_missing_error))
        }

        return isValid
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == PICK_TXT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            if (intent?.data == null) {
                return
            }

            try {
                readFile(intent.data!!)
                if (dataList.isEmpty()) {
                    showToast(getString(R.string.attach_file_empty_error))
                } else {
                    attachFileTxt.text = File(getAbsolutePath(intent.data)).name
                    attachFileTxt.setTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    private fun readFile(contentDescriber: Uri) {
        val inputStream = contentResolver.openInputStream(contentDescriber)
        val br = BufferedReader(InputStreamReader(inputStream))
        var line: String? = ""
        dataList.clear()
        while (line != null) {
            line = br.readLine() ?: break
            val split = line.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (split.size > 6) {

                val statisticDataEntity = StatisticDataEntity(
                        split[0],
                        split[1],
                        split[2].replace(",", ".").toDoubleOrNull() ?: continue,
                        split[3].replace(",", ".").toDoubleOrNull() ?: continue,
                        split[4].replace(",", ".").toFloatOrNull() ?: continue,
                        split[5].replace(",", ".").toFloatOrNull() ?: continue,
                        split[6].replace(",", ".").toFloatOrNull() ?: continue)

                dataList.add(statisticDataEntity)

            }
        }
        br.close()
    }

    private fun getAbsolutePath(uri: Uri): String? {
        val projection = arrayOf(MediaColumns.DATA)
        val cursor = managedQuery(uri, projection, null, null, null)
        return if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA)
            cursor.moveToFirst()
            cursor.getString(columnIndex)
        } else
            null
    }

    override fun onStatisticAdded() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun inject() {
        DaggerScreenComponent.builder()
                .applicationComponent(getApplicationComponent())
                .build()
                .inject(this)
    }

    override fun onDestroy() {
        presenter.destroy()
        super.onDestroy()
    }
}