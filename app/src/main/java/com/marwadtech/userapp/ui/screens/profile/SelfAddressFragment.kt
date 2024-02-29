package com.marwadtech.userapp.ui.screens.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentSelfAddressBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelfAddressFragment : BaseFragment() {
    private lateinit var binding: FragmentSelfAddressBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelfAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {

    }

    companion object {
        private val TAG = FragmentSelfAddressBinding::class.java.name
    }
}