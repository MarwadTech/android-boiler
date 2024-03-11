package com.marwadtech.userapp.ui.screens.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseDialogFragment
import com.marwadtech.userapp.databinding.FragmentChangePasswordBottomSheetBinding
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UpdatePasswordRequestModel
import com.marwadtech.userapp.retrofit.models.response.UserResponseModel
import com.marwadtech.userapp.utils.ToastType
import com.marwadtech.userapp.utils.getValue
import com.marwadtech.userapp.utils.setSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordBottomSheetFragment :BaseDialogFragment() {
    private lateinit var binding: FragmentChangePasswordBottomSheetBinding

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updatePassword.observe(
            viewLifecycleOwner,
            this::handleUpdatePasswordResult
        )
        onTextChangedListener()
        setOnClickListener()
        setToggleState()
    }
    private fun setToggleState() {
        binding.edtOldPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.imgOldPasswordToggle.setSingleClickListener(100) {
            if (binding.edtOldPassword.transformationMethod == null) {
                binding.edtOldPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.imgOldPasswordToggle.setImageResource(R.drawable.ic_eye_hide)
                binding.edtOldPassword.setSelection(binding.edtOldPassword.text.length)
            } else {
                binding.edtOldPassword.transformationMethod = null
                binding.imgOldPasswordToggle.setImageResource(R.drawable.ic_eye_show)
                binding.edtOldPassword.setSelection(binding.edtOldPassword.text.length)
            }
        }
        binding.edtNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.imgNewPasswordToggle.setSingleClickListener(100) {
            if (binding.edtNewPassword.transformationMethod == null) {
                binding.edtNewPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.imgNewPasswordToggle.setImageResource(R.drawable.ic_eye_hide)
                binding.edtNewPassword.setSelection(binding.edtNewPassword.text.length)
            } else {
                binding.edtNewPassword.transformationMethod = null
                binding.imgNewPasswordToggle.setImageResource(R.drawable.ic_eye_show)
                binding.edtNewPassword.setSelection(binding.edtNewPassword.text.length)
            }
        }
        binding.edtConformPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.imgConfirmPasswordToggle.setSingleClickListener(100) {
            if (binding.edtConformPassword.transformationMethod == null) {
                binding.edtConformPassword.transformationMethod =
                    PasswordTransformationMethod.getInstance()
                binding.imgConfirmPasswordToggle.setImageResource(R.drawable.ic_eye_hide)
                binding.edtConformPassword.setSelection(binding.edtConformPassword.text.length)
            } else {
                binding.edtConformPassword.transformationMethod = null
                binding.imgConfirmPasswordToggle.setImageResource(R.drawable.ic_eye_show)
                binding.edtConformPassword.setSelection(binding.edtConformPassword.text.length)
            }
        }
    }


    private fun setOnClickListener() {
        binding.btnSave.setSingleClickListener {
            if (validation()) {
                viewModel.updatePassword(
                    UpdatePasswordRequestModel(
                        currentPassword = binding.edtOldPassword.getValue(),
                        newPassword = binding.edtNewPassword.getValue(),
                        confirmNewPassword = binding.edtConformPassword.getValue()
                    )
                )
            }
        }
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun handleUpdatePasswordResult(result: BaseModel<UserResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleUpdatePasswordResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleUpdatePasswordResult: isSuccessfully")
                hideProgressbar()
                result.data?.apply {
                    customToast.setCustomView(
                        getString(R.string.success),
                        result.message.toString(),
                        ToastType.success
                    )
                    findNavController().popBackStack()
                }
            }

            result.isError() -> {
                Log.e(TAG, "handleUpdatePasswordResult: isError ${result.message} ${result.errors}")
                hideProgressbar()
                result.errors?.apply {
                    this.map {
                        customToast.setCustomView(
                            getString(R.string.error),
                            result.message.toString(),
                            ToastType.isError
                        )
                    }
                } ?: run {
                    customToast.setCustomView(
                        getString(R.string.error),
                        result.message.toString(),
                        ToastType.isError
                    )
                }
            }
        }
    }

    private fun validation(): Boolean {
        return when {
            binding.edtOldPassword.getValue().isEmpty() -> {
                binding.edtOldPassword.setBackgroundResource(R.drawable.shape_rounded_red_border_f3_10)
                false
            }
            binding.edtNewPassword.getValue().isEmpty() -> {
                binding.edtNewPassword.setBackgroundResource(R.drawable.shape_rounded_red_border_f3_10)
                false
            }
            binding.edtConformPassword.getValue().isEmpty() -> {
                binding.edtConformPassword.setBackgroundResource(R.drawable.shape_rounded_red_border_f3_10)
                false
            }
            binding.edtNewPassword.getValue() != binding.edtConformPassword.getValue()->{
                binding.edtNewPassword.setBackgroundResource(R.drawable.shape_rounded_red_border_f3_10)
                binding.edtConformPassword.setBackgroundResource(R.drawable.shape_rounded_red_border_f3_10)
                false
            }
            else -> true
        }
    }

    private fun onTextChangedListener() {
        binding.edtOldPassword.addTextChangedListener(createTextWatcher())
        binding.edtNewPassword.addTextChangedListener(createTextWatcher())
        binding.edtConformPassword.addTextChangedListener(createTextWatcher())
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.edtOldPassword.setBackgroundResource(R.drawable.shape_rounded_outline_grey_4dp)
                binding.edtNewPassword.setBackgroundResource(R.drawable.shape_rounded_outline_grey_4dp)
                binding.edtConformPassword.setBackgroundResource(R.drawable.shape_rounded_outline_grey_4dp)
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }


    companion object {
        private val TAG = FragmentChangePasswordBottomSheetBinding::class.java.name
    }
}