package com.lifecard.vpreca.ui.changeinfo

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.BodDialogBinding
import com.lifecard.vpreca.databinding.FragmentChangeInfoDataBinding
import com.lifecard.vpreca.databinding.FragmentChangePassCompleteBinding
import com.lifecard.vpreca.utils.hideToolbar
import com.lifecard.vpreca.utils.showToolbar
import java.text.SimpleDateFormat
import java.util.*

class ChangeInfoDataFragment : Fragment() {

    companion object {
        fun newInstance() = ChangeInfoDataFragment()
    }

    private lateinit var viewModel: ChangeInfoDataViewModel
    private var _binding: FragmentChangeInfoDataBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ChangeInfoDataViewModel::class.java)

        _binding = FragmentChangeInfoDataBinding.inflate(inflater, container, false)
        val btnBack = binding.appbarConfirmSignup.btnBack
        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_home) })
        val btnOpenDialog = binding.btnSubmitPolicy
        var cal = Calendar.getInstance()
        btnOpenDialog.setOnClickListener(
            View.OnClickListener {
                val view = View.inflate(requireContext(),R.layout.bod_dialog, null)
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
                fun doChangeText(text:String){
                    btnSubmit.isEnabled = text != "年/月/日"
                }
                tvDob.doAfterTextChanged { text -> doChangeText(text = text.toString())}

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
                    ).show()
                }

                btnCancel.setOnClickListener(View.OnClickListener { dialog.dismiss()
                })

                btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_change_info_input)
                dialog.dismiss()})

            }
        )
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hideToolbar()
    }

    override fun onDetach() {
        super.onDetach()
        showToolbar()
    }

}