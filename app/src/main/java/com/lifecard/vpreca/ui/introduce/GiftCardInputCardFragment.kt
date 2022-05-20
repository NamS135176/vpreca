package com.lifecard.vpreca.ui.introduce

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.data.model.SignupData
import com.lifecard.vpreca.databinding.FragmentGiftCardInputCardBinding
import com.lifecard.vpreca.databinding.IntroduceFragmentSecondFragmentBinding
import com.lifecard.vpreca.ui.signup.SignupInputFragmentDirections
import com.lifecard.vpreca.utils.hideToolbar
import com.lifecard.vpreca.utils.showToolbar

class GiftCardInputCardFragment : Fragment() {

    companion object {
        fun newInstance() = GiftCardInputCardFragment()
    }

    private lateinit var viewModel: GiftCardInputCardViewModel
    private var _binding: FragmentGiftCardInputCardBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GiftCardInputCardViewModel::class.java)
        _binding = FragmentGiftCardInputCardBinding.inflate(inflater, container, false)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_gift_card_policy)
            }
        })

        val btnSubmit = binding.btnSubmitInput
        val btnBack = binding.appbarGiftSecond.btnBack

        btnBack.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_gift_card_policy)
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            val giftCardConfirmData = GiftCardConfirmData("inputcard")
            val action = GiftCardInputCardFragmentDirections.actionGiftcardinputcardToGiftcardconfirm(giftCardConfirmData)
            findNavController().navigate(action)
//            findNavController().navigate(R.id.nav_gift_card_confirm)
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