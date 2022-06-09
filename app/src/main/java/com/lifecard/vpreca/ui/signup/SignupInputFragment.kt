package com.lifecard.vpreca.ui.signup

import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.SignupInputFragmentBinding
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import java.text.SimpleDateFormat
import java.util.*

class SignupInputFragment : Fragment() {

    companion object {
        fun newInstance() = SignupInputFragment()
    }

    private lateinit var viewModel: SignupInputViewModel
    private var _binding: SignupInputFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignupInputFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SignupInputViewModel::class.java)


        val container = binding.container
        val scrollView = binding.scrollView
        val spinnerGender = binding.spinnerGender
        val spinnerCity = binding.spinnerCity
        val spinnerSecret = binding.spinnerSecret
        val btnCancel = binding.appbarSignup.cancelBtn
        val btnDatePicker = binding.dobInputLayout
        val idLayout = binding.idInputLayout
        val usernameLayout = binding.usernameInputLayout
        val idEdt = binding.idInput
        val usernameEdit = binding.idUsername
        val btnSubmit = binding.btnSubmitPolicy
        val phoneEdt = binding.idPhone
        val phoneLayout = binding.phoneInputLayout
        val answerLayout = binding.secretAnswerInputLayout
        val answerEdt = binding.idSecret
        val passwordLayout = binding.passwordInputLayout
        val passwordEdt = binding.idPassword
        val cfPasswordLayout = binding.cfPasswordInputLayout
        val cfPasswordEdt = binding.idCfPassword
        val kanaFirstName = binding.kanaFirstName
        val kanaLastName = binding.kanaLastName
        val hiraFirstName = binding.hiraFirstName
        val hiraLastName = binding.hiraLastName

        fun dismissAllSpinner() {
            if (spinnerCity.isShowing) spinnerCity.dismiss()
            if (spinnerGender.isShowing) spinnerGender.dismiss()
            if (spinnerSecret.isShowing) spinnerSecret.dismiss()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (spinnerCity.isShowing || spinnerGender.isShowing || spinnerSecret.isShowing) {
                        dismissAllSpinner()
                        return
                    }
                    findNavController().navigate(R.id.nav_policy)
                }
            })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_signup_confirm_email)
            }
        })


        scrollView.setOnClickListener(View.OnClickListener { dismissAllSpinner() })

        val cal = Calendar.getInstance()

        fun updateDateInView() {
            val myFormat = "yyyy年MM月dd日" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            btnDatePicker!!.text = sdf.format(cal.getTime())
        }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        btnDatePicker.setOnClickListener {
            Locale.setDefault(Locale.JAPAN)
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
                .apply {
                    datePicker
                    setButton(
                        DatePickerDialog.BUTTON_POSITIVE, getString(R.string.button_ok),
                        DialogInterface.OnClickListener { _, _ ->
                            dateSetListener.onDateSet(
                                datePicker,
                                datePicker.year,
                                datePicker.month,
                                datePicker.dayOfMonth
                            )
                        })
                    setButton(
                        DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.button_cancel),
                        null as DialogInterface.OnClickListener?
                    )
                }.show()
        }


        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    findNavController().navigate(R.id.nav_login)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        })

        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.dateDataChanged(text = s.toString())
            }
        }

        viewModel.formState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            viewModel.checkValidForm()
        })

        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { isValid ->
                btnSubmit.isEnabled = isValid
            })

        viewModel.usernameError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                usernameLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.loginIdError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                idLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.phoneError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                phoneLayout.error = try {
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
        viewModel.passwordError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                passwordLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.cfPasswordError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                cfPasswordLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })

        viewModel.hiraFullNameError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                binding.hiraNameInputLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })
        viewModel.kanaFullNameError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                binding.nameInputLayout.error = try {
                    error?.let { getString(error) }
                } catch (e: Error) {
                    null
                }
            })

        viewModel.formResultState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.success?.let {
                findNavController().navigate(R.id.nav_complete)
//                val action =
//                    SignupInputFragmentDirections.actionSignupInputToSignupConfirm(viewModel.formState.value!!)
//                findNavController().navigate(action)
            }

        })
        btnDatePicker.addTextChangedListener(afterTextChangedListener)

        spinnerGender.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            newItem?.let { viewModel.genderDataChanged(text = it) }
        })
        spinnerCity.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            newItem?.let { viewModel.cityDataChanged(text = it) }
        })
        spinnerSecret.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            newItem?.let { viewModel.questionDataChanged(text = it) }
        })

        idEdt.doAfterTextChanged { text -> viewModel.loginIdDataChanged(text = text.toString()) }

        usernameEdit.doAfterTextChanged { text -> viewModel.usernameDataChanged(text = text.toString()) }
        passwordEdt.doAfterTextChanged { text -> viewModel.passwordDataChanged(text = text.toString()) }
        cfPasswordEdt.doAfterTextChanged { text -> viewModel.cfPasswordDataChanged(text = text.toString()) }
        answerEdt.doAfterTextChanged { text -> viewModel.answerDataChanged(text = text.toString()) }
        phoneEdt.doAfterTextChanged { text -> viewModel.phoneDataChanged(text = text.toString()) }

        kanaFirstName.doAfterTextChanged { text -> viewModel.kanaFirstNameDataChanged(text = text.toString()) }
        kanaLastName.doAfterTextChanged { text -> viewModel.kanaLastNameDataChanged(text = text.toString()) }
        hiraFirstName.doAfterTextChanged { text -> viewModel.hiraFirstNameDataChanged(text = text.toString()) }
        hiraLastName.doAfterTextChanged { text -> viewModel.hiraLastNameDataChanged(text = text.toString()) }

        btnSubmit.setOnClickListener(View.OnClickListener {
            viewModel.submit()
        })


        return binding.root
    }


}