package com.lifecard.vpreca.ui.issuecard

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.BalanceGiftData
import com.lifecard.vpreca.data.model.CardInfoRequestContentInfo
import com.lifecard.vpreca.data.model.DesignCard
import com.lifecard.vpreca.databinding.FragmentIssueCardSelectDesignBinding
import com.lifecard.vpreca.eventbus.ReloadCard
import com.lifecard.vpreca.utils.hideLoadingDialog
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showLoadingDialog
import com.lifecard.vpreca.utils.showPopupMessage
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus

@AndroidEntryPoint
class IssueCardSelectDesignFragment : Fragment() {

    companion object {
        fun newInstance() = IssueCardSelectDesignFragment()
    }

    private val args: IssueCardSelectDesignFragmentArgs by navArgs()
    private val viewModel: IssueCardSelectDesignViewModel by viewModels()
    private var _binding: FragmentIssueCardSelectDesignBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIssueCardSelectDesignBinding.inflate(inflater, container, false)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (args.selectDesignData?.preRoute) {
                        "selectSource" -> findNavController().navigate(R.id.selectdesign_to_select_card_source)
                        "valueConfirm" -> {
                            MaterialAlertDialogBuilder(requireContext()).apply {
                                setPositiveButton("はい") { _, _ ->
                                    // do something on positive button click
                                    findNavController().navigate(R.id.selectdesign_to_main)
                                }
                                setNegativeButton("いいえ", null)
                                setMessage(
                                    "操作途中ですがキャンセル\n" +
                                            "してもよろしいですか？"
                                )
                            }.create().show()
                        }
                        "balanceByCodeValueConfirm" -> {
                            val data = args.selectDesignGiftData
                            val action =
                                IssueCardSelectDesignFragmentDirections.selectdesignToValueConfirm(
                                    data
                                )
                            findNavController().navigate(action)
                        }
                    }

                }
            })

        val btnBack = binding.appbarGiftThird.btnBack
        val btnCancel = binding.appbarGiftThird.cancelBtn
        val rcView = binding.rvView
        val bigCard = binding.cardZone.cardInclude
        val btnSubmit = binding.btnSubmitIntroduceFirst
        var designId = "99999"
        val recyclerIndicator = binding.indicator

        val cardLayout = binding.cardZone
        binding.cardZone.termOfUse.visibility = View.INVISIBLE
        when (args.selectDesignData?.preRoute) {
            "selectSource" -> {
                btnBack.visibility = View.VISIBLE
                btnCancel.visibility = View.VISIBLE
                btnSubmit.text = getString(R.string.issue_card_select_design_btn)
            }
            "valueConfirm" -> {
                btnBack.visibility = View.GONE
                btnCancel.visibility = View.VISIBLE
                btnSubmit.text = getString(R.string.btn_next_policy)
            }
            "balanceByCodeValueConfirm" -> {
                btnBack.visibility = View.VISIBLE
                btnCancel.visibility = View.GONE
                btnSubmit.text = getString(R.string.btn_next_policy)
            }
        }
        btnBack.setOnClickListener {
            when (args.selectDesignData?.preRoute) {
                "selectSource" -> {
                    findNavController().navigate(R.id.selectdesign_to_select_card_source)
                }
                "valueConfirm" -> {
                    findNavController().navigate(R.id.nav_balance_value_confirm)
                }
                else -> {
                    val data = args.selectDesignGiftData
                    val action =
                        IssueCardSelectDesignFragmentDirections.selectdesignToValueConfirm(
                            data
                        )
                    findNavController().navigate(action)
                }
            }
        }

        btnCancel.setOnClickListener {
            when (args.selectDesignData?.preRoute) {
                "selectSource" -> findNavController().navigate(R.id.selectdesign_to_main)
                "valueConfirm" -> {
                    MaterialAlertDialogBuilder(requireContext()).apply {
                        setPositiveButton("はい") { _, _ ->
                            // do something on positive button click
                            findNavController().navigate(R.id.selectdesign_to_main)
                        }
                        setNegativeButton("いいえ", null)
                        setMessage(
                            "操作途中ですがキャンセル\n" +
                                    "してもよろしいですか？"
                        )
                    }.create().show()
                }
            }
        }

        viewModel.feeInfoResult.observe(
            viewLifecycleOwner,
            Observer { feeInfoResult ->
                feeInfoResult ?: return@Observer
                feeInfoResult.success?.let {
                    println("homeViewModel.cardInfoResult.observe success: ${feeInfoResult.success}")
                    findNavController().navigate(R.id.nav_issue_card_complete)
                }
                feeInfoResult.error?.let { error ->
                    error.messageResId?.let { showPopupMessage("", getString(it)) }
                    error.errorMessage?.let { showPopupMessage("", it) }
                }
                feeInfoResult.networkTrouble?.let {
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

        viewModel.listDesignResult.observe(
            viewLifecycleOwner,
            Observer { listDesignResult ->
                listDesignResult ?: return@Observer
                listDesignResult.success?.let {
                    binding.cardZone.termOfUse.visibility = View.VISIBLE
                    val list: List<DesignCard>
                    list = ArrayList()
                    bigCard.cardInfo.setBackgroundResource(R.drawable.third)
                    binding.cardZone.card = DesignCard("99033", "0")
                    for (i in 0 until listDesignResult.success.cardDesignInfo?.size!!) {
                        if (i == 0) {
                            list.add(
                                DesignCard(
                                    listDesignResult.success.cardDesignInfo[i].designId,
                                    "1"
                                )
                            )
                        } else {
                            list.add(
                                DesignCard(
                                    listDesignResult.success.cardDesignInfo[i].designId,
                                    "0"
                                )
                            )
                        }
                    }

                    val layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

                    rcView.layoutManager = layoutManager

                    val adapter = IssueCardSelectDesignAdapter(list)
                    adapter.setOnClickListener(object :
                        IssueCardSelectDesignAdapter.OnItemClickListener {
                        override fun onItemClick(position: Int) {
                            recyclerIndicator.setCurrentPosition(position)
                            if (list[position].isSelected != "1") {
                                for (i in list.indices) {
                                    if (i == position) {
                                        list[i].isSelected = "1"
                                    } else {
                                        list[i].isSelected = "0"
                                    }
                                }
                                when (list[position].designId) {
                                    "99033" -> {
                                        bigCard.cardInfo.setBackgroundResource(R.drawable.first)
                                        cardLayout.card = DesignCard(list[position].designId, "0")
                                        designId = "99033"
                                    }
                                    "99455" -> {
                                        bigCard.cardInfo.setBackgroundResource(R.drawable.second)
                                        cardLayout.card = DesignCard(list[position].designId, "0")
                                        designId = "99455"
                                    }
                                    "99999" -> {
                                        bigCard.cardInfo.setBackgroundResource(R.drawable.third)
                                        cardLayout.card = DesignCard(list[position].designId, "0")
                                        designId = "99999"
                                    }
                                    else -> {
                                        bigCard.cardInfo.setBackgroundResource(R.drawable.first)
                                        cardLayout.card = DesignCard("99033", "0")
                                        designId = "99033"
                                    }
                                }
                                adapter.notifyDataSetChanged()
                            }

                        }

                    })
                    rcView.adapter = adapter
//        rcView.addItemDecoration(PagerDecorator())
                    recyclerIndicator.attachToRecyclerView(rcView)
                }
                listDesignResult.error?.let { error ->
                    error.messageResId?.let { showPopupMessage("", getString(it)) }
                    error.errorMessage?.let { showPopupMessage("", it) }
                }
                listDesignResult.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                        binding.cardZone.termOfUse.visibility = View.INVISIBLE
                    }
                }
            })

        btnSubmit.setOnClickListener {
            when (args.selectDesignData?.preRoute) {
                "selectSource" -> {
                    val sumUpSrcCardInfo = ArrayList<CardInfoRequestContentInfo>()
                    var srcIndex = 0
                    args.selectSourceData?.listCard?.forEachIndexed { index, creditCard ->
                        if (args.selectSourceData?.listSelectCard?.get(index)?.isFirst == "1") {
                            srcIndex = index
                        }
                        if (args.selectSourceData?.listSelectCard?.get(index)?.isSelected == "1" && args.selectSourceData?.listSelectCard?.get(
                                index
                            )?.isFirst == "0"
                        ) {
                            val data = CardInfoRequestContentInfo(
                                creditCard.cardSchemeId,
                                creditCard.precaNumber,
                                creditCard.vcn
                            )
                            sumUpSrcCardInfo.add(data)
                        }
                    }

                    viewModel.creditCardSelectDataChanged(
                        args.selectSourceData?.listCard?.get(
                            srcIndex
                        )?.cardSchemeId!!,
                        designId,
                        args.selectSourceData?.listCard?.get(srcIndex)?.cardNickname!!,
                        args.selectSourceData?.listCard?.get(srcIndex)?.vcnName!!,
                        sumUpSrcCardInfo
                    )
                }
                "valueConfirm" -> {

                    val data = BalanceGiftData(
                        designId,
                        args.selectDesignGiftData?.giftAmount!!,
                        args.selectDesignGiftData?.giftNumber!!
                    )
                    val action =
                        IssueCardSelectDesignFragmentDirections.selectdesignToSelectway(data)
                    findNavController().navigate(action)
                }
                "balanceByCodeValueConfirm" -> {
                    val designCard = DesignCard(designId, "0")
                    val action = IssueCardSelectDesignFragmentDirections.selectdesignToConfirm(
                        args.selectDesignGiftData,
                        designCard
                    )
                    findNavController().navigate(action)
                }
//                "balanceByCodeValueConfirm" ->
            }
        }

        return binding.root
    }


}