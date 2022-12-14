package com.lifecard.vpreca.ui.introduce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentGiftCardInputCardBinding
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GiftCardInputCardFragment : Fragment() {

    companion object {
        fun newInstance() = IntroduceFragmentSecond()
    }

    private var _binding: FragmentGiftCardInputCardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GiftCardInputCardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGiftCardInputCardBinding.inflate(inflater, container, false)
        val btnSubmit = binding.btnSubmitInput
        val btnBack = binding.appbarGiftSecond.btnBack
        val buttonOcrDetection = binding.buttonOcrDetection
        val giftCodeLayout = binding.usernameLayout
        val giftCodeEdt = binding.textCode
        val vcnLayout = binding.giftVcnLayout
        val vcnInput = binding.giftVcnInput

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
        btnSubmit.setOnClickListener {
            viewModel.submit()
        }
        buttonOcrDetection.setOnClickListener {
            val action =
                GiftCardInputCardFragmentDirections.actionGiftcardinputcardToCameraOcr(getString(R.string.camera_ocr_hint_input_gift_card))
            findNavController().navigate(action)
            viewModel.formResultState.value = null
            viewModel._giftCardState.value = null
        }

        viewModel.formState.observe(viewLifecycleOwner) { viewModel.checkFormValid() }

        viewModel.giftError.observe(viewLifecycleOwner) { error: Int? ->
            giftCodeLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        viewModel.vcnError.observe(viewLifecycleOwner) { error: Int? ->
            vcnLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        viewModel.validForm.observe(
            viewLifecycleOwner
        ) { isValid ->
            btnSubmit.isEnabled = isValid
        }


        viewModel.formResultState.observe(viewLifecycleOwner) {
            it?.success?.let {
                viewModel.getGiftCardInfo(giftCodeEdt.text.toString(), vcnInput.text.toString())
            }
        }

        viewModel._giftCardState.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { changeInfoState ->
                changeInfoState ?: return@Observer
                changeInfoState.error?.messageResId?.let { showPopupMessage(message = getString(it)) }
                changeInfoState.error?.errorMessage?.let { showPopupMessage(message = it) }
                changeInfoState.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
                changeInfoState.success?.let {
                    val action =
                        GiftCardInputCardFragmentDirections.actionGiftcardinputcardToGiftcardconfirm(
                            changeInfoState.success,
                            GiftCardConfirmData("inputcard")
                        )
                    findNavController().navigate(action)
                }
            })

        viewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        }

        giftCodeEdt.doAfterTextChanged { text -> viewModel.giftNumberDataChanged(text = text.toString()) }
        vcnInput.doAfterTextChanged { text -> viewModel.vcnDataChanged(text = text.toString()) }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val livedata = getNavigationResult("ocr_code")
        livedata?.observe(viewLifecycleOwner) { ocr ->
            livedata.removeObservers(viewLifecycleOwner)
            if (!ocr.isNullOrEmpty()) {
                val textCode = binding.textCode
                textCode.setText(ocr)
                showToast(getString(R.string.camera_ocr_success), toastPosition = ToastPosition.Top)
            }
            livedata.value = null
        }
    }


}