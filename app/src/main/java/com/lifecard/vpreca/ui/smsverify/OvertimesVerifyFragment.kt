package com.lifecard.vpreca.ui.smsverify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentOvertimesVerifyBinding
import com.lifecard.vpreca.databinding.FragmentSmsVerifyBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OvertimesVerifyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OvertimesVerifyFragment : Fragment() {
    private var _binding: FragmentOvertimesVerifyBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentOvertimesVerifyBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() { findNavController().navigate(R.id.nav_login) }
        })
        val btnComplete = binding.btnOvertimes
        btnComplete.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_login)
        })
        return binding.root
    }

}