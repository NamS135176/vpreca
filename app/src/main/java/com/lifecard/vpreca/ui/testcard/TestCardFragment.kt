package com.lifecard.vpreca.ui.testcard

import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentTestCardBinding
import com.lifecard.vpreca.databinding.TermOfUseFragmentBinding


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