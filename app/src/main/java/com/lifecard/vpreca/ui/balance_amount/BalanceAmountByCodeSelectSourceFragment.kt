package com.lifecard.vpreca.ui.balance_amount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.SelectSourceConfirmData
import com.lifecard.vpreca.data.model.SelectedData
import com.lifecard.vpreca.databinding.FragmentBalanceAmountByCodeSelectSourceBinding
import com.lifecard.vpreca.databinding.SelectSourceBalanceItemBinding
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BalanceAmountByCodeSelectSourceFragment : Fragment() {

    companion object {
        fun newInstance() = BalanceAmountByCodeSelectSourceFragment()
    }

    private var _binding: FragmentBalanceAmountByCodeSelectSourceBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BalanceAmountByCodeSelectSourceViewModel by viewModels()
    private val args: BalanceAmountByCodeSelectSourceFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            FragmentBalanceAmountByCodeSelectSourceBinding.inflate(inflater, container, false)

        val rcView = binding.rvSelectSource
        val btnBack = binding.appbarGiftThird.btnBack
        val tvTotalAmount = binding.tvTotalAmount
        val tvSelect = binding.tvSelect
        val tvRemain = binding.tvRemain
        val btnSubmit = binding.btnSubmitIntroduceFirst

        val fakeBalanceRamain = args.balanceTotalRemain?.balanceAmount?.toInt()
        var remain = fakeBalanceRamain!!
        var select = 0
        var cardSchemeId = ""
        var designId = ""
        var cardNickname = ""
        var vcnName = ""
        var precaNumber = ""
        var vcn = ""

        tvTotalAmount.text = Converter.convertCurrency(fakeBalanceRamain)
        tvSelect.text = Converter.convertCurrency(select)
        tvRemain.text = Converter.convertCurrency(remain)

        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        btnBack.setOnClickListener { findNavController().popBackStack() }

        viewModel.creditCardResult.observe(
            viewLifecycleOwner,
            Observer { creditCardResult ->
                creditCardResult ?: return@Observer
                creditCardResult.success?.let {
                    println("homeViewModel.creditCardResult.observe success: ${creditCardResult.success}")
                    // The pager adapter, which provides the pages to the view pager widget.
                    when (creditCardResult.success.size) {
                        0 -> {
                        }
                        else -> {
                            val arrSelected: List<SelectedData>
                            val arrPolicy: List<CreditCard> =
                                creditCardResult.success.filter { creditCard -> creditCard.isEnable() }

                            arrSelected = arrPolicy.mapIndexed { _, creditCard ->
                                SelectedData(
                                    "0",
                                    creditCard.publishAmount,
                                    "0"
                                )
                            }

                            val linearLayoutManager =
                                LinearLayoutManager(context)
                            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                            val adapter = BalanceSelectSourceAdapter(arrPolicy, arrSelected)
                            rcView.adapter = adapter
                            rcView.layoutManager = linearLayoutManager

                            adapter.setOnClickListener(object :
                                BalanceSelectSourceAdapter.OnItemClickListener {
                                override fun onItemClick(
                                    position: Int,
                                    binding: SelectSourceBalanceItemBinding
                                ) {
                                    var lastPost = 0
                                    if (arrSelected[position].isSelected == "0") {
                                        for (i in arrSelected.indices) {
                                            if (arrSelected[i].isSelected == "1") {
                                                lastPost = i
                                            }
                                            if (i == position) {
                                                arrSelected[i].isSelected = "1"
                                            } else {
                                                arrSelected[i].isSelected = "0"
                                            }
                                        }
                                        println(arrSelected)

                                        select = arrPolicy[position].publishAmount.toInt()
                                        remain = fakeBalanceRamain - select
                                        if (remain > 0) {
                                            tvSelect.text = Converter.convertCurrency(select)
                                            tvRemain.text = Converter.convertCurrency(remain)
                                        } else {
                                            remain = 0
                                            tvSelect.text = Converter.convertCurrency(select)
                                            tvRemain.text = Converter.convertCurrency(0)
                                        }

                                        cardSchemeId = arrPolicy[position].cardSchemeId
                                        designId = arrPolicy[position].designId
                                        cardNickname = arrPolicy[position].cardNickname
                                        vcnName = arrPolicy[position].vcnName
                                        precaNumber = arrPolicy[position].precaNumber
                                        vcn = arrPolicy[position].vcn
                                        adapter.notifyItemChanged(position)
                                        adapter.notifyItemChanged(lastPost)
                                        btnSubmit.isEnabled = true
                                    } else {
                                        arrSelected[position].isSelected = "0"
                                        println(arrSelected)
                                        adapter.notifyItemChanged(position)
                                        select = 0
                                        remain = fakeBalanceRamain
                                        tvSelect.text = Converter.convertCurrency(select)
                                        tvRemain.text = Converter.convertCurrency(remain)
                                        btnSubmit.isEnabled = false
                                    }
                                }

                            })
                        }
                    }
                }
                creditCardResult.error?.let { error ->

                    error.messageResId?.let { showPopupMessage(message = getString(it)) }
                    error.errorMessage?.let { showPopupMessage(message = it) }
                }
                creditCardResult.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
            })
        viewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        }

        btnSubmit.setOnClickListener {
            val data = SelectSourceConfirmData(
                fakeBalanceRamain.toString(),
                select.toString(),
                remain.toString(),
                cardSchemeId,
                designId,
                cardNickname,
                vcnName,
                precaNumber,
                vcn
            )
            val action =
                BalanceAmountByCodeSelectSourceFragmentDirections.actionSelectsourceToConfirm(
                    data
                )
            findNavController().navigate(action)
        }

        return binding.root
    }
}