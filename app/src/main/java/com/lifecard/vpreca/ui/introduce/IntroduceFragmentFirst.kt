package com.lifecard.vpreca.ui.introduce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.IntroduceFragmentFirstFragmentBinding

class IntroduceFragmentFirst : Fragment() {

    companion object {
        fun newInstance() = IntroduceFragmentFirst()
    }
    private var _binding: IntroduceFragmentFirstFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IntroduceFragmentFirstFragmentBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.nav_login, inclusive = false)
            }
        })

        val btnBack = binding.appbarGift.btnBack
        val checkbox = binding.cbIntroduceFirst
        val btnSubmit = binding.btnSubmitIntroduceFirst
        btnSubmit.isEnabled = false
        btnBack.setOnClickListener {
            findNavController().popBackStack(R.id.nav_login, inclusive = false)
        }

        checkbox.setOnCheckedChangeListener { _, b ->
            run {
                btnSubmit.isEnabled = b
            }
        }

        btnSubmit.setOnClickListener {
            findNavController().navigate(R.id.nav_introduce_second)
        }
        return binding.root
    }


}