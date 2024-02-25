package com.marwadtech.userapp.retrofit

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PaginationModel<out T>(
    @SerializedName("meta")
    @Expose
    val metaPagination: MetaPagination? = null,
    @SerializedName("list")
    @Expose
    val rows: T? = null
)

data class MetaPagination(
    @SerializedName("page_size")
    @Expose
    var pageSize: Int,
    @SerializedName("total")
    @Expose
    var total: Int,
    @SerializedName("current_page")
    @Expose
    var currentPage: Int,
    @SerializedName("last_page")
    @Expose
    var lastPage: Int,
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int
) : java.io.Serializable
