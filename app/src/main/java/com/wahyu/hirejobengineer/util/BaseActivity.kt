package com.wahyu.hirejobengineer.util

import android.text.Editable
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    abstract fun initView()
    abstract fun initListener()

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

}