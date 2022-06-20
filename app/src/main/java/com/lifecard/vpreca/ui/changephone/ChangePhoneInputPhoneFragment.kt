package com.lifecard.vpreca.ui.changephone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentChangePhoneInputPhoneBinding

class ChangePhoneInputPhoneFragment : Fragment() {

    companion object {
        fun newInstance() = ChangePhoneInputPhoneFragment()
    }

    private lateinit var phoneViewModel: ChangePhoneInputPhoneViewModel
    private var _binding: FragmentChangePhoneInputPhoneBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        phoneViewModel = ViewModelProvider(this)[ChangePhoneInputPhoneViewModel::class.java]
        _binding = FragmentChangePhoneInputPhoneBinding.inflate(inflater, container, false)
        val inputPhone = binding.forgotPassEmailInput
        val btnSubmitPhone = binding.btnSubmitPolicy

        val btnCancel = binding.appbarForgotPass.cancelBtn
        val layout = binding.forgotPassEmailLayout
        btnCancel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    findNavController().popBackStack(R.id.nav_home, inclusive = false)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか?")
            }.create().show()
        }


        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setPositiveButton("はい") { _, _ ->
                        findNavController().popBackStack(R.id.nav_home, inclusive = false)
                    }
                    setNegativeButton("いいえ", null)
                    setMessage("途中ですがキャンセルしてもよろしいですか?")
                }.create().show()
            }
        })

        phoneViewModel.formState.observe(viewLifecycleOwner) { phoneViewModel.checkFormValid() }

        phoneViewModel.phoneError.observe(viewLifecycleOwner) { error: Int? ->
            layout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        phoneViewModel.validForm.observe(
            viewLifecycleOwner
        ) { isValid ->
            btnSubmitPhone.isEnabled = isValid
        }


        phoneViewModel.formResultState.observe(viewLifecycleOwner) {
            it?.success?.let {
                val action = ChangePhoneInputPhoneFragmentDirections.actionToConfirm(
                    GiftCardConfirmData(inputPhone.text.toString())
                )
                findNavController().navigate(action)
            }
        }

        inputPhone.doAfterTextChanged { text -> phoneViewModel.phoneDataChanged(text = text.toString()) }

        btnSubmitPhone.setOnClickListener {
            phoneViewModel.submit()
        }
        return binding.root
    }
}