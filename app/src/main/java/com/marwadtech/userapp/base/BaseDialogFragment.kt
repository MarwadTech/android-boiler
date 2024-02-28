package com.marwadtech.userapp.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marwadtech.userapp.R
import com.marwadtech.userapp.dialogs.ProgressDialog
import com.marwadtech.userapp.utils.CustomToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseDialogFragment :
    BottomSheetDialogFragment() {

    lateinit var customToast: CustomToast
    lateinit var loader: ProgressDialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun getTheme(): Int {
        return R.style.BottomSheetDialogTheme
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loader = ProgressDialog(requireActivity())
        customToast = CustomToast(requireActivity())
    }

//    @Inject
//    lateinit var spUtils: SpUtils

    var callback: BottomSheetCallback = object : BottomSheetCallback() {
        override fun onStateChanged(view: View, newState: Int) {
            // showing the different states
            when (newState) {
                BottomSheetBehavior.STATE_HIDDEN -> dismiss() // if you want the modal to be dismissed when user drags the bottomsheet down
                BottomSheetBehavior.STATE_EXPANDED -> {}
                BottomSheetBehavior.STATE_COLLAPSED -> {}
                BottomSheetBehavior.STATE_DRAGGING -> {}
                BottomSheetBehavior.STATE_SETTLING -> {}
            }
        }
        override fun onSlide(view: View, v: Float) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
