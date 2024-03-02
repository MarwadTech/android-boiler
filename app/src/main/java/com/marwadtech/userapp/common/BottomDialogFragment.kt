package com.marwadtech.userapp.common

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.marwadtech.userapp.R
import com.marwadtech.userapp.base.BaseDialogFragment
import com.marwadtech.userapp.databinding.FragmentBottomDialogBinding
import com.marwadtech.userapp.ui.auth.AuthActivity
import com.marwadtech.userapp.utils.BottomDialogRequestKey
import com.marwadtech.userapp.utils.RequestKey
import com.marwadtech.userapp.utils.logout
import com.marwadtech.userapp.utils.setSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BottomDialogFragment : BaseDialogFragment() {
    private lateinit var binding: FragmentBottomDialogBinding
    private val args: BottomDialogFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomDialogBinding.inflate(inflater, container, false)
        dialog?.setCancelable(false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtMassage.text = args.title ?: ""
        binding.txtMassageDescription.text = args.description ?: ""
        when (args.requestKey) {
            BottomDialogRequestKey.LOGOUT -> {
                binding.imgIcon.setImageResource(R.drawable.ic_attention)
                binding.btnYes.text = getString(R.string.logout)
            }

            BottomDialogRequestKey.DELETE_ACCOUNT,
            BottomDialogRequestKey.DELETE_ADDRESS,
            -> {
                binding.imgIcon.setImageResource(R.drawable.ic_attention)
                binding.btnYes.text = getString(R.string.delete)
            }

            else -> {
                binding.imgIcon.setImageResource(R.drawable.ic_attention)
            }
        }
        setClickListener()
    }

    private fun setClickListener() {
        binding.btnYes.setSingleClickListener() {
            when (args.requestKey) {
                BottomDialogRequestKey.LOGOUT -> {
                    logout(requireContext(), spUtils)
                }

                BottomDialogRequestKey.DELETE_ACCOUNT -> {
                    findNavController().popBackStack()
                    setFragmentResult(
                        RequestKey.DELETE_ACCOUNT,
                        bundleOf(RequestKey.DELETE_ACCOUNT to true)
                    )
                }

                BottomDialogRequestKey.DELETE_ADDRESS -> {
                    findNavController().popBackStack()
                    setFragmentResult(
                        RequestKey.DELETE_ADDRESS,
                        bundleOf(RequestKey.DELETE_ADDRESS to true)
                    )
                }
            }
        }

        binding.btnCancel.setSingleClickListener() {
            findNavController().popBackStack()
        }
    }

    private fun goAuth() {
        val intent = Intent(requireContext(), AuthActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    companion object {
        private val TAG = FragmentBottomDialogBinding::class.java.name
    }
}
