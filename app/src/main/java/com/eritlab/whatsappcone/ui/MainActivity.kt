package com.eritlab.whatsappcone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.LinearLayoutManager
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
            when (position) {
                0 -> {
                    tab.icon =
                        ContextCompat.getDrawable(this, R.drawable.ic_baseline_photo_camera_24)
                }
                1 -> {
                    tab.text = "CHATS"
                }
                2 -> {
                    tab.text = "STATUS"
                }
                3 -> {
                    tab.text = "CALLS"
                }
                else -> {
                    tab.text = "CHATS"
                }
            }


        }.attach()


        //resize camera tab
        val layout = (binding.tabLayout[0] as LinearLayout).getChildAt(0)
        val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0.5F
        layout.layoutParams = layoutParams

    }
}