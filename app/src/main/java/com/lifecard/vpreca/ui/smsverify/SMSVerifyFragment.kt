package com.lifecard.vpreca.ui.smsverify

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifecard.vpreca.R

class SMSVerifyFragment : Fragment() {

    companion object {
        fun newInstance() = SMSVerifyFragment()
    }

    private lateinit var viewModel: SMSVerifyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sms_verify, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SMSVerifyViewModel::class.java)
        // TODO: Use the ViewModel
    }

}