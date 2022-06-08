package com.lifecard.vpreca.ui.introduce

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.IntroduceFragmentSecondFragmentBinding
import com.lifecard.vpreca.databinding.IntroduceFragmentThirdFragmentBinding

class IntroduceFragmentThird : Fragment() {

    companion object {
        fun newInstance() = IntroduceFragmentThird()
    }

    private lateinit var viewModel: IntroduceFragmentThirdViewModel
    private var _binding: IntroduceFragmentThirdFragmentBinding? = null
    private val binding get() = _binding!!
    private val args:IntroduceFragmentThirdArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = IntroduceFragmentThirdFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(IntroduceFragmentThirdViewModel::class.java)
        // TODO: Use the ViewModel
        binding.card = args.cardData
        binding.cardZone.card = args.cardData
        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_introduce_second)
            }
        })

        val btnBack = binding.appbarGiftThird.btnBack
        val btnSubmit = binding.btnSubmitInput
        btnBack.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_introduce_second)
        })
        btnSubmit.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_login)
        })
        return binding.root
    }



}