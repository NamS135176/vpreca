package com.lifecard.vpreca.ui.balance_amount

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentBalanceAmountMenuBinding
import com.lifecard.vpreca.databinding.FragmentBalanceByCodeInputBinding
import org.joda.time.convert.Converter

class BalanceByCodeInputFragment : Fragment() {

    companion object {
        fun newInstance() = BalanceByCodeInputFragment()
    }

    private lateinit var viewModel: BalanceByCodeInputViewModel
    private var _binding: FragmentBalanceByCodeInputBinding? = null
    private val binding get() = _binding!!
    private val args: BalanceByCodeInputFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(BalanceByCodeInputViewModel::class.java)
        _binding = FragmentBalanceByCodeInputBinding.inflate(inflater, container, false)
        val totalAmount = binding.tvTotalAmount
        val giftCodeLayout = binding.issueCardByCodeInputLayout
        val giftCodeEdt = binding.issueCardByCodeInputCode
        val btnSubmit = binding.btnSubmitPolicy
        val btnBack = binding.appbarSignup.btnBack

        val fakeBalanceAmount = args.balanceTotalRemain?.balanceAmount?.toInt()!!

        totalAmount.text = com.lifecard.vpreca.utils.Converter.convertCurrency(fakeBalanceAmount)

        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_balance_amount_menu) })
        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_balance_amount_menu)
            }
        })
        viewModel.validForm.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { signupFormState ->
                if (giftCodeEdt.text.toString() == "" ) {
                    btnSubmit.isEnabled = false
                } else {
                    btnSubmit.isEnabled =
                        signupFormState.giftCodeError == null
                }
            })

        viewModel.giftCodeError.observe(viewLifecycleOwner, Observer { error: Int? ->
            giftCodeLayout.error = try {
                error?.let { getString(error) }
            } catch (e: Error) {
                null
            } })
        giftCodeEdt.doAfterTextChanged { text -> viewModel.giftCodeDataChanged(text = text.toString()) }

        btnSubmit.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_balance_value_confirm) })
        return binding.root
    }

}