package com.lifecard.vpreca.ui.changeinfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangeInfoConfirmDataBinding
import com.lifecard.vpreca.databinding.FragmentChangeInfoDataBinding

class ChangeInfoConfirmDataFragment : Fragment() {

    companion object {
        fun newInstance() = ChangeInfoConfirmDataFragment()
    }

    private lateinit var viewModel: ChangeInfoConfirmDataViewModel
    private var _binding: FragmentChangeInfoConfirmDataBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeInfoConfirmDataBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_change_info_input)
            }
        })
        val btnCancelConfirm = binding.btnCancelConfirm
        val btnSubmitConfirm = binding.btnSubmitConfirm
        val btnBack = binding.appbarConfirmSignup.btnBack
        val btnCancel = binding.appbarConfirmSignup.cancelBtn

        btnBack.setOnClickListener(View.OnClickListener {  findNavController().navigate(R.id.nav_change_info_input)})

        btnCancelConfirm.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_change_info_input) })

        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    findNavController().navigate(R.id.nav_home)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        })

        btnSubmitConfirm.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_change_info_complete) })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChangeInfoConfirmDataViewModel::class.java)
        // TODO: Use the ViewModel
    }

}