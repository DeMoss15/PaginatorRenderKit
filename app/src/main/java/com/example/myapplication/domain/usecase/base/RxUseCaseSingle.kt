package com.example.myapplication.domain.usecase.base

import com.example.myapplication.util.setDefaultSchedulers
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver

abstract class RxUseCaseSingle<T, Params> : BaseRxUseCase<Params, Single<T>, DisposableSingleObserver<T>>() {
    final override fun execute(observer: DisposableSingleObserver<T>, params: Params) {
        addDisposable(
            buildUseCaseObservable(params)
                .setDefaultSchedulers()
                .subscribeWith(observer)
        )
    }
}