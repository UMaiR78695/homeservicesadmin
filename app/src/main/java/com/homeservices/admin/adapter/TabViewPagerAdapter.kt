package com.homeservices.admin.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.homeservices.admin.ui.fragments.ActiveMaidFragment
import com.homeservices.admin.ui.fragments.AddMaidFragment
import com.homeservices.admin.ui.fragments.BuyerRequestFragment
import com.homeservices.admin.ui.fragments.HomeFragment

@Suppress("DEPRECATION")
class TabViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> ActiveMaidFragment()
            1 -> BuyerRequestFragment()
            2 -> AddMaidFragment()
            else -> ActiveMaidFragment()
        }
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Home"
            1 -> "Buyer Request"
            2 -> "Add  maid"
            else -> "Home"
        }
        return super.getPageTitle(position)
    }
}