package com.example.myapplication.base

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.base.mvvm.BaseView
import com.example.myapplication.base.mvvm.BaseViewModel

abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity(), BaseView<VM> {

    abstract val layoutResourceId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceId)
        observeViewModel()
    }

    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun hideKeyboard() {
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow((currentFocus ?: View(this)).windowToken, 0)
    }
}