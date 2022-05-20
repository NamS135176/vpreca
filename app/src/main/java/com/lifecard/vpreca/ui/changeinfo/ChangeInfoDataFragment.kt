package com.lifecard.vpreca.ui.changeinfo

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangeInfoDataBinding
import com.lifecard.vpreca.databinding.FragmentChangePassCompleteBinding
import com.lifecard.vpreca.utils.hideToolbar
import com.lifecard.vpreca.utils.showToolbar

class ChangeInfoDataFragment : Fragment() {

    companion object {
        fun newInstance() = ChangeInfoDataFragment()
    }

    private lateinit var viewModel: ChangeInfoDataViewModel
    private var _binding: FragmentChangeInfoDataBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ChangeInfoDataViewModel::class.java)
        _binding = FragmentChangeInfoDataBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        hideToolbar()
    }

    override fun onDetach() {
        super.onDetach()
        showToolbar()
    }

}