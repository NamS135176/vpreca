package com.lifecard.vpreca.ui.issuecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.*
import com.lifecard.vpreca.databinding.FragmentIssueCardSelectDesignBinding
import com.lifecard.vpreca.ui.balance_amount.BalanceAmountMenuFragmentDirections
import com.lifecard.vpreca.ui.introduce.GiftCardConfirmFragmentArgs
import com.lifecard.vpreca.utils.showInternetTrouble
import com.lifecard.vpreca.utils.showPopupMessage
import dagger.hilt.android.AndroidEntryPoint
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator

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
    ): View? {
        _binding = FragmentIssueCardSelectDesignBinding.inflate(inflater, container, false)
//        viewModel = ViewModelProvider(this).get(IssueCardSelectDesignViewModel::class.java)
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (args.selectDesignData?.preRoute) {
                        "selectSource" -> findNavController().navigate(R.id.nav_issue_card_select_source)
                        "valueConfirm" -> {
                            MaterialAlertDialogBuilder(requireContext()).apply {
                                setPositiveButton("はい") { dialog, which ->
                                    // do something on positive button click
                                    findNavController().navigate(R.id.nav_issue_card_main)
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
        val bigCard = binding.cardZone.bigCard
        val btnSubmit = binding.btnSubmitIntroduceFirst
        var designId = "001"
        val loading = binding.loading
        val recyclerIndicator = binding.indicator

        val cardLayout = binding.cardZone
        when (args.selectDesignData?.preRoute) {
            "selectSource" -> {
                btnBack.visibility = View.VISIBLE
                btnCancel.visibility = View.VISIBLE
                btnSubmit.text = getString(R.string.issue_card_select_design_btn)
            }
            "valueConfirm" -> {
                btnBack.visibility = View.GONE
                btnCancel.visibility = View.VISIBLE
                btnSubmit.text = getString(R.string.issue_card_select_design_btn)
            }
            "balanceByCodeValueConfirm" -> {
                btnBack.visibility = View.VISIBLE
                btnCancel.visibility = View.GONE
                btnSubmit.text = getString(R.string.btn_next_policy)
            }
        }
        btnBack.setOnClickListener(View.OnClickListener {
            if (args.selectDesignData?.preRoute == "selectSource") {
                findNavController().navigate(R.id.nav_issue_card_select_source)
            } else if (args.selectDesignData?.preRoute == "valueConfirm") {
                findNavController().navigate(R.id.nav_balance_value_confirm)
            } else {
                val data = args.selectDesignGiftData
                val action =
                    IssueCardSelectDesignFragmentDirections.selectdesignToValueConfirm(
                        data
                    )
                findNavController().navigate(action)
            }
        })

        btnCancel.setOnClickListener(View.OnClickListener {
            when (args.selectDesignData?.preRoute) {
                "selectSource" -> findNavController().navigate(R.id.nav_issue_card_main)
                "valueConfirm" -> {
                    MaterialAlertDialogBuilder(requireContext()).apply {
                        setPositiveButton("はい") { dialog, which ->
                            // do something on positive button click
                            findNavController().navigate(R.id.nav_issue_card_main)
                        }
                        setNegativeButton("いいえ", null)
                        setMessage(
                            "操作途中ですがキャンセル\n" +
                                    "してもよろしいですか？"
                        )
                    }.create().show()
                }
            }
        })

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

        viewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> loading.visibility = View.VISIBLE
                else -> loading.visibility = View.GONE
            }
        })

        viewModel.listDesignResult.observe(
            viewLifecycleOwner,
            Observer { listDesignResult ->
                listDesignResult ?: return@Observer
                listDesignResult.success?.let {
                    val list: List<DesignCard>
                    list = ArrayList<DesignCard>()
                    cardLayout.card = DesignCard("001", "0")
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
                                for (i in 0 until list.size) {
                                    if (i == position) {
                                        list[i].isSelected = "1"
                                    } else {
                                        list[i].isSelected = "0"
                                    }
                                }
                                when (list[position].designId) {
                                    "001" -> {
                                        bigCard.setBackgroundResource(R.drawable.bg_first)
                                        cardLayout.card = DesignCard(list[position].designId, "0")
                                        designId = "001"
                                    }
                                    "002" -> {
                                        bigCard.setBackgroundResource(R.drawable.bg_second)
                                        cardLayout.card = DesignCard(list[position].designId, "0")
                                        designId = "002"
                                    }
                                    "003" -> {
                                        bigCard.setBackgroundResource(R.drawable.bg_third)
                                        cardLayout.card = DesignCard(list[position].designId, "0")
                                        designId = "003"
                                    }
                                    "004" -> {
                                        bigCard.setBackgroundResource(R.drawable.bg_fourth)
                                        cardLayout.card = DesignCard(list[position].designId, "0")
                                        designId = "004"
                                    }
                                    "005" -> {
                                        bigCard.setBackgroundResource(R.drawable.bg_fifth)
                                        cardLayout.card = DesignCard(list[position].designId, "0")
                                        designId = "005"
                                    }
                                    "006" -> {
                                        bigCard.setBackgroundResource(R.drawable.bg_six)
                                        cardLayout.card = DesignCard(list[position].designId, "0")
                                        designId = "006"
                                    }
                                    "007" -> {
                                        bigCard.setBackgroundResource(R.drawable.bg_seven)
                                        cardLayout.card = DesignCard(list[position].designId, "0")
                                        designId = "007"
                                    }
                                    else -> {
                                        bigCard.setBackgroundResource(R.drawable.bg_seven)
                                        cardLayout.card = DesignCard("007", "0")
                                        designId = "007"
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
                    }
                }
            })

        btnSubmit.setOnClickListener(View.OnClickListener {
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
                        args.selectSourceData?.listCard?.get(srcIndex)?.precaNumber!!,
                        args.selectSourceData?.listCard?.get(srcIndex)?.vcn!!,
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
        })

        return binding.root
    }


}