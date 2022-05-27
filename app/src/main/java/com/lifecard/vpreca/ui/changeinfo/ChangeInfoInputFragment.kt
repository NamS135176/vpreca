package com.lifecard.vpreca.ui.changeinfo

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.SignupData
import com.lifecard.vpreca.databinding.FragmentChangeInfoDataBinding
import com.lifecard.vpreca.databinding.FragmentChangeInfoInputBinding
import com.lifecard.vpreca.ui.signup.SignupInputFragmentDirections
import com.lifecard.vpreca.utils.hideLoadingDialog
import java.text.SimpleDateFormat
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
        var container = binding.constraintChangeInfo
        container.setOnClickListener(View.OnClickListener { closeKeyBoard() })
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

        val list = mutableListOf(
            "選択してください",
            "Male",
            "Female",
        )
        val adapter: ArrayAdapter<String> = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            list
        ) {
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view: TextView = super.getDropDownView(
                    position,
                    convertView,
                    parent
                ) as TextView
                // set selected item style
                if (position == spinnerCity.selectedItemPosition && position != 0) {
                    view.background = ColorDrawable(Color.parseColor("#F7E7CE"))
                    view.setTextColor(Color.parseColor("#333399"))
                }

                // make hint item color gray
                if (position == 0) {
                    view.setTextColor(Color.LTGRAY)
                }

                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
        }



        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { signupFormState ->
                if (idEdt.text.toString() == "" || nicknameEdt.text.toString() == "" || email1Edt.text.toString() == "" || email2Edt.text.toString() == "" || email2ConfirmEdt.text.toString() == "" || email1ConfirmEdt.text.toString() == "" || spinnerCity.selectedItem.toString() == "選択してください" || answerEdt.text.toString() == "" || spinnerSecret.selectedItem.toString() == "選択してください") {
                    btnSubmit.isEnabled = false
                } else {
                    btnSubmit.isEnabled =
                        signupFormState.nicknameError == null && signupFormState.idError == null && signupFormState.email1Error == null && signupFormState.email2Error == null && signupFormState.email1ConfirmError == null && signupFormState.email2ConfirmError == null && signupFormState.cityError == null && signupFormState.questionError == null && signupFormState.answerError == null
                }
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


        spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.cityDataChanged(text = list.get(position))
            }
        }

        spinnerSecret?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.questionDataChanged(text = list.get(position))
            }
        }

        idEdt.doAfterTextChanged { text -> viewModel.idDataChanged(text = text.toString()) }

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

        spinnerCity.adapter = adapter
        spinnerSecret.adapter = adapter

        btnSubmit.setOnClickListener(View.OnClickListener {
//            val signupData = SignupData(idEdt.text.toString(), nicknameEdt.text.toString(), passwordEdt.text.toString())
//            val action = SignupInputFragmentDirections.actionSignupInputToSignupConfirm(signupData)
//            findNavController().navigate(action)
            findNavController().navigate(R.id.nav_change_info_confirm)
        })

        return binding.root
    }

    private fun closeKeyBoard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}