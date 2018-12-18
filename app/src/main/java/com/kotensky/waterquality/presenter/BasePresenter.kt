package com.kotensky.waterquality.presenter

abstract class BasePresenter<T> {

    var view: T? = null

    abstract fun cancel()

    abstract fun destroy()

}