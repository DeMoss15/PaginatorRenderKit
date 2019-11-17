package com.example.myapplication.base.mvvm

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demoss.paginatorrenderkit.AbsPaginalItem
import com.demoss.paginatorrenderkit.Paginator

abstract class BasePaginatorViewModel : BaseViewModel() {

    protected val _paginatorState: MutableLiveData<Paginator.State<AbsPaginalItem<*>>> = MutableLiveData()
    open val paginatorState: LiveData<Paginator.State<AbsPaginalItem<*>>> = _paginatorState

    protected open val paginator = Paginator.Store<AbsPaginalItem<*>>().apply {
        render = { _paginatorState.value = it }
        sideEffects.subscribe(
            {
                when (it) {
                    is Paginator.SideEffect.LoadPage -> loadPage(it.currentPage)
                    is Paginator.SideEffect.ErrorEvent -> { /*todo: handle error here*/ }
                }
            },
            { /* todo: handle error here */ }
        )
    }

    protected abstract fun loadPage(page: Int)

    @CallSuper
    open fun refresh() = paginator.proceed(Paginator.Action.Refresh())

    @CallSuper
    open fun loadNextPage() = paginator.proceed(Paginator.Action.LoadMore())
}