package com.lifecard.vpreca.ui.issuecard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.data.model.IssueSelectSourceData
import com.lifecard.vpreca.data.model.SelectedData
import com.lifecard.vpreca.databinding.CardDetailLayoutBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardSelectSourceBinding
import com.lifecard.vpreca.databinding.FragmentListVprecaBinding
import com.lifecard.vpreca.databinding.SelectSourceCardItemBinding
import com.lifecard.vpreca.ui.introduce.GiftCardInputCardFragmentDirections
import com.lifecard.vpreca.ui.listvpreca.GridDecoration
import com.lifecard.vpreca.ui.listvpreca.ListVprecaAdapter
import com.lifecard.vpreca.ui.listvpreca.ListVprecaFragmentDirections
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IssueCardSelectSourceFragment : Fragment() {

    companion object {
        fun newInstance() = IssueCardSelectSourceFragment()
    }

    private lateinit var viewModel: IssueCardSelectSourceViewModel
    private var _binding: FragmentIssueCardSelectSourceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIssueCardSelectSourceBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(IssueCardSelectSourceViewModel::class.java)
        val tvTotal = binding.tvTotalAmount
        val rcView = binding.rvSelectSource
        val btnCancel = binding.appbarGiftThird.cancelBtn
        val btnBack = binding.appbarGiftThird.btnBack
        val btnSubmit = binding.btnSubmitIntroduceFirst
        var arrSelected: List<SelectedData> = emptyList()
        var arrPolicy: List<CreditCard> = emptyList()
        val tvError = binding.error
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.nav_issue_card_by_plus_introduce)
                }
            })
        btnSubmit.setOnClickListener(View.OnClickListener {
            val giftCardConfirmData = GiftCardConfirmData("selectSource")
            val issueSelectSourceData = IssueSelectSourceData(arrPolicy, arrSelected)
            val action = IssueCardSelectSourceFragmentDirections.actionSelectsourceToSelectdesign(
                giftCardConfirmData,
                null,
                null,
                issueSelectSourceData
            )
            findNavController().navigate(action)
        })

        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_by_plus_introduce) })
        btnCancel.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.action_issue_select_source_to_main) })

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
                            rcView.layoutManager = linearLayoutManager
                            rcView.adapter = adapter
                            val list: MutableList<Int> = ArrayList<Int>()
                            adapter.setOnClickListener(object :
                                IssueCardSourceAdapter.OnItemClickListener {
                                override fun onItemClick(
                                    position: Int,
                                    binding: SelectSourceCardItemBinding
                                ) {
                                    tvError.visibility = View.INVISIBLE

                                    var count = 0
                                    for (it in arrSelected) {
                                        if (it.isSelected == "1") {
                                            count++
                                        }
                                    }

                                    val sum: Int = arrSelected.sumOf {
                                        try {
                                            if (it.isSelected == "1") {
                                                it.amount.toInt()
                                            } else {
                                                0
                                            }
                                        } catch (e: Exception) {
                                            0
                                        }
                                    }

                                    if (arrSelected[position].isSelected == "0") {
                                        if (count < 5 && sum <= (100000 - arrSelected[position].amount.toInt())) {
                                            btnSubmit.isEnabled = list.size > 0
                                            list.add(position)
                                            println(list)
                                            if (count == 0) {
                                                arrSelected[position].isSelected = "1"
                                                arrSelected[position].isFirst = "1"
                                                binding.select = arrSelected[position]
                                                val sumBalance: Int = arrSelected.sumOf {
                                                    try {
                                                        if (it.isSelected == "1") {
                                                            it.amount.toInt()
                                                        } else {
                                                            0
                                                        }
                                                    } catch (e: Exception) {
                                                        0
                                                    }
                                                }
                                                tvTotal.text = Converter.convertCurrency(sumBalance)
                                            } else {
                                                arrSelected[position].isSelected = "1"
                                                binding.select = arrSelected[position]
                                                val sumBalance: Int = arrSelected.sumOf {
                                                    try {
                                                        if (it.isSelected == "1") {
                                                            it.amount.toInt()
                                                        } else {
                                                            0
                                                        }
                                                    } catch (e: Exception) {
                                                        0
                                                    }
                                                }
                                                tvTotal.text = Converter.convertCurrency(sumBalance)
                                            }
                                        }
                                        else{
//                                            tvError.visibility = View.VISIBLE
                                            if(count >= 5 ){
                                                tvError.text = "最大5枚を選択してください"
                                                showAlertMessage("最大5枚を選択してください")
                                            }
                                            else if(sum > (100000 - arrSelected[position].amount.toInt())){
                                                tvError.text = "合算金額は10万円以内です。"
                                                showAlertMessage("合算金額は10万円以内です")
                                            }
                                        }
                                    } else {
                                        list.remove(position)
                                        btnSubmit.isEnabled = list.size > 1
                                        if (arrSelected[position].isFirst == "1") {

                                            println(list)
                                            if (list.size > 0) {
                                                arrSelected[list[0]].isFirst = "1"
                                                adapter.notifyItemChanged(list[0])
                                            }
                                            arrSelected[position].isFirst = "0"
                                            arrSelected[position].isSelected = "0"
                                            binding.select = arrSelected[position]

                                            val sumBalance: Int = arrSelected.sumOf {
                                                try {
                                                    if (it.isSelected == "1") {
                                                        it.amount.toInt()
                                                    } else {
                                                        0
                                                    }
                                                } catch (e: Exception) {
                                                    0
                                                }
                                            }
                                            tvTotal.text = Converter.convertCurrency(sumBalance)
                                        } else {
                                            arrSelected[position].isSelected = "0"
                                            binding.select = arrSelected[position]
                                            val sumBalance: Int = arrSelected.sumOf {
                                                try {
                                                    if (it.isSelected == "1") {
                                                        it.amount.toInt()
                                                    } else {
                                                        0
                                                    }
                                                } catch (e: Exception) {
                                                    0
                                                }
                                            }
                                            tvTotal.text = Converter.convertCurrency(sumBalance)
                                        }
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

        return binding.root
    }


}