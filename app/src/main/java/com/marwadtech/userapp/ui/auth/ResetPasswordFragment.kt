package com.marwadtech.userapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentResetPasswordBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment() {
    private lateinit var binding: FragmentResetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentResetPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    private fun setClickListener() {

    }

    companion object {
        private var TAG = FragmentResetPasswordBinding::class.java.name
    }
}