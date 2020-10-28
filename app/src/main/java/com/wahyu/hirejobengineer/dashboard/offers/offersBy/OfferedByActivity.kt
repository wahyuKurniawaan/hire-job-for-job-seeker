package com.wahyu.hirejobengineer.dashboard.offers.offersBy

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import com.wahyu.hirejobengineer.R
import com.wahyu.hirejobengineer.dashboard.offers.OffersApiService
import com.wahyu.hirejobengineer.databinding.ActivityOfferedByBinding
import com.wahyu.hirejobengineer.util.ApiClient
import com.wahyu.hirejobengineer.util.BaseActivity
import com.wahyu.hirejobengineer.util.SharedPreferencesUtil

class OfferedByActivity : BaseActivity() {

    private lateinit var sharedPref: SharedPreferencesUtil
    private lateinit var binding: ActivityOfferedByBinding
    private lateinit var viewModel: OfferedByViewModel
    private fun getPhotoImage(file: String): String = "http://34.234.66.114:8080/uploads/$file"

    override fun initView() {

    }

    override fun initListener() {
        binding.buttonApproved.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Confirmation ?")
                .setIcon(R.drawable.ic_round_error_outline_24)
                .setMessage("Are you sure want to approve this project ?")
                .setCancelable(true)
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
                .setPositiveButton("Yes") { dialog, id ->
                    viewModel.callEditStatusApi(intent.getIntExtra("ID_OFFER", 0), "approved")
                    finish()
                }
            dialog.show()
        }
        binding.buttonReject.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
                .setTitle("Confirmation ?")
                .setIcon(R.drawable.ic_round_error_outline_24)
                .setMessage("Are you sure want to reject this project ?")
                .setCancelable(true)
                .setNegativeButton("No") { dialog, id -> dialog.dismiss() }
                .setPositiveButton("Yes") { dialog, id ->
                    viewModel.callEditStatusApi(intent.getIntExtra("ID_OFFER", 0), "rejected")
                    finish()
                }
            dialog.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offered_by)
        sharedPref = SharedPreferencesUtil(this)

        val service = ApiClient.getApiClient(this)?.create(OffersApiService::class.java)
        viewModel = ViewModelProvider(this).get(OfferedByViewModel::class.java)
        viewModel.setSharedPref(sharedPref)
        if (service != null) {
            viewModel.setProfileService(service)
        }

        viewModel.callProfileApi(intent.getStringExtra("EMAIL_RECRUITER")!!)

        initView()
        initListener()
        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        viewModel.isLoadingLiveData.observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })

        viewModel.isProfileLiveData.observe(this, {
            if (it) {
                val data = viewModel.listLiveData.value?.firstOrNull()

                if (data?.profileImage.isNullOrEmpty()) binding.imageProfile.setImageResource(R.drawable.ic_person)
                else Picasso.get().load(getPhotoImage(data?.profileImage!!)).into(binding.imageProfile)

                binding.tvName.text = data?.name ?: "Full Name"
                binding.tvJobTitle.text = data?.roleJob ?: "Role Job"
                binding.tvCity.text = data?.city ?: "City"
                binding.tvDescription.text = data?.description ?: "Description"
                binding.tvEmail.text = data?.email ?: "Email"
                binding.tvInstagram.text = data?.instagramLink ?: "Instagram Link"
                binding.tvLinkedin.text = data?.linkedinLink ?: "Linkedin Link"

            } else {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}