package com.lifecard.vpreca.ui.card

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.databinding.CardDetailLayoutBinding
import com.lifecard.vpreca.ui.listvpreca.ListVprecaFragmentDirections
import showCustomToast

class CardDetailBottomSheetDialog(
    private val activity: Activity,
    private val creditCard: CreditCard
) : BottomSheetDialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var inflater = LayoutInflater.from(context)
        val bindingDialog =
            CardDetailLayoutBinding.inflate(inflater, null, false)
        setContentView(bindingDialog.root)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bindingDialog.card = creditCard
        val card = bindingDialog.cardZone
        val btnBack = bindingDialog.btnBack
        val btnCopy = bindingDialog.buttonCopy
        val btnUsage = bindingDialog.buttonUsage
        val btnLock = bindingDialog.buttonLock
        val btnRefresh = bindingDialog.buttonReload
        var currentLock = creditCard.vcnSecurityLockFlg

        btnRefresh.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(context).apply {
                setPositiveButton("はい") { _, _ ->
                    card.card = creditCard
                    bindingDialog.card = creditCard
                    Toast(context).showCustomToast(
                        "再発行しました",
                        activity = activity
                    )
                }
                setNegativeButton("いいえ", null)
                setMessage("カードを再発行しますよろしいですか？")
            }.create().show()
        })

        btnLock.setOnClickListener(View.OnClickListener {
            var newCard: CreditCard
            if (currentLock == "1") {
                currentLock = "0"
                newCard = creditCard.copy(
                    vcnSecurityLockFlg = "0",
                    cardImageFile = "",
                    cardUnusableDate = "",
                    thumbnailCardImageFile = ""
                )
                Toast(context).showCustomToast(
                    "ロックを解除しました",
                    activity
                )
            } else {
                currentLock = "1"
                newCard = creditCard.copy(
                    vcnSecurityLockFlg = "1",
                    cardImageFile = "",
                    cardUnusableDate = "",
                    thumbnailCardImageFile = ""
                )
                Toast(context).showCustomToast(
                    "ロックしました",
                    activity
                )
            }
            card.card = newCard
            bindingDialog.card = newCard

        })

        btnUsage.setOnClickListener(View.OnClickListener {
            dismiss()
            val action =
                ListVprecaFragmentDirections.actionToCardUsage(
                    creditCard
                )
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_main)
            navController.navigate(action)
        })

        btnCopy.setOnClickListener(View.OnClickListener {
            if (currentLock == "1") {
                MaterialAlertDialogBuilder(context).apply {
                    setMessage("ロックを解除してから\n" + "コピーしてください")
                    setNegativeButton("ok", null)
                }.create().show()
            } else {
                val clipboardManager =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText(
                    "text",
                    creditCard.precaNumber
                )
                clipboardManager.setPrimaryClip(clipData)
                Toast(context).showCustomToast(
                    "コピーしました！",
                    activity
                )
            }
        })

        btnBack.setOnClickListener(View.OnClickListener { dismiss() })
        card.card = creditCard
    }
}