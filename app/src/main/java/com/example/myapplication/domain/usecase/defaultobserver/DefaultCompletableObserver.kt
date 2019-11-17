package com.example.myapplication.domain.usecase.defaultobserver

import androidx.annotation.CallSuper
import io.reactivex.annotations.NonNull
import io.reactivex.observers.DisposableCompletableObserver

open class DefaultCompletableObserver : DisposableCompletableObserver() {

    override fun onComplete() {
    }

    @CallSuper
    override fun onError(@NonNull e: Throwable) {
        e.printStackTrace()
    }
}