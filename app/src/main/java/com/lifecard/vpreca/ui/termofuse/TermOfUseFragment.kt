package com.lifecard.vpreca.ui.termofuse

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lifecard.vpreca.LoginActivity
import com.lifecard.vpreca.R
import com.lifecard.vpreca.TermOfUseActivity
import com.lifecard.vpreca.databinding.FragmentConfirmPhoneBinding
import com.lifecard.vpreca.databinding.TermOfUseFragmentBinding
import com.lifecard.vpreca.ui.signup.PolicyAdapter
import com.lifecard.vpreca.ui.signup.TermOfUseAdapter
import com.lifecard.vpreca.utils.PreferenceHelper

class TermOfUseFragment : Fragment() {

    companion object {
        fun newInstance() = TermOfUseFragment()
    }
    private var _binding: TermOfUseFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: TermOfUseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TermOfUseFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TermOfUseViewModel::class.java)

        val rvTermOfUse = binding.rvTermOfUse
        val btnSubmit = binding.btnSubmitTermOfUse
        val cbTermOfUse = binding.cbTermOfUse

        val arrPolicy: ArrayList<String>
        arrPolicy = ArrayList()
        arrPolicy.add(getString(R.string.title_term_of_use))
        for (i in 0 until 12) {
            arrPolicy.add("利用規約內容。利用規約内容。利用規約內容。利用規 約內容。利用規約內容。利用規約內容。利用規約內 容。利用規約內容。")
        }

        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        val adapter = TermOfUseAdapter(arrPolicy)
        rvTermOfUse?.layoutManager = linearLayoutManager
        rvTermOfUse?.adapter = adapter

        cbTermOfUse.setOnClickListener(View.OnClickListener {
            context?.let { it1 -> PreferenceHelper.setAcceptTermOfUseFirstTime(appContext = it1, value = true) }
            btnSubmit.isEnabled = cbTermOfUse.isChecked
        })

        btnSubmit.setOnClickListener(View.OnClickListener {
            context?.let { it1 -> PreferenceHelper.setAcceptTermOfUseFirstTime(appContext = it1, value = true) }
            val intent = Intent(activity, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        })
        // TODO: Use the ViewModel
    }

}