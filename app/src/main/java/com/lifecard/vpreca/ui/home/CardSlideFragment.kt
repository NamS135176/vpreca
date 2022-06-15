package com.lifecard.vpreca.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.getBackgroundCard
import com.lifecard.vpreca.databinding.FragmentHomeBinding
import com.lifecard.vpreca.databinding.HomeCardContainerBinding
import com.lifecard.vpreca.databinding.HomeCardItemBinding

class CardSlideFragment(private val card: CreditCard) : Fragment() {
    private var _binding: HomeCardItemBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeCardItemBinding.inflate(inflater, container, false)
        binding.cardInclude.cardInfo.setBackgroundResource(card.getBackgroundCard())
        val root: View = binding.root
        binding.card = card
        binding.cardInclude.card = card
        return root;
    }
}