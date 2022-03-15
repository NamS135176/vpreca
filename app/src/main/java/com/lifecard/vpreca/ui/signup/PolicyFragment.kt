package com.lifecard.vpreca.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PolicyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PolicyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
            val phoneFragment = PhoneFragment()
            val manager = parentFragmentManager
            val transaction = manager.beginTransaction()
            transaction.replace(R.id.fmSignup, phoneFragment)
            transaction.addToBackStack(null)
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_from_left)
            transaction.commit()
        })


        var arrPolicy: ArrayList<String>
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PolicyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PolicyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}