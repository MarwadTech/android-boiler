package com.marwadtech.userapp.ui.screens.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.clearFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentDashboardBinding
import com.marwadtech.userapp.databinding.FragmentProfileBinding
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.customModels.ProfileOptionsModel
import com.marwadtech.userapp.retrofit.models.response.UserResponseModel
import com.marwadtech.userapp.ui.screens.profile.adapter.ProfileOptionAdapter
import com.marwadtech.userapp.utils.BottomDialogRequestKey
import com.marwadtech.userapp.utils.ProfileOptionId
import com.marwadtech.userapp.utils.RequestKey
import com.marwadtech.userapp.utils.ToastType
import com.marwadtech.userapp.utils.logout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var accountAdapter: ProfileOptionAdapter
    private lateinit var aboutAdapter: ProfileOptionAdapter
    private lateinit var dangerAdapter: ProfileOptionAdapter
    private val viewModel by viewModels<ProfileViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onResume() {
        super.onResume()
        setFragmentResultListener(RequestKey.DELETE_ACCOUNT) { _, bundle ->
            bundle.getBoolean(RequestKey.DELETE_ACCOUNT).let {
                if (it) {
                    viewModel.deleteUser()
                    clearFragmentResult(RequestKey.DELETE_ACCOUNT)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserDetails.observe(
            viewLifecycleOwner,
            this::handleGetUserDetailsResult
        )
        viewModel.getUserDetails()
        viewModel.deleteUser.observe(
            viewLifecycleOwner,
            this::handleDeleteUserResult
        )
        setOnClickListener()
        setAdapterData()
    }

    private fun setAdapterData() {
        if (!::accountAdapter.isInitialized) {
            accountAdapter = ProfileOptionAdapter(spUtils) {
                try {
                    when (it.id) {
                        ProfileOptionId.EDIT_PROFILE -> {
                            val directions =
                                ProfileFragmentDirections.actionProfileFragmentToEditProfileBottomSheetFragment()
                            findNavController().navigate(directions)
                        }

                        ProfileOptionId.CHANGE_PASSWORD -> {
                            val directions =
                                ProfileFragmentDirections.actionProfileFragmentToChangePasswordBottomSheetFragment()
                            findNavController().navigate(directions)
                        }

                        ProfileOptionId.ADDRESS -> {
                            val directions =
                                ProfileFragmentDirections.actionProfileFragmentToNavAddress(
                                    isFromProfile = true
                                )
                            findNavController().navigate(directions)
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "setAdapterData: $e")
                }
            }
        }
        binding.rvAccountSettingOption.adapter = accountAdapter
        accountAdapter.items.submitList(ProfileOptionsModel.accountOptions(requireActivity()))

        if (!::aboutAdapter.isInitialized) {
            aboutAdapter = ProfileOptionAdapter(spUtils) {
                try {
                    when (it.id) {
                        ProfileOptionId.RATE_US -> {
                            val directions =
                                ProfileFragmentDirections.actionProfileFragmentToFeedbackFragment()
                            findNavController().navigate(directions)
                        }

                        ProfileOptionId.PRIVACY_POLICY -> {

                        }

                        ProfileOptionId.TERMS_AND_CONDITIONS -> {

                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "setAdapterData: $e")
                }
            }
        }
        binding.rvAboutAppOption.adapter = aboutAdapter
        aboutAdapter.items.submitList(ProfileOptionsModel.aboutAppOptions(requireActivity()))

        if (!::dangerAdapter.isInitialized) {
            dangerAdapter = ProfileOptionAdapter(spUtils) {
                try {
                    when (it.id) {
                        ProfileOptionId.DELETE_ACCOUNT -> {
                            val directions =
                                ProfileFragmentDirections.actionProfileFragmentToNestedBottomDialogFragment(
                                    title = getString(R.string.are_you_sure),
                                    description = getString(R.string.you_want_to_delete_your_account),
                                    requestKey = BottomDialogRequestKey.DELETE_ACCOUNT
                                )
                            findNavController().navigate(directions)
                        }

                        ProfileOptionId.LOG_OUT -> {
                            val directions =
                                ProfileFragmentDirections.actionProfileFragmentToNestedBottomDialogFragment(
                                    title = getString(R.string.are_you_sure),
                                    description = getString(R.string.you_want_to_logout),
                                    requestKey = BottomDialogRequestKey.LOGOUT
                                )
                            findNavController().navigate(directions)
                        }
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "setAdapterData: $e")
                }
            }
        }
        binding.rvDangerZoneOption.adapter = dangerAdapter
        dangerAdapter.items.submitList(ProfileOptionsModel.dangerZoneOptions(requireActivity()))
    }

    private fun setProfileData() {
        binding.txtFinderName.text = spUtils.user?.name
        binding.txtMobileNumber.text = spUtils.user?.phoneNumber
        binding.txtLocation.text =
            spUtils.user?.addresses?.find { it.isDefault == true }.let { it?.city }
    }


    private fun refreshLayout() {
        binding.swipeRefreshLayout.isRefreshing = false
        viewModel.getUserDetails()
    }

    private fun setOnClickListener() {
        binding.swipeRefreshLayout.setOnRefreshListener { refreshLayout() }
    }

    private fun handleGetUserDetailsResult(result: BaseModel<UserResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleGetUserDetailsResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleGetUserDetailsResult: isSuccessfully")
                hideProgressbar()
                result.data?.apply {
                    spUtils.user = this
                    setProfileData()
                }
            }

            result.isError() -> {
                Log.e(
                    TAG,
                    "handleGetUserDetailsResult: isError ${result.message} ${result.errors}"
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

    private fun handleDeleteUserResult(result: BaseModel<UserResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleDeleteUserResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleDeleteUserResult: isSuccessfully")
                hideProgressbar()
                result.data?.apply {
                    logout(requireContext(),spUtils)
                }
            }

            result.isError() -> {
                Log.e(
                    TAG,
                    "handleDeleteUserResult: isError ${result.message} ${result.errors}"
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
        private var TAG = FragmentDashboardBinding::class.java.name
    }
}