package com.homeservices.admin.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.homeservices.admin.R
import com.homeservices.admin.databinding.FragmentAdminLoginBinding

class AdminLoginFragment : Fragment() {
    private var _binding: FragmentAdminLoginBinding? = null
    private val binding get() = _binding!!
    private var adminId: String = ""
    private var adminPassword:String = ""
    private lateinit var sharedPreferences: SharedPreferences
    private var isLogin = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAdminLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get sharedPreference value
        sharedPreferences = requireContext().getSharedPreferences(PrefName, Context.MODE_PRIVATE)
        isLogin = sharedPreferences.getBoolean(PrefKey,false)

        //check login or not
        if(isLogin){
            findNavController().navigate(R.id.action_adminLoginFragment_to_homeFragment)
        }

        //button click listener
        binding.signInButton.setOnClickListener{
            adminId = binding.adminIdEditText.text.toString().trim()
            adminPassword = binding.passwordEditText.text.toString().trim()
            if(adminId == AdminID && adminPassword == AdminPassword){
                Toast.makeText(requireActivity(),"Login Successfully",Toast.LENGTH_SHORT).show()
                sharedPreferences.edit().putBoolean(PrefKey,true).apply()
                findNavController().navigate(R.id.action_adminLoginFragment_to_homeFragment)
            }
            else{
                Toast.makeText(requireActivity(),"Invalid username and password",Toast.LENGTH_SHORT).show()
                Log.i(TAG, "onViewCreated: $adminId    $adminPassword")
            }
        }
    }


    companion object{
        const val TAG = "AdminLoginFragmentInfo"
        const val AdminID = "admin"
        const val AdminPassword = "12345"
        const val PrefName = "SaveLoginCredentials"
        const val PrefKey = "SaveLoginKey"
    }
}