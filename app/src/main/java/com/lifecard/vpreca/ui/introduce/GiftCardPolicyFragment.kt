package com.lifecard.vpreca.ui.introduce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentGiftCardPolicyBinding


/**
 * A simple [Fragment] subclass.
 * Use the [GiftCardPolicyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GiftCardPolicyFragment : Fragment() {
    private var _binding: FragmentGiftCardPolicyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentGiftCardPolicyBinding.inflate(inflater, container, false)

        val btnBack = binding.appbarGift.btnBack
        val checkbox = binding.cbIntroduceFirst
        val btnSubmit = binding.btnSubmitIntroduceFirst
        btnBack.setOnClickListener(View.OnClickListener {
            findNavController().popBackStack()
        })
        checkbox.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { _, isChecked ->
            btnSubmit.isEnabled = isChecked
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_gift_card_input_card)
        })
        return binding.root
    }
}