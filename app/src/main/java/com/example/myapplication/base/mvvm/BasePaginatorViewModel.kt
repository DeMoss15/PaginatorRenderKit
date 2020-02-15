package com.example.myapplication.base.mvvm

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demoss.paginatorrenderkit.Paginator

abstract class BasePaginatorViewModel : BaseViewModel() {

    protected val _paginatorState: MutableLiveData<Paginator.State<Any>> = MutableLiveData()
    val paginatorState: LiveData<Paginator.State<Any>> = _paginatorState

    protected open val paginatorStore = Paginator.Store<Any>().apply {
        render = { _paginatorState.value = it }
        executeSideEffect = {
            when (it) {
                is Paginator.SideEffect.LoadPage -> loadPage(it.currentPage)
                is Paginator.SideEffect.ErrorEvent -> { it.error.printStackTrace() }
                is Paginator.SideEffect.CancelLoadings -> cancelLoading()
            }
        }
    }

    protected abstract fun loadPage(page: Int)

    protected abstract fun cancelLoading()

    @CallSuper
    open fun restart() = paginatorStore.proceed(Paginator.Action.Restart())

    @CallSuper
    open fun refresh() = paginatorStore.proceed(Paginator.Action.Refresh())

    @CallSuper
    open fun loadNextPage() = paginatorStore.proceed(Paginator.Action.LoadMore())
}