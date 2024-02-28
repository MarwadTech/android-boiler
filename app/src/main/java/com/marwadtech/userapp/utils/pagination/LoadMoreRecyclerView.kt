package com.marwadtech.userapp.utils.pagination

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
class LoadMoreRecyclerView : RecyclerView {
    var isLoading = false
    private val visibleThreshold = 2
    private var lastVisibleItem = 0
    private var totalItemCount = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    fun setLoadMoreListener(mLoadMoreListener: LoadMoreListener) {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    if (layoutManager is LinearLayoutManager) {
                        val linearLayoutManager = (layoutManager as LinearLayoutManager?)!!
                        totalItemCount = linearLayoutManager.itemCount
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                    } else if (layoutManager is GridLayoutManager) {
                        val linearLayoutManager = (layoutManager as GridLayoutManager?)!!
                        totalItemCount = linearLayoutManager.itemCount
                        lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                    }
                    if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
                        Log.d("LoadMoreRecyclerView", "Load more triggered")
                        mLoadMoreListener.onLoadMore()
                        isLoading = true
                    }
                }
            }
        })
    }

    fun setLoadedCompleted() {
        isLoading = false
    }

    interface LoadMoreListener {
        fun onLoadMore()
    }
}
