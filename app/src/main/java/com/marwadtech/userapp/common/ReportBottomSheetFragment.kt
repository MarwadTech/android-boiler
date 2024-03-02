package com.marwadtech.userapp.common

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseDialogFragment
import com.marwadtech.userapp.databinding.FragmentReportBottomSheetBinding
import com.marwadtech.userapp.utils.emptyValidation
import com.marwadtech.userapp.utils.gone
import com.marwadtech.userapp.utils.setSingleClickListener
import com.marwadtech.userapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class  ReportBottomSheetFragment : BaseDialogFragment() {
    private lateinit var binding: FragmentReportBottomSheetBinding
    private var selectedReason: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBottomSheetBinding.inflate(inflater, container, false)
        dialog?.setCancelable(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        setupRadioGroupListener()
    }

    private fun setupRadioGroupListener() {
        binding.edtOtherReason.gone()
        selectedReason = getString(R.string.technical_issues)
        binding.rgReportIssueSelector.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbTechnicalIssue -> {
                    binding.edtOtherReason.gone()
                    selectedReason = getString(R.string.technical_issues)
                }

                R.id.rbServiceQuality -> {
                    binding.edtOtherReason.gone()
                    selectedReason = getString(R.string.service_quality)
                }

                R.id.rbUserExperience -> {
                    binding.edtOtherReason.gone()
                    selectedReason = getString(R.string.user_experience)
                }

                R.id.rbOther -> {
                    binding.edtOtherReason.visible()
                    binding.rbUserExperience.gone()
                    binding.rbServiceQuality.gone()
                    binding.rbTechnicalIssue.gone()
                    onTextChangedListener()
                    selectedReason = null
                }
            }
        }
    }

    private fun onTextChangedListener() {
        binding.btnReport.isEnabled = false
        binding.edtOtherReason.addTextChangedListener(createTextWatcher())
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateLoginButtonState()
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun updateLoginButtonState() {
        binding.btnReport.isEnabled = binding.edtOtherReason.emptyValidation()
    }

    private fun setClickListener() {
        binding.btnCancel.setSingleClickListener {
            findNavController().popBackStack()
        }
        binding.btnReport.setSingleClickListener {
//            viewModel.createReport(
//                modelId = args.modelId,
//                reportRequestModel = ReportRequestModel(
//                    description = selectedReason ?: binding.edtOtherReason.getValue(),
//                    model = args.model
//                )
//            )
        }
    }

//    private fun handleCreateReportResult(result: BaseModel<ReportResponseModel>) {
//        when {
//            result.isLoading() -> {
//                Log.e(TAG, "handleCreateReportResult: isLoading")
//                showProgressbar()
//            }
//
//            result.isSuccessfully() -> {
//                Log.e(TAG, "handleCreateReportResult: isSuccessfully")
//                hideProgressbar()
//                result.data?.apply {
//                    customToast.setCustomView(
//                        getString(R.string.success),
//                        result.message.toString(),
//                        ToastType.success
//                    )
//                    findNavController().popBackStack()
//                }
//            }
//
//            result.isError() -> {
//                Log.e(TAG, "handleCreateReportResult: isError ${result.message}")
//                hideProgressbar()
//                result.errors?.map {
//                    customToast.setCustomView(
//                        getString(R.string.error),
//                        it.message,
//                        ToastType.isError
//                    )
//                }?.run {
//                    customToast.setCustomView(
//                        getString(R.string.error),
//                        result.message,
//                        ToastType.isError
//                    )
//                }
//            }
//        }
//    }

    companion object {
        private val TAG = FragmentReportBottomSheetBinding::class.java.name
    }
}
