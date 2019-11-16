package com.demoss.paginatorrenderkit

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import java.util.concurrent.Executors

object Paginator {

    sealed class State<T> {
        class Empty<T> : State<T>()
        class EmptyProgress<T> : State<T>()
        data class EmptyError<T>(val error: Throwable) : State<T>()
        data class Data<T>(val pageCount: Int, val data: List<T>) : State<T>() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Data<*>

                if (pageCount != other.pageCount) return false
                if (data != other.data) return false

                return true
            }

            override fun hashCode(): Int {
                var result = pageCount
                result = 31 * result + data.hashCode()
                return result
            }
        }

        data class Refresh<T>(val pageCount: Int, val data: List<T>) : State<T>() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Refresh<*>

                if (pageCount != other.pageCount) return false
                if (data != other.data) return false

                return true
            }

            override fun hashCode(): Int {
                var result = pageCount
                result = 31 * result + data.hashCode()
                return result
            }
        }

        data class NewPageProgress<T>(val pageCount: Int, val data: List<T>) : State<T>() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as NewPageProgress<*>

                if (pageCount != other.pageCount) return false
                if (data != other.data) return false

                return true
            }

            override fun hashCode(): Int {
                var result = pageCount
                result = 31 * result + data.hashCode()
                return result
            }
        }

        data class FullData<T>(val pageCount: Int, val data: List<T>) : State<T>() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as FullData<*>

                if (pageCount != other.pageCount) return false
                if (data != other.data) return false

                return true
            }

            override fun hashCode(): Int {
                var result = pageCount
                result = 31 * result + data.hashCode()
                return result
            }
        }
    }

    sealed class Action<T> {
        class Refresh<T> : Action<T>()
        class Restart<T> : Action<T>()
        class LoadMore<T> : Action<T>()
        data class UpdateItem<T>(val item: T, val compareItemPredicate: (other: T) -> Boolean) : Action<T>()

        data class DeleteItem<T>(val index: Int) : Action<T>()
        data class NewPage<T>(val pageNumber: Int, val items: List<T>) : Action<T>()
        data class InsertData<T>(val position: Int, val items: List<T>) : Action<T>()
        data class PageError<T>(val error: Throwable) : Action<T>()
    }

    sealed class SideEffect {
        data class LoadPage(val currentPage: Int) : SideEffect()
        data class ErrorEvent(val error: Throwable) : SideEffect()
    }

    private fun <T> List<T>.replace(
        item: T,
        compareItemsPredicate: (T) -> Boolean
    ): List<T> = toMutableList().apply {
        indexOf(find(compareItemsPredicate)).let { idx ->
            if (idx < 0) return@apply
            removeAt(idx)
            add(idx, item)
        }
    }

    private fun <T> List<T>.insert(position: Int, items: List<T>): List<T> =
        toMutableList().apply {
            addAll(if (position > size) size else position, items)
        }

    private fun <T> reducer(
        action: Action<T>,
        state: State<T>,
        sideEffectListener: (SideEffect) -> Unit
    ): State<T> =
        when (action) {
            is Action.Refresh -> {
                sideEffectListener(
                    SideEffect.LoadPage(
                        1
                    )
                )
                when (state) {
                    is State.Empty -> State.EmptyProgress()
                    is State.EmptyError -> State.EmptyProgress()
                    is State.Data -> State.Refresh(
                        state.pageCount,
                        state.data
                    )
                    is State.NewPageProgress -> State.Refresh(
                        state.pageCount,
                        state.data
                    )
                    is State.FullData -> State.Refresh(
                        state.pageCount,
                        state.data
                    )
                    else -> state
                }
            }
            is Action.Restart -> {
                sideEffectListener(
                    SideEffect.LoadPage(
                        1
                    )
                )
                when (state) {
                    is State.Empty -> State.EmptyProgress()
                    is State.EmptyError -> State.EmptyProgress()
                    is State.Data -> State.EmptyProgress()
                    is State.Refresh -> State.EmptyProgress()
                    is State.NewPageProgress -> State.EmptyProgress()
                    is State.FullData -> State.EmptyProgress()
                    else -> state
                }
            }
            is Action.LoadMore -> {
                when (state) {
                    is State.Data -> {
                        sideEffectListener(
                            SideEffect.LoadPage(
                                state.pageCount + 1
                            )
                        )
                        State.NewPageProgress(
                            state.pageCount,
                            state.data
                        )
                    }
                    else -> state
                }
            }
            is Action.UpdateItem -> {
                when (state) {
                    is State.Data -> State.Data(
                        state.pageCount,
                        state.data.replace(action.item, action.compareItemPredicate)
                    )
                    is State.Refresh -> State.Refresh(
                        state.pageCount,
                        state.data.replace(action.item, action.compareItemPredicate)
                    )
                    is State.NewPageProgress -> State.NewPageProgress(
                        state.pageCount,
                        state.data.replace(action.item, action.compareItemPredicate)
                    )
                    is State.FullData -> State.FullData(
                        state.pageCount,
                        state.data.replace(action.item, action.compareItemPredicate)
                    )
                    else -> state
                }
            }
            is Action.DeleteItem -> {
                when (state) {
                    is State.Data -> State.Data(
                        state.pageCount,
                        state.data.toMutableList().apply { removeAt(action.index) }
                    )
                    is State.Refresh -> State.Refresh(
                        state.pageCount,
                        state.data.toMutableList().apply { removeAt(action.index) }
                    )
                    is State.NewPageProgress -> State.NewPageProgress(
                        state.pageCount,
                        state.data.toMutableList().apply { removeAt(action.index) }
                    )
                    is State.FullData -> State.FullData(
                        state.pageCount,
                        state.data.toMutableList().apply { removeAt(action.index) }
                    )
                    else -> state
                }
            }
            is Action.NewPage -> {
                val items = action.items
                when (state) {
                    is State.EmptyProgress -> {
                        if (items.isEmpty()) {
                            State.Empty()
                        } else {
                            State.Data(1, items)
                        }
                    }
                    is State.Refresh -> {
                        if (items.isEmpty()) {
                            State.Empty()
                        } else {
                            State.Data(1, items)
                        }
                    }
                    is State.NewPageProgress -> {
                        if (items.isEmpty()) {
                            State.FullData(
                                state.pageCount,
                                state.data
                            )
                        } else {
                            State.Data(
                                state.pageCount + 1,
                                state.data + items
                            )
                        }
                    }
                    else -> state
                }
            }
            is Action.InsertData -> {
                when (state) {
                    is State.Data -> State.Data(
                        state.pageCount,
                        action.run { state.data.insert(position, items) }
                    )
                    is State.FullData -> State.Data(
                        state.pageCount,
                        action.run { state.data.insert(position, items) }
                    )
                    is State.Empty -> State.Data(
                        1,
                        action.items
                    )
                    is State.Refresh -> State.Refresh(
                        state.pageCount,
                        action.run { state.data.insert(position, items) }
                    )
                    else -> state
                }
            }
            is Action.PageError -> {
                when (state) {
                    is State.EmptyProgress -> State.EmptyError(
                        action.error
                    )
                    is State.Refresh -> {
                        sideEffectListener(
                            SideEffect.ErrorEvent(
                                action.error
                            )
                        )
                        State.Data(
                            state.pageCount,
                            state.data
                        )
                    }
                    is State.NewPageProgress -> {
                        sideEffectListener(
                            SideEffect.ErrorEvent(
                                action.error
                            )
                        )
                        State.Data(
                            state.pageCount,
                            state.data
                        )
                    }
                    else -> state
                }
            }
        }

    class Store<T> {
        private var state: State<T> =
            State.Empty()
        var render: (State<T>) -> Unit = {}
            set(value) {
                field = value
                value(state)
            }

        private val sideEffectsExecutor = Executors.newSingleThreadExecutor()
        private val sideEffectRelay = PublishRelay.create<SideEffect>()
        val sideEffects: Observable<SideEffect> =
            sideEffectRelay
                .hide()
                .observeOn(AndroidSchedulers.mainThread())

        fun proceed(action: Action<T>) {
            Timber.d("Action: $action")
            val newState =
                reducer(action, state) { sideEffect ->
                    sideEffectsExecutor.submit { sideEffectRelay.accept(sideEffect) }
                }
            if (newState != state) {
                state = newState
                Timber.d("New state: $state")
                render(state)
            }
        }
    }
}