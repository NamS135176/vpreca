package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R


class ConfirmPhoneFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inputPhoneConfirm = view.findViewById<EditText>(R.id.edt_phone_confirm)
        val btnSubmitPhoneConfirm = view.findViewById<Button>(R.id.btn_submit_phone_confirm)
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                if(s?.length > 0){
                    btnSubmitPhoneConfirm.isEnabled = true
                }
            }
        }
        inputPhoneConfirm.addTextChangedListener(afterTextChangedListener)

        btnSubmitPhoneConfirm.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_email)
//            val emailFragment = EmailFragment()
//            val manager = parentFragmentManager
//            val transaction = manager.beginTransaction()
//            transaction.replace(R.id.fmSignup, emailFragment)
//            transaction.addToBackStack(null)
//            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_from_left)
//            transaction.commit()
        })
    }

}