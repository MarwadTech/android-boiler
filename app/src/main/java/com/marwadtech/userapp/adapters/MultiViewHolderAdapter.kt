package com.marwadtech.userapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marwadtech.userapp.databinding.ItemListTestingBinding
import com.marwadtech.userapp.retrofit.models.response.NotificationResponseModel

class MultiViewHolderAdapter(
    val isFirstItem: Boolean = false,
    val listener: (NotificationResponseModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class FirstViewHolder(val binding: ItemListTestingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationResponseModel) {

        }
    }

    inner class SecondViewHolder(val binding: ItemListTestingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationResponseModel) {

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (isFirstItem) {
            val binding = ItemListTestingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            FirstViewHolder(binding)
        } else {
            val binding = ItemListTestingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            SecondViewHolder(binding)
        }

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
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is MultiViewHolderAdapter.FirstViewHolder -> holder.bind(items.currentList[position])
            is MultiViewHolderAdapter.SecondViewHolder -> holder.bind(items.currentList[position])
        }
    }

    fun getItem(position: Int): NotificationResponseModel {
        return items.currentList[position]
    }

    override fun getItemCount(): Int {
        return items.currentList.size
    }
}