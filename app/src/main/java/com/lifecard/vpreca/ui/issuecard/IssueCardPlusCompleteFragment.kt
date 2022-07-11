package com.lifecard.vpreca.ui.issuecard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentIssueCardPlusCompleteBinding
import com.lifecard.vpreca.eventbus.ReloadCard
import org.greenrobot.eventbus.EventBus


class IssueCardPlusCompleteFragment : Fragment() {
    private var _binding: FragmentIssueCardPlusCompleteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIssueCardPlusCompleteBinding.inflate(inflater, container, false)
        val btnComplete = binding.btnSubmitPolicy
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.nav_home, inclusive = false)
                    EventBus.getDefault().post(ReloadCard())
                }
            })
        btnComplete.setOnClickListener {
            findNavController().popBackStack(R.id.nav_home, inclusive = false)
            EventBus.getDefault().post(ReloadCard())
        }
        return binding.root
    }


}