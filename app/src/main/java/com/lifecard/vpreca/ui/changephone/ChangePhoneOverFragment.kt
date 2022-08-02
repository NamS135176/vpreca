package com.lifecard.vpreca.ui.changephone

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePhoneCompleteBinding
import com.lifecard.vpreca.databinding.FragmentChangePhoneOverBinding

class ChangePhoneOverFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private var _binding: FragmentChangePhoneOverBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChangePhoneOverBinding.inflate(inflater, container, false)

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