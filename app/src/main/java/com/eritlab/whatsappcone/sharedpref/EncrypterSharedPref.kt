package com.eritlab.whatsappcone.sharedpref

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncrypterSharedPref : Application() {
    private var sharedPreferences: SharedPreferences? = null
    private lateinit var editor: SharedPreferences.Editor;
    override fun onCreate() {
        super.onCreate()
        val masterKey: MasterKey =
            MasterKey.Builder(applicationContext).setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
        sharedPreferences = EncryptedSharedPreferences.create(
            applicationContext,
            "FILE",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        editor = sharedPreferences?.edit()!!

    }

    fun saveUserUid(uid: String) {
        editor.putString("uid", uid)
        editor.apply()
    }


    fun getUserUid(): String {
        return sharedPreferences!!.getString("uid", "")!!
    }
}