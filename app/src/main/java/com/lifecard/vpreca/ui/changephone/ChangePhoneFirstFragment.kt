package com.lifecard.vpreca.ui.changephone

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.PhoneData
import com.lifecard.vpreca.databinding.FragmentChangeInfoDataBinding
import com.lifecard.vpreca.databinding.FragmentChangePhoneFirstBinding
import com.lifecard.vpreca.ui.changeinfo.ChangeInfoDataFragmentDirections
import com.lifecard.vpreca.utils.UserConverter
import com.lifecard.vpreca.utils.showAlertMessage
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
    ): View? {
        _binding = FragmentChangePhoneFirstBinding.inflate(inflater, container, false)
        val tvPhone = binding.tvConfirmID
        val btnBack = binding.appbarConfirmSignup.btnBack

        val btnOpenDialog = binding.btnSubmitPolicy
        val cal = Calendar.getInstance()
        var dateData = ""

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
                    val layout = view.findViewById<MaterialTextView>(R.id.tv_error)
                    println(date1)
                    println(dateData)
                    if (date1 == dateData) {
                        layout.visibility = View.INVISIBLE
                        findNavController().navigate(R.id.nav_change_phone_second)
                        dialog.dismiss()
                    }
                    else{
                        layout.visibility = View.VISIBLE
                    }
                })

            }
        )

        btnBack.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_home)
        })
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.nav_home)
                }
            })
        viewModel.user.observe(viewLifecycleOwner, androidx.lifecycle.Observer { user ->
            user?.let { tvPhone.text = UserConverter.formatPhone(user.telephoneNumber1!!)
            dateData = user.birthday!!
            }
        })
        viewModel.error.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { err -> err?.let { showAlertMessage(err) } })
        viewModel.getUser()

        return binding.root
    }


}