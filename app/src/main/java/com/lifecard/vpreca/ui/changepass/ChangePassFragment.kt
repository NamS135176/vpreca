package com.lifecard.vpreca.ui.changepass

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.ChangeInfoMemberData
import com.lifecard.vpreca.databinding.FragmentChangePassBinding
import com.lifecard.vpreca.databinding.FragmentListVprecaBinding
import com.lifecard.vpreca.ui.changeinfo.ChangeInfoInputFragmentDirections
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePassFragment : Fragment() {

    companion object {
        fun newInstance() = ChangePassFragment()
    }

    private val viewModel: ChangePassViewModel by viewModels()
    private var _binding: FragmentChangePassBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        viewModel = ViewModelProvider(this).get(ChangePassViewModel::class.java)
        _binding = FragmentChangePassBinding.inflate(inflater, container, false)
        val oldPassLayout = binding.changePassOldLayout
        val oldPassEdt = binding.changePassOldEdt
        val newPassLayout = binding.changePassNewLayout
        val newPassEdt = binding.changePassNewEdt
        val cfNewPassLayout = binding.changePassCfNewLayout
        val cfNewPassEdt = binding.changePassCfNewEdt
        val btnSubmit = binding.btnSubmitPolicy
        val btnBack = binding.appbarSignup.btnBack
        val loading = binding.loading

        viewModel.changePassState.observe(
            viewLifecycleOwner,
            Observer { changeInfoState ->
                changeInfoState ?: return@Observer
                changeInfoState.error?.messageResId?.let { showPopupMessage(message = getString(it)) }
                changeInfoState.error?.errorMessage?.let { showPopupMessage(message = it) }
                changeInfoState.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
                changeInfoState.success?.let {
                    findNavController().navigate(R.id.nav_change_pass_complete)
                }

            })

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> loading.visibility = View.VISIBLE
                else -> loading.visibility = View.GONE
            }
        })

        viewModel.formState.observe(viewLifecycleOwner, Observer { viewModel.checkFormValid() })
        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { isValid ->
                btnSubmit.isEnabled = isValid
            })

        viewModel.oldPassError.observe(viewLifecycleOwner, Observer { error: Int? ->
            oldPassLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        viewModel.newPassError.observe(viewLifecycleOwner, Observer { error: Int? ->
            newPassLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        viewModel.cfNewPassError.observe(viewLifecycleOwner, Observer { error: Int? ->
            cfNewPassLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        viewModel.formResultState.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.success?.let {
                viewModel.changePassData(oldPassEdt.text.toString(),newPassEdt.text.toString() )
            }
        })

        oldPassEdt.doAfterTextChanged { text -> viewModel.oldPasswordDataChanged(text = text.toString()) }
        newPassEdt.doAfterTextChanged { text -> viewModel.newPasswordDataChanged(text = text.toString()) }
        cfNewPassEdt.doAfterTextChanged { text -> viewModel.cfNewPasswordDataChanged(text = text.toString()) }


        btnBack.setOnClickListener(View.OnClickListener { findNavController().popBackStack() })
        btnSubmit.setOnClickListener(View.OnClickListener {
//            findNavController().navigate(R.id.nav_change_pass_complete)
            viewModel.submit()
        })


        return binding.root
    }
}