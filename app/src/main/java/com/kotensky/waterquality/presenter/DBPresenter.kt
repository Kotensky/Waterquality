package com.kotensky.waterquality.presenter

import com.kotensky.waterquality.interfaces.view.DBView
import com.kotensky.waterquality.model.entities.StatisticMainEntity
import com.kotensky.waterquality.model.room.dao.StatisticDao
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DBPresenter @Inject constructor(
        private val statisticDao: StatisticDao) : BasePresenter<DBView>() {

    private val compositeDisposable: CompositeDisposable? = CompositeDisposable()

    fun addStatistic(statisticMainEntity: StatisticMainEntity) {
        Completable.fromAction { statisticDao.insertStatistic(statisticMainEntity) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable?.add(d)
                    }

                    override fun onComplete() {
                        view?.onStatisticAdded()
                        view?.hideLoading()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        view?.hideLoading()
                    }
                })
    }

    fun loadData(){
        statisticDao.getAllStatistics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<StatisticMainEntity?>?> {
                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable?.add(d)
                    }

                    override fun onSuccess(statistics: List<StatisticMainEntity?>) {
                        view?.showData(statistics)
                        view?.hideLoading()
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        view?.hideLoading()
                    }
                })
    }

    override fun cancel() {
        if (compositeDisposable?.isDisposed == false) {
            compositeDisposable.dispose()
        }
    }

    override fun destroy() {
        cancel()
        view = null
    }
}