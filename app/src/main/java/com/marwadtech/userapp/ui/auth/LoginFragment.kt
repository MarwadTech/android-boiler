package com.marwadtech.userapp.ui.auth

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentLoginBinding
import com.marwadtech.userapp.dialogs.OtpVerificationDialog
import com.marwadtech.userapp.utils.FilePathUtils.Companion.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var binding: FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    private fun setClickListener() {

        binding.txtSignUpUsing.setOnClickListener(){
            val directions = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            findNavController().navigate(directions)
        }

        binding.txtCreateAccount.setOnClickListener(){
            val directions = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
            findNavController().navigate(directions)
        }

        binding.txtForgot.setOnClickListener(){
            val directions = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
            findNavController().navigate(directions)
        }


        binding.txtForgot.setOnClickListener(){

            if (binding.edtMobileNumber.text.isEmpty() || binding.edtMobileNumber.text.toString().length != 10){
             requireActivity().showToast(getString(R.string.required_mobile))
                binding.edtMobileNumber.setError(getString(R.string.required_mobile))
            }else{
                OtpVerificationDialog(
                    requireActivity()
                ){
                    showProgressbar()
                    Handler(Looper.getMainLooper()).postDelayed({
                        hideProgressbar()
                        val directions = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
                        findNavController().navigate(directions)
                    }, 1500)

                }.setShow(true)

            }


        }



    }

    companion object {
        private val TAG = LoginFragment::class.java.name
    }
}