package com.eritlab.whatsappcone.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eritlab.whatsappcone.databinding.ActivityLoginProfileBinding
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.EmojiPopup

class LoginProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginProfileBinding.inflate(layoutInflater)

        binding.emojiPicker.setOnClickListener {
           // val emojiPopup = EmojiPopup(binding.root, binding.userName)
            //emojiPopup.toggle() // Toggles visibility of the Popup.
            //    emojiPopup.dismiss() // Dismisses the Popup.
            //   emojiPopup.isShowing() // Returns true when Popup is showing.
        }


        setContentView(binding.root)
    }
}