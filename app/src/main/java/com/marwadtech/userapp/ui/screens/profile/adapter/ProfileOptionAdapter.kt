package com.marwadtech.userapp.ui.screens.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marwadtech.userapp.databinding.ItemProfileOptionBinding
import com.marwadtech.userapp.retrofit.models.customModels.ProfileOptionsModel
import com.marwadtech.userapp.utils.SpUtils
import com.marwadtech.userapp.utils.setSingleClickListener

class ProfileOptionAdapter(
    val spUtils: SpUtils,
    val listener: (ProfileOptionsModel) -> Unit
) : RecyclerView.Adapter<ProfileOptionAdapter.ProfileOptionViewHolder>() {

    inner class ProfileOptionViewHolder(val binding: ItemProfileOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProfileOptionsModel) {
            binding.txtTitle.text = item.title
            item.icon?.let {
                binding.txtTitle.setCompoundDrawablesRelativeWithIntrinsicBounds(it, 0, 0, 0)
            }
            binding.root.setSingleClickListener {
                listener(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileOptionViewHolder {
        val binding = ItemProfileOptionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProfileOptionViewHolder(binding)
    }

    private val differ = object : DiffUtil.ItemCallback<ProfileOptionsModel>() {
        override fun areItemsTheSame(
            oldItem: ProfileOptionsModel,
            newItem: ProfileOptionsModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProfileOptionsModel,
            newItem: ProfileOptionsModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    val items = AsyncListDiffer(this, differ)

    override fun onBindViewHolder(
        holder: ProfileOptionAdapter.ProfileOptionViewHolder,
        position: Int
    ) {
        holder.bind(items.currentList[position])
    }

    fun getItem(position: Int): ProfileOptionsModel {
        return items.currentList[position]
    }

    override fun getItemCount(): Int {
        return items.currentList.size
    }
}