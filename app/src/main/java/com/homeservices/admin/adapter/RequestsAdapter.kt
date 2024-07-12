package com.homeservices.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.homeservices.admin.R
import com.homeservices.admin.databinding.BuyerRequestLayoutBinding
import com.homeservices.admin.model.Maid
import com.homeservices.admin.model.MaidRequest

class RequestsAdapter: RecyclerView.Adapter<RequestsAdapter.RequestViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<MaidRequest>() {
        override fun areItemsTheSame(oldItem: MaidRequest, newItem: MaidRequest): Boolean {
            return oldItem.requestId == newItem.requestId
        }

        override fun areContentsTheSame(oldItem: MaidRequest, newItem: MaidRequest): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val itemBinding = BuyerRequestLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RequestViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = differ.currentList[position]
        holder.bind(request)
        holder.binding.approveButton.setOnClickListener{
            onItemClickListener?.let {
                it(request, true)
            }
        }
        holder.binding.declineButton.setOnClickListener{
            onItemClickListener?.let {
                it(request, false)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class RequestViewHolder(val binding: BuyerRequestLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(request: MaidRequest){
            binding.userNameTextView.text = request.user?.name
            binding.locationPlaceTextView.text = request.maid?.area
            binding.availabilityNeededValueTextView.text = request.maid?.availability
            binding.maidRequestedNameTextView.text = request.maid?.maidName
        }
    }

    private var onItemClickListener: ((MaidRequest, Boolean) -> Unit)? = null
    fun setOnItemClickListener(listener: (MaidRequest, Boolean) -> Unit) {
        onItemClickListener = listener
    }
}