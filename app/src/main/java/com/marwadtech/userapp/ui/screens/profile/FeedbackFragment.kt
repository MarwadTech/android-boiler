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
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentFeedbackBinding
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.ReviewRequestModel
import com.marwadtech.userapp.retrofit.models.response.ReviewResponseModel
import com.marwadtech.userapp.utils.ToastType
import com.marwadtech.userapp.utils.getValue
import com.marwadtech.userapp.utils.isEmpty
import com.marwadtech.userapp.utils.setSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedbackFragment : BaseFragment() {
    private lateinit var binding: FragmentFeedbackBinding
    private val viewModel by viewModels<ProfileViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.giveAppRating.observe(
            viewLifecycleOwner,
            this::handleGiveAppRatingResult
        )
        binding.ratingbar.setOnRatingBarChangeListener { _, rating, fromUser ->
            val minimumRating = 1f
            if (fromUser && rating < minimumRating) {
                binding.ratingbar.rating = minimumRating
            }
        }
        setOnClickListener()
        onTextChangedListener()
    }

    private fun validation(): Boolean {
        return when{
            binding.edtFeedback.isEmpty()->{
                binding.edtFeedback.setBackgroundResource(R.drawable.shape_rounded_red_border_f3_10)
                false
            }
            else -> true
        }
    }
    private fun onTextChangedListener() {
        binding.edtFeedback.addTextChangedListener(createTextWatcher())
    }
    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.edtFeedback.setBackgroundResource(R.drawable.shape_rounded_outline_grey_d9_8dp)
            }
            override fun afterTextChanged(s: Editable?) {}
        }
    }

    private fun setOnClickListener() {
        binding.btnSubmitFeedback.setSingleClickListener {
            if (validation()){
                viewModel.giveAppRating(
                    reviewRequestModel = ReviewRequestModel(
                        value = binding.ratingbar.rating.toDouble(),
                        comment = binding.edtFeedback.getValue()
                    )
                )
            }
        }
    }


    private fun handleGiveAppRatingResult(result: BaseModel<ReviewResponseModel>){
        when{
            result.isLoading()->{
                Log.e(TAG, "handleGiveAppRatingResult: isLoading")
                showProgressbar()
            }
            result.isSuccessfully()->{
                Log.e(TAG, "handleGiveAppRatingResult: isSuccessfully")
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
            result.isError()->{
                Log.e(
                    TAG,
                    "handleGiveAppRatingResult: isError ${result.message} ${result.errors}"
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
        private val TAG = SelfAddressFragment::class.java.name
    }
}