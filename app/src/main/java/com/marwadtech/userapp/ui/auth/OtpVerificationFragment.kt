package com.marwadtech.userapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentOtpVerificationBinding
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.UserAuthResponseModel
import com.marwadtech.userapp.ui.bottomNavigation.UserDashboardActivity
import com.marwadtech.userapp.utils.OtpType
import com.marwadtech.userapp.utils.ToastType
import com.marwadtech.userapp.utils.getValue
import com.marwadtech.userapp.utils.gone
import com.marwadtech.userapp.utils.invisible
import com.marwadtech.userapp.utils.isEmpty
import com.marwadtech.userapp.utils.isOtp
import com.marwadtech.userapp.utils.setSingleClickListener
import com.marwadtech.userapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpVerificationFragment : BaseFragment() {
    private lateinit var binding: FragmentOtpVerificationBinding
    private val args: OtpVerificationFragmentArgs by navArgs()
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var countDownTimer: CountDownTimer
    private var otpVerificationType: String? = null
    private var phoneOtp : String? = null
    private var emailOtp : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtpVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        otpVerificationType = args.otpType
        viewModel.sendOtp.observe(
            viewLifecycleOwner, this::handleSendOtpResult
        )
        viewModel.verifyOtp.observe(
            viewLifecycleOwner, this::handleVerifyOtpResult
        )
        viewModel.registerWithOtp.observe(
            viewLifecycleOwner,
            this::handleRegisterWithOtpResult
        )
        if (args.otpType == OtpType.verifyPhone) {
            binding.txtMobileNumber.text = getString(R.string.phone_number)
        } else {
            binding.txtMobileNumber.text = getString(R.string.email)
        }
        binding.btnResentOtp.gone()
        setClickListener()
        startTimer()
    }


    private fun startTimer() {
        if (!::countDownTimer.isInitialized) {
            countDownTimer = object : CountDownTimer(30000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.txtResentOtpTime.text = getString(
                        R.string.resend_otp_timer, (millisUntilFinished / 1000).toString()
                    )
                }

                override fun onFinish() {
                    binding.txtResentOtpTime.gone()
                    binding.btnResentOtp.visible()
                }
            }
            countDownTimer.start()
        } else {
            countDownTimer.cancel()
            countDownTimer.start()
        }
    }

    private fun setClickListener() {
        binding.btnConform.setSingleClickListener {
            if (validation()) {
                if (otpVerificationType == OtpType.verifyPhone){
                    phoneOtp = binding.edtOTP.getValue()
                    viewModel.verifyOtp(
                        UserRequestModel(
                            otp = binding.edtOTP.getValue(),
                            phoneNumber = args.userData?.phoneNumber
                        )
                    )
                }else{
                    emailOtp = binding.edtOTP.getValue()
                    viewModel.verifyOtp(
                        UserRequestModel(
                            otp = binding.edtOTP.getValue(),
                            email = args.userData?.email
                        )
                    )
                }
            }
        }
        binding.btnResentOtp.setSingleClickListener {
            binding.btnResentOtp.invisible()
            if (args.otpType == OtpType.verifyPhone) {
                otpVerificationType = OtpType.verifyPhone
                viewModel.sendOtp(
                    UserRequestModel(
                        phoneNumber = args.userData?.phoneNumber
                    )
                )
            } else {
                otpVerificationType = OtpType.verifyEmail
                viewModel.sendOtp(
                    UserRequestModel(
                        email = args.userData?.email
                    )
                )
            }
        }
    }

    private fun validation(): Boolean {
        return when {
            binding.edtOTP.isEmpty() -> {
                binding.edtOTP.error = getString(R.string.enter_otp_to_verify)
                false
            }

            !binding.edtOTP.isOtp() -> {
                binding.edtOTP.error = getString(R.string.otp_must_be_6_digits)
                false
            }

            else -> true
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
                    binding.edtOTP.text = null
                    binding.txtResentOtpTime.visible()
                    startTimer()
                }
            }

            result.isError() -> {
                Log.e(
                    TAG, "handleSendOtpResult: isError ${result.message} ${result.errors}"
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

    private fun handleVerifyOtpResult(result: BaseModel<UserAuthResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleVerifyOtpResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleVerifyOtpResult: isSuccessfully")
                hideProgressbar()
                result.data.apply {
                    if (args.isFromRegister) {
                        if (otpVerificationType == OtpType.verifyPhone) {
                            binding.txtMobileNumber.text = getString(R.string.email)
                            binding.btnResentOtp.gone()
                            binding.edtOTP.text = null
                            otpVerificationType = OtpType.verifyEmail
                            startTimer()
                        } else if (otpVerificationType == OtpType.verifyEmail) {
                            viewModel.registerWithOtp(
                                UserRequestModel(
                                    name = args.userData?.name,
                                    phoneNumber = args.userData?.phoneNumber,
                                    email = args.userData?.email,
                                    phoneOtp = phoneOtp,
                                    emailOtp = emailOtp,
                                    password = args.userData?.password
                                )
                            )
                        }
                    } else {
                        val directions =
                            OtpVerificationFragmentDirections.actionOtpVerificationFragmentToResetPasswordFragment(
                                userData =  UserRequestModel(
                                    phoneNumber = args.userData?.phoneNumber,
                                    email = args.userData?.email,
                                    otp = if (args.otpType == OtpType.verifyPhone){phoneOtp}else{emailOtp}
                                ),
                                otpType = args.otpType
                            )
                        findNavController().navigate(directions)
                    }
                }
            }

            result.isError() -> {
                Log.e(
                    TAG, "handleVerifyOtpResult: isError ${result.message} ${result.errors}"
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
    private fun handleRegisterWithOtpResult(result: BaseModel<UserAuthResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleRegisterWithOtpResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleRegisterWithOtpResult: isSuccessfully")
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
                    TAG, "handleRegisterWithOtpResult: isError ${result.message} ${result.errors}"
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
        private var TAG = FragmentOtpVerificationBinding::class.java.name
    }
}