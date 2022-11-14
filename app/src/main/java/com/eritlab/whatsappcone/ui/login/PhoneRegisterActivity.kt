package com.eritlab.whatsappcone.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eritlab.whatsappcone.databinding.ActivityPhoneRegisterBinding
import com.hbb20.CountryCodePicker

class PhoneRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhoneRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhoneRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ccp.registerCarrierNumberEditText(binding.editTextCarrierNumber)



        binding.nextBtn.setOnClickListener {

        }

    }
}