package com.homeservices.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.homeservices.admin.R
import com.homeservices.admin.model.Maid

class MaidListAdapter: RecyclerView.Adapter<MaidListAdapter.ShowMaidListViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Maid>() {
        override fun areItemsTheSame(oldItem: Maid, newItem: Maid): Boolean {
            return oldItem.maidName == newItem.maidName
        }

        override fun areContentsTheSame(oldItem: Maid, newItem: Maid): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowMaidListViewHolder {
        return ShowMaidListViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.maid_name_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShowMaidListViewHolder, position: Int) {
        holder.maidName.text = differ.currentList[position].maidName
        holder.maidAge.text = differ.currentList[position].age
        holder.maidSalaryPkg.text = differ.currentList[position].priceRange
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class ShowMaidListViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var maidName: TextView = itemview.findViewById(R.id.maidNameTextView)
        var maidAge: TextView = itemview.findViewById(R.id.maidAgeTextView)
        var maidSalaryPkg: TextView = itemview.findViewById(R.id.maidSalaryPackageTextView)
    }
}