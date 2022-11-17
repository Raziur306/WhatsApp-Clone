package com.eritlab.whatsappcone.ui.login

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.eritlab.whatsappcone.databinding.ActivityLoginProfileBinding
import com.eritlab.whatsappcone.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.google.GoogleEmojiProvider
import kotlinx.coroutines.MainScope
import java.net.URI

class LoginProfileActivity : AppCompatActivity() {
    private var readPermissionGranted: Boolean = false
    private var writePermissionGranted: Boolean = false
    private lateinit var binding: ActivityLoginProfileBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var resultLauncher: ActivityResultLauncher<String>
    private var profileImageUri: Uri? = null
    private val hashMap = HashMap<String, Any?>()
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
            updateOrRequestPermission()
        }

        binding.nextBtn.setOnClickListener {
            val name = binding.emojiEditText.text

            if (name!!.isNotEmpty()) {
                hashMap["name"] = name
            } else {
                binding.emojiEditText.error = "Can't be empty."
            }
            if (profileImageUri != null) {
                uploadImage()
            } else {
                fireStoreData()
            }


        }


        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
                readPermissionGranted = permission[READ_EXTERNAL_STORAGE] ?: readPermissionGranted
                writePermissionGranted =
                    permission[WRITE_EXTERNAL_STORAGE] ?: writePermissionGranted
            }

        //picking image
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri.let { uri ->
                    profileImageUri = uri
                    Glide.with(this).load(uri).into(binding.profileImagePicker)
                }
            }
    }


    private fun updateOrRequestPermission() {
        val readPermission = ContextCompat.checkSelfPermission(
            this, READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val writePermission = ContextCompat.checkSelfPermission(
            this, WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        readPermissionGranted = readPermission
        writePermissionGranted = writePermission || (SDK_INT >= Build.VERSION_CODES.Q)

        //permission request list
        val permissionRequestList = mutableListOf<String>()
        if (!writePermissionGranted) {
            permissionRequestList.add(WRITE_EXTERNAL_STORAGE)
        }
        if (!readPermissionGranted) {
            permissionRequestList.add(READ_EXTERNAL_STORAGE)
        }
        permissionLauncher.launch(permissionRequestList.toTypedArray())
        if (writePermissionGranted || readPermissionGranted) {
            pickImage()
        }
    }

    private fun pickImage() {
        resultLauncher.launch("image/*")
    }

    private fun uploadImage() {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef =
            storageRef.child(System.currentTimeMillis().toString() + "" + getImageExtension())
        storageRef.putFile(profileImageUri!!).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener {
                hashMap["profile_url"] = it
                fireStoreData()
            }
        }.addOnFailureListener {
            Toast.makeText(this@LoginProfileActivity, it.message.toString(), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getImageExtension(): String? {
        val contentResolver = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(profileImageUri!!))
    }

    //update firestore data
    private fun fireStoreData() {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("users").document(FirebaseAuth.getInstance().uid.toString())
            .update(hashMap).addOnCompleteListener {
                if (it.isSuccessful) {
                    startActivity(Intent(this@LoginProfileActivity, MainActivity::class.java))
                    finish()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this@LoginProfileActivity,
                    "Something went wrong",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}