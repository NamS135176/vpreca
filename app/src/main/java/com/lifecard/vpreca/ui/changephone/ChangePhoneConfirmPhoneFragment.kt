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
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePhoneConfirmPhoneBinding
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
    ): View {
        viewModel = ViewModelProvider(this)[ChangePhoneConfirmPhoneViewModel::class.java]
        _binding = FragmentChangePhoneConfirmPhoneBinding.inflate(inflater, container, false)
        val inputPhoneConfirm = binding.forgotPassEmailInput
        val btnSubmitPhoneConfirm = binding.btnSubmitPolicy
        val layout = binding.forgotPassEmailLayout
        val tvphone = binding.tvPhone
        val btnCancel = binding.appbarForgotPass.cancelBtn
        tvphone.text = UserConverter.formatPhone(args.cardData?.preRoute!!)
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

        viewModel.formState.observe(viewLifecycleOwner) { viewModel.checkFormValid() }

        viewModel.cfPhoneError.observe(viewLifecycleOwner) { error: Int? ->
            layout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        viewModel.validForm.observe(
            viewLifecycleOwner
        ) { isValid ->
            btnSubmitPhoneConfirm.isEnabled = isValid
        }


        viewModel.formResultState.observe(viewLifecycleOwner) {
            it?.success?.let {
                val action = ChangePhoneConfirmPhoneFragmentDirections.actionToConfirm(
                    args.cardData
                )
                findNavController().navigate(action)
            }
        }

        inputPhoneConfirm.doAfterTextChanged { text -> viewModel.cfPhoneDataChanged(text = text.toString()) }

        btnSubmitPhoneConfirm.setOnClickListener {
            viewModel.submit()
        }

        return binding.root
    }

}