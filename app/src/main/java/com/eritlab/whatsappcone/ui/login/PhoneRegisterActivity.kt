package com.eritlab.whatsappcone.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eritlab.whatsappcone.databinding.ActivityPhoneRegisterBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit

class PhoneRegisterActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityPhoneRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.ccp.registerCarrierNumberEditText(binding.editTextCarrierNumber)
        binding.nextBtn.setOnClickListener {
            startActivity(Intent(this, OtpVerificationActivity::class.java).apply {
                putExtra("number", binding.ccp.fullNumberWithPlus)
            })
        }

    }
}