package com.demoss.paginatorrenderkit

import timber.log.Timber

object Paginator {

    sealed class State<T> {

        open fun getStateData(): List<T> = emptyList<T>()

        class Empty<T> : State<T>()
        class EmptyProgress<T> : State<T>()
        data class EmptyError<T>(val error: Throwable) : State<T>()
        data class Data<T>(val pageCount: Int, val data: List<T>) : State<T>() {
            override fun getStateData(): List<T> = data
        }

        data class Refresh<T>(val pageCount: Int, val data: List<T>) : State<T>() {
            override fun getStateData(): List<T> = data
        }

        data class NewPageProgress<T>(val pageCount: Int, val data: List<T>) : State<T>() {
            override fun getStateData(): List<T> = data
        }

        data class FullData<T>(val pageCount: Int, val data: List<T>) : State<T>() {
            override fun getStateData(): List<T> = data
        }
    }

    sealed class Action<T> {
        class Refresh<T> : Action<T>()
        class Restart<T> : Action<T>()
        class LoadMore<T> : Action<T>()
        data class NewPage<T>(val pageNumber: Int, val items: List<T>, val isLastPage: Boolean) : Action<T>()
        data class PageError<T>(val error: Throwable) : Action<T>()
        // TODO: review next actions
        data class AddItems<T>(val position: Int, val items: List<T>) : Action<T>()
        data class UpdateItem<T>(val item: T, val compareItemPredicate: (other: T) -> Boolean) : Action<T>()
        data class DeleteItem<T>(val index: Int) : Action<T>()
    }

    sealed class SideEffect {
        data class LoadPage(val currentPage: Int) : SideEffect()
        data class ErrorEvent(val error: Throwable) : SideEffect()
        object CancelLoadings : SideEffect()
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
                when (state) {
                    is State.Empty -> State.EmptyProgress()
                    is State.EmptyError -> State.EmptyProgress()
                    is State.Data -> {
                        sideEffectListener(SideEffect.LoadPage(1))
                        State.Refresh(
                            state.pageCount,
                            state.data
                        )
                    }
                    is State.NewPageProgress -> {
                        sideEffectListener(SideEffect.LoadPage(1))
                        State.Refresh(
                            state.pageCount,
                            state.data
                        )
                    }
                    is State.FullData -> {
                        sideEffectListener(SideEffect.LoadPage(1))
                        State.Refresh(
                            state.pageCount,
                            state.data
                        )
                    }
                    else -> state
                }
            }
            is Action.Restart -> {
                sideEffectListener(SideEffect.CancelLoadings)
                sideEffectListener(SideEffect.LoadPage(1))
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
                        sideEffectListener(SideEffect.LoadPage(state.pageCount + 1))
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
                            if (action.isLastPage) {
                                State.FullData(1, items)
                            } else {
                                State.Data(1, items)
                            }
                        }
                    }
                    is State.Refresh -> {
                        if (items.isEmpty()) {
                            State.Empty()
                        } else {
                            if (action.isLastPage) {
                                State.FullData(1, items)
                            } else {
                                State.Data(1, items)
                            }
                        }
                    }
                    is State.NewPageProgress -> {
                        if (action.isLastPage) {
                            State.FullData(
                                state.pageCount + 1,
                                state.data + items
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
            is Action.AddItems -> {
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
                        sideEffectListener(SideEffect.ErrorEvent(action.error))
                        State.Data(
                            state.pageCount,
                            state.data
                        )
                    }
                    is State.NewPageProgress -> {
                        sideEffectListener(SideEffect.ErrorEvent(action.error))
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
        private var state: State<T> = State.Empty()
        var render: (State<T>) -> Unit = {}
            set(value) {
                field = value
                value(state)
            }

        var executeSideEffect: (SideEffect) -> Unit = {}

        fun proceed(action: Action<T>) {
            Timber.d("Action: $action")
            val newState = reducer(action, state) { sideEffect ->
                executeSideEffect(sideEffect)
            }
            if (newState != state) {
                state = newState
                Timber.d("New state: $state")
                render(state)
            }
        }
    }
}