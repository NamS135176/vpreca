package com.lifecard.vpreca.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CreditCard
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
        when (card.designId) {
            "001" -> binding.cardInfo.setBackgroundResource(R.drawable.bg_first)
            "002" -> binding.cardInfo.setBackgroundResource(R.drawable.bg_second)
            "003" -> binding.cardInfo.setBackgroundResource(R.drawable.bg_third)
            "004" -> binding.cardInfo.setBackgroundResource(R.drawable.bg_fourth)
            "005" -> binding.cardInfo.setBackgroundResource(R.drawable.bg_fifth)
            "006" -> binding.cardInfo.setBackgroundResource(R.drawable.bg_six)
            "007" -> binding.cardInfo.setBackgroundResource(R.drawable.bg_seven)
        }
        val root: View = binding.root
        binding.card = card
        return root;
    }
}