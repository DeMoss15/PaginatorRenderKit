package com.example.myapplication.domain.usecase.defaultobserver

import io.reactivex.annotations.NonNull
import io.reactivex.observers.DisposableObserver

open class DefaultObserver<Result> : DisposableObserver<Result>() {

    override fun onError(@NonNull e: Throwable) {
        e.printStackTrace()
    }

    override fun onComplete() {
        // nothing by default
    }

    override fun onNext(@NonNull t: Result) {
        // nothing by default
    }
}