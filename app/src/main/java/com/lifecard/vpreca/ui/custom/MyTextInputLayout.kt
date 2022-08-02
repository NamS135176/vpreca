package com.lifecard.vpreca.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.lifecard.vpreca.R


class MyTextInputLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {
    private lateinit var errorText: AppCompatTextView
    private var editText: AppCompatEditText? = null

    var error: String? = null
        set(value) {
            if (field == value) return;//do nothing
            field = value
            errorText.text = value
            if (value?.isNotEmpty() == true) {
                errorText.visibility = View.VISIBLE
                editText?.background = ContextCompat.getDrawable(context, R.drawable.textinput_error_background)
            } else {
                errorText.visibility = View.GONE
                editText?.background = ContextCompat.getDrawable(context, R.drawable.textinput_background)
            }
        }

    init {
        orientation = VERTICAL
        if (childCount > 0) {
            editText = children.findLast { view -> view is AppCompatEditText } as AppCompatEditText
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        if (childCount > 0) {
            editText = try {
                children.findLast { view -> view is AppCompatEditText } as AppCompatEditText
            } catch (e: Exception) {null}
        }

        errorText = LayoutInflater.from(context)
            .inflate(R.layout.textinput_error, null) as AppCompatTextView
        errorText.visibility = View.GONE
        addView(errorText)
    }
}