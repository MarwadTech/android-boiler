package com.marwadtech.userapp.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.marwadtech.userapp.dialogs.ProgressDialog
import com.marwadtech.userapp.utils.SpUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    @Inject
    lateinit var spUtils: SpUtils
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(requireActivity())
    }

    fun showProgressbar() {
        progressDialog.show()
    }

    fun hideProgressbar() {
        progressDialog.dismiss()
    }

    companion object {
        private val TAG = BaseFragment::class.java.name
    }
}
