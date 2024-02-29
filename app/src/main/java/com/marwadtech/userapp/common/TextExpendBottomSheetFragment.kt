package com.marwadtech.userapp.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.marwadtech.userapp.base.BaseDialogFragment
import com.marwadtech.userapp.databinding.FragmentTextExpendBottomSheetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextExpendBottomSheetFragment : BaseDialogFragment() {
    private lateinit var binding: FragmentTextExpendBottomSheetBinding
    private val args: TextExpendBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTextExpendBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListener()
        setData()
    }

    private fun setData() {
        binding.txtTitleDescription.text = args.fullText
    }

    private fun setClickListener() {
    }

    companion object {
        private val TAG = FragmentTextExpendBottomSheetBinding::class.java.name
    }
}
