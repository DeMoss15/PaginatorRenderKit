package com.example.myapplication.presentation.root

import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.base.BaseActivity
import com.example.myapplication.presentation.fragments.NewsFragment
import com.example.myapplication.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel>() {

    override lateinit var viewModel: MainViewModel
    override val layoutResourceId: Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        showFragment(NewsFragment.TAG) { NewsFragment() }
    }

    private fun showFragment(tag: String, fabric: () -> Fragment) {
        with(supportFragmentManager.findFragmentByTag(tag)) {
            if (this != null) executeTransaction { attach(this@with) }
            else executeTransaction { add(container.id, fabric(), tag) }
        }
    }

    private fun executeTransaction(transaction: FragmentTransaction.() -> FragmentTransaction) {
        supportFragmentManager.beginTransaction().transaction().commit()
    }

    override fun observeViewModel() {}

    override fun stopObserveViewModel() {}
}
