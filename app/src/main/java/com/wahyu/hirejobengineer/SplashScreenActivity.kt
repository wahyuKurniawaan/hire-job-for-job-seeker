package com.wahyu.hirejobengineer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.wahyu.hirejobengineer.databinding.ActivitySplashScreenBinding
import com.wahyu.hirejobengineer.util.BaseActivity

class SplashScreenActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    override fun initView() {

    }

    override fun initListener() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)

        val slideRight =
            AnimationUtils.loadAnimation(applicationContext, R.anim.slide_right_to_left)
        slideRight.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                binding.tvDescription.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(p0: Animation?) {
                @Suppress("DEPRECATION")
                Handler().postDelayed({
                    startActivity(Intent(this@SplashScreenActivity, OnBoardActivity::class.java))
                }, 1000)
            }

            override fun onAnimationRepeat(p0: Animation?) {}
        })

        val fadeIn = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                binding.tvDescription.visibility = View.GONE
            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.tvDescription.startAnimation(slideRight)
            }

            override fun onAnimationRepeat(p0: Animation?) {}
        })

        binding.ivIcon.startAnimation(fadeIn)
        initListener()
        initView()
    }
}