package com.kotensky.waterquality.view.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.kotensky.waterquality.R
import com.kotensky.waterquality.di.components.DaggerScreenComponent
import com.kotensky.waterquality.interfaces.StatisticDetailsItemClickListener
import com.kotensky.waterquality.interfaces.view.DBView
import com.kotensky.waterquality.model.entities.StatisticDataEntity
import com.kotensky.waterquality.model.entities.StatisticMainEntity
import com.kotensky.waterquality.presenter.DBPresenter
import com.kotensky.waterquality.utils.sortDataListByDate
import com.kotensky.waterquality.view.activities.EditStatisticDataItemActivity.Companion.ADD_STATISTIC_DATA_ITEM_REQUEST_CODE
import com.kotensky.waterquality.view.activities.EditStatisticDataItemActivity.Companion.EDIT_STATISTIC_DATA_ITEM_REQUEST_CODE
import com.kotensky.waterquality.view.activities.EditStatisticDataItemActivity.Companion.IS_NEED_REMOVE_KEY
import com.kotensky.waterquality.view.activities.EditStatisticDataItemActivity.Companion.POSITION_KEY
import com.kotensky.waterquality.view.activities.EditStatisticDataItemActivity.Companion.STATISTIC_DATA_TO_EDIT_ENTITY_KEY
import com.kotensky.waterquality.view.adapters.StatisticDetailsAdapter
import kotlinx.android.synthetic.main.activity_statistic_edit.*
import java.util.*
import javax.inject.Inject


class EditStatisticMainActivity : BaseActivity(), DBView, StatisticDetailsItemClickListener {

    companion object {
        const val EDIT_STATISTIC_REQUEST_CODE = 25
        const val STATISTIC_TO_EDIT_ENTITY_KEY = "statistic_to_edit_entity_key"
    }

    @Inject
    lateinit var presenter: DBPresenter
    @Inject
    lateinit var adapter: StatisticDetailsAdapter

    private var mainEntity: StatisticMainEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getIntentValues()

        if (mainEntity == null) {
            finish()
        }
        setContentView(R.layout.activity_statistic_edit)

        presenter.view = this

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.edit_statistic_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        confirmFab.setOnClickListener {
            if (isDataValid()) {
                if (mainEntity == null){
                    return@setOnClickListener
                }
                mainEntity?.name = fileNameInput?.text?.toString()
                presenter.editStatistic(mainEntity!!)
            }
        }

        fillFields()
    }

    private fun fillFields() {
        if (mainEntity == null) {
            return
        }
        fileNameInput.setText(mainEntity?.name)

        adapter.listener = this
        adapter.data = mainEntity?.data
        adapter.showAddItem = true

        dataRecycler.adapter = adapter
        dataRecycler.layoutManager = LinearLayoutManager(this)
        dataRecycler.addItemDecoration(DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL))
    }

    private fun getIntentValues() {
        try {
            mainEntity = intent.getSerializableExtra(STATISTIC_TO_EDIT_ENTITY_KEY) as StatisticMainEntity?
        } catch (e: Exception) {
            finish()
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
        if (mainEntity?.data?.isEmpty() == true) {
            isValid = false
            showToast(getString(R.string.items_list_empty_error))
        }

        return isValid
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EDIT_STATISTIC_DATA_ITEM_REQUEST_CODE) {
                try {
                    val dataEntity = intent?.getSerializableExtra(STATISTIC_DATA_TO_EDIT_ENTITY_KEY) as StatisticDataEntity?
                    val position = intent?.getIntExtra(POSITION_KEY, -1) ?: -1
                    val isNeedRemove = intent?.getBooleanExtra(IS_NEED_REMOVE_KEY, false) ?: false
                    val dataMutableList = mainEntity?.data?.toMutableList()
                    if (isNeedRemove) {
                        dataMutableList?.removeAt(position)
                    } else {
                        dataMutableList?.set(position, dataEntity)
                    }
                    sortDataListByDate(dataMutableList, Locale.getDefault())
                    mainEntity?.data = dataMutableList
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            if (requestCode == ADD_STATISTIC_DATA_ITEM_REQUEST_CODE) {
                try {
                    val dataEntity = intent?.getSerializableExtra(STATISTIC_DATA_TO_EDIT_ENTITY_KEY) as StatisticDataEntity?
                    val dataMutableList = mainEntity?.data?.toMutableList()
                    dataMutableList?.add(dataEntity)
                    sortDataListByDate(dataMutableList, Locale.getDefault())
                    mainEntity?.data = dataMutableList
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            adapter.data = mainEntity?.data
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, EditStatisticDataItemActivity::class.java)
        intent.putExtra(STATISTIC_DATA_TO_EDIT_ENTITY_KEY, mainEntity?.data?.getOrNull(position))
        intent.putExtra(POSITION_KEY, position)
        startActivityForResult(intent, EDIT_STATISTIC_DATA_ITEM_REQUEST_CODE)
    }

    override fun onAddItemClick() {
        val intent = Intent(this, EditStatisticDataItemActivity::class.java)
        startActivityForResult(intent, ADD_STATISTIC_DATA_ITEM_REQUEST_CODE)
    }

    override fun onStatisticInserted() {
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