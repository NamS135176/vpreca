package com.lifecard.vpreca.ui.changepass

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePassBinding
import com.lifecard.vpreca.utils.hideLoadingDialog
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showLoadingDialog
import com.lifecard.vpreca.utils.showPopupMessage
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
    ): View {
        _binding = FragmentChangePassBinding.inflate(inflater, container, false)
        val oldPassLayout = binding.changePassOldLayout
        val oldPassEdt = binding.changePassOldEdt
        val newPassLayout = binding.changePassNewLayout
        val newPassEdt = binding.changePassNewEdt
        val cfNewPassLayout = binding.changePassCfNewLayout
        val cfNewPassEdt = binding.changePassCfNewEdt
        val btnSubmit = binding.btnSubmitPolicy
        val btnBack = binding.appbarSignup.btnBack
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

        viewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        }

        viewModel.formState.observe(viewLifecycleOwner) { viewModel.checkFormValid() }
        viewModel.validForm.observe(
            viewLifecycleOwner
        ) { isValid ->
            btnSubmit.isEnabled = isValid
        }

        viewModel.oldPassError.observe(viewLifecycleOwner) { error: Int? ->
            oldPassLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        viewModel.newPassError.observe(viewLifecycleOwner) { error: Int? ->
            newPassLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        viewModel.cfNewPassError.observe(viewLifecycleOwner) { error: Int? ->
            cfNewPassLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        }

        viewModel.formResultState.observe(viewLifecycleOwner) {
            it?.success?.let {
                viewModel.changePassData(oldPassEdt.text.toString(), newPassEdt.text.toString())
            }
        }

        oldPassEdt.doAfterTextChanged { text -> viewModel.oldPasswordDataChanged(text = text.toString()) }
        newPassEdt.doAfterTextChanged { text -> viewModel.newPasswordDataChanged(text = text.toString()) }
        cfNewPassEdt.doAfterTextChanged { text -> viewModel.cfNewPasswordDataChanged(text = text.toString()) }

        btnBack.setOnClickListener { findNavController().popBackStack() }
        btnSubmit.setOnClickListener {
            viewModel.submit()
        }


        return binding.root
    }
}