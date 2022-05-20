package com.lifecard.vpreca.ui.introduce

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentGiftCardConfirmBinding
import com.lifecard.vpreca.databinding.FragmentGiftCardConfirmDetailBinding

class GiftCardConfirmFragment : Fragment() {

    companion object {
        fun newInstance() = GiftCardConfirmFragment()
    }

    private lateinit var viewModel: GiftCardConfirmViewModel
    private var _binding: FragmentGiftCardConfirmBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GiftCardConfirmViewModel::class.java)
        _binding = FragmentGiftCardConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }



}