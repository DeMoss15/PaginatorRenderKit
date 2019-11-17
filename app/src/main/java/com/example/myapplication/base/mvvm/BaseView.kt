package com.example.myapplication.base.mvvm

interface BaseView<VM : BaseViewModel> {

    val viewModel: VM

    fun observeViewModel()
    fun stopObserveViewModel()
}