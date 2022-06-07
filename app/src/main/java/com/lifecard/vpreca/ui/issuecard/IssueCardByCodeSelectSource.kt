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
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.data.model.IssueByCodeSelectSourceConfirmData
import com.lifecard.vpreca.data.model.SelectedData
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeSelectSourceBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardSelectDesignBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardSelectSourceBinding
import com.lifecard.vpreca.databinding.SelectSourceCardItemBinding
import com.lifecard.vpreca.utils.Converter
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
        tvTotal.text = Converter.convertCurrency(fakeGiftValue)
        var dataSelectCard = "0"
        var number = ""
        var expireDate = Date()
        var pin = ""
        var name = ""
        var cardSchemeId = ""
        var vcnName = ""

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
                                        vcnName = arrPolicy[position].vcnName
                                        cardSchemeId = arrPolicy[position].cardSchemeId
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
            val data = IssueByCodeSelectSourceConfirmData(
                fakeGiftValue.toString(),
                dataTotal.toString(),
                expireDate,
                pin,
                name,
                number,
                args.issuePlusData?.giftNumber!!,
                vcnName,
                cardSchemeId,
                args.issuePlusData?.balanceAmount!!
            )
            val action = IssueCardByCodeSelectSourceDirections.actionSelectsourceToConfirm(
                data
            )
            findNavController().navigate(action)
        })
        return binding.root
    }


}