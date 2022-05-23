package com.lifecard.vpreca.ui.introduce

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentGiftCardConfirmBinding
import com.lifecard.vpreca.databinding.FragmentGiftCardConfirmDetailBinding
import com.lifecard.vpreca.ui.signup.ConfirmSignupDataFragmentArgs
import com.lifecard.vpreca.utils.hideToolbar
import com.lifecard.vpreca.utils.showToolbar

class GiftCardConfirmFragment : Fragment() {

    companion object {
        fun newInstance() = GiftCardConfirmFragment()
    }
    private val args: GiftCardConfirmFragmentArgs by navArgs()
    private lateinit var viewModel: GiftCardConfirmViewModel
    private var _binding: FragmentGiftCardConfirmBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GiftCardConfirmViewModel::class.java)
        _binding = FragmentGiftCardConfirmBinding.inflate(inflater, container, false)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
              if(args?.giftCardConfirmData?.preRoute == "inputcard"){
                  findNavController().navigate(R.id.nav_gift_card_input_card)
              }
                else{
                  findNavController().navigate(R.id.nav_gift_card_input)
              }
            }
        })

        val btnBack = binding.appbarGiftThird.btnBack
        val btnSubmit = binding.btnSubmitInput
        btnBack.setOnClickListener(View.OnClickListener {
            if(args?.giftCardConfirmData?.preRoute == "inputcard"){
                findNavController().navigate(R.id.nav_gift_card_input_card)
            }
            else{
                findNavController().navigate(R.id.nav_gift_card_input)
            }
        })
        btnSubmit.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_gift_card_complete)
        })
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hideToolbar()
    }

    override fun onDetach() {
        super.onDetach()
        showToolbar()
    }


}