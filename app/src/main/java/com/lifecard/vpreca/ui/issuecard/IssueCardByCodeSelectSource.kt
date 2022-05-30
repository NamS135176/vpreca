package com.lifecard.vpreca.ui.issuecard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.data.model.IssueByCodeSelectSourceConfirmData
import com.lifecard.vpreca.data.model.SelectedData
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeSelectSourceBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardSelectDesignBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardSelectSourceBinding
import com.lifecard.vpreca.databinding.SelectSourceCardItemBinding
import com.lifecard.vpreca.utils.Converter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class IssueCardByCodeSelectSource : Fragment() {

    companion object {
        fun newInstance() = IssueCardByCodeSelectSource()
    }

    private lateinit var viewModel: IssueCardByCodeSelectSourceViewModel
    private var _binding: FragmentIssueCardByCodeSelectSourceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(IssueCardByCodeSelectSourceViewModel::class.java)
        _binding = FragmentIssueCardByCodeSelectSourceBinding.inflate(inflater, container, false)

        val rcView = binding.rvSelectSource
        val btnSubmit = binding.btnSubmitIntroduceFirst
        val btnBack = binding.appbarGiftThird.btnBack
        val btnCancel = binding.appbarGiftThird.cancelBtn
        val tvTotal = binding.tvTotalAmount

        val fakeGiftValue = 5000
        tvTotal.text = Converter.convertCurrency(fakeGiftValue)
        var dataSelectCard = "0"
        var number = ""
        var expireDate = Date()
        var pin = ""
        var name = ""
        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_issue_card_by_code_select_way)
            }
        })

        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_by_code_select_way) })

        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    // do something on positive button click
                    findNavController().navigate(R.id.nav_issue_card_by_code_complete_without_card)
                }
                setNegativeButton("いいえ", null)
                setMessage(
                    "加算処理を中止して\n" +
                            "発行処理をしても\n" +
                            "よろしいですか？"
                )
            }.create().show()
        })

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
                                    if (arrSelected[position].isSelected == "0") {
                                        for (i in 0 until arrSelected.size) {
                                            if (i == position) {
                                                arrSelected[i].isSelected = "1"
                                            } else {
                                                arrSelected[i].isSelected = "0"
                                            }
                                        }
                                        println(arrSelected)
                                        adapter.notifyDataSetChanged()
                                        btnSubmit.isEnabled = true
                                        dataSelectCard = arrPolicy[position].publishAmount
                                        name = arrPolicy[position].cardNickname
                                        expireDate = arrPolicy[position].precaExpirationDate
                                        pin = arrPolicy[position].vcn
                                        number = arrPolicy[position].precaNumber
                                    } else {
                                        arrSelected[position].isSelected = "0"
                                        println(arrSelected)
                                        adapter.notifyItemChanged(position)
                                        btnSubmit.isEnabled = false
                                        dataSelectCard = "0"
                                    }
                                }
                            })
                        }
                    }
                }
                creditCardResult.error?.let {

                    MaterialAlertDialogBuilder(requireContext()).apply {
                        setPositiveButton("ok", null)
                        setMessage(getString(it.messageResId))
                    }.create().show()
                }
            })
        btnSubmit.setOnClickListener(View.OnClickListener {
            val dataTotal = dataSelectCard.toInt() + fakeGiftValue.toInt()
            val data = IssueByCodeSelectSourceConfirmData(
                fakeGiftValue.toString(),
                dataTotal.toString(),
                expireDate,
                pin,
                name,
                number
            )
            val action = IssueCardByCodeSelectSourceDirections.actionSelectsourceToConfirm(
                data
            )
            findNavController().navigate(action)
        })
        return binding.root
    }


}