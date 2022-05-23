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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangeInfoCompleteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangeInfoCompleteFragment : Fragment() {

    private var _binding: FragmentChangeInfoCompleteBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeInfoCompleteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_home)
            }
        })

        val btnConfirm = binding.btnCompleteForgot

        btnConfirm.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_home) })

        return binding.root
    }

}