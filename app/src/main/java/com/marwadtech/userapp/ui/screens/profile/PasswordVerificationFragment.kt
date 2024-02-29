package com.marwadtech.userapp.ui.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentDeleteAccountBottomSheetBinding
import com.marwadtech.userapp.databinding.FragmentPasswordVerificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordVerificationFragment : BaseFragment() {
    private lateinit var binding: FragmentPasswordVerificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {

    }

    companion object {
        private val TAG = FragmentPasswordVerificationBinding::class.java.name
    }
}