package com.marwadtech.userapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentOtpVerificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpVerificationFragment : BaseFragment() {
    private lateinit var binding: FragmentOtpVerificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpVerificationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    private fun setClickListener() {

    }

    companion object {
        private var TAG = FragmentOtpVerificationBinding::class.java.name
    }
}