package com.lifecard.vpreca.ui.card

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.model.CardInfo
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.getBackgroundCard
import com.lifecard.vpreca.databinding.CardDetailLayoutBinding
import com.lifecard.vpreca.eventbus.ReloadCard
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.card_usage.CardUsageFragmentArgs
import com.lifecard.vpreca.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import kotlin.coroutines.CoroutineContext

class CardBottomSheetCustom(
    private val activity: FragmentActivity,
    private val creditCard: CardInfo,
    private val creditCardRepository: CreditCardRepository
) : BottomSheetDialog(activity, R.style.AppBottomSheetDialogTheme), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var newCard = creditCard.copy()
        println(newCard)
        val inflater = LayoutInflater.from(context)

        val bindingDialog =
            CardDetailLayoutBinding.inflate(inflater, null, false)
        bindingDialog.cardZone.cardInfo.cardInfo.setBackgroundResource(newCard.getBackgroundCard())
        setContentView(bindingDialog.root)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bindingDialog.card = convertObject(newCard)


        val card = bindingDialog.cardZone
        val btnBack = bindingDialog.btnBack
        val btnCopy = bindingDialog.buttonCopy
        val btnUsage = bindingDialog.buttonUsage
        val btnLock = bindingDialog.buttonLock
        val btnRefresh = bindingDialog.buttonReload

        btnRefresh.setOnClickListener {
            if (!newCard.isCardInfoLock() && newCard.isInfoAvailable()) {
                MaterialAlertDialogBuilder(context).apply {
                    setPositiveButton("??????") { _, _ ->
                        launch {
                            (activity as AppCompatActivity).showLoadingDialog()
                            val res = creditCardRepository.republishCard(convertObject(newCard))
                            if (res is Result.Success) {
                                println(res)
                                card.card = res.data
                                bindingDialog.card = res.data
                                card.cardInfo.card = res.data

                                Toast(context).showCustomToast(
                                    "?????????????????????",
                                    activity = activity
                                )
                                EventBus.getDefault().post(ReloadCard())
                            } else if (res is Result.Error) {
                                when (res.exception) {
                                    is NoConnectivityException -> (activity).showInternetTrouble()
                                    is ApiException -> (activity).showPopupMessage(
                                        "",
                                        res.exception.message
                                    )
                                    else ->
                                        (activity).showPopupMessage(
                                            "",
                                            activity.getString(R.string.get_list_card_failure)
                                        )

                                }
                            }
                            (activity).hideLoadingDialog()
                        }
                    }
                    setNegativeButton("?????????", null)
                    setMessage("??????????????????????????????????????????????????????")
                }.create().show()
            }
        }

        btnLock.setOnClickListener {
            launch {
                (activity as AppCompatActivity).showLoadingDialog()
                val new = newCard.copyCardInfoLockInverse()
                println(new)
                val res = creditCardRepository.updateCard(convertObject(new))
                if (res is Result.Success) {
                    println(res)
                    EventBus.getDefault().post(ReloadCard())
                    val toastMessage = when (new.isCardInfoLock()) {
                        true -> "?????????????????????"
                        else -> "??????????????????????????????"
                    }
                    Toast(context).showCustomToast(
                        message = toastMessage,
                        activity
                    )
                    newCard = new.copy()
                    card.card = convertObject(new)
                    card.cardInfo.card = convertObject(new)
                    bindingDialog.card = convertObject(new)
                } else if (res is Result.Error) {
                    when (res.exception) {
                        is NoConnectivityException -> (activity).showInternetTrouble()
                        is ApiException -> (activity).showPopupMessage(
                            "",
                            res.exception.message
                        )
                        else ->
                            (activity).showPopupMessage(
                                "",
                                activity.getString(R.string.get_list_card_failure)
                            )

                    }
                }
                (activity).hideLoadingDialog()
            }

        }

        btnUsage.setOnClickListener {
            dismiss()
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_main)
            navController.navigate(
                R.id.nav_card_usage,
                CardUsageFragmentArgs(card = convertObject(newCard)).toBundle()
            )

        }

        btnCopy.setOnClickListener {
            if (newCard.isCardInfoLock()) {
                MaterialAlertDialogBuilder(context).apply {
                    setMessage("??????????????????????????????\n" + "???????????????????????????")
                    setNegativeButton("ok", null)
                }.create().show()
            } else {
                val clipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText(
                    "text",
                    newCard.precaNumber
                )
                clipboardManager.setPrimaryClip(clipData)
                Toast(context).showCustomToast(
                    "????????????????????????",
                    activity
                )
            }
        }

        btnBack.setOnClickListener { dismiss() }
        card.card = convertObject(newCard)
        card.cardInfo.card = convertObject(newCard)
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