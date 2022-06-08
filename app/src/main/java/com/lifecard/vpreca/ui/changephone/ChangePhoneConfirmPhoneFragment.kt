package com.lifecard.vpreca.ui.changephone

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentChangePhoneConfirmPhoneBinding
import com.lifecard.vpreca.databinding.FragmentChangePhoneInputPhoneBinding
import com.lifecard.vpreca.utils.UserConverter

class ChangePhoneConfirmPhoneFragment : Fragment() {

    companion object {
        fun newInstance() = ChangePhoneConfirmPhoneFragment()
    }

    private lateinit var viewModel: ChangePhoneConfirmPhoneViewModel
    private var _binding: FragmentChangePhoneConfirmPhoneBinding? = null
    private val binding get() = _binding!!
    private val args:ChangePhoneConfirmPhoneFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ChangePhoneConfirmPhoneViewModel::class.java)
        _binding = FragmentChangePhoneConfirmPhoneBinding.inflate(inflater, container, false)
        val inputPhoneConfirm = binding.forgotPassEmailInput
        val btnSubmitPhoneConfirm = binding.btnSubmitPolicy
        val layout = binding.forgotPassEmailLayout
        val tvphone = binding.tvPhone
        val btnCancel = binding.appbarForgotPass.cancelBtn
        tvphone.text = UserConverter.formatPhone(args.cardData?.preRoute!!)
        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    findNavController().navigate(R.id.nav_change_phone_input_phone)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        })


        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_change_phone_input_phone)
            }
        })

        viewModel.formState.observe(viewLifecycleOwner, Observer { viewModel.checkFormValid() })

        viewModel.cfPhoneError.observe(viewLifecycleOwner, Observer { error: Int? ->
            layout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { isValid ->
                btnSubmitPhoneConfirm.isEnabled = isValid
            })


        viewModel.formResultState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.success?.let {
                val action = ChangePhoneConfirmPhoneFragmentDirections.actionToConfirm(
                   args.cardData
                )
                findNavController().navigate(action)
            }
        })

        inputPhoneConfirm.doAfterTextChanged { text -> viewModel.cfPhoneDataChanged(text = text.toString()) }

        btnSubmitPhoneConfirm.setOnClickListener(View.OnClickListener {
            viewModel.submit()
        })

        return binding.root
    }

}