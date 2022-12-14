package com.lifecard.vpreca.ui.smsverify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentCodeExpiredBinding

class CodeExpiredFragment : Fragment() {
    private var _binding: FragmentCodeExpiredBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCodeExpiredBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {  findNavController().popBackStack(R.id.nav_login, inclusive = false) }
        })
        // Inflate the layout for this fragment
        val btnComplete = binding.btnCodeExpired
        btnComplete.setOnClickListener {
            findNavController().popBackStack(R.id.nav_login, inclusive = false)
        }
        return binding.root
    }


}