package com.lifecard.vpreca.ui.testcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lifecard.vpreca.databinding.FragmentTestCardBinding


class TestCardFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private var _binding: FragmentTestCardBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTestCardBinding.inflate(inflater, container, false)
//        val tv = binding.cardUsername
//        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(tv, 6, 16, 2,
//            TypedValue.COMPLEX_UNIT_DIP);
        return binding.root
    }

}