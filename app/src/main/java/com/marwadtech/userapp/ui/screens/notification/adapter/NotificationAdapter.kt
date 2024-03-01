package com.marwadtech.userapp.ui.screens.notification.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marwadtech.userapp.databinding.ItemListTestingBinding
import com.marwadtech.userapp.retrofit.models.response.NotificationResponseModel

class NotificationAdapter (
    private val listener: (NotificationResponseModel) -> Unit,
):RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){

    inner class NotificationViewHolder(val binding: ItemListTestingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationResponseModel) {

        }
    }

    private val differ = object : DiffUtil.ItemCallback<NotificationResponseModel>() {
        override fun areItemsTheSame(oldItem: NotificationResponseModel, newItem: NotificationResponseModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NotificationResponseModel, newItem: NotificationResponseModel): Boolean {
            return oldItem == newItem
        }
    }

    val items = AsyncListDiffer(this, differ)


    override fun getItemCount(): Int {
        return items.currentList.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(items.currentList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            ItemListTestingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    companion object {
        private val TAG = NotificationAdapter::class.java.name
    }
}