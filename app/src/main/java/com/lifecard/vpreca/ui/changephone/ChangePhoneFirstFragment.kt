package com.lifecard.vpreca.ui.changephone

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePhoneFirstBinding
import com.lifecard.vpreca.utils.UserConverter
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showPopupMessage
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ChangePhoneFirstFragment : Fragment() {

    companion object {
        fun newInstance() = ChangePhoneFirstFragment()
    }

    private val viewModel: ChangePhoneFirstViewModel by viewModels()
    private var _binding: FragmentChangePhoneFirstBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePhoneFirstBinding.inflate(inflater, container, false)
        val tvPhone = binding.tvConfirmID
        val btnBack = binding.appbarConfirmSignup.btnBack

        val btnOpenDialog = binding.btnSubmitPolicy
        val cal = Calendar.getInstance()
        var dateData = ""

        btnOpenDialog.setOnClickListener {
            val view = View.inflate(requireContext(), R.layout.bod_dialog, null)
            val builder = AlertDialog.Builder(requireContext())
            builder.setView(view)
            val dialog = builder.create()
            dialog.show()

            val btnCancel = view.findViewById<MaterialButton>(R.id.btn_dialog_cancel)
            val tvDob = view.findViewById<MaterialTextView>(R.id.dob_dialog)
            val btnSubmit = view.findViewById<MaterialButton>(R.id.btn_dialog_submit)
            fun doChangeText(text: String) {
                btnSubmit.isEnabled = text != "年/月/日"
            }
            tvDob.doAfterTextChanged { text -> doChangeText(text = text.toString()) }

            fun updateDateInView() {
                val myFormat = "yyyy年MM月dd日" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                tvDob!!.text = sdf.format(cal.time)
            }

            val dateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView()
                }

            tvDob.setOnClickListener {
                Locale.setDefault(Locale.JAPAN)
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

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            btnSubmit.setOnClickListener {
                val myFormat = "yyyyMMdd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val date1 = sdf.format(cal.time)
                val layout = view.findViewById<MaterialTextView>(R.id.tv_error)
                val datenow = sdf.format(Date())
                val cmp = sdf.parse(date1)?.compareTo(sdf.parse(datenow))

                if (cmp != null) {
                    when {
                        cmp < 0 -> {
                            if (date1 == dateData) {
                                tvDob.setBackgroundResource(R.drawable.input_signup_style)
                                layout.visibility = View.INVISIBLE
                                findNavController().navigate(R.id.nav_change_phone_second)
                                dialog.dismiss()
                            } else {
                                tvDob.setBackgroundResource(R.drawable.input_signup_selected)
                                layout.visibility = View.VISIBLE
                            }
                        }
                        else -> {
                            layout.visibility = View.VISIBLE
                            tvDob.setBackgroundResource(R.drawable.input_signup_selected)
                        }

                    }
                }


            }

        }

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })

        viewModel.changeInfoDataState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { state ->

                state ?: return@Observer

                state.networkTrouble?.let { if (it) showInternetTrouble() }
                state.error?.messageResId?.let { showPopupMessage(message = getString(it)) }
                state.error?.errorMessage?.let { showPopupMessage(message = it) }
                state.errorText?.let { errorText -> showPopupMessage(message = errorText) }
                state.success?.let { memberInfo ->
                    tvPhone.text = UserConverter.formatPhone(memberInfo.telephoneNumber1!!)
                    dateData = memberInfo.birthday!!
                }
            })
//        viewModel.changeInfoDataState.observe(viewLifecycleOwner, androidx.lifecycle.Observer { user ->
//            user?.let {
//                tvPhone.text = UserConverter.formatPhone(user.telephoneNumber1!!)
//                dateData = user.birthday!!
//            }
//        })
        viewModel.getUser()

        return binding.root
    }


}