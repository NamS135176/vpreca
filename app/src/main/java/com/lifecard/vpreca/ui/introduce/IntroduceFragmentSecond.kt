package com.lifecard.vpreca.ui.introduce

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.LoginActivity
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.IntroduceFragmentFirstFragmentBinding
import com.lifecard.vpreca.databinding.IntroduceFragmentSecondFragmentBinding

class IntroduceFragmentSecond : Fragment() {

    companion object {
        fun newInstance() = IntroduceFragmentSecond()
    }
    private var _binding: IntroduceFragmentSecondFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: IntroduceFragmentSecondViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = IntroduceFragmentSecondFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(IntroduceFragmentSecondViewModel::class.java)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
               findNavController().popBackStack()
            }
        })

        val btnSubmit = binding.btnSubmitInput
        val imgIntroduceBack = binding.appbarGiftSecond.imgBackIntroduce
        val tvIntroduceBack = binding.appbarGiftSecond.tvBackIntroduce

        imgIntroduceBack.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_introduce_first)
        })

        tvIntroduceBack.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_introduce_first)
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_introduce_third)
        })
        // TODO: Use the ViewModel
        return binding.root
    }


}