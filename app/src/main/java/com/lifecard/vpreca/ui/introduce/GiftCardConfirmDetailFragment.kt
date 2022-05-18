package com.lifecard.vpreca.ui.introduce

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentGiftCardCompleteBinding
import com.lifecard.vpreca.databinding.FragmentGiftCardConfirmDetailBinding

class GiftCardConfirmDetailFragment : Fragment() {

    companion object {
        fun newInstance() = GiftCardConfirmDetailFragment()
    }

    private lateinit var viewModel: GiftCardConfirmDetailViewModel
    private var _binding: FragmentGiftCardConfirmDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GiftCardConfirmDetailViewModel::class.java)
        _binding = FragmentGiftCardConfirmDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


}