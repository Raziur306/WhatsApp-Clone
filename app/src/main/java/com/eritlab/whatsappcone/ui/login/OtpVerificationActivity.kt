package com.eritlab.whatsappcone.ui.login

import android.app.ActivityOptions
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eritlab.whatsappcone.databinding.ActivityOtpVerificationBinding

class OtpVerificationActivity : AppCompatActivity() {
    private lateinit var phoneNumber: String
    private lateinit var binding: ActivityOtpVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        phoneNumber = intent.getStringExtra("number")!!
        binding.phoneNumber.text = phoneNumber
        binding.phoneNumberView.text = phoneNumber
    }
}