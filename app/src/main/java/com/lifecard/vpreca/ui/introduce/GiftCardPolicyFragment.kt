package com.lifecard.vpreca.ui.introduce

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.base.NoToolbarFragment
import com.lifecard.vpreca.databinding.FragmentGiftCardPolicyBinding
import com.lifecard.vpreca.databinding.IntroduceFragmentFirstFragmentBinding
import com.lifecard.vpreca.utils.getNavigationResult
import com.lifecard.vpreca.utils.hideToolbar
import com.lifecard.vpreca.utils.showToast
import com.lifecard.vpreca.utils.showToolbar


/**
 * A simple [Fragment] subclass.
 * Use the [GiftCardPolicyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GiftCardPolicyFragment : NoToolbarFragment() {
    private var _binding: FragmentGiftCardPolicyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGiftCardPolicyBinding.inflate(inflater, container, false)
//        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                findNavController().navigate(R.id.nav_home)
//            }
//        })

        val btnBack = binding.appbarGift.btnBack
        val checkbox = binding.cbIntroduceFirst
        val btnSubmit = binding.btnSubmitIntroduceFirst

        checkbox.isChecked = false
        btnSubmit.isEnabled = false
        btnBack.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_home)
        })

        checkbox.setOnClickListener(View.OnClickListener {
            btnSubmit.isEnabled = checkbox.isChecked
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_gift_card_input_card)
        })
        return binding.root
    }
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        hideToolbar()
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        showToolbar()
//    }

}