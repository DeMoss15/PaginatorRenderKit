package com.example.myapplication.domain.usecase.defaultobserver

import io.reactivex.observers.DisposableMaybeObserver

open class DefaultMaybeObserver<Result> : DisposableMaybeObserver<Result>() {

    override fun onSuccess(t: Result) {
        // nothing by default
    }

    override fun onComplete() {
        // nothing by default
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }
}
