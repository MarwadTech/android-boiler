package com.marwadtech.userapp.ui.screens.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseDialogFragment
import com.marwadtech.userapp.databinding.FragmentEditProfileBottomSheetBinding
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.ImageResponseModel
import com.marwadtech.userapp.retrofit.models.response.UserResponseModel
import com.marwadtech.userapp.utils.ToastType
import com.marwadtech.userapp.utils.emailValidation
import com.marwadtech.userapp.utils.emptyValidation
import com.marwadtech.userapp.utils.getValue
import com.marwadtech.userapp.utils.mobileNumberValidation
import com.marwadtech.userapp.utils.setSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileBottomSheetFragment : BaseDialogFragment() {
    private lateinit var binding: FragmentEditProfileBottomSheetBinding
    private val viewModel by viewModels<ProfileViewModel>()
    private var imageId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateProfile.observe(
            viewLifecycleOwner,
            this::handleUpdateProfileResult
        )
        viewModel.uploadImage.observe(
            viewLifecycleOwner,
            this::handleUploadImageResult
        )
        setOnClickListener()
        setData()
    }

    private fun setData() {
        binding.edtName.setText(spUtils.user?.name)
        binding.edtContactNumber.setText(spUtils.user?.phoneNumber)
        binding.edtEmail.setText(spUtils.user?.email)
        if (spUtils.user?.avatar != null) {
            Glide.with(this).asDrawable().load(spUtils.user?.avatar?.picLarge)
                .into(binding.imgProfile)
        } else {
            binding.imgProfile.setImageResource(R.drawable.ic_user_profile)
        }
    }

    private fun setOnClickListener() {
        binding.btnSave.setSingleClickListener {
            if (validation()) {
                val userRequestModel = UserRequestModel(
                    name = binding.edtName.getValue(),
                    phoneNumber = binding.edtContactNumber.getValue(),
                    email = binding.edtEmail.getValue()
                )
                viewModel.updateProfile(userRequestModel)
            }
        }
    }

    private fun handleUpdateProfileResult(result: BaseModel<UserResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleUpdateProfileResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleUpdateProfileResult: isSuccessfully")
                hideProgressbar()
                result.data?.apply {
                    spUtils.user = this
                    findNavController().popBackStack()
                }
            }

            result.isError() -> {
                Log.e(TAG, "handleUpdateProfileResult: isError ${result.message} ${result.errors}")
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

    private fun handleUploadImageResult(result: BaseModel<ImageResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleUploadImageResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleUploadImageResult: isSuccessfully")
//                hideProgressbar()
                result.data?.apply {
                    imageId = this.id
                }
            }

            result.isError() -> {
                Log.e(TAG, "handleUploadImageResult: isError result.message()")
                hideProgressbar()
                customToast.setCustomView(
                    getString(R.string.error),
                    result.message.toString(),
                    ToastType.isError
                )
            }
        }
    }

    private fun validation(): Boolean {
        return when {
            !binding.edtName.emptyValidation() -> {
                binding.edtName.error = getString(R.string.name_cannot_be_empty)
                false
            }

            !binding.edtEmail.emailValidation() -> {
                binding.edtEmail.error = getString(R.string.email_must_not_be_empty)
                false
            }

            !binding.edtContactNumber.mobileNumberValidation() -> {
                binding.edtContactNumber.error = getString(R.string.mobilenumber_cannot_be_empty)
                false
            }

            else -> true
        }
    }

    companion object {
        private val TAG = FragmentEditProfileBottomSheetBinding::class.java.name
    }
}