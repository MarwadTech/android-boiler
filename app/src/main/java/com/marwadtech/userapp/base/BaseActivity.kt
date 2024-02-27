package com.marwadtech.userapp.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.marwadtech.userapp.dialogs.ProgressDialog
import com.marwadtech.userapp.utils.CustomToast
import com.marwadtech.userapp.utils.SpUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var spUtils: SpUtils
    private lateinit var progressDialog: ProgressDialog
    lateinit var customToast: CustomToast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(this)
        customToast = CustomToast(this)

    }

    fun showProgressbar() {
        progressDialog.show()
    }

    fun hideProgressbar() {
        progressDialog.dismiss()
    }

    companion object {
        private val TAG = BaseActivity::class.java.name
    }
}
