package com.wahyu.hirejobengineer.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.wahyu.hirejobengineer.R
import com.wahyu.hirejobengineer.databinding.ActivityRegisterBinding
import com.wahyu.hirejobengineer.login.LoginActivity
import com.wahyu.hirejobengineer.util.ApiClient
import com.wahyu.hirejobengineer.util.BaseActivity
import com.wahyu.hirejobengineer.util.Key
import com.wahyu.hirejobengineer.util.SharedPreferencesUtil

class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var sharedPref: SharedPreferencesUtil
    private lateinit var viewModel: RegisterViewModel

    override fun initView() {

    }

    override fun initListener() {
        binding.buttonRegister.setOnClickListener {
            if (binding.etInputPassword.text.toString() == binding.etInputConfirmPassword.text.toString()) {
                viewModel.callSignUpApi(
                    binding.etInputFullName.text.toString(),
                    binding.etInputEmail.text.toString(),
                    binding.etInputPassword.text.toString(),
                    binding.etInputPhoneNumber.text.toString()
                )
            } else Toast.makeText(this, "The password and confirm password do not match!", Toast.LENGTH_SHORT).show()
        }

        binding.tvLoginHere.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        sharedPref = SharedPreferencesUtil(this)
        val service = ApiClient.getApiClient(this)?.create(RegisterApiService::class.java)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        viewModel.setSharedPref(sharedPref)

        if (service != null) {
            viewModel.setRegisterService(service)
        }

        initView()
        initListener()
        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        viewModel.isLoadingLiveData.observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.isRegisterLiveData.observe(this, {
            if (it) {
                sharedPref.put(Key.PREF_NAME, binding.etInputFullName.editableText.toString())
                sharedPref.put(Key.PREF_EMAIL, binding.etInputEmail.text.toString())
                sharedPref.put(Key.PREF_PASSWORD, binding.etInputPassword.text.toString())
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Error Register")
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
}