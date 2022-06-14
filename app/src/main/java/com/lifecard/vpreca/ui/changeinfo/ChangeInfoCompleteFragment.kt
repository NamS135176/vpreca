package com.lifecard.vpreca.ui.changeinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangeInfoCompleteBinding
import com.lifecard.vpreca.databinding.FragmentChangeInfoConfirmDataBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ChangeInfoCompleteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangeInfoCompleteFragment : Fragment() {

    private var _binding: FragmentChangeInfoCompleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeInfoCompleteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_to_home)
                }
            })

        val btnConfirm = binding.btnCompleteForgot

        btnConfirm.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.action_to_home) })

        return binding.root
    }

}