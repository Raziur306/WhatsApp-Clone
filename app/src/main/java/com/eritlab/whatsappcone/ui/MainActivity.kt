package com.eritlab.whatsappcone.ui

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.method.ReplacementTransformationMethod
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams
import androidx.core.content.ContextCompat
import androidx.core.view.ViewGroupCompat
import androidx.core.view.get
import androidx.core.view.marginTop
import androidx.core.view.setMargins
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.LinearLayoutManager
import com.eritlab.whatsappcone.R
import com.eritlab.whatsappcone.adapter.TabLayoutViewPagerAdapter
import com.eritlab.whatsappcone.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private var currentTab = 1;
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            currentTab = savedInstanceState.getInt("currentTab")
        }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.default_color)
        setContentView(binding.root)

        setTabLayoutItem()


//animate to hide top bar
        val typedValue = TypedValue().apply {
            this@MainActivity.theme.resolveAttribute(
                android.R.attr.actionBarSize,
                this,
                true
            )
        }
        val height = TypedValue.complexToDimension(typedValue.data, resources.displayMetrics)
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    binding.tabLayout.animate()
                        .translationY(binding.tabLayout.height.toFloat() * -1)
                        .withStartAction {
                            binding.appbar.animate()
                                .translationY(binding.appbar.height.toFloat() * -1)
                                .withEndAction {
                                    binding.tabLayout.visibility = View.GONE
                                    binding.appbar.visibility = View.GONE
                                }
                                .start()
                            val params = binding.tabContentViewPager.layoutParams as LayoutParams
                            params.setMargins(0, height.toInt() * -1, 0, 0)
                            binding.tabContentViewPager.layoutParams = params
                        }

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val params = binding.tabContentViewPager.layoutParams as LayoutParams
                if (tab?.position == 0) {
                    binding.appbar.animate().translationY(0f)
                        .withStartAction {
                            binding.tabLayout.visibility = View.VISIBLE
                            binding.appbar.visibility = View.VISIBLE
                            binding.tabLayout.animate().translationY(0f)
                                .start()
                            params.setMargins(0, height.toInt(), 0, 0)
                            binding.tabContentViewPager.layoutParams = params
                        }
                        .start()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })


    }

    //save current tab
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("currentTab", 0)//binding.tabContentViewPager.currentItem)
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

//preselected tab item
        binding.tabLayout.getTabAt(currentTab)?.select()

    }


}