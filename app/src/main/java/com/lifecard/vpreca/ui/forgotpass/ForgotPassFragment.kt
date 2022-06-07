package com.lifecard.vpreca.ui.forgotpass

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentForgotPassBinding
import com.lifecard.vpreca.utils.closeKeyBoard
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showPopupMessage
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
    ): View? {
        _binding = FragmentForgotPassBinding.inflate(inflater, container, false)
//        viewModel = ViewModelProvider(this).get(ForgotPassViewModel::class.java)

        val container = binding.container
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
        val loading = binding.loading
        container.setOnClickListener { closeKeyBoard() }
        spinnerQuestion.lifecycleOwner = viewLifecycleOwner

        val cal = Calendar.getInstance()

        cancelBtn.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    // do something on positive button click
                    backFunction()
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        })

        btnBack.setOnClickListener(View.OnClickListener { backFunction() })


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
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()

        }

        tvDatePicker.setOnClickListener {
            showDatePicker()
        }

        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { isValid ->
                btnSubmit.isEnabled = isValid
            })

        viewModel.emailError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                emailLayout.error = try {
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

        viewModel.dateError.observe(viewLifecycleOwner, androidx.lifecycle.Observer { error: Int? ->
            dateLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        viewModel.questionError.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { error: Int? ->
                questionLayout.error = try {
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

        viewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                true -> loading.visibility = View.VISIBLE
                else -> loading.visibility = View.GONE
            }
        })

        viewModel.forgotPassState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.success?.let {
                viewModel.resetPassData(
                    emailEdt.text.toString(),
                    tvDatePicker.text.toString(),
                    phoneEdt.text.toString(),
                    question,
                    answerEdt.text.toString()
                )
//                println(question)
            }
        })

        spinnerQuestion.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            newItem?.let {
                checkValidForm()
                question = it
            }
        })

        tvDatePicker.doAfterTextChanged { _ -> checkValidForm() }
        emailEdt.doAfterTextChanged { _ -> checkValidForm() }
        phoneEdt.doAfterTextChanged { _ -> checkValidForm() }
        answerEdt.doAfterTextChanged { _ -> checkValidForm() }

        btnSubmit.setOnClickListener(View.OnClickListener { submit() })

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
        findNavController().popBackStack()
    }
}