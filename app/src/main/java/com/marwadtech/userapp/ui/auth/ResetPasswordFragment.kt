package com.marwadtech.userapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentResetPasswordBinding
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.UserAuthResponseModel
import com.marwadtech.userapp.ui.bottomNavigation.UserDashboardActivity
import com.marwadtech.userapp.utils.ToastType
import com.marwadtech.userapp.utils.getValue
import com.marwadtech.userapp.utils.isEmpty
import com.marwadtech.userapp.utils.passwordValidation
import com.marwadtech.userapp.utils.setSingleClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ResetPasswordFragment : BaseFragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    private val viewModel by viewModels<AuthViewModel>()
    private val args: ResetPasswordFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.resetPassword.observe(
            viewLifecycleOwner,
            this::handleResetPasswordResult
        )
        setClickListener()
        setEyeToggle()
    }

    private fun setEyeToggle() {
        binding.imgNewPassBtnToggle.setSingleClickListener(100) {
            if (binding.edtNewPassword.transformationMethod == null) {
                binding.edtNewPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.imgNewPassBtnToggle.setImageResource(R.drawable.ic_eye_hide)
                binding.edtNewPassword.setSelection(binding.edtConfirmPassword.text.length)
            } else {
                binding.edtNewPassword.transformationMethod = null
                binding.imgNewPassBtnToggle.setImageResource(R.drawable.ic_eye_show)
                binding.edtNewPassword.setSelection(binding.edtNewPassword.text.length)
            }
        }
        binding.imgConfirmPassBtnToggle.setSingleClickListener(100) {
            if (binding.edtConfirmPassword.transformationMethod == null) {
                binding.edtConfirmPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.imgConfirmPassBtnToggle.setImageResource(R.drawable.ic_eye_hide)
                binding.edtConfirmPassword.setSelection(binding.edtConfirmPassword.text.length)
            } else {
                binding.edtConfirmPassword.transformationMethod = null
                binding.imgConfirmPassBtnToggle.setImageResource(R.drawable.ic_eye_show)
                binding.edtConfirmPassword.setSelection(binding.edtConfirmPassword.text.length)
            }
        }
    }


    private fun setClickListener() {
        binding.btnConform.setSingleClickListener {
            if (validation()) {
                viewModel.resetPassword(
                    UserRequestModel(
                        phoneNumber = args.userData?.phoneNumber,
                        email = args.userData?.email,
                        otp = args.userData?.otp,
                        newPassword = binding.edtNewPassword.getValue(),
                        confirmNewPassword = binding.edtConfirmPassword.getValue()
                    )
                )
            }
        }
    }

    private fun validation(): Boolean {
        return when {
            binding.edtNewPassword.isEmpty() -> {
                binding.edtNewPassword.error = getString(R.string.please_enter_password)
                false
            }

            !binding.edtNewPassword.passwordValidation() -> {
                binding.edtNewPassword.error = getString(R.string.please_enter_a_valid_password)
                false
            }

            binding.edtConfirmPassword.isEmpty() -> {
                binding.edtConfirmPassword.error = getString(R.string.please_enter_password)
                false
            }

            !binding.edtConfirmPassword.passwordValidation() -> {
                binding.edtConfirmPassword.error = getString(R.string.please_enter_a_valid_password)
                false
            }

            binding.edtNewPassword.getValue() != binding.edtConfirmPassword.getValue() -> {
                binding.edtConfirmPassword.error = getString(R.string.password_does_not_match)
                false
            }

            else -> true
        }
    }

    private fun handleResetPasswordResult(result: BaseModel<UserAuthResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleResetPasswordResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleResetPasswordResult: isSuccessfully")
                hideProgressbar()
                result.data?.apply {
                    if (this.user != null && this.token != null) {
                        spUtils.accessToken = this.token
                        spUtils.user = this.user
                        spUtils.isLoggedIn = true
                        goToDashboard()
                    }
                }
            }

            result.isError() -> {
                Log.e(
                    TAG,
                    "handleResetPasswordResult: isError ${result.message} ${result.errors}"
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

    private fun goToDashboard() {
        val intent = Intent(requireContext(), UserDashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    companion object {
        private var TAG = FragmentResetPasswordBinding::class.java.name
    }
}