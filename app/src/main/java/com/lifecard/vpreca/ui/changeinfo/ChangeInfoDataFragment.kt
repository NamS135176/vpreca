package com.lifecard.vpreca.ui.changeinfo

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.ChangeInfoMemberData
import com.lifecard.vpreca.data.model.PhoneData
import com.lifecard.vpreca.data.model.User
import com.lifecard.vpreca.databinding.FragmentChangeInfoDataBinding
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class ChangeInfoDataFragment : Fragment() {

    companion object {
        fun newInstance() = ChangeInfoDataFragment()
    }

    private val viewModel: ChangeInfoDataViewModel by viewModels()
    private var _binding: FragmentChangeInfoDataBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeInfoDataBinding.inflate(inflater, container, false)
        val btnBack = binding.appbarConfirmSignup.btnBack
        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_home) })
//        requireActivity().onBackPressedDispatcher.addCallback(
//            viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    findNavController().navigate(R.id.nav_home)
//                }
//            })
        val btnOpenDialog = binding.btnSubmitPolicy
        val cal = Calendar.getInstance()

        var dateData = ""
        var phone = ""
        val memberNumber = ""
        btnOpenDialog.setOnClickListener(
            View.OnClickListener {
                val view = View.inflate(requireContext(), R.layout.bod_dialog, null)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(view)
                val dialog = builder.create()
                dialog.show()
//                val window = dialog.window
//                window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//                window?.setGravity(Gravity.CENTER)

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
                    tvDob!!.text = sdf.format(cal.getTime())
                }

                val dateSetListener =
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
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
                        setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.button_ok),
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
                    }
                        .show()
                }

                btnCancel.setOnClickListener(View.OnClickListener {
                    dialog.dismiss()
                })

                btnSubmit.setOnClickListener(View.OnClickListener {
                    val myFormat = "yyyyMMdd" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    val date1 = sdf.format(cal.getTime())
//                    val l = LocalDate.parse(dateData)
//                    val date2 = sdf.format(l)
                    println(date1)
                    println(dateData)
                    val layout = view.findViewById<MaterialTextView>(R.id.tv_error)

                    if (date1 == dateData) {
                        layout.visibility = View.INVISIBLE
                        val action = ChangeInfoDataFragmentDirections.actionChangeInfoDataToInput(
                            ChangeInfoMemberData(
                                memberNumber,
                                binding.user?.loginId!!,
                                binding.user?.memberRoman!!,
                                binding.user?.memberKana!!,
                                binding.user?.memberName!!,
                                binding.user?.addressCity!!,
                                binding.user?.mailAddress1!!,
                                binding.user?.mailAddress2!!,
                                binding.user?.secretQuestion!!,
                                binding.user?.secretQuestionAnswer!!,
                                binding.user?.mail1AdMailRecieveFlg!!,
                                binding.user?.mail2AdMailRecieveFlg!!,
                                binding.user?.mail1RecievFlg!!,
                                binding.user?.mail2RecievFlg!!,
                                phone,
                            )
                        )
                        dialog.dismiss()
                        findNavController().navigate(action)
                    } else {
                        layout.visibility = View.VISIBLE
                    }

//                    findNavController().navigate(R.id.nav_change_info_input)
//                    dialog.dismiss()
                })

            }
        )

        viewModel.changeInfoDataState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { state ->

                state ?: return@Observer

                state.networkTrouble?.let { if (it) showInternetTrouble() }
                state.error?.messageResId?.let { showPopupMessage(message = getString(it)) }
                state.error?.errorMessage?.let { showPopupMessage(message = it) }
                state.errorText?.let { errorText -> showPopupMessage(message = errorText) }
                state.success?.let { memberInfo ->
                    binding.user = memberInfo
                    dateData = memberInfo.birthday!!
                    phone = memberInfo.telephoneNumber1!!
                    btnOpenDialog.isEnabled = true
                }
            })

        viewModel.getUser()

        return binding.root
    }

}