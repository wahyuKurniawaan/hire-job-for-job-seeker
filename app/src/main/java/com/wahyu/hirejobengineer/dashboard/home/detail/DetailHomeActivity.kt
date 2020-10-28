package com.wahyu.hirejobengineer.dashboard.home.detail

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wahyu.hirejobengineer.R
import com.wahyu.hirejobengineer.dashboard.home.HomeApiService
import com.wahyu.hirejobengineer.dashboard.home.detail.portofolio.PortofolioAdapter
import com.wahyu.hirejobengineer.databinding.ActivityDetailHomeBinding
import com.wahyu.hirejobengineer.util.ApiClient
import com.wahyu.hirejobengineer.util.BaseActivity
import com.wahyu.hirejobengineer.util.SharedPreferencesUtil

class DetailHomeActivity : BaseActivity() {

    private lateinit var sharedPref: SharedPreferencesUtil
    private lateinit var binding: ActivityDetailHomeBinding
    private lateinit var viewModel: DetailHomeViewModel
    private fun getPhotoImage(file: String) : String = "http://34.234.66.114:8080/uploads/$file"

    override fun initView() {

    }

    override fun initListener() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  DataBindingUtil.setContentView(this, R.layout.activity_detail_home)
        sharedPref = SharedPreferencesUtil(this)

        val service = ApiClient.getApiClient(this)?.create(HomeApiService::class.java)
        viewModel = ViewModelProvider(this).get(DetailHomeViewModel::class.java)
        viewModel.setSharedPref(sharedPref)
        if (service != null) {
            viewModel.setDetailProfileService(service)
        }

        binding.recycleViewPortofolio.adapter = PortofolioAdapter()
        binding.recycleViewPortofolio.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        viewModel.callDetailProfileApi(intent.getIntExtra("id", 1))
        viewModel.callPortofolioApi(intent.getIntExtra("id", 1))
        initView()
        initListener()
        subscribeLiveData()
    }

    private fun subscribeLiveData() {
        viewModel.isDetailProfileLiveData.observe(this, {
            val data = viewModel.detailProfileLiveData.value?.firstOrNull()

            if (data?.image.isNullOrEmpty()) binding.ivProfileImage.setImageResource(R.drawable.ic_person)
            else Picasso.get().load(getPhotoImage(data?.image!!)).into(binding.ivProfileImage)

            binding.tvName.text = data?.name ?: "Full Name"
            binding.tvJobTitle.text = data?.jobTitle ?: "Job Title"
            binding.tvStatusJob.text = data?.statusJob ?: "Status Job"
            binding.tvAddress.text = data?.address ?: "Address"
            binding.tvCity.text = data?.city ?: "City"
            binding.tvDescription.text = data?.description ?: "Description"
            binding.tvSkillValue.text = data?.idSkill ?: "Skill Job Seeker"
        })
        viewModel.isLoadingLiveData.observe(this, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
        viewModel.portofolioLiveData.observe(this, {
            (binding.recycleViewPortofolio.adapter as PortofolioAdapter).addList(it)
        })
    }
}