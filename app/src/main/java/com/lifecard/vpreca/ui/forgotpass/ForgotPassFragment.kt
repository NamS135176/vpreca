package com.lifecard.vpreca.ui.forgotpass

import android.app.DatePickerDialog
import android.content.Intent
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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentCompleteSignupBinding
import com.lifecard.vpreca.databinding.FragmentForgotPassBinding
import java.text.SimpleDateFormat
import java.util.*

class ForgotPassFragment : Fragment() {

    companion object {
        fun newInstance() = ForgotPassFragment()
    }

    private lateinit var viewModel: ForgotPassViewModel
    private var _binding: FragmentForgotPassBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPassBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(ForgotPassViewModel::class.java)

//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
//            OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                backFunction()
//            }
//        })

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
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            tvDatePicker!!.text = sdf.format(cal.getTime())
        }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        tvDatePicker.setOnClickListener {
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
                if (position == spinnerQuestion.selectedItemPosition && position != 0) {
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
        spinnerQuestion.adapter = adapter

        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { forgotPassState ->
                if (emailEdt.text.toString() == "" || tvDatePicker.text.toString() == "年/月/日" || phoneEdt.text.toString() == "" || spinnerQuestion.selectedItem.toString() == "選択してください" || answerEdt.text.toString() == "") {
                    btnSubmit.isEnabled = false
                } else {
                    btnSubmit.isEnabled =
                        (forgotPassState.emailError == null && forgotPassState.phoneError == null && forgotPassState.dateError == null && forgotPassState.questionError == null && forgotPassState.answerError == null)
                }
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

        spinnerQuestion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        tvDatePicker.doAfterTextChanged { text -> viewModel.dateDataChanged(text = text.toString()) }
        emailEdt.doAfterTextChanged { text -> viewModel.emailDataChanged(text = text.toString()) }
        emailEdt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
            }
            false
        }

        phoneEdt.doAfterTextChanged { text -> viewModel.phoneDataChanged(text = text.toString()) }
        phoneEdt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
            }
            false
        }

        answerEdt.doAfterTextChanged { text -> viewModel.answerDataChanged(text = text.toString()) }
        answerEdt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
            }
            false
        }

        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_forgot_complete) })

        return binding.root
    }

    fun backFunction() {
        findNavController().popBackStack()
    }
}