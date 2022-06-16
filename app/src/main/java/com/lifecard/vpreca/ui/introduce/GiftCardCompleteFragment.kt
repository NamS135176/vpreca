package com.lifecard.vpreca.ui.introduce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentGiftCardCompleteBinding

class GiftCardCompleteFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentGiftCardCompleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGiftCardCompleteBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.nav_home, inclusive = false)
            }
        })

        val btnComplete = binding.btnSubmitPolicy
        btnComplete.setOnClickListener {
            findNavController().popBackStack(R.id.nav_home, inclusive = false)
        }
        return binding.root
    }


}