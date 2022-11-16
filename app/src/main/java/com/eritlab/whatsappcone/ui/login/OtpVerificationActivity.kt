package com.eritlab.whatsappcone.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.eritlab.whatsappcone.databinding.ActivityOtpVerificationBinding
import com.eritlab.whatsappcone.ui.MainActivity
import com.google.android.gms.common.internal.Objects.ToStringHelper
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class OtpVerificationActivity : AppCompatActivity() {
    private lateinit var phoneNumber: String
    private var timeInterval = 60000L
    private lateinit var timer: CountDownTimer
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var firebaseAUth: FirebaseAuth
    private lateinit var verificationID: String
    private lateinit var firestore: FirebaseFirestore
    private lateinit var binding: ActivityOtpVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        phoneNumber = intent.getStringExtra("number")!!
        binding.phoneNumber.text = phoneNumber
        binding.phoneNumberView.text = phoneNumber
        firebaseAUth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()

        binding.verifyBtn.setOnClickListener {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            binding.progressBar.visibility = View.VISIBLE
            val verificationCode = binding.verificationCodeEditText.text.toString()
            if (verificationCode.isNotEmpty() && verificationCode.toString().length == 6) {
                createCredential(verificationCode)
            } else
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
        }
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(
                        this@OtpVerificationActivity,
                        "Invalid Request",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (e is FirebaseTooManyRequestsException) {
                    Toast.makeText(
                        this@OtpVerificationActivity,
                        "The SMS quota for the project has been exceeded", Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                Toast.makeText(
                    this@OtpVerificationActivity,
                    "OTP send successful",
                    Toast.LENGTH_SHORT
                ).show()
                timeInterval *= 2
                verificationID = verificationId
                timer.start()
            }
        }
        //phone auth
        val option = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()


//        timer = object : CountDownTimer(timeInterval, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                val diffSeconds = millisUntilFinished / 1000;
//                binding.resendSMS.text = diffSeconds.toString() + "s"
//            }
//
//            override fun onFinish() {
//                binding.resendSMS.apply {
//                    text = "Resend"
//                    isClickable = true
//                }
//            }
//
//        }

        sendOTP(option)
        //resend otp
        binding.resendSMS.setOnClickListener {
            binding.resendSMS.isClickable = false
            sendOTP(option)
        }

        timer = object : CountDownTimer(timeInterval, 10000) {
            override fun onTick(millisUntilFinished: Long) {
                val timeInSecond = millisUntilFinished / 1000;
                binding.resendSMS.text = timeInSecond.toString() + " s"
            }

            override fun onFinish() {
                binding.resendSMS.isClickable = true

            }

        }

    }


    private fun sendOTP(option: PhoneAuthOptions) {
        PhoneAuthProvider.verifyPhoneNumber(option)
        binding.resendSMS.isClickable = false


    }

    private fun createCredential(verificationCode: String) {
        val credential = PhoneAuthProvider.getCredential(verificationID, verificationCode)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        val hashMap = HashMap<String, Any?>()
        hashMap["phone"] = phoneNumber
        firebaseAUth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                firestore.collection("users").document(firebaseAUth.uid.toString()).set(hashMap)
                    .addOnCompleteListener {
                        if (task.isSuccessful) {
                            startActivity(
                                Intent(
                                    this@OtpVerificationActivity,
                                    MainActivity::class.java
                                )
                            )
                            finish()
                        } else {
                            Toast.makeText(
                                this@OtpVerificationActivity,
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            )
                        }
                    }
                binding.progressBar.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            } else {
                binding.progressBar.visibility = View.GONE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                Toast.makeText(this@OtpVerificationActivity, "Invalid OTP", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}