package com.marwadtech.userapp.internet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.marwadtech.userapp.base.BaseFragment
import com.marwadtech.userapp.databinding.FragmentInternetConnectionBinding
import com.marwadtech.userapp.utils.isConnectedToInternet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InternetConnectionFragment : BaseFragment() {

    private lateinit var binding: FragmentInternetConnectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun newInstance(): InternetConnectionFragment {
            val args = Bundle()
            val fragment = InternetConnectionFragment()
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInternetConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnReload.setOnClickListener {
            if (isConnectedToInternet(requireActivity(), false)) {
                callback.isEnabled = false
                findNavController().navigateUp()
            }
        }
    }

    val callback: OnBackPressedCallback = object : OnBackPressedCallback(
        true // default to enabled
    ) {
        override fun handleOnBackPressed() {
            // nothing
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this, // LifecycleOwner
            callback
        )
    }
}
