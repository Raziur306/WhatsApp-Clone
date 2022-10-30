package com.eritlab.whatsappcone.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.eritlab.whatsappcone.ui.tabLayout.Calls
import com.eritlab.whatsappcone.ui.tabLayout.Chats
import com.eritlab.whatsappcone.ui.tabLayout.Status
import java.util.PrimitiveIterator

class TabLayoutViewPagerAdapter(
    private val fragmentManager: FragmentManager,
    private val lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                Chats()
            }
            1 -> {
                Status()
            }
            2 -> {
                Calls()
            }
            else -> {
                Chats()
            }

        }
    }
}