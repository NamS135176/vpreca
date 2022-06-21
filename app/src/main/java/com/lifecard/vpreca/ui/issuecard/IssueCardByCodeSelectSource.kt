package com.lifecard.vpreca.ui.issuecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.IssueSelectSourceData
import com.lifecard.vpreca.data.model.SelectedData
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeSelectSourceBinding
import com.lifecard.vpreca.databinding.SelectSourceCardItemBinding
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IssueCardByCodeSelectSource : Fragment() {

    companion object {
        fun newInstance() = IssueCardByCodeSelectSource()
    }

    private lateinit var viewModel: IssueCardByCodeSelectSourceViewModel
    private var _binding: FragmentIssueCardByCodeSelectSourceBinding? = null
    private val binding get() = _binding!!
    private val args: IssueCardByCodeSelectSourceArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[IssueCardByCodeSelectSourceViewModel::class.java]
        _binding = FragmentIssueCardByCodeSelectSourceBinding.inflate(inflater, container, false)

        val rcView = binding.rvSelectSource
        val btnSubmit = binding.btnSubmitIntroduceFirst
        val btnBack = binding.appbarGiftThird.btnBack
        val btnCancel = binding.appbarGiftThird.cancelBtn
        val tvTotal = binding.tvTotalAmount
        val fakeGiftValue = args.issuePlusData?.giftAmount
        tvTotal.text = Converter.convertCurrency(fakeGiftValue)
        var arrSelected: List<SelectedData> = emptyList()
        var arrPolicy: List<CreditCard> = emptyList()
        requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })

        viewModel.issueGiftReqResult.observe(
            viewLifecycleOwner,
            Observer { issueGiftReqResult ->
                issueGiftReqResult ?: return@Observer
                issueGiftReqResult.success?.let {
                    findNavController().navigate(R.id.nav_issue_card_by_code_complete_without_card)
                }
                issueGiftReqResult.error?.let { error ->
                    error.messageResId?.let { showPopupMessage("",getString(it)) }
                    error.errorMessage?.let { showPopupMessage("",it) }
                }
                issueGiftReqResult.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
            })

        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        btnCancel.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { _, _ ->
                    // do something on positive button click
                    viewModel.issueGiftCardWithoutCard(
                        args.issuePlusData?.balanceAmount!!,
                        args.issuePlusData?.giftNumber!!
                    )
                }
                setNegativeButton("いいえ", null)
                setMessage(
                    "加算処理を中止して\n" +
                            "発行処理をしても\n" +
                            "よろしいですか？"
                )
            }.create().show()
        }

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
                            val adapter = IssueCardSourceAdapter(arrPolicy, arrSelected)
                            rcView.layoutManager = linearLayoutManager
                            rcView.adapter = adapter
                            val list: MutableList<Int> = ArrayList()
                            adapter.setOnClickListener(object :
                                IssueCardSourceAdapter.OnItemClickListener {
                                override fun onItemClick(
                                    position: Int,
                                    binding: SelectSourceCardItemBinding
                                ) {
                                    btnSubmit.isEnabled = true
                                    var count = 0
                                    for (item in arrSelected) {
                                        if (item.isSelected == "1") {
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
                                        if (count < 4 && sum <= (100000 - arrSelected[position].amount.toInt())) {

                                            list.add(position)
                                            println(list)
                                            if (count == 0) {
                                                arrSelected[position].isSelected = "1"
                                                arrSelected[position].isFirst = "0"
                                                binding.select = arrSelected[position]

                                            } else {
                                                arrSelected[position].isSelected = "1"
                                                binding.select = arrSelected[position]

                                            }
                                        }
                                        else{
                                            if(count >= 4 ){
                                                showAlertMessage("最大4枚を選択してください")
                                            }
                                            else if(sum > (100000 - arrSelected[position].amount.toInt())){
                                                showAlertMessage("合算金額は10万円以内です")
                                            }
                                        }
                                    } else {
                                        list.remove(position)
                                        btnSubmit.isEnabled = list.size != 0
                                        if (arrSelected[position].isFirst == "0") {

                                            println(list)
                                            if (list.size > 0) {
                                                arrSelected[list[0]].isFirst = "0"
                                                adapter.notifyItemChanged(list[0])
                                            }
                                            arrSelected[position].isFirst = "0"
                                            arrSelected[position].isSelected = "0"
                                            binding.select = arrSelected[position]
                                        } else {
                                            arrSelected[position].isSelected = "0"
                                            binding.select = arrSelected[position]
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

        viewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        }

        btnSubmit.setOnClickListener {
            val action = IssueCardByCodeSelectSourceDirections.actionSelectsourceToConfirm(
                IssueSelectSourceData(arrPolicy, arrSelected),
                args.issuePlusData
            )
            findNavController().navigate(action)
        }
        return binding.root
    }


}