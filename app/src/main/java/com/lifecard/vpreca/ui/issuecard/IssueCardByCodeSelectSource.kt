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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.*
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeSelectSourceBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardSelectDesignBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardSelectSourceBinding
import com.lifecard.vpreca.databinding.SelectSourceCardItemBinding
import com.lifecard.vpreca.utils.Converter
import com.lifecard.vpreca.utils.showAlertMessage
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showPopupMessage
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
    private val args: IssueCardByCodeSelectSourceArgs by navArgs()
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
        val loading = binding.loading
        val fakeGiftValue = args.issuePlusData?.giftAmount
        val tvError = binding.desBottom
        tvTotal.text = Converter.convertCurrency(fakeGiftValue)
        var dataSelectCard = "0"
        var number = ""
        var expireDate = Date()
        var pin = ""
        var name = ""
        var cardSchemeId = ""
        var vcnName = ""
        var arrSelected: List<SelectedData> = emptyList()
        var arrPolicy: List<CreditCard> = emptyList()
        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = IssueCardByCodeSelectSourceDirections.actionSelectsourceToSelectway(args.issuePlusData)
                findNavController().navigate(action)
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

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> loading.visibility = View.VISIBLE
                else -> loading.visibility = View.GONE
            }
        })
        btnBack.setOnClickListener(View.OnClickListener {
            val action = IssueCardByCodeSelectSourceDirections.actionSelectsourceToSelectway(args.issuePlusData)
            findNavController().navigate(action)
        })

        btnCancel.setOnClickListener(View.OnClickListener {
            MaterialAlertDialogBuilder(requireContext()).apply {
                setPositiveButton("はい") { dialog, which ->
                    // do something on positive button click
                    viewModel.issueGiftCardWithoutCard(args.issuePlusData?.balanceAmount!!, args.issuePlusData?.giftNumber!!)
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
//                            var arrSelected: List<SelectedData>
//                            val arrPolicy: List<CreditCard>
//                            arrPolicy = creditCardResult.success
//
//                            arrSelected = arrPolicy.mapIndexed { index, creditCard ->
//                                SelectedData(
//                                    "0",
//                                    creditCard.publishAmount,
//                                    "0"
//                                )
//                            }
//
//                            val linearLayoutManager: LinearLayoutManager =
//                                LinearLayoutManager(context)
//                            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
//                            val adapter = IssueCardSourceAdapter(arrPolicy, arrSelected)
//                            rcView.adapter = adapter
//                            rcView.layoutManager = linearLayoutManager
//
//                            adapter.setOnClickListener(object :
//                                IssueCardSourceAdapter.OnItemClickListener {
//                                override fun onItemClick(
//                                    position: Int,
//                                    binding: SelectSourceCardItemBinding
//                                ) {
//                                    if (arrSelected[position].isSelected == "0") {
//                                        for (i in 0 until arrSelected.size) {
//                                            if (i == position) {
//                                                arrSelected[i].isSelected = "1"
//                                            } else {
//                                                arrSelected[i].isSelected = "0"
//                                            }
//                                        }
//                                        println(arrSelected)
//                                        adapter.notifyDataSetChanged()
//                                        btnSubmit.isEnabled = true
//                                        dataSelectCard = arrPolicy[position].publishAmount
//                                        name = arrPolicy[position].cardNickname
//                                        expireDate = arrPolicy[position].precaExpirationDate
//                                        pin = arrPolicy[position].vcn
//                                        number = arrPolicy[position].precaNumber
//                                        vcnName = arrPolicy[position].vcnName
//                                        cardSchemeId = arrPolicy[position].cardSchemeId
//                                    } else {
//                                        arrSelected[position].isSelected = "0"
//                                        println(arrSelected)
//                                        adapter.notifyItemChanged(position)
//                                        btnSubmit.isEnabled = false
//                                        dataSelectCard = "0"
//                                    }
//                                }
//                            })

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
                                    btnSubmit.isEnabled = true
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
                                            tvError.visibility = View.VISIBLE
                                            if(count >= 4 ){
                                                tvError.text = "最大4枚を選択してください"
                                                showAlertMessage("最大4枚を選択してください")
                                            }
                                            if(sum > (100000 - arrSelected[position].amount.toInt())){
                                                tvError.text = "合算金額は10万円以内です。"
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
        btnSubmit.setOnClickListener(View.OnClickListener {
            val dataTotal = dataSelectCard.toInt() + fakeGiftValue?.toInt()!!
//            val data = IssueByCodeSelectSourceConfirmData(
//                fakeGiftValue.toString(),
//                dataTotal.toString(),
//                expireDate,
//                pin,
//                name,
//                number,
//                args.issuePlusData?.giftNumber!!,
//                vcnName,
//                cardSchemeId,
//                args.issuePlusData?.balanceAmount!!
//            )
            val action = IssueCardByCodeSelectSourceDirections.actionSelectsourceToConfirm(
              IssueSelectSourceData(arrPolicy, arrSelected),
                args.issuePlusData
            )
            findNavController().navigate(action)
        })
        return binding.root
    }


}