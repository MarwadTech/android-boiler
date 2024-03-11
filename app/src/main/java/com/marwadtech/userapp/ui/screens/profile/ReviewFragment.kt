package com.marwadtech.userapp.ui.screens.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.gson.JsonObject
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentReviewBinding
import com.marwadtech.userapp.retrofit.PaginationModel
import com.marwadtech.userapp.retrofit.PaginationRequestModel
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.response.ReviewResponseModel
import com.marwadtech.userapp.ui.screens.profile.adapter.ReviewListAdapter
import com.marwadtech.userapp.utils.ToastType
import com.marwadtech.userapp.utils.pagination.LoadMoreRecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : BaseFragment(), LoadMoreRecyclerView.LoadMoreListener {
    private lateinit var binding: FragmentReviewBinding
    private val viewModel by viewModels<ProfileViewModel>()
    private lateinit var adapter : ReviewListAdapter

    private var startIndex = 1
    private var endIndex = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getReviews.observe(
            viewLifecycleOwner,
            this::handleGetReviewsResult,
        )
        setOnClickListener()
        initView()
        setUpAdapter()
        setReviewsApi()
    }

    private fun setReviewsApi() {
        val order = JsonObject()
        viewModel.getReviews(
            PaginationRequestModel(
                limit = 10,
                page = startIndex,
                order = order,
            )
        )
    }
    private fun setUpAdapter() {
        if (!::adapter.isInitialized){
            adapter = ReviewListAdapter(){

            }
        }
        binding.rvReviewList.adapter = adapter
    }


    private fun setOnClickListener() {

    }

    private fun handleGetReviewsResult(
        result: BaseModel<PaginationModel<List<ReviewResponseModel>>>,
    ) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleGetReviewsResult: isLoading")
                if (startIndex == 1)
                    showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleGetReviewsResult: isSuccessfully")
                binding.rvReviewList.setLoadedCompleted()
                result.data?.apply {
                    if (this.metaPagination?.currentPage == 1) {
                        endIndex = this.metaPagination.totalPages
                        adapter.items.submitList(this.rows)
                        hasData(this.rows?.size ?: 0)
                        Log.e(TAG, "handleGetReviewsResult: $endIndex")
                    } else {
                        endIndex = this.metaPagination?.totalPages ?: 1
                        Log.e(TAG, "handleGetReviewsResult: $endIndex")
                        val oldData = adapter.items.currentList.toMutableList()
                        Log.e(TAG, "handleGetReviewsResult: $oldData")
                        this.rows?.let { pinTransactions ->
                            oldData.addAll(ArrayList(pinTransactions))
                        }
                        Log.e(TAG, "handleGetReviewsResult: $oldData")
                        adapter.items.submitList(oldData)
                        hasData(adapter.items.currentList.size)
                    }
                }

                hideProgressbar()
            }

            result.isError() -> {
                Log.e(TAG, "handleGetReviewsResult: isError ${result.message}")
                hideProgressbar()
                hasData(adapter.items.currentList.size)
                customToast.setCustomView(
                    getString(R.string.error),
                    result.message.toString(),
                    ToastType.isError
                )
            }
        }

    }

    private fun initView() {
        binding.rvReviewList.setLoadMoreListener(this)
    }

    override fun onLoadMore() {
        if (startIndex <= endIndex) {
            Log.e(TAG, "onLoadMore: $startIndex")
            startIndex++
            setReviewsApi()
        }
    }
    private fun hasData(size: Int) {
        Log.e(TAG, "hasData: called")
        if (size == 0) {
//                binding.imgEmptyScreen.visible()
//                binding.rvPhRhLink.gone()
        } else {
//                binding.imgEmptyScreen.gone()
//                binding.rvPhRhLink.visible()
        }
    }
    companion object {
        private val TAG = FragmentReviewBinding::class.java.name
    }
}