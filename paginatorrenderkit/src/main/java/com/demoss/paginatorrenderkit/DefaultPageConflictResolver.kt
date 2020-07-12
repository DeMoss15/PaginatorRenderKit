package com.demoss.paginatorrenderkit

import android.util.Log

class DefaultPageConflictResolver : PageConflictResolver {

    companion object {
        const val LOG_TAG = "PaginationConflict"
    }

    override fun <T> resolve(
        action: Paginator.Action.NewPage<T>,
        state: Paginator.State.NewPageProgress<T>
    ): Paginator.State<T> {
        if (BuildConfig.DEBUG) {
            Log.e(
                LOG_TAG,
                "HEADS UP! You have a conflict in pagination between displayed data and new page!\n" +
                        "Paginator will resolve it, but don't ignore this message!\n" +
                        "It may lead to items duplication or wrong items placement in the list!"
            )
        }
        if (action.items.isEmpty()) {
            return createCorrectState(action.isLastPage, state.pageCount, state.data)
        }
        if (action.pageNumber > (state.pageCount + 1)) {
            return createCorrectState(
                action.isLastPage,
                action.pageNumber,
                state.data + action.items
            )
        }

        val pageEntryIndex = calculatePageEntryIndex(action.items, state.data)
        if (pageEntryIndex < 0) {
            val possiblePageEntry = calculatePossiblePageEntryIndex(
                dataSize = state.data.size,
                pageSize = action.items.size,
                pageNumber = action.pageNumber
            )
            val newStateData = insertNewPageInData(possiblePageEntry, action, state)
            return createCorrectState(action.isLastPage, state.pageCount, newStateData)
        }

        val pageEndIndex = pageEntryIndex + action.items.size
        if (pageEndIndex > state.data.lastIndex && BuildConfig.DEBUG) {
            throw RuntimeException(
                "Something went totally wrong in your pagination logic.\n" +
                        "New page is already displayed but displayed data doesn't contain the whole page :("
            )
        }

        val newStateData = replacePageInData(
            state.data,
            action.items,
            pageEntryIndex,
            pageEndIndex
        )
        return createCorrectState(action.isLastPage, state.pageCount, newStateData)
    }

    private fun <T> createCorrectState(
        isLastPage: Boolean,
        pageCount: Int,
        stateData: List<T>
    ): Paginator.State<T> {
        return if (isLastPage) {
            Paginator.State.FullData(pageCount, stateData)
        } else {
            Paginator.State.Data(pageCount, stateData)
        }
    }

    private fun <T> calculatePageEntryIndex(pageItems: List<T>, data: List<T>): Int {
        var indexOfPageEntry = -1
        pageItems.forEachIndexed { itemIndexInPage, item ->
            val itemIndexInData = data.indexOf(item)
            if (itemIndexInData >= 0) {
                indexOfPageEntry = itemIndexInData - itemIndexInPage
                return@forEachIndexed
            }
        }
        return indexOfPageEntry
    }

    private fun calculatePossiblePageEntryIndex(
        dataSize: Int,
        pageSize: Int,
        pageNumber: Int
    ): Int {
        val itemsInPreviousPages = pageSize * (pageNumber - 1)
        return if (dataSize < itemsInPreviousPages) 0 else itemsInPreviousPages - 1
    }

    private fun <T> insertNewPageInData(
        possiblePageEntry: Int,
        action: Paginator.Action.NewPage<T>,
        state: Paginator.State.NewPageProgress<T>
    ): List<T> {
        return state.data.toMutableList().apply { addAll(possiblePageEntry, action.items) }
    }

    private fun <T> replacePageInData(
        data: List<T>,
        page: List<T>,
        pageEntryIndex: Int,
        pageEndIndex: Int
    ): List<T> {
        val result = data.subList(0, pageEntryIndex) + page
        return if (data.lastIndex > pageEndIndex) {
            result + data.subList(pageEndIndex, data.lastIndex)
        } else {
            result
        }
    }
}