package com.lifecard.vpreca.ui.forgotpass

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.base.PowerSpinnerAdapter
import com.lifecard.vpreca.databinding.FragmentForgotPassBinding
import com.lifecard.vpreca.utils.*
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ForgotPassFragment : Fragment() {

    companion object {
        fun newInstance() = ForgotPassFragment()
    }

    private val viewModel: ForgotPassViewModel by viewModels()
    private var _binding: FragmentForgotPassBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotPassBinding.inflate(inflater, container, false)

        val containerDiv = binding.container
        val spinnerQuestion = binding.spinnerQuestion
        val tvDatePicker = binding.dobInputLayoutForgot
        val emailEdt = binding.forgotPassEmailInput
        val btnSubmit = binding.btnSubmitForgot
        val emailLayout = binding.forgotPassEmailLayout
        val btnBack = binding.appbarForgotPass.btnBack
        val cancelBtn = binding.appbarForgotPass.cancelBtn
        val dateLayout = binding.dobInputLayoutForgotLay
        val phoneEdt = binding.forgotPassPhoneInput
        val phoneLayout = binding.forgotPassPhoneLayout
        val questionLayout = binding.forgotPassQuestionLayout
        val answerLayout = binding.forgotPassSecretAnswerLayout
        val answerEdt = binding.forgotPassSecretAnswerInput
        var question = ""
        containerDiv.setOnClickListener { closeKeyBoard() }
        spinnerQuestion.lifecycleOwner = viewLifecycleOwner
        spinnerQuestion.setOnClickListener {
            spinnerQuestion.showOrDismiss()
            closeKeyBoard()
        }

        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, _, _, _ ->
            dismissAllSpinner()
        })

        val cal = Calendar.getInstance()
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backFunction()
            }
        })
        cancelBtn.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    // do something on positive button click
                    backFunction()
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか?")
            }.create().show()
        }

        btnBack.setOnClickListener { backFunction() }


        fun updateDateInView() {
            val myFormat = "yyyy年MM月dd日" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)
            tvDatePicker.text = sdf.format(cal.time)
        }

        fun showDatePicker() {
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView()
                }
            Locale.setDefault(Locale.JAPAN)
            KeyboardUtils.hideKeyboard(requireContext(), binding.root)
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).apply {
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
            }
                .show()

        }

        tvDatePicker.setOnClickListener {
            showDatePicker()
        }

        viewModel.validForm.observe(
            viewLifecycleOwner
        ) { isValid ->
            btnSubmit.isEnabled = isValid
        }

        viewModel.emailError.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            emailLayout.error = try {
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

        viewModel.dateError.observe(viewLifecycleOwner) { error: Int? ->
            dateLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        viewModel.questionError.observe(
            viewLifecycleOwner
        ) { error: Int? ->
            questionLayout.error = try {
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

        viewModel.resetPassState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { changeInfoState ->
                changeInfoState ?: return@Observer
                changeInfoState.error?.messageResId?.let { showPopupMessage(message = getString(it)) }
                changeInfoState.error?.errorMessage?.let { showPopupMessage(message = it) }
                changeInfoState.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
                changeInfoState.success?.let {
                    findNavController().navigate(R.id.nav_forgot_complete)
                }
            })

        viewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        }

        viewModel.forgotPassState.observe(viewLifecycleOwner) {
            it?.success?.let {
                val myFormat = "yyyyMMdd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.JAPAN)
                val date = sdf.format(cal.time)
                viewModel.resetPassData(
                    emailEdt.text.toString(),
                    date,
                    phoneEdt.text.toString(),
                    question,
                    answerEdt.text.toString()
                )
            }
        }

        val listQuestion =
            requireContext().resources.getStringArray(R.array.secret_question).toList()
        val spinnerSecretAdapter = PowerSpinnerAdapter(spinnerQuestion)
        spinnerSecretAdapter.setItems(listQuestion)
        spinnerQuestion.setSpinnerAdapter(spinnerSecretAdapter)

        spinnerQuestion.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { _, _, _, newItem ->
            newItem?.let {
                checkValidForm()
                question = it
            }
        })
        spinnerQuestion.setOnSpinnerOutsideTouchListener { _, _ -> spinnerQuestion.dismiss() }

        tvDatePicker.doAfterTextChanged { checkValidForm() }
        emailEdt.doAfterTextChanged { checkValidForm() }
        phoneEdt.doAfterTextChanged { checkValidForm() }
        answerEdt.doAfterTextChanged { checkValidForm() }

        btnSubmit.setOnClickListener { submit() }

        return binding.root
    }

    private fun submit() {
        viewModel.submit(
            email = binding.forgotPassEmailInput.text.toString(),
            date = binding.dobInputLayoutForgot.text.toString(),
            phone = binding.forgotPassPhoneInput.text.toString(),
            question = binding.spinnerQuestion.text.toString(),
            answer = binding.forgotPassSecretAnswerInput.text.toString()
        )
    }


    private fun checkValidForm() {
        viewModel.checkValidForm(
            email = binding.forgotPassEmailInput.text.toString(),
            date = binding.dobInputLayoutForgot.text.toString(),
            phone = binding.forgotPassPhoneInput.text.toString(),
            question = binding.spinnerQuestion.text.toString(),
            answer = binding.forgotPassSecretAnswerInput.text.toString()
        )
    }

    fun backFunction() {
        findNavController().popBackStack(
            R.id.nav_login,
            inclusive = false,
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dismissAllSpinner()
    }

    private fun dismissAllSpinner() {
        binding.spinnerQuestion.dismiss()
    }
}