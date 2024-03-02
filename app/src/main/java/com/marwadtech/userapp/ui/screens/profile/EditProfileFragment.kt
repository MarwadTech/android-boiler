package com.marwadtech.userapp.ui.screens.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentEditProfileBinding
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.response.UserResponseModel
import com.marwadtech.userapp.utils.ToastType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel by viewModels<ProfileViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.updateProfile.observe(
            viewLifecycleOwner,
            this::handleUpdateProfileResult
        )
        setOnClickListener()
    }

    private fun setOnClickListener() {
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
                Log.e(
                    TAG,
                    "handleUpdateProfileResult: isError ${result.message} ${result.errors}"
                )
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


    companion object {
        private val TAG = FragmentEditProfileBinding::class.java.name
    }
}