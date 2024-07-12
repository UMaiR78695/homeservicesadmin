package com.homeservices.admin.ui.fragments

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.homeservices.admin.adapter.RequestsAdapter
import com.homeservices.admin.databinding.FragmentBuyerRequestBinding
import com.homeservices.admin.extensions.showToast
import com.homeservices.admin.model.MaidRequest
import com.homeservices.admin.model.Notification
import com.homeservices.admin.model.Request
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class BuyerRequestFragment : Fragment() {
    private var _binding: FragmentBuyerRequestBinding? = null
    private val binding get() = _binding!!
    private lateinit var requestsAdapter: RequestsAdapter
    var progressDialog: ProgressDialog? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        _binding = FragmentBuyerRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestsAdapter = RequestsAdapter()
        binding.buyerRequestRecycleView.layoutManager = LinearLayoutManager(requireActivity())
        binding.buyerRequestRecycleView.adapter = requestsAdapter
        fetchRequests()
        requestsAdapter.setOnItemClickListener { request, isApproved ->
            val requestStatus : String = if (isApproved) Request.APPROVED.value else Request.DECLINED.value
            val message = if (isApproved) "Request Approved" else "Request declined"
            Firebase.database.reference.child("requests")
                .child(request.requestId)
                .setValue(request.copy(actionTaken = true, requestStatus = requestStatus, jobActive = isApproved))
                .addOnSuccessListener {
                    changeMaidStatus(request, message)
                }
        }
    }

    private fun changeMaidStatus(request:  MaidRequest, message: String) {
        request.maid?.let {
            Firebase.database.reference.child("maids").child(it.maidId)
                .child("active").setValue(true)
                .addOnSuccessListener {
                    generateNotification(request,message)
                }
        }
    }

    private fun generateNotification(request: MaidRequest, message: String) {
        val notification = Notification(
            id = System.currentTimeMillis().toString(),
            title = "Maid Appointed",
            message = "${request.maid!!.maidName} has been appointed to you",
            appointed = true,
            toUser = request.user!!.userId,
            maid = request.maid,
            date = getTimeStamp()
        )
        Firebase.database.reference.child("notifications")
            .child(notification.id)
            .setValue(notification).addOnSuccessListener {
                requireActivity().showToast(message)
            }
    }

    private fun fetchRequests() {
        loadingProgressDialogue()
        Firebase.database.reference.child("requests").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var requests : ArrayList<MaidRequest> = ArrayList()
                for (postSnapshot in dataSnapshot.children) {
                    var request = postSnapshot.getValue(MaidRequest::class.java)
                    request?.let { if (!it.actionTaken) requests.add(it) }
                }
                requestsAdapter.differ.submitList(requests)
                progressDialog?.dismiss()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun getTimeStamp(): String {
        val sfd = SimpleDateFormat("dd MMM, yyyy hh:mm a")
        val pakistan = TimeZone.getTimeZone("Asia/Karachi")
        sfd.timeZone = pakistan
        return sfd.format(Date()).orEmpty()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadingProgressDialogue(){
        progressDialog = ProgressDialog(requireActivity())
        progressDialog?.setTitle("Please Wait")
        progressDialog?.setMessage("Loading...")
        progressDialog?.show()
    }
}