package com.marwadtech.userapp.ui.screens.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marwadtech.userapp.R
import com.marwadtech.userapp.databinding.FragmentMyOrderBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MyOrderFragment : Fragment() {
    private lateinit var binding: FragmentMyOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentMyOrderBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCLickListener()
    }

    private fun setCLickListener() {

    }

    companion object {
        private var TAG = FragmentMyOrderBinding::class.java.name
    }
}