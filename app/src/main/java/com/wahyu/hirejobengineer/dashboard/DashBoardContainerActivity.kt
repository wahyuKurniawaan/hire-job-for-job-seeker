package com.wahyu.hirejobengineer.dashboard

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.wahyu.hirejobengineer.R
import com.wahyu.hirejobengineer.dashboard.home.HomeFragment
import com.wahyu.hirejobengineer.dashboard.offers.OffersFragment
import com.wahyu.hirejobengineer.dashboard.profile.ProfileFragment
import com.wahyu.hirejobengineer.dashboard.search.SearchFragment
import com.wahyu.hirejobengineer.databinding.ActivityDashBoardContainerBinding
import com.wahyu.hirejobengineer.util.BaseActivity

class DashBoardContainerActivity : BaseActivity() {

    private lateinit var binding: ActivityDashBoardContainerBinding
    private val homeFragment = HomeFragment()
    private val searchFragment = SearchFragment()
    private val profileFragment = ProfileFragment()
    private val offersFragment = OffersFragment()

    override fun initView() {
        makeCurrentFragment(homeFragment)
    }

    override fun initListener() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_home -> makeCurrentFragment(homeFragment)
                R.id.bottom_nav_search -> makeCurrentFragment(searchFragment)
                R.id.bottom_nav_offers -> makeCurrentFragment(offersFragment)
                R.id.bottom_nav_profile -> makeCurrentFragment(profileFragment)
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dash_board_container)
        initView()
        initListener()
    }

    override fun onBackPressed() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Confirm exit!")
            .setIcon(R.drawable.ic_baseline_exit_to_app_24)
            .setMessage("Are you sure?")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                this.finishAffinity()}
            .setNegativeButton("No") {  dialog, id -> }
        dialog.show()
    }

    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }
}