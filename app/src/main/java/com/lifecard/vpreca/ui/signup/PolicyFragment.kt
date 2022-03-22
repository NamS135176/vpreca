package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.R



class PolicyFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_policy, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnSubmitPolicy = view.findViewById<Button>(R.id.btn_submit_policy)
        val cbPolicy = view.findViewById<CheckBox>(R.id.cbPolicy)
        val rcPolicy = view?.findViewById<RecyclerView>(R.id.svPolicy)

        cbPolicy.setOnClickListener(View.OnClickListener {
            btnSubmitPolicy.isEnabled = cbPolicy.isChecked
        })

        btnSubmitPolicy.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.nav_phone)
        })


        val arrPolicy: ArrayList<String>
        arrPolicy = ArrayList()
        for (i in 0 until 12) {
            arrPolicy.add("私は、暴力団などの反社会的勢力や、反社会的勢力の関係者ではないことを表明します。")
        }
        Log.e("months",arrPolicy.toString())
        val linearLayoutManager:LinearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        val adapter = PolicyAdapter(arrPolicy)
        rcPolicy?.layoutManager = linearLayoutManager
        rcPolicy?.adapter = adapter
    }



}