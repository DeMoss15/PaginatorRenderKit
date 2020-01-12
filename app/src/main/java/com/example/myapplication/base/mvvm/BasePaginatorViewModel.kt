package com.example.myapplication.base.mvvm

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demoss.paginatorrenderkit.Paginator
import com.demoss.paginatorrenderkit.view.model.PaginatorItem

abstract class BasePaginatorViewModel : BaseViewModel() {

    protected val _paginatorState: MutableLiveData<Paginator.State<PaginatorItem<*>>> =
        MutableLiveData()
    open val paginatorState: LiveData<Paginator.State<PaginatorItem<*>>> = _paginatorState

    protected open val paginator = Paginator.Store<PaginatorItem<*>>().apply {
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
    open fun restart() = paginator.proceed(Paginator.Action.Restart())

    @CallSuper
    open fun refresh() = paginator.proceed(Paginator.Action.Refresh())

    @CallSuper
    open fun loadNextPage() = paginator.proceed(Paginator.Action.LoadMore())
}