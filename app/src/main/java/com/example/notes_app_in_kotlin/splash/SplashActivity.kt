package com.example.notes_app_in_kotlin.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.notes_app_in_kotlin.R
import com.example.notes_app_in_kotlin.databinding.ActivitySplashBinding
import com.example.notes_app_in_kotlin.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        initializeFields()

    }

    private fun initializeFields() {

        val SPLASH_SCREEN_TIME_OUT = 3000

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        },SPLASH_SCREEN_TIME_OUT.toLong())


    }
}