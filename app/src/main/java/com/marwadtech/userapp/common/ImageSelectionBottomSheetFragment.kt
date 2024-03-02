package com.marwadtech.userapp.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.marwadtech.userapp.base.BaseDialogFragment
import com.marwadtech.userapp.databinding.FragmentImageSelectionBottomSheetBinding
import com.marwadtech.userapp.utils.ImageSelection
import com.marwadtech.userapp.utils.RequestKey
import com.marwadtech.userapp.utils.gone
import com.marwadtech.userapp.utils.setSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageSelectionBottomSheetFragment : BaseDialogFragment() {

    private lateinit var binding: FragmentImageSelectionBottomSheetBinding
    private val args: ImageSelectionBottomSheetFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageSelectionBottomSheetBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

//    override fun getTheme(): Int {
//        return R.style.BottomSheetDialogStyle
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!args.hasBrowser) {
            binding.btnSelectFromFiles.gone()
        }
        if (!args.hasGallery) {
            binding.btnImageFromGallery.gone()
            binding.btnTakeAPicker.gone()
        }

        binding.btnTakeAPicker.setSingleClickListener {
            setFragmentResult(
                RequestKey.IMAGE_SELECTION,
                bundleOf(RequestKey.IMAGE_SELECTION to ImageSelection.Camera)
            )
            dismiss()
        }

        binding.btnImageFromGallery.setSingleClickListener {
            setFragmentResult(
                RequestKey.IMAGE_SELECTION,
                bundleOf(RequestKey.IMAGE_SELECTION to ImageSelection.Gallery)
            )
            dismiss()
        }

        binding.btnSelectFromFiles.setSingleClickListener {
            setFragmentResult(
                RequestKey.IMAGE_SELECTION,
                bundleOf(RequestKey.IMAGE_SELECTION to ImageSelection.Browser)
            )
            dismiss()
        }

        binding.btnCancel.setSingleClickListener {
            dismiss()
        }
    }

    companion object {
        fun newInstance(): ImageSelectionBottomSheetFragment {
            val args = Bundle()

            val fragment = ImageSelectionBottomSheetFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
