package com.lifecard.vpreca.ui.introduce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.databinding.FragmentGiftCardConfirmBinding
import com.lifecard.vpreca.utils.navigateToLogin
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GiftCardConfirmFragment : Fragment() {

    companion object {
        fun newInstance() = GiftCardConfirmFragment()
    }

    @Inject
    lateinit var userManager: UserManager

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

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (args.giftCardConfirmData?.preRoute == "inputcard") {
                    findNavController().navigate(R.id.nav_gift_card_input_card)
                } else {
                    findNavController().navigate(R.id.nav_gift_card_input)
                }
            }
        })

        val btnBack = binding.appbarGiftThird.btnBack
        val btnSubmit = binding.btnSubmitInput
        btnBack.setOnClickListener(View.OnClickListener {
            if (args.giftCardConfirmData?.preRoute == "inputcard") {
                findNavController().navigate(R.id.nav_gift_card_input_card)
            } else {
                findNavController().navigate(R.id.nav_gift_card_input)
            }
        })
        //check if user has logged in

        if (!userManager.isLoggedIn) {
            btnSubmit.text = getString(R.string.action_sign_in)
            btnSubmit.setOnClickListener(View.OnClickListener {
                navigateToLogin()
            })
        } else {
            btnSubmit.setOnClickListener(View.OnClickListener {
                //check if
                findNavController().navigate(R.id.nav_gift_card_complete)
            })
        }
        return binding.root
    }
}