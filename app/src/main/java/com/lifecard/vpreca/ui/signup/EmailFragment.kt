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
import com.lifecard.vpreca.databinding.FragmentConfirmPhoneBinding
import com.lifecard.vpreca.databinding.FragmentEmailBinding

class EmailFragment : Fragment() {

    private var _binding:FragmentEmailBinding? = null
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
        _binding = FragmentEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inputEmail = binding.forgotPassEmailInput
        val btnSubmitEmail = binding.btnSubmitPolicy
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                if(s.length > 0 && android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()){
                    btnSubmitEmail.isEnabled = true
                }
            }
        }
        inputEmail.addTextChangedListener(afterTextChangedListener)

        btnSubmitEmail.setOnClickListener(View.OnClickListener {

        })
    }

    companion object {
    }
}