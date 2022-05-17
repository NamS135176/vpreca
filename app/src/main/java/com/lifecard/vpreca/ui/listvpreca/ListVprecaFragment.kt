package com.lifecard.vpreca.ui.listvpreca

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.FragmentListVprecaBinding
import com.lifecard.vpreca.databinding.IntroduceFragmentFirstFragmentBinding
import com.lifecard.vpreca.utils.hideToolbar
import com.lifecard.vpreca.utils.showToolbar

class ListVprecaFragment : Fragment() {

    companion object {
        fun newInstance() = ListVprecaFragment()
    }

    private lateinit var viewModel: ListVprecaViewModel
    private var _binding: FragmentListVprecaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListVprecaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListVprecaViewModel::class.java)
        // TODO: Use the ViewModel
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