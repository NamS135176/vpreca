package com.lifecard.vpreca.ui.card_usage

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.databinding.FragmentCardUsageBinding
import com.lifecard.vpreca.databinding.FragmentLoginBinding
import com.lifecard.vpreca.ui.login.LoginViewModel
import com.lifecard.vpreca.utils.Converter
import com.lifecard.vpreca.utils.hideToolbar
import com.lifecard.vpreca.utils.showToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CardUsageFragment : Fragment() {

    companion object {
        fun newInstance() = CardUsageFragment()
    }

    private val viewModel: CardUsageViewModel by viewModels()
    private var _binding: FragmentCardUsageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCardUsageBinding.inflate(inflater, container, false)
        val listCardUsageHistory = binding.listCardUsageHistory
        val loading = binding.loading
        val btnBack = binding.appbarCardUsage.btnBack

        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_home) })

        viewModel.cardUsageHistoryResult.observe(viewLifecycleOwner, Observer {
            if (it is Result.Success) {
                //display list
                listCardUsageHistory.adapter = CardUsageHistoryAdapter(it.data)
                val dividerItemDecoration = DividerItemDecoration(
                    context,
                    LinearLayoutCompat.VERTICAL
                )
                listCardUsageHistory.addItemDecoration(dividerItemDecoration)
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> loading.visibility = View.VISIBLE
                else -> loading.visibility = View.GONE
            }
        })

        val card = arguments?.getParcelable<CreditCard>("card")
        card?.let {
            binding.cardNo.text = it.precaNumber
            binding.balance.text = Converter.convertCurrency(it.chargeBalance)
            viewModel.getCardUsageHistory(card)
        }
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        hideToolbar()
    }

    override fun onDetach() {
        super.onDetach()
        showToolbar()
    }
}