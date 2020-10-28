package com.wahyu.hirejobengineer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.wahyu.hirejobengineer.databinding.ActivityOnBoardBinding
import com.wahyu.hirejobengineer.login.LoginActivity
import com.wahyu.hirejobengineer.util.BaseActivity

class OnBoardActivity : BaseActivity() {

    private lateinit var binding: ActivityOnBoardBinding
    override fun initView() {

    }

    override fun initListener() {
        binding.buttonLoginForRecruiter.setOnClickListener {
            Toast.makeText(this, "error !", Toast.LENGTH_SHORT).show()
        }
        binding.buttonLoginForJobSeeker.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_on_board)
        initListener()
        initView()
    }

    override fun onBackPressed() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Confirm exit!")
            .setIcon(R.drawable.ic_baseline_exit_to_app_24)
            .setMessage("Are you sure?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                super.onBackPressed()}
            .setNegativeButton("No") {  dialog, id -> }
        dialog.show()
    }
}