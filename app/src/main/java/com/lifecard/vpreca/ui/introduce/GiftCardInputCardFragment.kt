package com.lifecard.vpreca.ui.introduce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.base.NoToolbarFragment
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentGiftCardInputCardBinding
import com.lifecard.vpreca.utils.ToastPosition
import com.lifecard.vpreca.utils.getNavigationResult
import com.lifecard.vpreca.utils.showToast

class GiftCardInputCardFragment : NoToolbarFragment() {

    companion object {
        fun newInstance() = GiftCardInputCardFragment()
    }

    private lateinit var viewModel: GiftCardInputCardViewModel
    private var _binding: FragmentGiftCardInputCardBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GiftCardInputCardViewModel::class.java)
        _binding = FragmentGiftCardInputCardBinding.inflate(inflater, container, false)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_gift_card_policy)
            }
        })
        val btnSubmit = binding.btnSubmitInput
        val btnBack = binding.appbarGiftSecond.btnBack
        val buttonOcrDetection = binding.buttonOcrDetection
        val giftCodeLayout = binding.usernameLayout
        val giftCodeEdt = binding.textCode

        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { signupFormState ->
                if (giftCodeEdt.text.toString() == "" ) {
                    btnSubmit.isEnabled = false
                } else {
                    btnSubmit.isEnabled =
                        signupFormState.giftCodeError == null
                }
            })

        viewModel.giftCodeError.observe(viewLifecycleOwner, Observer { error: Int? ->
            giftCodeLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            } })
        giftCodeEdt.doAfterTextChanged { text -> viewModel.giftCodeDataChanged(text = text.toString()) }

        btnBack.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_gift_card_policy)
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            val giftCardConfirmData = GiftCardConfirmData("inputcard")
            val action =
                GiftCardInputCardFragmentDirections.actionGiftcardinputcardToGiftcardconfirm(
                    giftCardConfirmData
                )
            findNavController().navigate(action)
//            findNavController().navigate(R.id.nav_gift_card_confirm)
        })
        buttonOcrDetection.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_camera_ocr)
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val livedata = getNavigationResult("ocr_code")
        livedata?.observe(viewLifecycleOwner, Observer { ocr ->
            livedata.removeObservers(viewLifecycleOwner)
            if (!ocr.isNullOrEmpty()) {
                val textCode = binding.textCode
                textCode.setText(ocr)
                showToast(getString(R.string.camera_ocr_success), toastPosition = ToastPosition.Top)
                println("GiftCardPolicyFragment... get ocr code $ocr")
            }
        })
    }

}