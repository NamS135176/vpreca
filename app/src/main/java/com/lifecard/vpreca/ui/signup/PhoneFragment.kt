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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentPhoneBinding
import com.lifecard.vpreca.databinding.FragmentPolicyBinding


class PhoneFragment : Fragment() {

    private val phoneViewModel by lazy { ViewModelProvider(this).get(PhoneViewModel::class.java) }
    private var _binding:FragmentPhoneBinding? = null
    private val binding get() = _binding!!

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

//        return inflater.inflate(R.layout.fragment_phone, container, false)
        _binding = FragmentPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inputPhone = binding.edtPhone
        val btnSubmitPhone = binding.btnSubmitPhone
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
//                if(phoneViewModel.isPhoneValid(s.toString())){
//                    btnSubmitPhone.isEnabled = true
//                }
                phoneViewModel.checkPhone(s.toString())
                btnSubmitPhone.isEnabled = phoneViewModel.enableNext
            }
        }
        inputPhone.addTextChangedListener(afterTextChangedListener)

        btnSubmitPhone.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_confirm_phone)
        })
    }

}