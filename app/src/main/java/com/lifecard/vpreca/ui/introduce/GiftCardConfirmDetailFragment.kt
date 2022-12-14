package com.lifecard.vpreca.ui.introduce


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentGiftCardConfirmDetailBinding
import com.lifecard.vpreca.utils.hideLoadingDialog
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showLoadingDialog
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
    ): View {
        _binding = FragmentGiftCardConfirmDetailBinding.inflate(inflater, container, false)
        binding.card = args.cardData
        binding.cardZone.card = args.cardData
        binding.cardZone.cardInclude.card = args.cardData
        val btnBack = binding.appbarGiftThird.btnBack
        val btnSubmit = binding.btnSubmitInput

        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

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

        viewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        }

        btnSubmit.setOnClickListener {
            viewModel.giftCardRelation(args.cardData!!)
        }
        return binding.root
    }


}