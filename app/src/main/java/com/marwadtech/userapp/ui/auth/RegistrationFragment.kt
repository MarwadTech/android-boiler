package com.marwadtech.userapp.ui.auth

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentRegistrationBinding
import com.marwadtech.userapp.utils.emailValidation
import com.marwadtech.userapp.utils.isEmpty
import com.marwadtech.userapp.utils.mobileNumberValidation
import com.marwadtech.userapp.utils.passwordValidation
import com.marwadtech.userapp.utils.setSingleClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationFragment : BaseFragment() {
    private lateinit var binding: FragmentRegistrationBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        setEyeToggle()
    }

    private fun setEyeToggle() {
        binding.imgBtnToggle.setOnClickListener {
            if (binding.edtPassword.transformationMethod == null) {
                binding.edtPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.imgBtnToggle.setImageResource(R.drawable.ic_eye_hide)
                binding.edtPassword.setSelection(binding.edtPassword.text.length)
            } else {
                binding.edtPassword.transformationMethod = null
                binding.imgBtnToggle.setImageResource(R.drawable.ic_eye_show)
                binding.edtPassword.setSelection(binding.edtPassword.text.length)
            }
        }
    }


    private fun setClickListener() {
        binding.btnSignUp.setSingleClickListener {
            if (validation()){
                val directions = RegistrationFragmentDirections.actionRegistrationFragmentToOtpVerificationFragment()
                findNavController().navigate(directions)
            }
        }
        binding.txtLogin.setOnClickListener(){
            val directions = RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
            findNavController().navigate(directions)
        }
    }

    private fun validation(): Boolean {
        return when {
            binding.edtName.isEmpty()->{
                binding.edtName.error = "Please enter your name"
                 false
            }
            binding.edtMobileNumber.isEmpty() -> {
                binding.edtMobileNumber.error = "Please enter a mobile number"
                 false
            }
            !binding.edtMobileNumber.mobileNumberValidation() -> {
                binding.edtMobileNumber.error = "Please enter a valid mobile number"
                 false
            }
            binding.edtMail.isEmpty()->{
                binding.edtMail.error = "Please enter a email"
                false
            }
            !binding.edtMail.emailValidation()->{
                binding.edtMail.error = "Please enter a valid email"
                false
            }
            binding.edtPassword.isEmpty() -> {
                binding.edtPassword.error = "Please enter a password"
                 false
            }
            !binding.edtPassword.passwordValidation() -> {
                binding.edtPassword.error = "Please enter a valid password"
                 false
            }
            !binding.cbTermsAndConditions.isChecked ->{
                binding.cbTermsAndConditions.error = "Please accept the terms and conditions"
                false
            }
            else -> true
        }
    }

    companion object {
        private var TAG = FragmentRegistrationBinding::class.java.name
    }
}