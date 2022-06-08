package com.lifecard.vpreca.ui.introduce

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentGiftCardCompleteBinding
import com.lifecard.vpreca.databinding.FragmentGiftCardConfirmDetailBinding
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showPopupMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GiftCardConfirmDetailFragment  : Fragment() {

    companion object {
        fun newInstance() = GiftCardConfirmDetailFragment()
    }

    private val viewModel: GiftCardConfirmDetailViewModel by viewModels()
    private var _binding: FragmentGiftCardConfirmDetailBinding? = null
    private val binding get() = _binding!!
    private val args:GiftCardConfirmDetailFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel = ViewModelProvider(this).get(GiftCardConfirmDetailViewModel::class.java)
        _binding = FragmentGiftCardConfirmDetailBinding.inflate(inflater, container, false)
        binding.card = args.cardData
        binding.cardZone.card = args.cardData
        val btnBack = binding.appbarGiftThird.btnBack
        val btnSubmit = binding.btnSubmitInput
        val loading = binding.loading

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = GiftCardConfirmDetailFragmentDirections.actionGiftcardinputcardToGiftcardconfirm(args.cardData, GiftCardConfirmData("inputcard"))
                findNavController().navigate(action)
            }
        })

        btnBack.setOnClickListener(View.OnClickListener {
            val action = GiftCardConfirmDetailFragmentDirections.actionGiftcardinputcardToGiftcardconfirm(args.cardData, GiftCardConfirmData("inputcard"))
            findNavController().navigate(action)
        })

        viewModel.giftCardState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { changeInfoState ->
                changeInfoState ?: return@Observer
                changeInfoState.error?.messageResId?.let { showPopupMessage(message = getString(it)) }
                changeInfoState.error?.errorMessage?.let { showPopupMessage(message = it) }
                changeInfoState.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
                changeInfoState.success?.let {
                    findNavController().navigate(R.id.nav_gift_card_complete)
                }
            })

        viewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                true -> loading.visibility = View.VISIBLE
                else -> loading.visibility = View.GONE
            }
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            viewModel.giftCardRelation(args.cardData!!)
        })
        return binding.root
    }


}