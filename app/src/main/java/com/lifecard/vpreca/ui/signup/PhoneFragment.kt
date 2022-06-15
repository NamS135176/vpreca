package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.BalanceTotalRemain
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentPhoneBinding
import com.lifecard.vpreca.databinding.FragmentPolicyBinding
import com.lifecard.vpreca.ui.balance_amount.BalanceSelectSourceConfirmFragmentDirections
import com.lifecard.vpreca.ui.changeinfo.ChangeInfoInputViewModel
import com.lifecard.vpreca.ui.changephone.ChangePhoneInputPhoneFragmentDirections


class PhoneFragment : Fragment() {

    private lateinit var phoneViewModel: PhoneViewModel
    private var _binding:FragmentPhoneBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        phoneViewModel = ViewModelProvider(this).get(PhoneViewModel::class.java)
        _binding = FragmentPhoneBinding.inflate(inflater, container, false)
        val inputPhone = binding.forgotPassEmailInput
        val btnSubmitPhone = binding.btnSubmitPolicy
        val btnBack = binding.appbarForgotPass.btnBack
        val btnCancel = binding.appbarForgotPass.cancelBtn
        val layout = binding.forgotPassEmailLayout
        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    findNavController().navigate(R.id.action_phone_to_login)
                }
                setNegativeButton("いいえ", null)
                setMessage("入力途中ですがキャンセル\n" +
                        "してもよろしいですか？")
            }.create().show()
        })
        btnBack.setOnClickListener(View.OnClickListener {
            val action = PhoneFragmentDirections.actionToPolicy(
                GiftCardConfirmData("1")
            )
            findNavController().navigate(action)
        })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = PhoneFragmentDirections.actionToPolicy(
                    GiftCardConfirmData("1")
                )
                findNavController().navigate(action)
            }
        })

        phoneViewModel.formState.observe(viewLifecycleOwner, Observer { phoneViewModel.checkFormValid() })

        phoneViewModel.phoneError.observe(viewLifecycleOwner, Observer { error: Int? ->
            layout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        phoneViewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { isValid ->
                btnSubmitPhone.isEnabled = isValid
            })


        phoneViewModel.formResultState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.success?.let {
                val action = PhoneFragmentDirections.actionPhoneToConfirm(
                    GiftCardConfirmData(inputPhone.text.toString())
                )
                findNavController().navigate(action)
//                findNavController().navigate(R.id.nav_signup_confirm_phone)
            }
        })

        inputPhone.doAfterTextChanged { text -> phoneViewModel.phoneDataChanged(text = text.toString()) }

        btnSubmitPhone.setOnClickListener(View.OnClickListener {
            phoneViewModel.submit()
        })
        return binding.root
    }



}