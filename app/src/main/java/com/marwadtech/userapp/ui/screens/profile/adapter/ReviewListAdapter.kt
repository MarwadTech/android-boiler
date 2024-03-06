package com.marwadtech.userapp.ui.screens.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marwadtech.userapp.databinding.ItemMyReviewListBinding
import com.marwadtech.userapp.retrofit.models.response.ReviewResponseModel

class ReviewListAdapter(
    val listener: (ReviewResponseModel) -> Unit,
) : RecyclerView.Adapter<ReviewListAdapter.ReviewsViewHolder>() {

    inner class ReviewsViewHolder(val binding: ItemMyReviewListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReviewResponseModel) {

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewsViewHolder {
        val binding = ItemMyReviewListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewsViewHolder(binding)
    }

    private val differ = object : DiffUtil.ItemCallback<ReviewResponseModel>() {
        override fun areItemsTheSame(
            oldItem: ReviewResponseModel,
            newItem: ReviewResponseModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ReviewResponseModel,
            newItem: ReviewResponseModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    val items = AsyncListDiffer(this, differ)

    override fun onBindViewHolder(
        holder: ReviewsViewHolder,
        position: Int
    ) {
        holder.bind(items.currentList[position])
    }

    fun getItem(position: Int): ReviewResponseModel{
        return items.currentList[position]
    }

    override fun getItemCount(): Int {
        return items.currentList.size
    }
}