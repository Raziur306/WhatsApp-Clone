package com.eritlab.whatsappcone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import com.eritlab.whatsappcone.R
import com.eritlab.whatsappcone.adapter.TabLayoutViewPagerAdapter
import com.eritlab.whatsappcone.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.default_color)
        setContentView(binding.root)
        setTabLayoutItem()
    }

    private fun setTabLayoutItem() {
        binding.tabContentViewPager.adapter =
            TabLayoutViewPagerAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(binding.tabLayout, binding.tabContentViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> {
                    "CHATS"
                }
                1 -> {
                    "STATUS"
                }
                3 -> {
                    "CALLS"
                }
                else -> {
                    "CHATS"
                }
            }
        }.attach()
    }
}