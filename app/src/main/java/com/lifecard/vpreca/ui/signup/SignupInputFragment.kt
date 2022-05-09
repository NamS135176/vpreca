package com.lifecard.vpreca.ui.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.SignupInputFragmentBinding

class SignupInputFragment : Fragment() {

    companion object {
        fun newInstance() = SignupInputFragment()
    }

    private lateinit var viewModel: SignupInputViewModel
    private var _binding : SignupInputFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignupInputFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SignupInputViewModel::class.java)
        // TODO: Use the ViewModel
    }

}