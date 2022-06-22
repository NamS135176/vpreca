package com.lifecard.vpreca.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentAlertBinding

class AlertDialogFragment : DialogFragment() {
    companion object {
        const val FRAGMENT_TAG = "AlertDialogFragment";

        fun newInstance(
            title: String? = null,
            message: String,
            buttonCancel: Int? = R.string.button_close
        ): AlertDialogFragment {
            val f = AlertDialogFragment()
            f.arguments =
                bundleOf("title" to title, "message" to message, "buttonCancel" to buttonCancel)
            return f
        }
    }

    private var _binding: FragmentAlertBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlertBinding.inflate(inflater)

        val title = arguments?.getString("title")
        val message = arguments?.getString("message")
        val buttonCancel = arguments?.getInt("buttonCancel")
        title?.let { binding.alertTitle = it }
        message?.let { binding.alertMessage = message }
        buttonCancel?.let { binding.buttonOk.text = getString(buttonCancel) }
        binding.buttonOk.setOnClickListener(View.OnClickListener {
            dismiss()
        })
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.window?.let { window ->
            window.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val attributes = window.attributes
            attributes.dimAmount = 0.25f
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            window.attributes = attributes
        }
    }
}