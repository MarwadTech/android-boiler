package com.marwadtech.userapp.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val filterValue = MutableLiveData<String?>()

    val filterLiveData get() = filterValue

    fun addFilter(message: String?) {
        filterValue.value = message
    }

    private val searchValue = MutableLiveData<String?>()

    val searchLiveData get() = searchValue

    fun addSearch(message: String?) {
        searchValue.value = message
    }

    private val acceptOrRejectOrder = MutableLiveData<String?>()

    val acceptOrRejectLiveData get() = acceptOrRejectOrder

    fun acceptOrReject(message: String?) {
        acceptOrRejectOrder.value = message
    }
}
