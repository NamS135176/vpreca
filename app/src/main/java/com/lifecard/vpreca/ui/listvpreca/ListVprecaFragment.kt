package com.lifecard.vpreca.ui.listvpreca

import android.R
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.lifecard.vpreca.data.model.VprecaCard
import com.lifecard.vpreca.databinding.FragmentListVprecaBinding
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

        val btnBack = binding.appbarListVpreca.btnBack
        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(com.lifecard.vpreca.R.id.nav_home) })

        val listVpreca = binding.listVprecaCard
        val arrPolicy: ArrayList<VprecaCard>
        arrPolicy = ArrayList()
        for (i in 0 until 12) {
            if(i % 3 == 0){
                arrPolicy.add(VprecaCard("#000000","1",((i+1) * 10000).toString()))
            }
            else{
                arrPolicy.add(VprecaCard("#000000","0",((i+1) * 10000).toString()))
            }
        }
        val layoutManager = GridLayoutManager(context, 2)

        // Create a custom SpanSizeLookup where the first item spans both columns

        // Create a custom SpanSizeLookup where the first item spans both columns
//        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                return if (position == 0) 2 else 1
//            }
//        }
        listVpreca.layoutManager = layoutManager
//        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.)
        listVpreca.addItemDecoration(GridDecoration(2, 0, false, 0))
        listVpreca.adapter = ListVprecaAdapter(arrPolicy)
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