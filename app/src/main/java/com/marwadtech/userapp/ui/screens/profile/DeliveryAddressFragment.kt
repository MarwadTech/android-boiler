package com.marwadtech.userapp.ui.screens.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentDeliveryAddressBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeliveryAddressFragment : BaseFragment() {
    private lateinit var binding: FragmentDeliveryAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentDeliveryAddressBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    private fun setClickListener() {

    }

    companion object {
        private var TAG = FragmentDeliveryAddressBinding::class.java.name
    }
}