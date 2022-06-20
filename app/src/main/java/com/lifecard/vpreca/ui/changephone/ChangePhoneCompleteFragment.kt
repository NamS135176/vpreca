package com.lifecard.vpreca.ui.changephone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePhoneCompleteBinding


class ChangePhoneCompleteFragment : Fragment() {

    private var _binding: FragmentChangePhoneCompleteBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePhoneCompleteBinding.inflate(inflater, container, false)
        val btnComplete = binding.btnSubmitPolicy
        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.nav_home, inclusive = false)
            }
        })
        btnComplete.setOnClickListener {
            findNavController().popBackStack(
                R.id.nav_home,
                inclusive = false
            )
        }
        return binding.root
    }

}