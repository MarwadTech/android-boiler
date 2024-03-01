package com.marwadtech.userapp.ui.screens.notification

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.gson.JsonObject
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentNotificationBinding
import com.marwadtech.userapp.retrofit.PaginationModel
import com.marwadtech.userapp.retrofit.PaginationRequestModel
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.response.NotificationResponseModel
import com.marwadtech.userapp.ui.screens.notification.adapter.NotificationAdapter
import com.marwadtech.userapp.utils.ToastType
import com.marwadtech.userapp.utils.pagination.LoadMoreRecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment(), LoadMoreRecyclerView.LoadMoreListener {
    private lateinit var binding: FragmentNotificationBinding
    private val viewModel by viewModels<NotificationViewModel>()
    private lateinit var adapter : NotificationAdapter

    private var startIndex = 1
    private var endIndex = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllNotifications.observe(
            viewLifecycleOwner,
            this::handleGetAllNotificationsResult,
        )
        setOnClickListener()
        setNotificationApi()
        setUpAdapter()
    }

    private fun setNotificationApi() {
        val order = JsonObject()
        viewModel.getAllNotifications(
            PaginationRequestModel(
                limit = 10,
                page = startIndex,
                order = order,
            )
        )
    }

    private fun setUpAdapter() {
        if (!::adapter.isInitialized){
            adapter =NotificationAdapter(){

            }
        }
//        binding.rvlist.adapter = adapter
    }


    private fun setOnClickListener() {

    }

    private fun handleGetAllNotificationsResult(
        result: BaseModel<PaginationModel<List<NotificationResponseModel>>>,
    ) {
        when {
            result.isLoading() -> {
                Log.e(TAG, "handleGetAllNotificationsResult: isLoading")
                if (startIndex == 1)
                    showProgressbar()
            }

            result.isSuccessfully() -> {
                Log.e(TAG, "handleGetAllNotificationsResult: isSuccessfully")
//                binding.rvPhRhLink.setLoadedCompleted()
                result.data?.apply {
                    if (this.metaPagination?.currentPage == 1) {
                        endIndex = this.metaPagination.totalPages
                        adapter.items.submitList(this.rows)
                        hasData(this.rows?.size ?: 0)
                        Log.e(TAG, "handleGetAllNotificationsResult: $endIndex")
                    } else {
                        endIndex = this.metaPagination?.totalPages ?: 1
                        Log.e(TAG, "handleGetAllNotificationsResult: $endIndex")
                        val oldData = adapter.items.currentList.toMutableList()
                        Log.e(TAG, "handleGetAllNotificationsResult: $oldData")
                        this.rows?.let { pinTransactions ->
                            oldData.addAll(ArrayList(pinTransactions))
                        }
                        Log.e(TAG, "handleGetAllNotificationsResult: $oldData")
                        adapter.items.submitList(oldData)
                        hasData(adapter.items.currentList.size)
                    }
                }

                hideProgressbar()
            }

            result.isError() -> {
                Log.e(TAG, "handleGetPinTransactionPaginationResult: isError ${result.message}")
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
    override fun onLoadMore() {
        if (startIndex <= endIndex) {
            Log.e(TAG, "onLoadMore: $startIndex")
            startIndex++
            setNotificationApi()
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
        private val TAG = FragmentNotificationBinding::class.java.name
    }


}