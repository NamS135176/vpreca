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
import com.lifecard.vpreca.data.model.CardInfo
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.databinding.IntroduceFragmentThirdFragmentBinding

class IntroduceFragmentThird : Fragment() {

    companion object {
        fun newInstance() = IntroduceFragmentThird()
    }

    private lateinit var viewModel: IntroduceFragmentThirdViewModel
    private var _binding: IntroduceFragmentThirdFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: IntroduceFragmentThirdArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = IntroduceFragmentThirdFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[IntroduceFragmentThirdViewModel::class.java]
        binding.card = args.cardData
        binding.cardZone.cardInclude.card = args.cardData
        binding.cardZone.card = args.cardData
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_third_to_second)
            }
        })

        val btnBack = binding.appbarGiftThird.btnBack
        val btnSubmit = binding.btnSubmitInput
        val btnUsage = binding.btnMid

        btnUsage.setOnClickListener {
            val action = IntroduceFragmentThirdDirections.actionThirdToUsage(
                convertObject(args.cardData!!),
            )
            findNavController().navigate(action)
        }

        btnBack.setOnClickListener {
            findNavController().navigate(R.id.action_third_to_second)
        }
        btnSubmit.setOnClickListener {
            findNavController().navigate(R.id.action_third_to_login)
        }
        return binding.root
    }

    private fun convertObject(cardInfo: CardInfo): CreditCard {
        return CreditCard(
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
    }


}