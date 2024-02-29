package com.marwadtech.userapp.ui.screens.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseDialogFragment
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentEditProfileBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileBottomSheetFragment : BaseDialogFragment() {
    private lateinit var binding : FragmentEditProfileBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {

    }

    companion object {
        private val TAG = FragmentEditProfileBottomSheetBinding::class.java.name
    }
}