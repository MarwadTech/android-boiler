package com.marwadtech.userapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentLoginBinding
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.UserAuthResponseModel
import com.marwadtech.userapp.ui.user.UserDashboardActivity
import com.marwadtech.userapp.utils.CustomToast
import com.marwadtech.userapp.utils.GoogleLoginKeys
import com.marwadtech.userapp.utils.ToastType
import com.marwadtech.userapp.utils.getValue
import com.marwadtech.userapp.utils.isEmpty
import com.marwadtech.userapp.utils.mobileNumberValidation
import com.marwadtech.userapp.utils.passwordValidation
import com.marwadtech.userapp.utils.setSingleClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var binding: FragmentLoginBinding
    private lateinit var googleSignInClient: GoogleSignInClient
    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(GoogleLoginKeys.WebClientKey)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.login.observe(
            viewLifecycleOwner,
            this::handleLoginResult
        )
        viewModel.loginWithGoogle.observe(
            viewLifecycleOwner,
            this::handleLoginWithGoogleResult
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

    private fun onTextChangedListener() {
        binding.btnLogin.isEnabled = false
        binding.edtMobileNumber.addTextChangedListener(createTextWatcher())
        binding.edtMobileNumber.addTextChangedListener(createTextWatcher())
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun updateButtonState() {
        binding.btnLogin.isEnabled =
            binding.edtMobileNumber.mobileNumberValidation() && binding.edtPassword.passwordValidation()
    }


    private fun setClickListener() {

        binding.btnLogin.setSingleClickListener {
            if (validation()) {
                viewModel.login(
                    UserRequestModel(
                        phoneNumber = binding.edtMobileNumber.getValue(),
                        password = binding.edtPassword.getValue()
                    )
                )
            }
        }

        binding.imgGoogle.setSingleClickListener {
            loginWithGoogle()
        }

        binding.txtCreateAccount.setOnClickListener() {
            val directions = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            findNavController().navigate(directions)
        }

        binding.txtForgot.setOnClickListener() {
            val directions = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
            findNavController().navigate(directions)
        }

    }

    private fun validation(): Boolean {
        return when {
            binding.edtMobileNumber.isEmpty() -> {
                binding.edtMobileNumber.error = getString(R.string.please_enter_mobile_number)
                false
            }

            !binding.edtMobileNumber.mobileNumberValidation() -> {
                binding.edtMobileNumber.error = getString(R.string.please_enter_valid_mobile_number)
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

            else -> true
        }
    }

    private fun loginWithGoogle() {
        val signedInAccount = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (signedInAccount != null) {
            googleSignInClient.signOut().addOnCompleteListener {
                val signInIntent = googleSignInClient.signInIntent
                startActivityForResult(signInIntent, GoogleLoginKeys.RequestCode)
            }
        } else {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, GoogleLoginKeys.RequestCode)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GoogleLoginKeys.RequestCode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            Log.e(TAG, "handleSignInResult: ${account.email}")
            Log.e(TAG, "handleSignInResult: ${account.displayName}")
            Log.e(TAG, "handleSignInResult: ${account.idToken}")
            viewModel.loginWithGoogle(
                UserRequestModel(
                    googleIdToken = account.idToken
                )
            )
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun handleLoginResult(result: BaseModel<UserAuthResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleLoginResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleLoginResult: isSuccessfully")
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
                    TAG, "handleLoginResult: isError ${result.message} ${result.errors}"
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

    private fun handleLoginWithGoogleResult(result: BaseModel<UserAuthResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleLoginWithGoogleResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleLoginWithGoogleResult: isSuccessfully")
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
                    TAG, "handleLoginWithGoogleResult: isError ${result.message} ${result.errors}"
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
        private val TAG = LoginFragment::class.java.name
    }
}