package com.lifecard.vpreca.ui.issuecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.data.model.DesignCard
import com.lifecard.vpreca.databinding.FragmentIssueCardSelectDesignBinding


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

        val rcView = binding.rvView

        val list:List<DesignCard>
        list = ArrayList<DesignCard>()

        for (i in 0 until 8) {
            list.add(DesignCard(i.toString()))
        }

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        rcView.layoutManager = layoutManager
        val adapter = IssueCardSelectDesignAdapter(list)
        adapter.setOnClickListener(object : IssueCardSelectDesignAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
            }

        })
        rcView.adapter = adapter
//        rcView.addItemDecoration(CirclePagerIndicatorDecoration())
        return binding.root
    }


}