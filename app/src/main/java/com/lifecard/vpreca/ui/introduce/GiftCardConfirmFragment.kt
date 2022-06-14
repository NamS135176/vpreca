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
import com.lifecard.vpreca.data.model.CardInfo
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.GiftCardConfirmData
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
        val btnUsage = binding.btnMid

        btnUsage.setOnClickListener(View.OnClickListener {
//            println("sjkdfhksjdhfksdf")
            val data = GiftCardConfirmData("logged")
            val action = GiftCardConfirmFragmentDirections.actionThirdToUsage(convertObject(args.cardData!!), data)
            findNavController().navigate(action)
        })

        binding.card = args.cardData
        binding.cardZone.cardInclude.card = args.cardData
        binding.cardZone.card  = args.cardData
        btnBack.setOnClickListener(View.OnClickListener {
            if (args.giftCardConfirmData?.preRoute == "inputcard") {
                findNavController().navigate(R.id.nav_gift_card_input_card)
            } else {
                findNavController().navigate(R.id.nav_gift_card_input)
            }
        })
        //check if user has logged in

//        if (!userManager.isLoggedIn) {
//            btnSubmit.text = getString(R.string.action_sign_in)
//            btnSubmit.setOnClickListener(View.OnClickListener {
//                navigateToLogin()
//            })
//        } else {
//            btnSubmit.setOnClickListener(View.OnClickListener {
//                //check if
//                findNavController().navigate(R.id.nav_gift_card_complete)
//            })
//        }

        btnSubmit.setOnClickListener(View.OnClickListener {
            val action = GiftCardConfirmFragmentDirections.actionToConfirmDetail(args.cardData)
            findNavController().navigate(action)
        })
        return binding.root
    }

    fun convertObject(cardInfo: CardInfo): CreditCard {
        val obj = CreditCard(
            cardInfo.activateStatus,
            cardInfo.activateDate,
            cardInfo.autoChargeAmount,
            cardInfo.autoChargeErrFlg,
            cardInfo.autoChargeFlg,
            cardInfo.autoChargeThereshold,
            cardInfo.cardAccessDate,
            cardInfo.cardAccessFlg,
            cardInfo.cardNickname,
            cardInfo.cardImageFile,
            cardInfo.cardPublishDate,
            cardInfo.cardPublishFlg,
            cardInfo.cardRegistDate,
            cardInfo.cardSchemeId,
            cardInfo.cardStatus,
            cardInfo.cardUnusableDate,
            cardInfo.cardUseStopFlg,
            cardInfo.chargeBalance,
            cardInfo.chargeLimit,
            cardInfo.confirmationNumber,
            cardInfo.cooperatorNumber,
            cardInfo.cooperatorNumberId,
            cardInfo.creditBrandId,
            cardInfo.designId,
            cardInfo.designName,
            cardInfo.initialPurchaseAmount,
            cardInfo.lastUseDate,
            cardInfo.memberRepublishCnt,
            cardInfo.memberRepublishDate,
            cardInfo.onlinePinRegFlg,
            cardInfo.precaExpirationDate,
            cardInfo.precaNumber,
            cardInfo.publishAddFree,
            cardInfo.publishAmount,
            cardInfo.publishFee,
            cardInfo.publishType,
            cardInfo.rechargedActFlg,
            cardInfo.thumbnailCardImageFile,
            cardInfo.useLimit,
            cardInfo.vcn,
            cardInfo.vcnCardId,
            cardInfo.vcnName,
            cardInfo.vcnExpirationDate,
            cardInfo.vcnSecurityLockFlg
        )
        return obj
    }
}