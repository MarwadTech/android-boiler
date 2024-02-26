package com.marwadtech.userapp.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment() {
    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
    }

    private fun setClickListener() {

        binding.btnConform.setOnClickListener(){
            val directions = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToResetPasswordFragment2()
            findNavController().navigate(directions)
        }
        binding.txtLogin.setOnClickListener(){
            val directions = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment()
            findNavController().navigate(directions)
        }
    }

    companion object {
        private var TAG = FragmentForgotPasswordBinding::class.java.name
    }
}