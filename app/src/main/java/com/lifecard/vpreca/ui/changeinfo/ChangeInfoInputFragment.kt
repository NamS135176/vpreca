package com.lifecard.vpreca.ui.changeinfo

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangeInfoInputBinding
import com.lifecard.vpreca.utils.closeKeyBoard
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import java.util.*

class ChangeInfoInputFragment : Fragment() {

    companion object {
        fun newInstance() = ChangeInfoInputFragment()
    }

    private lateinit var viewModel: ChangeInfoInputViewModel
    private var _binding: FragmentChangeInfoInputBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ChangeInfoInputViewModel::class.java)
        _binding = FragmentChangeInfoInputBinding.inflate(inflater, container, false)

        binding.constraintChangeInfo.setOnClickListener(View.OnClickListener { closeKeyBoard() })

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.nav_change_info_data)
                }
            })


        val spinnerCity = binding.spinnerCity
        val spinnerSecret = binding.spinnerSecret
        val btnCancel = binding.appbarSignup.cancelBtn
        val idLayout = binding.idInputLayout
        val usernameLayout = binding.usernameInputLayout
        val idEdt = binding.idInput
        val nicknameEdt = binding.idNickname
        val btnSubmit = binding.btnSubmitPolicy
        val answerLayout = binding.secretAnswerInputLayout
        val answerEdt = binding.idSecret
        val btnBack = binding.appbarSignup.btnBack
        val email1Layout = binding.changeInfoEmail1Layout
        val email1Edt = binding.email1Input
        val email1ConfirmLayout = binding.changeInfoEmail1ConfirmLayout
        val email1ConfirmEdt = binding.email1ConfirmInput
        val email2Layout = binding.changeInfoEmail2Layout
        val email2Edt = binding.email2Input
        val email2ConfirmLayout = binding.changeInfoEmail2ConfirmLayout
        val email2ConfirmEdt = binding.email2ConfirmInput

        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_change_info_data) })

        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    // do something on positive button click
//                    val intent = Intent(requireContext(), LoginActivity::class.java).apply {
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    }
//                    startActivity(intent)
                    findNavController().navigate(R.id.nav_home)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        })

        spinnerSecret.lifecycleOwner = viewLifecycleOwner
        spinnerCity.lifecycleOwner = viewLifecycleOwner

        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { isValid ->
                btnSubmit.isEnabled = isValid
            })

        viewModel.nicknameError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                usernameLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.idError.observe(viewLifecycleOwner, androidx.lifecycle.Observer { error: Int? ->
            idLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })
        viewModel.email1Error.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                email1Layout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.email2Error.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                email2Layout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.email1ConfirmError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                email1ConfirmLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.email2ConfirmError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                email2ConfirmLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.answerError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                answerLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })

        viewModel.formResultState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.success?.let { findNavController().navigate(R.id.nav_change_info_confirm) }
        })
        viewModel.formState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { viewModel.checkValidForm() })

        spinnerSecret.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            newItem?.let { viewModel.questionDataChanged(text = it) }
        })
        spinnerCity.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            newItem?.let { viewModel.cityDataChanged(text = it) }
        })

        idEdt.doAfterTextChanged { text -> viewModel.loginIdDataChanged(text = text.toString()) }

        nicknameEdt.doAfterTextChanged { text -> viewModel.nicknameDataChanged(text = text.toString()) }
        answerEdt.doAfterTextChanged { text -> viewModel.answerDataChanged(text = text.toString()) }
        email1Edt.doAfterTextChanged { text -> viewModel.email1DataChanged(text = text.toString()) }
        email2Edt.doAfterTextChanged { text -> viewModel.email2DataChanged(text = text.toString()) }
        email1ConfirmEdt.doAfterTextChanged { text ->
            viewModel.email1ConfirmDataChanged(
                text = text.toString(),
                email1Edt.text.toString()
            )
        }
        email2ConfirmEdt.doAfterTextChanged { text ->
            viewModel.email2ConfirmDataChanged(
                text = text.toString(),
                email2Edt.text.toString()
            )
        }

        btnSubmit.setOnClickListener(View.OnClickListener {
            viewModel.submit()
        })

        return binding.root
    }
}