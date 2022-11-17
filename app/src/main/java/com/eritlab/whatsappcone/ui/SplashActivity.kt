package com.eritlab.whatsappcone.ui

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.health.UidHealthStats
import android.view.WindowManager
import com.eritlab.whatsappcone.databinding.ActivitySplashBinding
import com.eritlab.whatsappcone.sharedpref.EncrypterSharedPref
import com.eritlab.whatsappcone.ui.login.WelcomeScreen
import kotlinx.coroutines.GlobalScope

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController?.hide(
                android.view.WindowInsets.Type.statusBars()
            );
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setContentView(binding.root)
        val sharedPRef = applicationContext as EncrypterSharedPref
        val uId: String = sharedPRef.getUserUid()
        val intent = if (uId.isNotEmpty()) {
            Intent(this@SplashActivity, MainActivity::class.java)
        } else {
            Intent(this@SplashActivity, WelcomeScreen::class.java)
        }

        //splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
        }, 2500)
    }
}