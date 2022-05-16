package com.lifecard.vpreca.ui.forgotpass

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentForgotPassBinding
import com.lifecard.vpreca.databinding.FragmentForgotPassCompleteBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CompleteForgotPassFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CompleteForgotPassFragment : Fragment() {

    private var _binding : FragmentForgotPassCompleteBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPassCompleteBinding.inflate(inflater, container, false)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() { backFunction() }
        })

        val btnComplete = binding.btnCompleteForgot
        btnComplete.setOnClickListener(View.OnClickListener { backFunction() })

        return binding.root
    }

    fun backFunction(){
//        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        }
//        startActivity(intent)
        findNavController().navigate(R.id.nav_login)
    }

}