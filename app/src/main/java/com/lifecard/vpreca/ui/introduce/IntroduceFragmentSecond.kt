package com.lifecard.vpreca.ui.introduce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.IntroduceFragmentSecondFragmentBinding
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IntroduceFragmentSecond : Fragment() {

    companion object {
        fun newInstance() = IntroduceFragmentSecond()
    }

    private var _binding: IntroduceFragmentSecondFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: IntroduceFragmentSecondViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = IntroduceFragmentSecondFragmentBinding.inflate(inflater, container, false)
//        viewModel = ViewModelProvider(this).get(IntroduceFragmentSecondViewModel::class.java)

        val btnSubmit = binding.btnSubmitInput
        val btnBack = binding.appbarGiftSecond.btnBack
        val buttonOcrDetection = binding.buttonOcrDetection
        val giftCodeLayout = binding.usernameLayout
        val giftCodeEdt = binding.textCode
        val vcnLayout = binding.giftVcnLayout
        val vcnInput = binding.giftVcnInput
        val loading = binding.loading

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        btnBack.setOnClickListener(View.OnClickListener {
            findNavController().popBackStack()
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
//            findNavController().navigate(R.id.nav_introduce_third)
            viewModel.submit()
        })
        buttonOcrDetection.setOnClickListener(View.OnClickListener {
            val action =
                IntroduceFragmentSecondDirections.actionToCameraOcr(getString(R.string.camera_ocr_hint_input_gift_card))
            findNavController().navigate(action)
        })

        viewModel.formState.observe(viewLifecycleOwner, Observer { viewModel.checkFormValid() })

        viewModel.giftError.observe(viewLifecycleOwner, Observer { error: Int? ->
            giftCodeLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        viewModel.vcnError.observe(viewLifecycleOwner, Observer { error: Int? ->
            vcnLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { isValid ->
                btnSubmit.isEnabled = isValid
            })


        viewModel.formResultState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.success?.let {
                viewModel.getGiftCardInfo(giftCodeEdt.text.toString(), vcnInput.text.toString())
//                findNavController().navigate(R.id.nav_signup_input)
            }
        })

        viewModel.giftCardState.observe(
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
                        IntroduceFragmentSecondDirections.actionSecondToThird(changeInfoState.success)
                    findNavController().navigate(action)
                }
            })

        viewModel.loading.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        })

        giftCodeEdt.doAfterTextChanged { text -> viewModel.giftNumberDataChanged(text = text.toString()) }
        vcnInput.doAfterTextChanged { text -> viewModel.vcnDataChanged(text = text.toString()) }

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
                println("IntroduceFragmentSecond... get ocr code $ocr")
            }
        })
    }


}