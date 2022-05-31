package com.lifecard.vpreca.utils

import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.lifecard.vpreca.R
import java.lang.Exception

enum class ToastType {
    Success, Warning, Error
}

enum class ToastPosition {
    Bottom,
    Top
}

fun Toast.showCustomToast(
    message: String,
    activity: FragmentActivity,
    toastType: ToastType? = ToastType.Success,
    toastPosition: ToastPosition? = ToastPosition.Bottom
) {
    try {
        val layout = activity.layoutInflater.inflate(
            R.layout.custom_toast,
            null
        )
        // set the text of the TextView of the message
        val textView = layout.findViewById<AppCompatTextView>(R.id.toast_text)
        textView.text = message
        when (toastType) {
            ToastType.Success -> textView.setBackgroundResource(R.drawable.toast_success_shape)
            ToastType.Error -> textView.setBackgroundResource(R.drawable.toast_error_shape)
            ToastType.Warning -> textView.setBackgroundResource(R.drawable.toast_warning_shape)
            else -> {}
        }

        // use the application extension function
//        val height = activity.window.decorView.height
//        val yOffset = (0.265 * height).toInt()//0.265 = 110 / 414
        val yOffsetBottom = activity.resources.getDimension(R.dimen.toast_mb).toInt()
        val yOffsetTop = activity.resources.getDimension(R.dimen.toast_mt).toInt()

        println("showCustomToast...  - yOffset = $yOffset")
        this.apply {
            when (toastPosition) {
                ToastPosition.Bottom -> setGravity(
                    Gravity.FILL_HORIZONTAL or Gravity.BOTTOM,
                    0,
                    yOffsetBottom
                )
                else -> setGravity(Gravity.FILL_HORIZONTAL or Gravity.TOP, 0, yOffsetTop)
            }
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    } catch (e: Exception) {
    }
}

fun Fragment.showToast(
    message: String,
    toastType: ToastType? = ToastType.Success,
    toastPosition: ToastPosition? = ToastPosition.Bottom
) {
    try {
        Toast(requireContext()).showCustomToast(
            message,
            toastType = toastType,
            activity = requireActivity(),
            toastPosition = toastPosition
        )
    } catch (e: Exception) {

    }
}