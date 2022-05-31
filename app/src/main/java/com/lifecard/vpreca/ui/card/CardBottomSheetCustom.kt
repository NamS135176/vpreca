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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.databinding.CardDetailLayoutBinding
import com.lifecard.vpreca.ui.listvpreca.ListVprecaFragmentDirections
import com.lifecard.vpreca.utils.copyCardLockInverse
import com.lifecard.vpreca.utils.isCardLock
import showCustomToast

class CardBottomSheetCustom(
    private val activity: Activity,
    private val creditCard: CreditCard
) : BottomSheetDialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var newCard = creditCard.copy()
        val inflater = LayoutInflater.from(context)
        val bindingDialog =
            CardDetailLayoutBinding.inflate(inflater, null, false)
        setContentView(bindingDialog.root)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bindingDialog.card = newCard
        val card = bindingDialog.cardZone
        val btnBack = bindingDialog.btnBack
        val btnCopy = bindingDialog.buttonCopy
        val btnUsage = bindingDialog.buttonUsage
        val btnLock = bindingDialog.buttonLock
        val btnRefresh = bindingDialog.buttonReload

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
//                setNegativeButton("いいえ", null)
                setMessage("カードを再発行しますよろしいですか？")
            }.create().show()
        })

        btnLock.setOnClickListener(View.OnClickListener {
            val new = newCard.copyCardLockInverse()
            val toastMessage = when(new.isCardLock()) {
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
            card.card = new
            bindingDialog.card = new

        })

        btnUsage.setOnClickListener(View.OnClickListener {
            dismiss()
            val action =
                ListVprecaFragmentDirections.actionToCardUsage(
                    newCard
                )
            val navController =
                Navigation.findNavController(activity, R.id.nav_host_fragment_content_main)
            navController.navigate(action)
        })

        btnCopy.setOnClickListener(View.OnClickListener {
            if (newCard.isCardLock()) {
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
        card.card = newCard
    }
}