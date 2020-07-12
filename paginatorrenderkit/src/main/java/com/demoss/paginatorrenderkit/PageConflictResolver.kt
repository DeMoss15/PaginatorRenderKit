package com.demoss.paginatorrenderkit

interface PageConflictResolver {

    fun <T> resolve(
        action: Paginator.Action.NewPage<T>,
        state: Paginator.State.NewPageProgress<T>
    ): Paginator.State<T>
}