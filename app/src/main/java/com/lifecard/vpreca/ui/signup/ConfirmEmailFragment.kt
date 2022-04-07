package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentConfirmEmailBinding
import com.lifecard.vpreca.databinding.FragmentConfirmPhoneBinding


class ConfirmEmailFragment : Fragment() {

    private var _binding:FragmentConfirmEmailBinding? = null
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
        _binding = FragmentConfirmEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
    }
}