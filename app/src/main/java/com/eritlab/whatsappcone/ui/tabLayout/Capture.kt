package com.eritlab.whatsappcone.ui.tabLayout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.eritlab.whatsappcone.R
import com.eritlab.whatsappcone.databinding.FragmentCaptureBinding

class Capture : Fragment() {
    private lateinit var binding: FragmentCaptureBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCaptureBinding.inflate(layoutInflater)




        return binding.root
    }


}