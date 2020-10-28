package com.wahyu.hirejobengineer.dashboard.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wahyu.hirejobengineer.R
import com.wahyu.hirejobengineer.databinding.FragmentHomeBinding
import com.wahyu.hirejobengineer.util.ApiClient
import com.wahyu.hirejobengineer.util.SharedPreferencesUtil

class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var sharedPref: SharedPreferencesUtil

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        sharedPref = SharedPreferencesUtil(requireContext())
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val service = ApiClient.getApiClient(this.requireContext())?.create(HomeApiService::class.java)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        if (service != null) {
            viewModel.setHomeService(service)
        }

        binding.recycleView.adapter = HomeAdapter()
        binding.recycleView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        val adapter = ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.job_titles,
            R.layout.spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = this

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.topToolbar)

        subscribeLiveData()
        return binding.root
    }

    private fun subscribeLiveData() {
        viewModel.listLiveData.observe(viewLifecycleOwner, {
            (binding.recycleView.adapter as HomeAdapter).addList(it)
        })
        viewModel.isLoadingLiveData.observe(viewLifecycleOwner, {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        viewModel.callHomeApi(parent?.getItemAtPosition(position).toString())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}