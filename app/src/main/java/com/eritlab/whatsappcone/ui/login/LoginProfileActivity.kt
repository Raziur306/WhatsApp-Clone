package com.eritlab.whatsappcone.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eritlab.whatsappcone.databinding.ActivityLoginProfileBinding
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.google.GoogleEmojiProvider

class LoginProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EmojiManager.install(GoogleEmojiProvider())
        binding = ActivityLoginProfileBinding.inflate(layoutInflater)
        val emojiPopup = EmojiPopup(binding.root, binding.emojiEditText)
        binding.emojiPicker.setOnClickListener {
            emojiPopup.toggle()
        }
        setContentView(binding.root)
        binding.profileImagePicker.setOnClickListener {
            checkPermission()
        }

        binding.nextBtn.setOnClickListener {
            val name = binding.emojiEditText.text
            if (name!!.isNotEmpty()) {
                val hash = HashMap<String, Any?>()
                hash["name"] = name
            } else {
                binding.emojiEditText.error = "Can't be empty."
            }
        }


    }

    private fun checkPermission() {

    }
}