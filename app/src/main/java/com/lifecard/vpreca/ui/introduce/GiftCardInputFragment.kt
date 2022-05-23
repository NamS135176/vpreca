package com.lifecard.vpreca.ui.introduce

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentGiftCardConfirmBinding
import com.lifecard.vpreca.databinding.FragmentGiftCardInputBinding

class GiftCardInputFragment : Fragment() {

    companion object {
        fun newInstance() = GiftCardInputFragment()
    }

    private lateinit var viewModel: GiftCardInputViewModel
    private var _binding: FragmentGiftCardInputBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GiftCardInputViewModel::class.java)
        _binding = FragmentGiftCardInputBinding.inflate(inflater, container, false)
        val btnSubmit = binding.btnSubmitPolicy
        btnSubmit.setOnClickListener(View.OnClickListener {
            val giftCardConfirmData = GiftCardConfirmData("input")
            val action = GiftCardInputFragmentDirections.actionGiftcardinputToGiftcardconfirm(giftCardConfirmData)
            findNavController().navigate(action)
        })
        return binding.root
    }



}