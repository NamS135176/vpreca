package com.lifecard.vpreca.ui.issuecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.DesignCard
import com.lifecard.vpreca.databinding.FragmentIssueCardSelectDesignBinding
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator


class IssueCardSelectDesignFragment : Fragment() {

    companion object {
        fun newInstance() = IssueCardSelectDesignFragment()
    }

    private lateinit var viewModel: IssueCardSelectDesignViewModel
    private var _binding: FragmentIssueCardSelectDesignBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIssueCardSelectDesignBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(IssueCardSelectDesignViewModel::class.java)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_issue_card_select_source)
            }
        })

        val btnBack = binding.appbarGiftThird.btnBack
        val btnCancel = binding.appbarGiftThird.cancelBtn
        val rcView = binding.rvView
        val bigCard = binding.cardZone.bigCard
        val btnSubmit = binding.btnSubmitIntroduceFirst

        val list: List<DesignCard>
        list = ArrayList<DesignCard>()
        val cardLayout = binding.cardZone

        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_select_source) })
        btnCancel.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_main) })
        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_complete) })

        cardLayout.card = DesignCard("0", "0")
        for (i in 0 until 7) {
            if (i == 0) {
                list.add(DesignCard(i.toString(), "1"))
            } else {
                list.add(DesignCard(i.toString(), "0"))
            }
        }

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        rcView.layoutManager = layoutManager

        val adapter = IssueCardSelectDesignAdapter(list)
        adapter.setOnClickListener(object : IssueCardSelectDesignAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (list[position].isSelected != "1") {
                    for (i in 0 until 7) {
                        if (i == position) {
                            list[i].isSelected = "1"
                        } else {
                            list[i].isSelected = "0"
                        }
                    }
                    when (position) {
                        0 -> {
                            bigCard.setBackgroundResource(R.drawable.bg_first)
                            cardLayout.card = DesignCard("0", "0")
                        }
                        1 -> {
                            bigCard.setBackgroundResource(R.drawable.bg_second)
                            cardLayout.card = DesignCard("1", "0")
                        }
                        2 -> {
                            bigCard.setBackgroundResource(R.drawable.bg_third)
                            cardLayout.card = DesignCard("1", "0")
                        }
                        3 -> {
                            bigCard.setBackgroundResource(R.drawable.bg_fourth)
                            cardLayout.card = DesignCard("1", "0")
                        }
                        4 -> {
                            bigCard.setBackgroundResource(R.drawable.bg_fifth)
                            cardLayout.card = DesignCard("1", "0")
                        }
                        5 -> {
                            bigCard.setBackgroundResource(R.drawable.bg_six)
                            cardLayout.card = DesignCard("1", "0")
                        }
                        6 -> {
                            bigCard.setBackgroundResource(R.drawable.bg_seven)
                            cardLayout.card = DesignCard("1", "0")
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

            }

        })
        rcView.adapter = adapter
//        rcView.addItemDecoration(PagerDecorator())
        val recyclerIndicator: ScrollingPagerIndicator = binding.indicator
        recyclerIndicator.attachToRecyclerView(rcView)
        return binding.root
    }


}