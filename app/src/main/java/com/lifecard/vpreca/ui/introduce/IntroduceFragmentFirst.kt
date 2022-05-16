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
import com.lifecard.vpreca.MainActivity
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
//                val intent = Intent(requireContext(), LoginActivity::class.java).apply {
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                }
//                startActivity(intent)
                findNavController().popBackStack()
            }
        })

        val imgIntroduceBack = binding.appbarGift.imgBackIntroduce
        val tvIntroduceBack = binding.appbarGift.tvBackIntroduce
        val checkbox = binding.cbIntroduceFirst
        val btnSubmit = binding.btnSubmitIntroduceFirst
        checkbox.isChecked = false
        btnSubmit.isEnabled = false
        imgIntroduceBack.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        })

        tvIntroduceBack.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        })

        checkbox.setOnClickListener(View.OnClickListener {
            btnSubmit.isEnabled = checkbox.isChecked
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_introduce_second)
        })
        return binding.root
    }


}