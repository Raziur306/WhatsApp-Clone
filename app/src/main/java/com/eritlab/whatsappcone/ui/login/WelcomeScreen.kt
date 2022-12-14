package com.eritlab.whatsappcone.ui.login

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eritlab.whatsappcone.databinding.ActivityWelcomeScreenBinding
import com.eritlab.whatsappcone.sharedpref.EncrypterSharedPref
import com.eritlab.whatsappcone.ui.MainActivity

class WelcomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = applicationContext as EncrypterSharedPref

        binding.acceptBtn.setOnClickListener {
            startActivity(Intent(this, PhoneRegisterActivity::class.java))
            finish()
        }


    }
}