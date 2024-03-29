package com.marwadtech.userapp.ui.screens.levels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentLevelBinding
import com.marwadtech.userapp.databinding.FragmentPasswordVerificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LevelFragment : BaseFragment() {
    private lateinit var binding: FragmentLevelBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListener()
    }

    private fun setOnClickListener() {

    }

    companion object {
        private val TAG = FragmentLevelBinding::class.java.name
    }
}