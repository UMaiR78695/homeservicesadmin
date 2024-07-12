package com.homeservices.admin.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.homeservices.admin.adapter.TabViewPagerAdapter
import com.homeservices.admin.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //button click listener
        clickListenerOnButton()

        //set fragments category in tab layout with viewpager
        binding.viewPager.adapter = TabViewPagerAdapter(childFragmentManager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    private fun clickListenerOnButton() {
        binding.logoutButtonImageview.setOnClickListener {
            sharedPreferences = requireContext().getSharedPreferences(AdminLoginFragment.PrefName, Context.MODE_PRIVATE)
            sharedPreferences.edit().putBoolean(AdminLoginFragment.PrefKey,false).apply()
            requireActivity().finish()
        }
    }
}