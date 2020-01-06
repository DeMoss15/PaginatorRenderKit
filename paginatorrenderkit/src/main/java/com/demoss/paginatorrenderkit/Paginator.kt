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
        data class EditCurrentStateData<T>(val transaction: (data: List<T>) -> List<T>) : Action<T>()
    }

    sealed class SideEffect {
        data class LoadPage(val currentPage: Int) : SideEffect()
        data class ErrorEvent(val error: Throwable) : SideEffect()
        object CancelLoadings : SideEffect()
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
            is Action.EditCurrentStateData -> {
                val editedData = action.transaction(state.getStateData())
                when(state) {
                    is State.Empty -> State.Empty()
                    is State.EmptyProgress -> State.EmptyProgress()
                    is State.EmptyError -> State.EmptyError(state.error)
                    is State.Data -> State.Data(state.pageCount, editedData)
                    is State.Refresh -> State.Refresh(state.pageCount, editedData)
                    is State.NewPageProgress -> State.NewPageProgress(state.pageCount, editedData)
                    is State.FullData -> State.FullData(state.pageCount, editedData)
                }
            }
        }

    class Store<T> {

        companion object {
            const val PAGINATOR_LOG_TAG = "PAGINATOR"
        }

        private var state: State<T> = State.Empty()
        var render: (State<T>) -> Unit = {}
            set(value) {
                field = value
                value(state)
            }
        var executeSideEffect: (SideEffect) -> Unit = {}

        fun proceed(action: Action<T>) {
            Timber.tag(PAGINATOR_LOG_TAG).d("Action: $action")
            val newState = reducer(action, state) { sideEffect ->
                executeSideEffect(sideEffect)
            }
            if (newState != state) {
                state = newState
                Timber.tag(PAGINATOR_LOG_TAG).d("New state: $state")
                render(state)
            }
        }
    }
}