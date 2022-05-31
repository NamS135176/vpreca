package com.lifecard.vpreca.ui.introduce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.IntroduceFragmentSecondFragmentBinding
import com.lifecard.vpreca.utils.ToastPosition
import com.lifecard.vpreca.utils.getNavigationResult
import com.lifecard.vpreca.utils.showToast

class IntroduceFragmentSecond : Fragment() {

    companion object {
        fun newInstance() = IntroduceFragmentSecond()
    }

    private var _binding: IntroduceFragmentSecondFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: IntroduceFragmentSecondViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = IntroduceFragmentSecondFragmentBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(IntroduceFragmentSecondViewModel::class.java)

        val btnSubmit = binding.btnSubmitInput
        val btnBack = binding.appbarGiftSecond.btnBack
        val buttonOcrDetection = binding.buttonOcrDetection

        btnBack.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_introduce_first)
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_introduce_third)
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
                println("IntroduceFragmentSecond... get ocr code $ocr")
            }
        })
    }


}