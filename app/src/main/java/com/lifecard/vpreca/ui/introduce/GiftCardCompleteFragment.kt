package com.lifecard.vpreca.ui.introduce

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentChangePassCompleteBinding
import com.lifecard.vpreca.databinding.FragmentGiftCardCompleteBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GiftCardCompleteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GiftCardCompleteFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentGiftCardCompleteBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGiftCardCompleteBinding.inflate(inflater, container, false)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_gift_complete_to_home)
            }
        })

        val btnComplete = binding.btnSubmitPolicy
        btnComplete.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_gift_complete_to_home)
        })
        return binding.root
    }


}