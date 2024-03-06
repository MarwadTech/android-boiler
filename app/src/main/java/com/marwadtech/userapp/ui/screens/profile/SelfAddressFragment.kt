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
import androidx.navigation.fragment.navArgs
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentSelfAddressBinding
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.response.AddressResponseModel
import com.marwadtech.userapp.ui.screens.profile.adapter.AddressAdapter
import com.marwadtech.userapp.utils.BottomDialogRequestKey
import com.marwadtech.userapp.utils.RequestKey
import com.marwadtech.userapp.utils.ToastType
import com.marwadtech.userapp.utils.setSingleClickListener
import com.marwadtech.userapp.utils.visibleOrGone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelfAddressFragment : BaseFragment() {
    private lateinit var binding: FragmentSelfAddressBinding
    private val viewModel by viewModels<ProfileViewModel>()
    private val args: SelfAddressFragmentArgs by navArgs()
    private lateinit var adapter: AddressAdapter
    private var addressId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(RequestKey.DELETE_ADDRESS) { _, bundle ->
            bundle.getBoolean(RequestKey.DELETE_ADDRESS).let {
                if (it) {
                    viewModel.deleteUserAddress(addressId ?: "")
                    clearFragmentResult(RequestKey.DELETE_ADDRESS)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelfAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getUserAddress.observe(
            viewLifecycleOwner,
            this::handleGetUserAddressResult
        )
        viewModel.getUserAddress()
        viewModel.deleteUserAddress.observe(
            viewLifecycleOwner,
            this::handleDeleteUserAddressResult
        )
        viewModel.makeAddressDefault.observe(
            viewLifecycleOwner,
            this::handleMakeAddressDefaultResult
        )
        setOnClickListener()
        setUpAdapter()
        setData()
    }

    private fun setData() {
        binding.btnNext.visibleOrGone(!args.isFromProfile)

    }

    private fun setUpAdapter() {
        if (!::adapter.isInitialized) {
            adapter = AddressAdapter(
                isFromProfile = true,
                deleteListener = {
                    addressId = it.id
                    val directions =
                        SelfAddressFragmentDirections.actionSelfAddressFragmentToNestedBottomDialogFragment(
                            title = getString(R.string.are_you_sure),
                            description = getString(R.string.you_want_to_delete_this_address),
                            requestKey = BottomDialogRequestKey.DELETE_ADDRESS
                        )
                    findNavController().navigate(directions)
                },
                updateListener = {
                    addressId = it.id
                    val directions = SelfAddressFragmentDirections.actionSelfAddressFragmentToDeliveryAddressFragment(
                        addressData = it,
                        mapFetchData = null
                    )
                    findNavController().navigate(directions)
                },
                makeDefaultListener = {
                    viewModel.makeAddressDefault(it.id ?: "")
                }
            )
        }
        binding.rvAddress.adapter = adapter
    }

    private fun setOnClickListener() {
        binding.btnAddMore.setSingleClickListener {
            val directions = SelfAddressFragmentDirections.actionSelfAddressFragmentToDeliveryAddressFragment(
                addressData = null,
                mapFetchData = null
            )
            findNavController().navigate(directions)
        }
    }

    private fun handleGetUserAddressResult(result: BaseModel<ArrayList<AddressResponseModel>>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleGetUserAddressResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleGetUserAddressResult: isSuccessfully")
                hideProgressbar()
                result.data?.apply {
                    spUtils.user?.addresses = this
                    adapter.items.submitList(this)
                }
            }

            result.isError() -> {
                Log.e(TAG, "handleGetUserAddressResult: isError ${result.message} ${result.errors}")
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

    private fun handleDeleteUserAddressResult(result: BaseModel<AddressResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleDeleteUserAddressResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleDeleteUserAddressResult: isSuccessfully")
                hideProgressbar()
                result.data?.apply {
                    viewModel.getUserAddress()
                }
            }

            result.isError() -> {
                Log.e(
                    TAG,
                    "handleDeleteUserAddressResult: isError ${result.message} ${result.errors}"
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

    private fun handleMakeAddressDefaultResult(result: BaseModel<AddressResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleMakeAddressDefaultResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleMakeAddressDefaultResult: isSuccessfully")
                hideProgressbar()
                result.data?.apply {
                    viewModel.getUserAddress()
                }
            }

            result.isError() -> {
                Log.e(
                    TAG,
                    "handleMakeAddressDefaultResult: isError ${result.message} ${result.errors}"
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
        private val TAG = FragmentSelfAddressBinding::class.java.name
    }
}