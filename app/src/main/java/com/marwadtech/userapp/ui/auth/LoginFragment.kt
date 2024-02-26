package com.marwadtech.userapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.marwadtech.userapp.dialogs.OtpVerificationDialog
import com.marwadtech.userapp.utils.FilePathUtils.Companion.showToast
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("your_web_client_id")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

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

        binding.btnLogin.setSingleClickListener {
            if (validation()){

            }
        }

        binding.imgGoogle.setSingleClickListener{
            loginWithGoogle()
        }

        binding.txtCreateAccount.setOnClickListener(){
            val directions = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            findNavController().navigate(directions)
        }

        binding.txtForgot.setOnClickListener(){
            val directions = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
            findNavController().navigate(directions)
        }

    }

    private fun validation(): Boolean {
        return when {
            binding.edtMobileNumber.isEmpty() -> {
                binding.edtMobileNumber.error = "Please enter a mobile number"
                false
            }
            !binding.edtMobileNumber.mobileNumberValidation() -> {
                binding.edtMobileNumber.error = "Please enter a valid mobile number"
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
            else ->  true
        }
    }

    private fun loginWithGoogle(){
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 1) // Replace RC_SIGN_IN with a unique request code
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                handleSignedInUser(account)
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }
    }
    private fun handleSignedInUser(account: GoogleSignInAccount) {
        val idToken = account.idToken
        val email = account.email
        val name = account.displayName
        Log.e(TAG, "handleSignedInUser: $account", )
    }

    companion object {
        private val TAG = LoginFragment::class.java.name
    }
}