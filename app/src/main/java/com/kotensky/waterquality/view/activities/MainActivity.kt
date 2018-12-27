package com.kotensky.waterquality.view.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.transition.TransitionManager
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.view.View
import com.kotensky.waterquality.R
import com.kotensky.waterquality.di.components.DaggerScreenComponent
import com.kotensky.waterquality.interfaces.MainStatisticItemClickListener
import com.kotensky.waterquality.interfaces.view.DBView
import com.kotensky.waterquality.model.entities.StatisticMainEntity
import com.kotensky.waterquality.presenter.DBPresenter
import com.kotensky.waterquality.view.activities.EditStatisticMainActivity.Companion.EDIT_STATISTIC_REQUEST_CODE
import com.kotensky.waterquality.view.activities.EditStatisticMainActivity.Companion.STATISTIC_TO_EDIT_ENTITY_KEY
import com.kotensky.waterquality.view.activities.ImportStatisticActivity.Companion.ADD_STATISTIC_REQUEST_CODE
import com.kotensky.waterquality.view.activities.StatisticDetailsActivity.Companion.STATISTIC_DETAILS_ENTITY_KEY
import com.kotensky.waterquality.view.adapters.StatisticMainAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileWriter
import java.io.IOException
import javax.inject.Inject




class MainActivity : BaseActivity(), DBView, MainStatisticItemClickListener {

    companion object {
        private const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1
    }

    @Inject
    lateinit var presenter: DBPresenter
    @Inject
    lateinit var statisticMainAdapter: StatisticMainAdapter

    private val statistics = ArrayList<StatisticMainEntity?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        presenter.view = this

        statisticMainAdapter.listener = this
        statisticMainAdapter.statistics = statistics

        dataRecycler.adapter = statisticMainAdapter
        dataRecycler.layoutManager = LinearLayoutManager(this)

        addWaterDataFab.setOnClickListener { view ->
            startActivityForResult(Intent(this, ImportStatisticActivity::class.java), ADD_STATISTIC_REQUEST_CODE)
        }

        dataSwipeRefresh.setOnRefreshListener {
            presenter.loadData()
        }
        presenter.loadData()
    }

    private fun showRemoveItemDialog(item: StatisticMainEntity?) {
        if (item == null)
            return

        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.remove_dialog_title)
                .setMessage(getString(R.string.remove_dialog_message_tmp, item.name))
                .setPositiveButton(R.string.yes) { dialog, which ->
                    presenter.removeStatistic(item)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog, which ->
                    dialog.dismiss()
                }
        builder.create().show()
    }

    private fun isWriteStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT < 23) {
            return true
        }
        return if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), WRITE_EXTERNAL_STORAGE_REQUEST_CODE)
            false
        }
    }


    private fun exportToFile(statisticMainEntity: StatisticMainEntity?) {
        if (statisticMainEntity == null || statisticMainEntity.name.isNullOrEmpty() || statisticMainEntity.data == null)
            return

        try {
            val root = File(Environment.getExternalStorageDirectory(), getString(R.string.app_name))
            if (!root.exists()) {
                root.mkdirs()
            }
            var gpxfile = File(root, "${statisticMainEntity.name}.txt")
            if (gpxfile.exists()) {
                var i = 0
                while (gpxfile.exists()) {
                    i++
                    gpxfile = File(root, "${statisticMainEntity.name} ($i).txt")
                }
            }
            val stringBuilder = StringBuilder()
            statisticMainEntity.data?.toMutableList()?.forEach {
                stringBuilder.append(it.toString())
                stringBuilder.append("\n")
            }

            val writer = FileWriter(gpxfile)
            writer.append(stringBuilder.toString())
            writer.flush()
            writer.close()
            showToastLong(getString(R.string.saved_tmp, gpxfile.absolutePath))
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE){
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(rootView,
                        getString(R.string.need_write_external_permission),
                        Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.go_to_settings)) {
                            try {
                                val intent = Intent()
                                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                val uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivity(intent)
                            } catch (e:Exception){
                                e.printStackTrace()
                            }
                        }
                        .show()
            }
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_STATISTIC_REQUEST_CODE || requestCode == EDIT_STATISTIC_REQUEST_CODE) {
                presenter.loadData()
            }
        }
    }

    override fun showData(statistics: List<StatisticMainEntity?>) {
        this.statistics.clear()
        this.statistics.addAll(statistics)
        TransitionManager.beginDelayedTransition(rootView)
        statisticMainAdapter.notifyDataSetChanged()
        if (statistics.isEmpty()) {
            dataBaseEmptyTxt.visibility = View.VISIBLE
            dataSwipeRefresh.visibility = View.GONE
        } else {
            dataBaseEmptyTxt.visibility = View.GONE
            dataSwipeRefresh.visibility = View.VISIBLE
        }
    }

    override fun onStatisticRemoved() {
        presenter.loadData()
    }

    override fun hideLoading() {
        dataSwipeRefresh.isRefreshing = false
        if (statistics.isEmpty()) {
            dataBaseEmptyTxt.visibility = View.VISIBLE
            dataSwipeRefresh.visibility = View.GONE
        } else {
            dataBaseEmptyTxt.visibility = View.GONE
            dataSwipeRefresh.visibility = View.VISIBLE
        }
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, StatisticDetailsActivity::class.java)
        intent.putExtra(STATISTIC_DETAILS_ENTITY_KEY, statistics.getOrNull(position))
        startActivity(intent)
    }

    override fun onItemClickMore(view: View, position: Int) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_main_item_more)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.export_action -> {
                    if (isWriteStoragePermissionGranted()) {
                        exportToFile(statistics.getOrNull(position))
                    }
                    true
                }
                R.id.edit_action -> {
                    val intent = Intent(this, EditStatisticMainActivity::class.java)
                    intent.putExtra(STATISTIC_TO_EDIT_ENTITY_KEY, statistics.getOrNull(position))
                    startActivityForResult(intent, EDIT_STATISTIC_REQUEST_CODE)
                    true
                }
                R.id.remove_action -> {
                    showRemoveItemDialog(statistics.getOrNull(position))
                    true
                }
                else ->
                    false
            }
        }
        popupMenu.show()
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
