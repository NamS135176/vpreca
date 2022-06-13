package com.lifecard.vpreca.ui.balance_amount

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.*
import com.lifecard.vpreca.databinding.FragmentBalanceAmountByCodeSelectSourceBinding
import com.lifecard.vpreca.databinding.FragmentBalanceAmountMenuBinding
import com.lifecard.vpreca.databinding.SelectSourceCardItemBinding
import com.lifecard.vpreca.ui.issuecard.IssueCardByCodeSelectSourceDirections
import com.lifecard.vpreca.ui.issuecard.IssueCardSourceAdapter
import com.lifecard.vpreca.utils.Converter
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showPopupMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BalanceAmountByCodeSelectSourceFragment : Fragment() {

    companion object {
        fun newInstance() = BalanceAmountByCodeSelectSourceFragment()
    }

    private var _binding: FragmentBalanceAmountByCodeSelectSourceBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BalanceAmountByCodeSelectSourceViewModel
    private val args: BalanceAmountByCodeSelectSourceFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this).get(BalanceAmountByCodeSelectSourceViewModel::class.java)
        _binding =
            FragmentBalanceAmountByCodeSelectSourceBinding.inflate(inflater, container, false)

        val rcView = binding.rvSelectSource
        val btnBack = binding.appbarGiftThird.btnBack
        val tvTotalAmount = binding.tvTotalAmount
        val tvSelect = binding.tvSelect
        val tvRemain = binding.tvRemain
        val btnSubmit = binding.btnSubmitIntroduceFirst

        var fakeBalanceRamain = args?.balanceTotalRemain?.balanceAmount?.toInt()
        var remain = fakeBalanceRamain!!
        var select = 0
        var cardSchemeId = ""
        var designId = ""
        var cardNickname = ""
        var vcnName = ""
        var precaNumber = ""
        var vcn = ""

        tvTotalAmount.text = Converter.convertCurrency(fakeBalanceRamain!!)
        tvSelect.text = Converter.convertCurrency(select)
        tvRemain.text = Converter.convertCurrency(remain)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_balance_amount_menu)
            }
        })

        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_balance_amount_menu) })

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
                            var arrSelected: List<SelectedData>
                            val arrPolicy: List<CreditCard>
                            arrPolicy = creditCardResult.success

                            arrSelected = arrPolicy.mapIndexed { index, creditCard ->
                                SelectedData(
                                    "0",
                                    creditCard.publishAmount,
                                    "0"
                                )
                            }

                            val linearLayoutManager: LinearLayoutManager =
                                LinearLayoutManager(context)
                            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                            val adapter = IssueCardSourceAdapter(arrPolicy, arrSelected)
                            rcView.adapter = adapter
                            rcView.layoutManager = linearLayoutManager

                            adapter.setOnClickListener(object :
                                IssueCardSourceAdapter.OnItemClickListener {
                                override fun onItemClick(
                                    position: Int,
                                    binding: SelectSourceCardItemBinding
                                ) {
                                    var lastPost = 0
                                    if (arrSelected[position].isSelected == "0") {
                                        for (i in 0 until arrSelected.size) {
                                            if(arrSelected[i].isSelected == "1"){
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
                                        if(remain > 0){
                                            tvSelect.text = Converter.convertCurrency(select)
                                            tvRemain.text = Converter.convertCurrency(remain)
                                        }
                                        else{
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
//                                        dataSelectCard = arrPolicy[position].publishAmount
//                                        name = arrPolicy[position].cardNickname
//                                        expireDate = arrPolicy[position].precaExpirationDate
//                                        pin = arrPolicy[position].vcn
//                                        number = arrPolicy[position].precaNumber
                                    } else {
                                        arrSelected[position].isSelected = "0"
                                        println(arrSelected)
                                        adapter.notifyItemChanged(position)
                                        select = 0
                                        remain = fakeBalanceRamain
                                        tvSelect.text = Converter.convertCurrency(select)
                                        tvRemain.text = Converter.convertCurrency(remain)
                                        btnSubmit.isEnabled = false
//                                        dataSelectCard = "0"
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


        btnSubmit.setOnClickListener(View.OnClickListener {
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
        })

        return binding.root
    }
}