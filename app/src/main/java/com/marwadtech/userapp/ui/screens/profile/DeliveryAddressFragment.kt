package com.marwadtech.userapp.ui.screens.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentDeliveryAddressBinding
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.AddressRequestModel
import com.marwadtech.userapp.retrofit.models.response.AddressResponseModel
import com.marwadtech.userapp.utils.ToastType
import com.marwadtech.userapp.utils.getValue
import com.marwadtech.userapp.utils.mobileNumberValidation
import com.marwadtech.userapp.utils.setSingleClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeliveryAddressFragment : BaseFragment() {
    private lateinit var binding: FragmentDeliveryAddressBinding
    private val viewModel by viewModels<ProfileViewModel>()
    private val args: DeliveryAddressFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeliveryAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.addUserAddress.observe(
            viewLifecycleOwner,
            this::handleAddUserAddressResult
        )
        viewModel.updateUserAddress.observe(
            viewLifecycleOwner,
            this::handleUpdateUserAddressResult
        )
        setClickListener()
        setData()
        onTextChangedListener()
    }

    private fun setData() {
        val addressData = args.addressData
        val mapFetchData = args.mapFetchData
        val user = spUtils.user
        binding.edtFullName.setText(addressData?.userName ?: user?.name)
        binding.edtContactNumber.setText(addressData?.phoneNumber ?: user?.phoneNumber)
        if (mapFetchData != null) {
            binding.edtPincode.setText(mapFetchData.pinCode)
            binding.edtState.setText(mapFetchData.state)
            binding.edtCity.setText(mapFetchData.city)
            binding.edtHouse.setText(extractTextBeforeCity(mapFetchData.addressLine ?: "", mapFetchData.city ?: ""))
        } else if(addressData != null) {
            binding.edtPincode.setText(addressData.pinCode)
            binding.edtState.setText(addressData.state)
            binding.edtCity.setText(addressData.city)
            binding.edtHouse.setText(addressData.lineOne)
            binding.rbHome.isChecked = addressData.type == "home"
            binding.rbOffice.isChecked = addressData.type == "office"
        }
    }

    private fun setClickListener() {
        binding.btnSave.setSingleClickListener {
            val addressRequestModel = AddressRequestModel(
                userName = binding.edtFullName.getValue(),
                phoneNumber = binding.edtContactNumber.getValue(),
                line1 = binding.edtHouse.getValue(),
                city = binding.edtCity.getValue(),
                state = binding.edtState.getValue(),
                pinCode = binding.edtPincode.getValue(),
                type = if (binding.rbHome.isChecked) {
                    "home"
                } else {
                    "office"
                }
            )
            if (validation()) {
                if (args.addressData != null) {
                    viewModel.updateUserAddress(
                        addressId = args.addressData?.id ?: "",
                        addressRequestModel = addressRequestModel
                    )
                } else {
                    viewModel.addUserAddress(
                        addressRequestModel
                    )
                }
            }
        }
        binding.btnLocation.setSingleClickListener {
            val directions =
                DeliveryAddressFragmentDirections.actionDeliveryAddressFragmentToMapFragment()
            findNavController().navigate(directions)
        }
    }

    private fun validation(): Boolean {
        return when {
            binding.edtFullName.getValue().isEmpty() -> {
                binding.edtFullName.setBackgroundResource(R.drawable.shape_rounded_red_border_f3_10)
                false
            }

            !binding.edtContactNumber.mobileNumberValidation() -> {
                binding.edtContactNumber.setBackgroundResource(R.drawable.shape_rounded_red_border_f3_10)
                false
            }

            binding.edtPincode.getValue().isEmpty() -> {
                binding.edtPincode.setBackgroundResource(R.drawable.shape_rounded_red_border_f3_10)
                false
            }

            binding.edtState.getValue().isEmpty() -> {
                binding.edtState.setBackgroundResource(R.drawable.shape_rounded_red_border_f3_10)
                false
            }

            binding.edtCity.getValue().isEmpty() -> {
                binding.edtCity.setBackgroundResource(R.drawable.shape_rounded_red_border_f3_10)
                false
            }

            binding.edtHouse.getValue().isEmpty() -> {
                binding.edtHouse.setBackgroundResource(R.drawable.shape_rounded_red_border_f3_10)
                false
            }

            else -> true
        }
    }

    private fun onTextChangedListener() {
        binding.edtFullName.addTextChangedListener(createTextWatcher())
        binding.edtContactNumber.addTextChangedListener(createTextWatcher())
        binding.edtPincode.addTextChangedListener(createTextWatcher())
        binding.edtState.addTextChangedListener(createTextWatcher())
        binding.edtCity.addTextChangedListener(createTextWatcher())
        binding.edtHouse.addTextChangedListener(createTextWatcher())
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.edtFullName.setBackgroundResource(R.drawable.shape_rounded_gray_f3_10)
                binding.edtContactNumber.setBackgroundResource(R.drawable.shape_rounded_gray_f3_10)
                binding.edtPincode.setBackgroundResource(R.drawable.shape_rounded_gray_f3_10)
                binding.edtState.setBackgroundResource(R.drawable.shape_rounded_gray_f3_10)
                binding.edtCity.setBackgroundResource(R.drawable.shape_rounded_gray_f3_10)
                binding.edtHouse.setBackgroundResource(R.drawable.shape_rounded_gray_f3_10)
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun handleAddUserAddressResult(result: BaseModel<AddressResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleAddUserAddressResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleAddUserAddressResult: isSuccessfully")
                hideProgressbar()
                result.data?.apply {
                    findNavController().popBackStack()
                }
            }

            result.isError() -> {
                Log.e(TAG, "handleAddUserAddressResult: isError ${result.message} ${result.errors}")
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

    private fun handleUpdateUserAddressResult(result: BaseModel<AddressResponseModel>) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleUpdateUserAddressResult: isLoading")
                showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleUpdateUserAddressResult: isSuccessfully")
                hideProgressbar()
                result.data?.apply {
                    findNavController().popBackStack()
                }
            }

            result.isError() -> {
                Log.e(
                    TAG,
                    "handleUpdateUserAddressResult: isError ${result.message} ${result.errors}"
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

    private fun extractTextBeforeCity(inputString: String, city: String): String {
        val index = inputString.indexOf(city)
        return if (index != -1) {
            inputString.substring(0, index).trim()
        } else {
            Log.e(TAG, "extractTextBeforeCity: City not found in the input string")
            ""
        }
    }

    companion object {
        private var TAG = FragmentDeliveryAddressBinding::class.java.name
    }
}