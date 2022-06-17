package com.lifecard.vpreca.ui.changeinfo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.base.BackPressFragment
import com.lifecard.vpreca.databinding.FragmentChangeInfoConfirmDataBinding
import com.lifecard.vpreca.utils.hideLoadingDialog
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showLoadingDialog
import com.lifecard.vpreca.utils.showPopupMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeInfoConfirmDataFragment : BackPressFragment() {

    companion object {
        fun newInstance() = ChangeInfoConfirmDataFragment()
    }

    private val viewModel: ChangeInfoConfirmDataViewModel by viewModels()
    private var _binding: FragmentChangeInfoConfirmDataBinding? = null
    private val binding get() = _binding!!
    private val args: ChangeInfoConfirmDataFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeInfoConfirmDataBinding.inflate(inflater, container, false)

        val btnCancelConfirm = binding.btnCancelConfirm
        val btnSubmitConfirm = binding.btnSubmitConfirm
        val btnBack = binding.appbarConfirmSignup.btnBack
        val btnCancel = binding.appbarConfirmSignup.cancelBtn
        args.changeInfoData.let { changeInfoData -> binding.user = changeInfoData }

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        btnCancelConfirm.setOnClickListener {
            findNavController().popBackStack()
        }

        btnCancel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    findNavController().popBackStack(R.id.nav_home, inclusive = false)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか?")
            }.create().show()
        }

        btnSubmitConfirm.setOnClickListener {
            viewModel.changeInfoData(args.changeInfoData)
        }

        viewModel.changeInfoState.observe(
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
                    findNavController().navigate(R.id.nav_change_info_complete)
                }

            })

        viewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        }

        return binding.root
    }


}