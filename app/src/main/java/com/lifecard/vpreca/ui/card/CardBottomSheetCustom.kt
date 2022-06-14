package com.lifecard.vpreca.ui.card

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.api.ApiService
import com.lifecard.vpreca.data.model.CardInfo
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.CardDetailLayoutBinding
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.ErrorMessageException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.listvpreca.CardInfoResult
import com.lifecard.vpreca.ui.listvpreca.ListVprecaFragmentDirections
import com.lifecard.vpreca.ui.listvpreca.ListVprecaViewModel
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import showCustomToast
import kotlin.coroutines.CoroutineContext

class CardBottomSheetCustom(
    private val activity: Activity,
    private val creditCard: CardInfo,
    private val creditCardRepository: CreditCardRepository
//    private val card: CreditCard
) : BottomSheetDialog(activity), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var newCard = creditCard.copy()
        val inflater = LayoutInflater.from(context)

        val bindingDialog =
            CardDetailLayoutBinding.inflate(inflater, null, false)
        val loading = bindingDialog.loading
        when (creditCard.designId) {
            "001" -> bindingDialog.cardZone.cardInfo.cardInfo.setBackgroundResource(R.drawable.first)
            "002" -> bindingDialog.cardZone.cardInfo.cardInfo.setBackgroundResource(R.drawable.second)
            "003" -> bindingDialog.cardZone.cardInfo.cardInfo.setBackgroundResource(R.drawable.third)
            else -> bindingDialog.cardZone.cardInfo.cardInfo.setBackgroundResource(R.drawable.first)
//            "005" -> bindingDialog.cardZone.cardInfo.setBackgroundResource(R.drawable.bg_fifth)
//            "006" -> bindingDialog.cardZone.cardInfo.setBackgroundResource(R.drawable.bg_six)
//            "007" -> bindingDialog.cardZone.cardInfo.setBackgroundResource(R.drawable.bg_seven)
        }
        setContentView(bindingDialog.root)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bindingDialog.card = convertObject(newCard)

        val card = bindingDialog.cardZone
        val btnBack = bindingDialog.btnBack
        val btnCopy = bindingDialog.buttonCopy
        val btnUsage = bindingDialog.buttonUsage
        val btnLock = bindingDialog.buttonLock
        val btnRefresh = bindingDialog.buttonReload

        btnRefresh.setOnClickListener(View.OnClickListener {
            if(!newCard.isCardInfoLock()){
                MaterialAlertDialogBuilder(context).apply {
                    setPositiveButton("はい") { _, _ ->
                        launch {
                            loading.visibility = View.VISIBLE
                            val res = creditCardRepository.republishCard(convertObject(creditCard))
                            if(res is Result.Success){
                                card.card = res.data
                                bindingDialog.card = res.data
                                card.cardInfo.card = res.data
                                newCard = creditCard.copy()
                                Toast(context).showCustomToast(
                                    "再発行しました",
                                    activity = activity
                                )
                            }
                            else if (res is Result.Error) {
                                when (res.exception) {
                                    is NoConnectivityException -> (activity as AppCompatActivity).showInternetTrouble()
                                    is ApiException -> (activity as AppCompatActivity).showPopupMessage("", res.exception.message)
                                    else ->
                                        (activity as AppCompatActivity).showPopupMessage("", activity.getString( R.string.get_list_card_failure))

                                }
                            }
                            loading.visibility = View.GONE
                        }
                    }
                setNegativeButton("いいえ", null)
                    setMessage("カードを再発行しますよろしいですか？")
                }.create().show()
            }
        })

        btnLock.setOnClickListener(View.OnClickListener {
         launch {
             loading.visibility = View.VISIBLE
             val new = newCard.copyCardInfoLockInverse()
             val res = creditCardRepository.updateCard(convertObject(new))
             if (res is Result.Success) {

                 val toastMessage = when (new.isCardInfoLock()) {
                     true -> "ロックしました"
                     else -> "ロックを解除しました"
                 }
                 Toast(context).showCustomToast(
                     message = toastMessage,
                     activity
                 )
//            val new = newCard.copyCardLockInverse()
//            println(new.isCardLock())
                 newCard = new.copy()
                 card.card = convertObject(new)
                 card.cardInfo.card = convertObject(new)
                 bindingDialog.card = convertObject(new)
             } else if (res is Result.Error) {
                 when (res.exception) {
                     is NoConnectivityException -> (activity as AppCompatActivity).showInternetTrouble()
                     is ApiException -> (activity as AppCompatActivity).showPopupMessage("", res.exception.message)
                     else ->
                         (activity as AppCompatActivity).showPopupMessage("", activity.getString( R.string.get_list_card_failure))

                 }
             }
             loading.visibility = View.GONE
         }

        })

        btnUsage.setOnClickListener(View.OnClickListener {
            dismiss()
            val data = GiftCardConfirmData("logged")
            val action =
                ListVprecaFragmentDirections.actionToCardUsage(
                    convertObject(newCard),
                    data
                )
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_main)
            navController.navigate(action)
        })

        btnCopy.setOnClickListener(View.OnClickListener {
            if (newCard.isCardInfoLock()) {
                MaterialAlertDialogBuilder(context).apply {
                    setMessage("ロックを解除してから\n" + "コピーしてください")
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
                    "コピーしました！",
                    activity
                )
            }
        })

        btnBack.setOnClickListener(View.OnClickListener { dismiss() })
        card.card = convertObject(newCard)
        card.cardInfo.card = convertObject(newCard)
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