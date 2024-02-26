package com.marwadtech.userapp.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentForgotPasswordBinding
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.UserAuthResponseModel
import com.marwadtech.userapp.utils.NextRoute
import com.marwadtech.userapp.utils.OtpType
import com.marwadtech.userapp.utils.emailValidation
import com.marwadtech.userapp.utils.getValue
import com.marwadtech.userapp.utils.isEmpty
import com.marwadtech.userapp.utils.mobileNumberValidation
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment() {
    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel by viewModels<AuthViewModel>()
    private  var otpVerificationType : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.sendOtp.observe(
            viewLifecycleOwner,
            this::handleSendOtpResult
        )
        setClickListener()
    }

    private fun checkInputType() : Boolean {
        return if (binding.edtMobileNumber.mobileNumberValidation()){
            otpVerificationType = OtpType.verifyPhone
            true
        } else if (binding.edtMobileNumber.emailValidation()){
            otpVerificationType = OtpType.verifyEmail
            true
        }else{
            false
        }
    }

    private fun setClickListener() {
        binding.btnConform.setOnClickListener(){
            if (validation()){

            }
        }
        binding.txtLogin.setOnClickListener(){
            val directions = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment()
            findNavController().navigate(directions)
        }
    }

    private fun validation():Boolean{
        return when{
            binding.edtMobileNumber.isEmpty()->{
                binding.edtMobileNumber.error = getString(R.string.please_enter_mobile_number_or_email)
                false
            }
            checkInputType()->{
                binding.edtMobileNumber.error = "Please enter a valid input"
                false
            }
            else->true
        }
    }

    private fun handleSendOtpResult(result: BaseModel<UserAuthResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleSendOtpResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleSendOtpResult: isSuccessfully")
                hideProgressbar()
                result.data?.apply {
                        navigationOtpVerification()
                }
            }

            result.isError() -> {
                Log.e(
                    TAG,
                    "handleSendOtpResult: isError ${result.message} ${result.errors}"
                )
                hideProgressbar()
                result.errors?.apply {
                    this.map {
                    }
                } ?: run {
                }
            }
        }
    }


    private fun navigationOtpVerification() {
        val directions = RegistrationFragmentDirections.actionRegistrationFragmentToOtpVerificationFragment(
            userData = UserRequestModel(),
            otpType = otpVerificationType,
            isFromRegister = false
        )
        findNavController().navigate(directions)
    }

    companion object {
        private var TAG = FragmentForgotPasswordBinding::class.java.name
    }
}