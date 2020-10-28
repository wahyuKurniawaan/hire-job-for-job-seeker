package com.wahyu.hirejobengineer.dashboard.home.detail.portofolio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.wahyu.hirejobengineer.R
import com.wahyu.hirejobengineer.databinding.FragmentDialogPortofolioBinding

class DialogPortofolioFragment(
    private val portofolioName: String?,
    val portofolioDescription: String?,
    val publishLink: String?,
    val repositoryLink: String?,
    val type: String?
) : DialogFragment() {

    private lateinit var binding: FragmentDialogPortofolioBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_portofolio, container, false)
        binding.tvName.text = portofolioName
        binding.tvDescription.text = portofolioDescription
        binding.tvPublishLink.text = publishLink
        binding.tvRepoLink.text = repositoryLink
        binding.tvType.text = type
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}