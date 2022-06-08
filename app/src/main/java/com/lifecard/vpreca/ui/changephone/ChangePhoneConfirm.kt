package com.lifecard.vpreca.ui.changephone

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePhoneConfirmBinding
import com.lifecard.vpreca.databinding.FragmentChangePhoneConfirmPhoneBinding
import com.lifecard.vpreca.utils.UserConverter

class ChangePhoneConfirm : Fragment() {

    companion object {
        fun newInstance() = ChangePhoneConfirm()
    }

    private lateinit var viewModel: ChangePhoneConfirmViewModel
    private var _binding: FragmentChangePhoneConfirmBinding? = null
    private val binding get() = _binding!!
    private val args:ChangePhoneConfirmArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ChangePhoneConfirmViewModel::class.java)
        _binding = FragmentChangePhoneConfirmBinding.inflate(inflater, container, false)
        val tvPhone = binding.tvConfirmID
        val btnCancel = binding.btnCancelConfirm
        val btnSubmit = binding.btnSubmitConfirm
        tvPhone.text = UserConverter.formatPhone(args.cardData?.preRoute!!)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_change_phone_input_phone)
            }
        })

        btnCancel.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_change_phone_input_phone) })
        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_change_phone_complete) })

        return binding.root
    }

}