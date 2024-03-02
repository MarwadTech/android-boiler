package com.marwadtech.userapp.ui.screens.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.marwadtech.userapp.R
import com.marwadtech.userapp.databinding.ItemAddressNewBinding
import com.marwadtech.userapp.retrofit.models.response.AddressResponseModel
import com.marwadtech.userapp.utils.gone
import com.marwadtech.userapp.utils.setSingleClickListener
import com.marwadtech.userapp.utils.visibleOrGone
import java.util.Locale

class AddressAdapter(
    val isFromProfile: Boolean = false,
    val deleteListener: (AddressResponseModel) -> Unit,
    val makeDefaultListener: (AddressResponseModel) -> Unit,
    val updateListener: (AddressResponseModel) -> Unit,
) : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(val binding: ItemAddressNewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AddressResponseModel) {
            binding.txtAddressType.text = "(${item.type?.capitalize(Locale.ROOT)})"
            binding.txtName.text = item.userName
            binding.txtMobileNumber.text = item.phoneNumber
            if (item.type == "home") {
                binding.imgAddressType.setImageResource(R.drawable.ic_home_new)
            } else if (item.type == "office") {
                binding.imgAddressType.setImageResource(R.drawable.ic_building_new)
            }
            binding.txtCityArea.text = "${item.lineOne}, ${item.lineTwo}, ${item.city}, ${item.state}, ${item.country} (${item.pinCode})"
            binding.imgDelete.setSingleClickListener {
                deleteListener(item)
            }
            binding.btnDefault.setSingleClickListener {
                makeDefaultListener(item)
            }
            binding.btnEdit.setSingleClickListener {
                updateListener(item)
            }
            binding.txtDefault.visibleOrGone(item.isDefault == true)
            if (isFromProfile){
                binding.btnDefault.visibleOrGone(item.isDefault != true)
            }else{
                binding.btnDefault.gone()
                if (item.isSelected) {
                    binding.percent.setBackgroundResource(R.drawable.shape_rounded_light_purple_outline)
                } else {
                    binding.percent.setBackgroundResource(R.drawable.shape_rounded_smoke_grey_outline)
                }
                binding.root.setSingleClickListener { view ->
                    items.currentList.toMutableList().map {
                        it.isSelected = false
                    }
                    item.isSelected = true
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddressViewHolder {
        val binding = ItemAddressNewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AddressViewHolder(binding)
    }

    private val differ = object : DiffUtil.ItemCallback<AddressResponseModel>() {
        override fun areItemsTheSame(
            oldItem: AddressResponseModel,
            newItem: AddressResponseModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: AddressResponseModel,
            newItem: AddressResponseModel
        ): Boolean {
            return oldItem == newItem
        }
    }

    val items = AsyncListDiffer(this, differ)

    override fun onBindViewHolder(
        holder: AddressViewHolder,
        position: Int
    ) {
        holder.bind(items.currentList[position])
    }

    fun getItem(position: Int): AddressResponseModel {
        return items.currentList[position]
    }

    override fun getItemCount(): Int {
        return items.currentList.size
    }
}