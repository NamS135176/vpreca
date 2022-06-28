package com.lifecard.vpreca.ui.changephone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePhoneExpireBinding
import com.lifecard.vpreca.databinding.FragmentChangePhoneOverBinding


class ChangePhoneExpireFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private var _binding: FragmentChangePhoneExpireBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePhoneExpireBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() { findNavController().popBackStack(R.id.nav_home, inclusive = false)  }
        })
        val btnComplete = binding.btnOvertimes
        btnComplete.setOnClickListener {
            findNavController().popBackStack(R.id.nav_home, inclusive = false)
        }
        return binding.root
    }
}