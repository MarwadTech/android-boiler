package com.marwadtech.userapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marwadtech.userapp.databinding.ItemListTestingBinding
import com.marwadtech.userapp.retrofit.models.response.NotificationResponseModel

class SimpleAdapter(
    val listener: (NotificationResponseModel) -> Unit
) : RecyclerView.Adapter<SimpleAdapter.SimpleViewHolder>() {

    inner class SimpleViewHolder(val binding:ItemListTestingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationResponseModel) {

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleAdapter.SimpleViewHolder {
        val binding = ItemListTestingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SimpleViewHolder(binding)
    }

    private val differ = object : DiffUtil.ItemCallback<NotificationResponseModel>() {
        override fun areItemsTheSame(
            oldItem: NotificationResponseModel,
            newItem: NotificationResponseModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NotificationResponseModel,
            newItem: NotificationResponseModel
        ): Boolean {
            return true
        }
    }

    val items = AsyncListDiffer(this, differ)

    override fun onBindViewHolder(
        holder: SimpleAdapter.SimpleViewHolder,
        position: Int
    ) {
        holder.bind(items.currentList[position])
    }

    fun getItem(position: Int): NotificationResponseModel {
        return items.currentList[position]
    }

    override fun getItemCount(): Int {
        return items.currentList.size
    }
}