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
        container.setOnClickListener(View.OnClickListener {closeKeyBoard()  })
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_change_info_data)
            }
        })

        val spinnerGender = binding.spinnerGender
        val spinnerCity = binding.spinnerCity
        val spinnerSecret = binding.spinnerSecret
        val btnCancel = binding.appbarSignup.cancelBtn
        val btnDatePicker = binding.dobInputLayout
        val idLayout = binding.idInputLayout
        val usernameLayout = binding.usernameInputLayout
        val idEdt = binding.idInput
        val nicknameEdt = binding.idNickname
        val btnSubmit = binding.btnSubmitPolicy
        val phoneEdt = binding.idPhone
        val phoneLayout = binding.phoneInputLayout
        val answerLayout = binding.secretAnswerInputLayout
        val answerEdt = binding.idSecret
        val btnBack = binding.appbarSignup.btnBack

        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_change_info_data) })

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
            ).show()
        }


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
        val adapter: ArrayAdapter<String> = object: ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            list
        ){
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
                if (position == spinnerGender.selectedItemPosition && position !=0 ){
                    view.background = ColorDrawable(Color.parseColor("#F7E7CE"))
                    view.setTextColor(Color.parseColor("#333399"))
                }

                // make hint item color gray
                if(position == 0){
                    view.setTextColor(Color.LTGRAY)
                }

                return view
            }

            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }
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

        viewModel.validForm.observe(viewLifecycleOwner, androidx.lifecycle.Observer { signupFormState ->
            if(idEdt.text.toString() == "" || nicknameEdt.text.toString() == "" || spinnerGender.selectedItem.toString() == "選択してください" || btnDatePicker.text.toString() == "1980年1月1日" || spinnerCity.selectedItem.toString() == "選択してください" || phoneEdt.text.toString() == "" ||answerEdt.text.toString() == "" || spinnerSecret.selectedItem.toString() == "選択してください"){
                btnSubmit.isEnabled = false
            }
            else{
                btnSubmit.isEnabled =
                    signupFormState.nicknameError == null && signupFormState.idError == null && signupFormState.genderError == null && signupFormState.dateError == null && signupFormState.cityError == null && signupFormState.phoneError == null && signupFormState.questionError == null && signupFormState.answerError == null
            }
        })

        viewModel.nicknameError.observe(viewLifecycleOwner, androidx.lifecycle.Observer {  error: Int? ->
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
        viewModel.phoneError.observe(viewLifecycleOwner, androidx.lifecycle.Observer { error: Int? ->
            phoneLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        viewModel.answerError.observe(viewLifecycleOwner, androidx.lifecycle.Observer { error: Int? ->
            answerLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        btnDatePicker.addTextChangedListener(afterTextChangedListener)
        spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.genderDataChanged(text = list.get(position))
            }

        }
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
        phoneEdt.doAfterTextChanged { text -> viewModel.phoneDataChanged(text = text.toString()) }
        phoneEdt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
            }
            false
        }

        spinnerGender.adapter = adapter
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
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}