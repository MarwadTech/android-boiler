package com.marwadtech.userapp.ui.auth

import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentRegistrationBinding
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.UserAuthResponseModel
import com.marwadtech.userapp.utils.NextRoute
import com.marwadtech.userapp.utils.OtpType
import com.marwadtech.userapp.utils.ToastType
import com.marwadtech.userapp.utils.emailValidation
import com.marwadtech.userapp.utils.getValue
import com.marwadtech.userapp.utils.isEmpty
import com.marwadtech.userapp.utils.mobileNumberValidation
import com.marwadtech.userapp.utils.passwordValidation
import com.marwadtech.userapp.utils.setSingleClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationFragment : BaseFragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkUser.observe(
            viewLifecycleOwner,
            this::handleCheckUserResult
        )
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
                viewModel.checkUser(
                    UserRequestModel(
                        fullName = binding.edtName.getValue(),
                        phoneNumber = binding.edtMobileNumber.getValue(),
                        email =  binding.edtMail.getValue(),
                        password =  binding.edtPassword.getValue()
                    )
                )
            }
        }
        binding.txtLogin.setOnClickListener(){
            val directions = RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment()
            findNavController().navigate(directions)
        }
    }

    private fun validation(): Boolean {
        return when {
            binding.edtName.isEmpty() -> {
                binding.edtName.error = getString(R.string.please_enter_your_name)
                false
            }
            binding.edtMobileNumber.isEmpty() -> {
                binding.edtMobileNumber.error = getString(R.string.please_enter_mobile_number)
                false
            }
            !binding.edtMobileNumber.mobileNumberValidation() -> {
                binding.edtMobileNumber.error = getString(R.string.please_enter_valid_mobile_number)
                false
            }
            binding.edtMail.isEmpty() -> {
                binding.edtMail.error = getString(R.string.please_enter_email)
                false
            }
            !binding.edtMail.emailValidation() -> {
                binding.edtMail.error = getString(R.string.please_enter_valid_email)
                false
            }
            binding.edtPassword.isEmpty() -> {
                binding.edtPassword.error = getString(R.string.please_enter_password)
                false
            }
            !binding.edtPassword.passwordValidation() -> {
                binding.edtPassword.error = getString(R.string.please_enter_valid_password)
                false
            }
            !binding.cbTermsAndConditions.isChecked -> {
                binding.cbTermsAndConditions.error = getString(R.string.please_accept_terms_and_conditions)
                false
            }
            else -> true
        }
    }


    private fun handleCheckUserResult(result: BaseModel<UserAuthResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleCheckUserResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleCheckUserResult: isSuccessfully")
                hideProgressbar()
                result.data?.apply {
                    if (this.nextRoute == NextRoute.verifyOtp){
                        navigationOtpVerification()
                    }
                }
            }

            result.isError() -> {
                Log.e(
                    TAG,
                    "handleCheckUserResult: isError ${result.message} ${result.errors}"
                )
                hideProgressbar()
                result.errors?.apply {
                    this.map {
                        customToast.setCustomView(
                            getString(R.string.error),
                            it.message,
                            ToastType.isError
                        )
                    }
                } ?: run {
                    customToast.setCustomView(
                        getString(R.string.error),
                        result.message,
                        ToastType.isError
                    )
                }
            }
        }
    }

    private fun navigationOtpVerification() {
        val userData = UserRequestModel(
            fullName = binding.edtName.getValue(),
            phoneNumber = binding.edtMobileNumber.getValue(),
            email =  binding.edtMail.getValue(),
            password =  binding.edtPassword.getValue()
        )
        val directions = RegistrationFragmentDirections.actionRegistrationFragmentToOtpVerificationFragment(
            userData = userData,
            otpType = OtpType.verifyPhone,
            isFromRegister = true
        )
        findNavController().navigate(directions)
    }


    companion object {
        private var TAG = FragmentRegistrationBinding::class.java.name
    }
}