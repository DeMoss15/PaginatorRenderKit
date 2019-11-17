package com.example.myapplication.util

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.reflect.KMutableProperty

// Default schedulers ==================================================================================================

fun <T> Observable<T>.setDefaultSchedulers(): Observable<T> = this.observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(Schedulers.io())

fun <T> Single<T>.setDefaultSchedulers(): Single<T> = this.observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(Schedulers.io())

fun Completable.setDefaultSchedulers(): Completable = this.observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(Schedulers.io())

fun <T> Maybe<T>.setDefaultSchedulers(): Maybe<T> = this.observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(Schedulers.io())

// Other ===============================================================================================================

fun <T> Observable<T>.updateValue(target: KMutableProperty<T>): Observable<T> =
    this.applyForEach { target.setter.call(it) }

fun <T> Observable<T>.applyForEach(mapper: (T) -> Unit): Observable<T> = this.map { it.apply { mapper(it) } }