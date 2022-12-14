package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentCompleteSignupBinding

class CompleteSignupFragment : Fragment() {
    private var _binding : FragmentCompleteSignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCompleteSignupBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.nav_login, inclusive = false)
            }
        })

        val btnComplete = binding.btnComplete
        btnComplete.setOnClickListener {
            findNavController().popBackStack(R.id.nav_login, inclusive = false)
        }
        return binding.root
    }
}