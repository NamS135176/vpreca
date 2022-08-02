package com.lifecard.vpreca.ui.card

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.NumberPicker
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.DateBottomLayoutBinding
import com.lifecard.vpreca.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import kotlin.coroutines.CoroutineContext


class CardBottomSheetDate(
    private val activity: FragmentActivity
) : BottomSheetDialog(activity, R.style.AppBottomSheetDialogTheme), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = LayoutInflater.from(context)

        val bindingDialog =
            DateBottomLayoutBinding.inflate(inflater, null, false)
        setContentView(bindingDialog.root)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        bindingDialog.numberYear.minValue = 1970
        bindingDialog.numberYear.maxValue = 2022
        bindingDialog.numberYear.setFormatter(NumberPicker.Formatter { value -> value.toString() + "年" })

        try {
            val method: Method = bindingDialog.numberYear.javaClass
                .getDeclaredMethod("changeValueByOne", Boolean::class.javaPrimitiveType)
            method.setAccessible(true)
            method.invoke(bindingDialog.numberYear, true)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        bindingDialog.numberYear.wrapSelectorWheel = true
        bindingDialog.numberYear.value = 2012

        bindingDialog.numberMonth.minValue = 1
        bindingDialog.numberMonth.maxValue = 12
        bindingDialog.numberMonth.setFormatter(NumberPicker.Formatter { value -> value.toString() + "月" })

        try {
            val method: Method = bindingDialog.numberMonth.javaClass
                .getDeclaredMethod("changeValueByOne", Boolean::class.javaPrimitiveType)
            method.setAccessible(true)
            method.invoke(bindingDialog.numberMonth, true)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        bindingDialog.numberMonth.wrapSelectorWheel = true
        bindingDialog.numberMonth.value = 7

        bindingDialog.numberDay.minValue = 1
        bindingDialog.numberDay.maxValue = 31
        bindingDialog.numberDay.setFormatter(NumberPicker.Formatter { value -> value.toString() + "日" })

        try {
            val method: Method = bindingDialog.numberDay.javaClass
                .getDeclaredMethod("changeValueByOne", Boolean::class.javaPrimitiveType)
            method.setAccessible(true)
            method.invoke(bindingDialog.numberDay, true)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        bindingDialog.numberDay.wrapSelectorWheel = true
        bindingDialog.numberDay.value = 1

    }
}