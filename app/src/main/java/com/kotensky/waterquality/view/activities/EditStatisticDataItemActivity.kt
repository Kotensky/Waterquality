package com.kotensky.waterquality.view.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.kotensky.waterquality.R
import com.kotensky.waterquality.di.components.DaggerScreenComponent
import com.kotensky.waterquality.model.entities.StatisticDataEntity
import kotlinx.android.synthetic.main.activity_statistic_data_item_edit.*
import java.text.SimpleDateFormat
import java.util.*


class EditStatisticDataItemActivity : BaseActivity() {

    companion object {
        const val ADD_STATISTIC_DATA_ITEM_REQUEST_CODE = 26
        const val EDIT_STATISTIC_DATA_ITEM_REQUEST_CODE = 27
        const val STATISTIC_DATA_TO_EDIT_ENTITY_KEY = "statistic_data_to_edit_entity_key"
        const val POSITION_KEY = "position_key"
        const val IS_NEED_REMOVE_KEY = "is_need_remove_key"
    }

    private val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    private var dataEntity: StatisticDataEntity? = null
    private var position = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentValues()
        setContentView(R.layout.activity_statistic_data_item_edit)

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.edit_statistic_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        timeContainer?.setOnClickListener {


            val cal = Calendar.getInstance()
            if (!timeTxt.text.isNullOrEmpty()) {
                cal.time = timeFormat.parse(timeTxt.text.toString())
            }

            val timePickerDialog = TimePickerDialog(
                    this, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->

                cal.set(Calendar.HOUR_OF_DAY, selectedHour)
                cal.set(Calendar.MINUTE, selectedMinute)
                cal.set(Calendar.SECOND, 0)
                timeTxt.text = timeFormat.format(cal.time)

            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true)
            timePickerDialog.show()
        }

        dateContainer?.setOnClickListener {

            val cal = Calendar.getInstance()
            if (!dateTxt.text.isNullOrEmpty()) {
                cal.time = dateFormat.parse(dateTxt.text.toString())
            }

            val datePickerDialog = DatePickerDialog(
                    this, DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDay ->

                cal.set(selectedYear, selectedMonth, selectedDay)
                dateTxt.text = dateFormat.format(cal.time)

            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.maxDate = Date().time
            datePickerDialog.show()
        }

        confirmFab?.setOnClickListener {
            if (isDataValid()) {
                if (dataEntity == null) {
                    dataEntity = StatisticDataEntity()
                }

                dataEntity?.time = timeTxt?.text?.toString()
                dataEntity?.date = dateTxt?.text?.toString()
                dataEntity?.lat = latInput?.text?.toString()?.toDoubleOrNull()
                dataEntity?.lon = lonInput?.text?.toString()?.toDoubleOrNull()
                dataEntity?.temperature = temperatureInput?.text?.toString()?.toFloatOrNull()
                dataEntity?.ph = phInput?.text?.toString()?.toFloatOrNull()
                dataEntity?.ppm = ppmInput?.text?.toString()?.toFloatOrNull()

                val intent = Intent()
                intent.putExtra(STATISTIC_DATA_TO_EDIT_ENTITY_KEY, dataEntity)
                intent.putExtra(POSITION_KEY, position)
                setResult(RESULT_OK, intent)
                finish()
            }
        }

        fillFields()

    }

    private fun getIntentValues() {
        try {
            dataEntity = intent.getSerializableExtra(STATISTIC_DATA_TO_EDIT_ENTITY_KEY) as StatisticDataEntity?
            position = intent.getIntExtra(POSITION_KEY, -1)
            if (position >= 0) {
                invalidateOptionsMenu()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun fillFields() {
        val currentDate = Date()
        timeTxt.text = dataEntity?.time ?: timeFormat.format(currentDate)
        dateTxt.text = dataEntity?.date ?: dateFormat.format(currentDate)

        if (dataEntity == null) {
            return
        }

        latInput.setText(dataEntity?.lat?.toString() ?: "")
        lonInput.setText(dataEntity?.lon?.toString() ?: "")
        temperatureInput.setText(dataEntity?.temperature?.toString() ?: "")
        phInput.setText(dataEntity?.ph?.toString() ?: "")
        ppmInput.setText(dataEntity?.ppm?.toString() ?: "")
    }

    private fun showRemoveItemDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.remove_dialog_title)
                .setMessage(getString(R.string.remove_dialog_message))
                .setPositiveButton(R.string.yes) { dialog, which ->
                    val intent = Intent()
                    intent.putExtra(IS_NEED_REMOVE_KEY, true)
                    intent.putExtra(POSITION_KEY, position)
                    setResult(RESULT_OK, intent)
                    finish()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog, which ->
                    dialog.dismiss()
                }
        builder.create().show()
    }

    private fun isDataValid(): Boolean {
        var isValid = true
        if (timeTxt?.text.isNullOrEmpty()) {
            isValid = false
            showToast(getString(R.string.field_time_empty_error))
        }
        if (dateTxt?.text.isNullOrEmpty()) {
            isValid = false
            showToast(getString(R.string.field_date_empty_error))
        }
        if (latInput?.text.isNullOrEmpty()) {
            isValid = false
            latInputContainer.error = getString(R.string.field_empty_error)
        } else {
            if (latInput?.text?.toString()?.toDoubleOrNull() == null) {
                isValid = false
                latInputContainer.error = getString(R.string.field_data_error)
            } else {
                latInputContainer.error = null
            }
        }
        if (lonInput?.text.isNullOrEmpty()) {
            isValid = false
            lonInputContainer.error = getString(R.string.field_empty_error)
        } else {
            if (lonInput?.text?.toString()?.toDoubleOrNull() == null) {
                isValid = false
                lonInputContainer.error = getString(R.string.field_data_error)
            } else {
                lonInputContainer.error = null
            }
        }
        if (temperatureInput?.text.isNullOrEmpty()) {
            isValid = false
            temperatureInputContainer.error = getString(R.string.field_empty_error)
        } else {
            if (temperatureInput?.text?.toString()?.toFloatOrNull() == null) {
                isValid = false
                temperatureInputContainer.error = getString(R.string.field_data_error)
            } else {
                temperatureInputContainer.error = null
            }
        }
        if (phInput?.text.isNullOrEmpty()) {
            isValid = false
            phInputContainer.error = getString(R.string.field_empty_error)
        } else {
            if (phInput?.text?.toString()?.toFloatOrNull() == null) {
                isValid = false
                phInputContainer.error = getString(R.string.field_data_error)
            } else {
                phInputContainer.error = null
            }
        }
        if (ppmInput?.text.isNullOrEmpty()) {
            isValid = false
            ppmInputContainer.error = getString(R.string.field_empty_error)
        } else {
            if (ppmInput?.text?.toString()?.toFloatOrNull() == null) {
                isValid = false
                ppmInputContainer.error = getString(R.string.field_data_error)
            } else {
                ppmInputContainer.error = null
            }
        }

        return isValid
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_statistic_data_item, menu)
        menu?.findItem(R.id.remove_action)?.isVisible = position >= 0
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.remove_action -> {
                showRemoveItemDialog()
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
}