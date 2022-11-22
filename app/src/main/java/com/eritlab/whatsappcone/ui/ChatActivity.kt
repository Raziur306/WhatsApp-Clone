package com.eritlab.whatsappcone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.eritlab.whatsappcone.R
import com.eritlab.whatsappcone.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.default_color)
        setContentView(binding.root)
    }
}