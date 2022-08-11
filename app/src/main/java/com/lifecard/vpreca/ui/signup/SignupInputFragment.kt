package com.lifecard.vpreca.ui.signup

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.base.PowerSpinnerAdapter
import com.lifecard.vpreca.databinding.SignupInputFragmentBinding
import com.lifecard.vpreca.utils.KeyboardUtils
import com.lifecard.vpreca.utils.closeKeyBoard
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import java.text.SimpleDateFormat
import java.util.*

class SignupInputFragment : Fragment() {

    companion object {
        fun newInstance() = SignupInputFragment()
    }

    private val viewModel: SignupInputViewModel by viewModels()
    private var _binding: SignupInputFragmentBinding? = null
    private val binding get() = _binding!!
    private var saveState: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignupInputFragmentBinding.inflate(inflater, container, false)
        println(savedInstanceState)
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
        val dateLayout = binding.dateLayout

        scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
            dismissAllSpinner()
        })

        val cal = Calendar.getInstance()

        fun updateDateInView() {
            val myFormat = "yyyy年MM月dd日" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)
            btnDatePicker.text = sdf.format(cal.time)
        }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        btnDatePicker.setOnClickListener {
            Locale.setDefault(Locale.JAPAN)
            KeyboardUtils.hideKeyboard(requireContext(), binding.root)
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
                        DatePickerDialog.BUTTON_POSITIVE, getString(R.string.button_ok)
                    ) { _, _ ->
                        dateSetListener.onDateSet(
                            datePicker,
                            datePicker.year,
                            datePicker.month,
                            datePicker.dayOfMonth
                        )
                    }
                    setButton(
                        DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.button_cancel),
                        null as DialogInterface.OnClickListener?
                    )
                }.show()
        }


        btnCancel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    findNavController().popBackStack(R.id.nav_login, inclusive = false)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか?")
            }.create().show()
        }

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

        viewModel.formState.observe(viewLifecycleOwner) {
            viewModel.checkValidForm()
        }

        viewModel.validForm.observe(
            viewLifecycleOwner
        ) { isValid ->
            btnSubmit.isEnabled = isValid
        }

        viewModel.usernameError.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            usernameLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }
        viewModel.loginIdError.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            idLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }
        viewModel.phoneError.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            phoneLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        viewModel.dateError.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            dateLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }
        viewModel.answerError.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            answerLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }
        viewModel.passwordError.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            passwordLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }
        viewModel.cfPasswordError.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            cfPasswordLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }


        viewModel.nameError.observe(
            viewLifecycleOwner
        ) { errors: Array<Number?>? ->
            binding.nameInputLayout.error = try {
                val errorInt = errors?.filterNotNull()
                if (!errorInt.isNullOrEmpty()) {
                    println("errorInt: $errorInt - text ${errorInt.map { getString(it as Int) }}")
                    errorInt.joinToString(separator = "\n") { getString(it as Int) }
                } else {
                    null
                }
            } catch (e: Error) {
                null
            }
        }

        viewModel.furiganaNameError.observe(viewLifecycleOwner) { error ->
            when (error) {
                true -> {
                    binding.hiraFirstName.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.input_signup_selected
                    )
                    binding.hiraLastName.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.input_signup_selected
                    )
                }
                else -> {
                    binding.hiraFirstName.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.input_signup_style)
                    binding.hiraLastName.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.input_signup_style)
                }
            }
        }
        viewModel.kanaNameError.observe(viewLifecycleOwner) { error ->
            when (error) {
                true -> {
                    binding.kanaFirstName.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.input_signup_selected
                    )
                    binding.kanaLastName.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.input_signup_selected
                    )
                }
                else -> {
                    binding.kanaFirstName.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.input_signup_style)
                    binding.kanaLastName.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.input_signup_style)
                }
            }
        }

        viewModel.formResultState.observe(viewLifecycleOwner) {
            it?.success?.let {
//                findNavController().navigate(R.id.nav_complete)
                val action =
                    SignupInputFragmentDirections.actionSignupInputToSignupConfirm(viewModel.formState.value!!)
                findNavController().navigate(action)
                viewModel.formResultState.value = null
            }

        }
        btnDatePicker.addTextChangedListener(afterTextChangedListener)

        val listGender = requireContext().resources.getStringArray(R.array.genders).toList()
        val spinnerGenderAdapter = PowerSpinnerAdapter(spinnerGender)
        spinnerGenderAdapter.setItems(listGender)
        spinnerGender.setSpinnerAdapter(spinnerGenderAdapter)

        spinnerGender.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { _, _, _, newItem ->
            newItem?.let { viewModel.genderDataChanged(text = it) }
        })

        val listCity = requireContext().resources.getStringArray(R.array.cities).toList()
        val spinnerCityAdapter = PowerSpinnerAdapter(spinnerCity)
        spinnerCityAdapter.setItems(listCity)
        spinnerCity.setSpinnerAdapter(spinnerCityAdapter)

        spinnerCity.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { _, _, _, newItem ->
            newItem?.let { viewModel.cityDataChanged(text = it) }
        })

        val listQuestion =
            requireContext().resources.getStringArray(R.array.secret_question).toList()
        val spinnerSecretAdapter = PowerSpinnerAdapter(spinnerSecret)
        spinnerSecretAdapter.setItems(listQuestion)
        spinnerSecret.setSpinnerAdapter(spinnerSecretAdapter)

        spinnerSecret.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { _, _, _, newItem ->
            newItem?.let { viewModel.questionDataChanged(text = it) }
        })

        spinnerGender.setOnSpinnerOutsideTouchListener { _, _ -> spinnerGender.dismiss() }
        spinnerSecret.setOnSpinnerOutsideTouchListener { _, _ -> spinnerSecret.dismiss() }
        spinnerCity.setOnSpinnerOutsideTouchListener { _, _ -> spinnerCity.dismiss() }

        spinnerGender.setOnClickListener(View.OnClickListener {
            closeKeyBoard()
            spinnerGender.showOrDismiss()
        })

        spinnerSecret.setOnClickListener(View.OnClickListener {
            closeKeyBoard()
            spinnerSecret.showOrDismiss()
        })

        spinnerCity.setOnClickListener(View.OnClickListener {
            closeKeyBoard()
            spinnerCity.showOrDismiss()
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

        btnSubmit.setOnClickListener {
            viewModel.submit()
        }

        saveState?.let { bundle ->
            spinnerCity.selectItemByIndex(bundle.getInt("city"))
            spinnerGender.selectItemByIndex(bundle.getInt("gender"))
            spinnerSecret.selectItemByIndex(bundle.getInt("question"))
            btnDatePicker.text = bundle.getString("birthdate")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveData()
        dismissAllSpinner()
    }

    private fun saveData() {
        val cityAdapter = binding.spinnerCity.getSpinnerAdapter<PowerSpinnerAdapter>()
        val genderAdapter = binding.spinnerGender.getSpinnerAdapter<PowerSpinnerAdapter>()
        val secretAdapter = binding.spinnerSecret.getSpinnerAdapter<PowerSpinnerAdapter>()

        saveState = bundleOf(
            "city" to cityAdapter.index,
            "gender" to genderAdapter.index,
            "question" to secretAdapter.index,
            "birthdate" to binding.dobInputLayout.text.toString()
        )
    }

    private fun dismissAllSpinner() {
        binding.spinnerCity.dismiss()
        binding.spinnerGender.dismiss()
        binding.spinnerSecret.dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.spinnerCity.isShowing || binding.spinnerGender.isShowing || binding.spinnerSecret.isShowing) {
                        dismissAllSpinner()
                        return
                    }
                    MaterialAlertDialogBuilder(requireContext()).apply {
                        setPositiveButton("はい") { _, _ ->
                            findNavController().popBackStack(R.id.nav_login,inclusive = false)
                        }
                        setNegativeButton("いいえ",  null)
                        setMessage("途中ですがキャンセルしてもよろしいですか?")
                    }.create().show()
                }
            })
    }
}