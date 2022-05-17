package com.lifecard.vpreca.ui.changepass

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePassBinding
import com.lifecard.vpreca.databinding.FragmentChangePassCompleteBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePassCompleteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePassCompleteFragment : Fragment() {
    private var _binding: FragmentChangePassCompleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentChangePassCompleteBinding.inflate(inflater, container, false)
        val btnSubmit = binding.btnCompleteForgot
        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_home) })
        return binding.root
    }

}