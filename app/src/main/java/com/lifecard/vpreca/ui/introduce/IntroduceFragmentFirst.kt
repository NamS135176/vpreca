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
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.IntroduceFragmentFirstFragmentBinding

class IntroduceFragmentFirst : Fragment() {

    companion object {
        fun newInstance() = IntroduceFragmentFirst()
    }
    private var _binding: IntroduceFragmentFirstFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: IntroduceFragmentFirstViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IntroduceFragmentFirstFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(IntroduceFragmentFirstViewModel::class.java)
        // TODO: Use the ViewModel

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_first_login)
            }
        })

        val btnBack = binding.appbarGift.btnBack
        val checkbox = binding.cbIntroduceFirst
        val btnSubmit = binding.btnSubmitIntroduceFirst
//        checkbox.isChecked = false
        btnSubmit.isEnabled = false
        btnBack.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_first_login)
        })

//        checkbox.setOnClickListener(View.OnClickListener {
//            btnSubmit.isEnabled = checkbox.isChecked
//        })

        checkbox.setOnCheckedChangeListener { compoundButton, b ->
            run {
                btnSubmit.isEnabled = b
            }
        }

        btnSubmit.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_introduce_second)
        })
        return binding.root
    }


}