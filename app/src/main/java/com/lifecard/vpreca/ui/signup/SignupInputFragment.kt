package com.lifecard.vpreca.ui.signup

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.LoginActivity
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.SignupInputFragmentBinding

class SignupInputFragment : Fragment() {

    companion object {
        fun newInstance() = SignupInputFragment()
    }

    private lateinit var viewModel: SignupInputViewModel
    private var _binding : SignupInputFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignupInputFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SignupInputViewModel::class.java)

        val spinnerGender = binding.spinnerGender
        val spinnerCity = binding.spinnerCity
        val spinnerSecret = binding.spinnerSecret
        val btnCancel = binding.appbarSignup.cancelBtn

        btnCancel.setOnClickListener(View.OnClickListener {
            val builder = MaterialAlertDialogBuilder(requireContext())

            // dialog title
//            builder.setTitle("Dialog Title")
            // drawable for dialog title
            // dialog message
            builder.setMessage("途中ですがキャンセルしてもよろしいですか？")
            // dialog background color

            builder.setPositiveButton("はい"){ dialog,which->
                // do something on positive button click
                val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
            }
            // icon for positive button


            builder.setNegativeButton("いいえ"){dialog,which->
                // do something when negative button clicked
            }

            builder.setNegativeButtonIcon(
                ContextCompat.getDrawable(requireContext(),R.drawable.ic_baseline_arrow_back_ios_24)
            )


            builder.setOnCancelListener {
                // do something on cancel listener
            }
            builder.setOnDismissListener {
                // do something on dismiss listener
            }

            // set dialog non cancelable
            builder.setCancelable(false)


            // finally, create the alert dialog and show it
            val dialog = builder.create()
            dialog.show()
        })

        val list = mutableListOf(
            "選択してください",
            "Male",
            "Female",
        )
        val adapter:ArrayAdapter<String> = object: ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            list
        ){
            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view:TextView = super.getDropDownView(
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

        spinnerGender.adapter = adapter
        spinnerCity.adapter = adapter
        spinnerSecret.adapter = adapter
        return binding.root
    }

}