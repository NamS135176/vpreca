package com.lifecard.vpreca.ui.introduce

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = IntroduceFragmentThirdFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(IntroduceFragmentThirdViewModel::class.java)
        // TODO: Use the ViewModel

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        val btnBack = binding.appbarGiftThird.btnBack

        btnBack.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_introduce_second)
        })

        return binding.root
    }



}