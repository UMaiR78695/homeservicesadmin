package com.homeservices.admin.ui.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.homeservices.admin.adapter.MaidListAdapter
import com.homeservices.admin.databinding.FragmentActiveMaidBinding
import com.homeservices.admin.model.Maid

private const val TAG = "ActiveMaidFragmentInfo"
class ActiveMaidFragment : Fragment() {
    private var _binding: FragmentActiveMaidBinding? = null
    private val binding get() = _binding!!
    private lateinit var maidListAdapter: MaidListAdapter
    private lateinit var database: DatabaseReference
    var progressDialog: ProgressDialog? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentActiveMaidBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //read data from database
        readMaidListFromDatabase()

        maidListAdapter = MaidListAdapter()
        binding.maidListRecyclerview.layoutManager = LinearLayoutManager(requireActivity())
        binding.maidListRecyclerview.adapter = maidListAdapter
    }

    private fun readMaidListFromDatabase() {
        loadingProgressDialogue()
        database = FirebaseDatabase.getInstance().getReference("maids")
        database.orderByChild("active").equalTo(true).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var maidList : ArrayList<Maid> = ArrayList()
                for (postSnapshot in dataSnapshot.children) {
                    var data = postSnapshot.getValue(Maid::class.java)
                    maidList.add(data!!)
                    Log.i(TAG, "onDataChange: $postSnapshot    ${postSnapshot.value}   //   ${data.maidName}")
                }
                maidListAdapter.differ.submitList(maidList)
                progressDialog?.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    private fun loadingProgressDialogue(){
        progressDialog = ProgressDialog(requireActivity())
        progressDialog?.setTitle("Please Wait")
        progressDialog?.setMessage("Loading...")
        progressDialog?.show()
    }

}