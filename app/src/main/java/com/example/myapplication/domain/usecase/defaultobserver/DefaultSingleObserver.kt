package com.example.myapplication.domain.usecase.defaultobserver

import io.reactivex.observers.DisposableSingleObserver

open class DefaultSingleObserver<Result> : DisposableSingleObserver<Result>() {

    override fun onSuccess(t: Result) {
        // nothing by default
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }
}
