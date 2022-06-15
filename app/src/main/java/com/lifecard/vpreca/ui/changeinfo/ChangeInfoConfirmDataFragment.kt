package com.lifecard.vpreca.ui.changeinfo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.base.BackPressFragment
import com.lifecard.vpreca.data.model.PhoneData
import com.lifecard.vpreca.databinding.FragmentChangeInfoConfirmDataBinding
import com.lifecard.vpreca.databinding.FragmentChangeInfoDataBinding
import com.lifecard.vpreca.ui.card.CardBottomSheetCustom
import com.lifecard.vpreca.utils.showInternetTrouble
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
    ): View? {
        _binding = FragmentChangeInfoConfirmDataBinding.inflate(inflater, container, false)

        val btnCancelConfirm = binding.btnCancelConfirm
        val btnSubmitConfirm = binding.btnSubmitConfirm
        val btnBack = binding.appbarConfirmSignup.btnBack
        val btnCancel = binding.appbarConfirmSignup.cancelBtn
        val loading = binding.loading
        args.changeInfoData.let { changeInfoData -> binding.user = changeInfoData }

        btnBack.setOnClickListener(View.OnClickListener {
            findNavController().popBackStack()
        })

        btnCancelConfirm.setOnClickListener(View.OnClickListener {
            val action = ChangeInfoConfirmDataFragmentDirections.actionChangeConfirmDataToInput(
                args.changeInfoData
            )
            findNavController().navigate(action)
        })

        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    findNavController().navigate(R.id.nav_home)
                }
                setNegativeButton("いいえ", null)
                setMessage("途中ですがキャンセルしてもよろしいですか")
            }.create().show()
        })

        btnSubmitConfirm.setOnClickListener(View.OnClickListener {
//            findNavController().navigate(R.id.nav_change_info_complete)
            viewModel.changeInfoData(args.changeInfoData!!)
        })

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

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> loading.visibility = View.VISIBLE
                else -> loading.visibility = View.GONE
            }
        })

        return binding.root
    }


}