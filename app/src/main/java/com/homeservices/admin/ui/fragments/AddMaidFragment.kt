package com.homeservices.admin.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.homeservices.admin.R
import com.homeservices.admin.databinding.FragmentAddMaidBinding
import com.homeservices.admin.extensions.showToast
import com.homeservices.admin.model.Maid


private const val TAG = "AddMaidFragmentInfo"
class AddMaidFragment : Fragment() {

    private var _binding: FragmentAddMaidBinding? = null
    private val binding get() = _binding!!

    private var maidName = ""
    private var cnic = ""
    private var phone = ""
    private var age = ""
    private var priceRange = ""
    private var experience = ""
    private var area = ""
    private var availability = ""
    private val services = mutableListOf<String>()
    private var database = Firebase.database.reference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddMaidBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListenerButton()
        binding.availabilityChipGroup.setOnCheckedChangeListener { chipGroup, i ->
            val chip: Chip = chipGroup.findViewById(i)
            availability = chip.text.toString()
        }
    }

    private fun clickListenerButton() {
        binding.addMaidButton.setOnClickListener {
            getInput()
            if (validateInput()) {
                val maid = Maid(
                    System.currentTimeMillis().toString(), maidName, cnic, phone, age, priceRange,
                    experience, area, availability, services
                )
                database.child("maids").child(maid.maidId).setValue(maid)
                resetAllFields()
            }
        }
    }

    private fun getInput() {
        maidName = binding.enterMaidNameEditText.text.toString().trim()
        cnic = binding.enterCnicEditText.text.toString().trim()
        phone = binding.enterPhoneEditText.text.toString().trim()
        age = binding.enterAgeEditText.text.toString().trim()
        priceRange = binding.enterPriceRangeEditText.text.toString().trim()
        experience = binding.experienceEditText.text.toString().trim()
        area = binding.enterAreaEditText.text.toString().trim()
        services.clear()
        binding.serviceChipGroup.checkedChipIds.forEach { id ->
            val chip: Chip = binding.serviceChipGroup.findViewById(id)
            services.add(chip.text.toString())
        }
    }

    private fun validateInput(): Boolean {
        return if (maidName.isEmpty()) {
            requireActivity().showToast(getString(R.string.enterMaidName))
            false
        } else if (phone.isEmpty()) {
            requireActivity().showToast(getString(R.string.enterPhone))
            false
        } else if (phone.length != 11) {
            requireActivity().showToast(getString(R.string.phone_num_not_valid))
            false
        } else if (cnic.isEmpty()) {
            requireActivity().showToast(getString(R.string.enterCNIC))
            false
        } else if (age.isEmpty()) {
            requireActivity().showToast(getString(R.string.enterAge))
            false
        } else if (priceRange.isEmpty()) {
            requireActivity().showToast(getString(R.string.enterPrice))
            false
        } else if (experience.isEmpty()) {
            requireActivity().showToast(getString(R.string.enterExperienceInYears))
            false
        } else if (area.isEmpty()) {
            requireActivity().showToast(getString(R.string.enterArea))
            false
        } else if (availability.isEmpty()) {
            requireActivity().showToast(getString(R.string.selectOneAvailability))
            false
        } else if (services.isEmpty()) {
            requireActivity().showToast(getString(R.string.selectOneService))
            false
        } else {
            true
        }
    }

    private fun resetAllFields(){
        binding.enterMaidNameEditText.text = null
        binding.enterCnicEditText.text = null
        binding.enterPhoneEditText.text = null
        binding.enterAgeEditText.text = null
        binding.enterPriceRangeEditText.text = null
        binding.experienceEditText.text = null
        binding.enterAreaEditText.text = null
    }
}