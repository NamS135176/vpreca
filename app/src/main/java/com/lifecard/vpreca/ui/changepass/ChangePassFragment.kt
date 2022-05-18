package com.lifecard.vpreca.ui.changepass

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePassBinding
import com.lifecard.vpreca.databinding.FragmentListVprecaBinding
import com.lifecard.vpreca.utils.hideToolbar
import com.lifecard.vpreca.utils.showToolbar

class ChangePassFragment : Fragment() {

    companion object {
        fun newInstance() = ChangePassFragment()
    }

    private lateinit var viewModel: ChangePassViewModel
    private var _binding: FragmentChangePassBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ChangePassViewModel::class.java)
        _binding = FragmentChangePassBinding.inflate(inflater, container, false)
        val oldPassLayout = binding.changePassOldLayout
        val oldPassEdt = binding.changePassOldEdt
        val newPassLayout = binding.changePassNewLayout
        val newPassEdt = binding.changePassNewEdt
        val cfNewPassLayout = binding.changePassCfNewLayout
        val cfNewPassEdt = binding.changePassCfNewEdt
        val btnSubmit = binding.btnSubmitPolicy
        val btnBack = binding.appbarSignup.btnBack

        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { forgotPassState ->
                if (oldPassEdt.text.toString() == "" || newPassEdt.text.toString() == "" || cfNewPassEdt.text.toString() == "" ) {
                    btnSubmit.isEnabled = false
                } else {
                    btnSubmit.isEnabled =
                        (forgotPassState.oldPassError == null && forgotPassState.newPassError == null && forgotPassState.cfNewPassError == null )
                }
            })

        viewModel.oldPassError.observe(viewLifecycleOwner, Observer {error: Int? ->
            oldPassLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        viewModel.newPassError.observe(viewLifecycleOwner, Observer {error: Int? ->
            newPassLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        viewModel.cfNewPassError.observe(viewLifecycleOwner, Observer {error: Int? ->
            cfNewPassLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            }
        })

        oldPassEdt.doAfterTextChanged { text -> viewModel.oldPasswordDataChanged(text = text.toString()) }
        newPassEdt.doAfterTextChanged { text -> viewModel.newPasswordDataChanged(text = text.toString()) }
        cfNewPassEdt.doAfterTextChanged { text -> viewModel.cfNewPasswordDataChanged(text = text.toString(), newPassEdt.text.toString()) }

        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_change_pass_complete) })
        btnBack.setOnClickListener(View.OnClickListener { findNavController().popBackStack() })
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hideToolbar()
    }

    override fun onDetach() {
        super.onDetach()
        showToolbar()
    }
}