package com.lifecard.vpreca.ui.changephone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePhoneConfirmBinding
import com.lifecard.vpreca.utils.UserConverter

class ChangePhoneConfirm : Fragment() {

    companion object {
        fun newInstance() = ChangePhoneConfirm()
    }

    private var _binding: FragmentChangePhoneConfirmBinding? = null
    private val binding get() = _binding!!
    private val args:ChangePhoneConfirmArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePhoneConfirmBinding.inflate(inflater, container, false)
        val tvPhone = binding.tvConfirmID
        val btnCancel = binding.btnCancelConfirm
        val btnSubmit = binding.btnSubmitConfirm
        tvPhone.text = UserConverter.formatPhone(args.cardData?.preRoute!!)
        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_confirm_to_phone_input)
            }
        })

        btnCancel.setOnClickListener { findNavController().navigate(R.id.action_confirm_to_phone_input) }
        btnSubmit.setOnClickListener { findNavController().navigate(R.id.nav_change_phone_complete) }

        return binding.root
    }

}