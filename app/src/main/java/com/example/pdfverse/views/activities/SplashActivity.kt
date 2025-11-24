package com.example.pdfverse.views.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.pdfverse.R
import com.example.pdfverse.databinding.ActivitySplashBinding
import com.example.pdfverse.utils.StartActivity
import com.example.pdfverse.utils.setStatusBarAppearanceAndColor

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val myActivity = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        setStatusBarAppearanceAndColor(R.color.white)
//        setStatusBarColor(window, R.color.sky_blue_alpha65)

        moveToIndexScreen()
    }

    private fun moveToIndexScreen() {
        // Using Handler to delay the start of another activity
        Handler(Looper.getMainLooper()).postDelayed({
            myActivity.StartActivity(HomeActivity::class.java, finishActivity = true)
            // Finish splash activity to prevent user from going back
//            finish()
        }, 1500)
    }
}