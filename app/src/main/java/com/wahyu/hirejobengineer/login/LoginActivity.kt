package com.wahyu.hirejobengineer.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wahyu.hirejobengineer.R
import com.wahyu.hirejobengineer.dashboard.DashBoardContainerActivity
import com.wahyu.hirejobengineer.databinding.ActivityLoginBinding
import com.wahyu.hirejobengineer.register.RegisterActivity
import com.wahyu.hirejobengineer.util.ApiClient
import com.wahyu.hirejobengineer.util.BaseActivity
import com.wahyu.hirejobengineer.util.Key
import com.wahyu.hirejobengineer.util.SharedPreferencesUtil

class LoginActivity : BaseActivity() {

    private var backPressedTime: Long = 0
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPreferencesUtil
    private lateinit var viewModel: LoginViewModel

    override fun initView() {
        binding.etInputEmail.text = sharedPref.getString(Key.PREF_EMAIL)?.toEditable()
        binding.etInputPassword.text = sharedPref.getString(Key.PREF_PASSWORD)?.toEditable()
    }

    override fun initListener() {
        binding.tvRegisterHere.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.buttonLogin.setOnClickListener {
            viewModel.callSignInApi(
                binding.etInputEmail.text.toString(),
                binding.etInputPassword.text.toString()
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        sharedPref = SharedPreferencesUtil(this)

        val service = ApiClient.getApiClient(this)?.create(LoginApiService::class.java)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.setSharedPref(sharedPref)

        if (service != null) {
            viewModel.setLoginService(service)
        }
        initView()
        initListener()
        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        viewModel.isLoadingLiveData.observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.isLoginLiveData.observe(this, {
            if (it) {
                sharedPref.put(Key.PREF_EMAIL, binding.etInputEmail.text.toString())
                sharedPref.put(Key.PREF_PASSWORD, binding.etInputPassword.text.toString())
                val intent = Intent(this, DashBoardContainerActivity::class.java)
                startActivity(intent)
            } else {
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Error Login")
                    .setIcon(R.drawable.ic_round_error_outline_24)
                    .setMessage(viewModel.errorMessageLiveData.value)
                    .setCancelable(true)
                    .setPositiveButton("Ok") { dialog, id ->
                        dialog.dismiss()
                    }
                dialog.show()
            }
        })
    }

    override fun onBackPressed() {
        val time = System.currentTimeMillis()
        if (time - backPressedTime > 2000) {
            backPressedTime = time
            Toast.makeText(this, "Press again to close", Toast.LENGTH_SHORT).show()
        } else super.onBackPressed()
    }
}