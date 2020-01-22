package com.demoss.paginatorrenderkit

import com.demoss.paginatorrenderkit.Paginator.Action
import com.demoss.paginatorrenderkit.Paginator.State
import com.demoss.paginatorrenderkit.Paginator.Store
import com.demoss.paginatorrenderkit.Paginator.reducer
import timber.log.Timber

/**
 * Original idea and huge peace of code is taken from GitFox client source code
 * all links are provided in @see[README.md]
 * or @see[https://github.com/DeMoss15/PaginatorRenderKit]
 *
 * Changes:
 * Added @param[T] for [State], [Action], [Store] and [reducer] for more restrictive data type workflow
 */
object Paginator {

    /**
     * Changes:
     * Added [getStateData]
     */
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

    /**
     * Changes:
     * Added action @see[Action.EditCurrentStateData] for compatibility with realtime paginated list
     * changes (insert, remove, update etc.)
     */
    sealed class Action<T> {
        class Refresh<T> : Action<T>()
        class Restart<T> : Action<T>()
        class LoadMore<T> : Action<T>()
        data class NewPage<T>(val pageNumber: Int, val items: List<T>, val isLastPage: Boolean) : Action<T>()
        data class PageError<T>(val error: Throwable) : Action<T>()
        data class EditCurrentStateData<T>(val transaction: (data: List<T>) -> List<T>) : Action<T>()
    }

    /**
     * Changes:
     * Added side effect @see[SideEffect.CancelLoadings] for request optimization and avoid content
     * duplication
     */
    sealed class SideEffect {
        data class LoadPage(val currentPage: Int) : SideEffect()
        data class ErrorEvent(val error: Throwable) : SideEffect()
        object CancelLoadings : SideEffect()
    }

    /**
     * The method contains logic for changing states @see[State] depending on
     * @see[Action] and current state
     * Also has side effects @see[SideEffect] to inform business layer about the state's needs
     *
     * Changes:
     * Added side effect @see[SideEffect.CancelLoadings]
     * Added handling of @see[Action.EditCurrentStateData]
     * Fixed @see[SideEffect.LoadPage] for each @see[Action.Refresh]
     *
     * @param T items' type
     * @param state current state stored in @see[Store]
     * @param sideEffectListener informs about state's needs
     */
    private fun <T> reducer(
        action: Action<T>,
        state: State<T>,
        sideEffectListener: (SideEffect) -> Unit
    ): State<T> = when (action) {
        is Action.Refresh -> {
            when (state) {
                is State.Empty -> State.EmptyProgress()
                is State.EmptyError -> State.EmptyProgress()
                is State.Data -> {
                    sideEffectListener(SideEffect.LoadPage(1))
                    State.Refresh(state.pageCount, state.data)
                }
                is State.NewPageProgress -> {
                    sideEffectListener(SideEffect.LoadPage(1))
                    State.Refresh(state.pageCount, state.data)
                }
                is State.FullData -> {
                    sideEffectListener(SideEffect.LoadPage(1))
                    State.Refresh(state.pageCount, state.data)
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
                    State.NewPageProgress(state.pageCount, state.data)
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
                            State.FullData(action.pageNumber, items)
                        } else {
                            State.Data(action.pageNumber, items)
                        }
                    }
                }
                is State.Refresh -> {
                    if (items.isEmpty()) {
                        State.Empty()
                    } else {
                        if (action.isLastPage) {
                            State.FullData(action.pageNumber, items)
                        } else {
                            State.Data(action.pageNumber, items)
                        }
                    }
                }
                is State.NewPageProgress -> {
                    if (action.isLastPage) {
                        State.FullData(action.pageNumber, state.data + items)
                    } else {
                        State.Data(action.pageNumber, state.data + items)
                    }
                }
                else -> state
            }
        }
        is Action.PageError -> {
            when (state) {
                is State.EmptyProgress -> State.EmptyError(action.error)
                is State.Refresh -> {
                    sideEffectListener(SideEffect.ErrorEvent(action.error))
                    State.Data(state.pageCount, state.data)
                }
                is State.NewPageProgress -> {
                    sideEffectListener(SideEffect.ErrorEvent(action.error))
                    State.Data(state.pageCount, state.data)
                }
                else -> state
            }
        }
        is Action.EditCurrentStateData -> {
            val editedData = action.transaction(state.getStateData())
            if (editedData == state.getStateData()) {
                state
            } else if (editedData.isEmpty()) {
                State.Empty()
            } else {
                when(state) {
                    is State.Empty,
                    is State.EmptyProgress,
                    is State.EmptyError -> State.FullData(1, editedData)
                    is State.Data -> State.Data(state.pageCount, editedData)
                    is State.Refresh -> State.Refresh(state.pageCount, editedData)
                    is State.NewPageProgress -> State.NewPageProgress(state.pageCount, editedData)
                    is State.FullData -> State.FullData(state.pageCount, editedData)
                }
            }
        }
    }

    /**
     * Store represents Paginator states to business layer
     * Every time you want to use Paginator you should create a new Store
     * Don't forget to set @property[render] and @property[executeSideEffect]
     *
     * Changes:
     * Removed RxJava dependencies
     * Added tag to logs
     *
     * @param T states' data type
     */
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