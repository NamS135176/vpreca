package com.lifecard.vpreca.ui.issuecard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.GiftCardConfirmData
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeInputCodeBinding
import com.lifecard.vpreca.databinding.FragmentIssueCardByCodeValueConfirmBinding
import com.lifecard.vpreca.utils.Converter

class IssueCardByCodeValueConfirm : Fragment() {
    private var _binding: FragmentIssueCardByCodeValueConfirmBinding? = null
    private val binding get() = _binding!!
    private val args: IssueCardByCodeValueConfirmArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIssueCardByCodeValueConfirmBinding.inflate(inflater, container, false)
        val btnCancel = binding.appbarSignup.cancelBtn
        val btnSubmit = binding.btnSubmitPolicy
        val tvTotalAmount = binding.tvTotalAmount

        tvTotalAmount.text = Converter.convertCurrency(args.giftData?.giftAmount)

        btnSubmit.setOnClickListener(View.OnClickListener {
            val giftCardConfirmData = GiftCardConfirmData("valueConfirm")
            val action = IssueCardByCodeValueConfirmDirections.actionValueconfirmToSelectdesign(giftCardConfirmData, args.giftData)
            findNavController().navigate(action)
        })

        btnCancel.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_by_code_input) })

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.nav_issue_card_by_code_input)
            }
        })
        // Inflate the layout for this fragment
        return binding.root
    }

}