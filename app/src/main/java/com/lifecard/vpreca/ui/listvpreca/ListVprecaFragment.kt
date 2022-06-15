package com.lifecard.vpreca.ui.listvpreca

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.databinding.FragmentListVprecaBinding
import com.lifecard.vpreca.eventbus.ReloadCard
import com.lifecard.vpreca.ui.card.CardBottomSheetCustom
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@AndroidEntryPoint
class ListVprecaFragment : Fragment() {
    @Inject
    lateinit var creditCardRepository: CreditCardRepository

    companion object {
        fun newInstance() = ListVprecaFragment()
    }

    private val listVprecaViewModel: ListVprecaViewModel by viewModels()
    private var _binding: FragmentListVprecaBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListVprecaBinding.inflate(inflater, container, false)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_list_to_home)
            }
        })
        val btnBack = binding.appbarListVpreca.btnBack
        btnBack.setOnClickListener(View.OnClickListener {  findNavController().navigate(R.id.action_list_to_home) })
        val tvTotalAmount = binding.tvTotalAmount
        val listVpreca = binding.listVprecaCard
        val loading = binding.loading
        listVprecaViewModel.creditCardResult.observe(
            viewLifecycleOwner,
            Observer { creditCardResult ->
                creditCardResult ?: return@Observer
                creditCardResult.success?.let {
                    println("homeViewModel.creditCardResult.observe success: ${creditCardResult.success}")
                    // The pager adapter, which provides the pages to the view pager widget.
                    when (creditCardResult.success.size) {
                        0 -> {
                        }
                        else -> {
                            val arrPolicy: List<CreditCard>
                            arrPolicy = creditCardResult.success
                            val layoutManager = GridLayoutManager(context, 2)

                            listVpreca.layoutManager = layoutManager
                            listVpreca.addItemDecoration(GridDecoration(2, 0, false, 0))
                            val adapter = ListVprecaAdapter(arrPolicy)
                            listVpreca.adapter = adapter

                            adapter.setOnClickListener(object :
                                ListVprecaAdapter.OnItemClickListener {
                                override fun onItemClick(position: Int) {
                                    listVprecaViewModel.creditCardSelectDataChanged(arrPolicy[position])
                                }

                            })

                            val sumBalance: Int = creditCardResult.success.sumOf {
                                try {
                                    it.publishAmount.toInt()
                                } catch (e: Exception) {
                                    0
                                }
                            }
                            tvTotalAmount.text = Converter.convertCurrency(sumBalance)
                        }
                    }
                }
                creditCardResult.error?.messageResId?.let { showPopupMessage(message = getString(it)) }
                creditCardResult.error?.errorMessage?.let { showPopupMessage(message = it) }
                creditCardResult.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
            })

        listVprecaViewModel.cardInfoResult.observe(
            viewLifecycleOwner,
            Observer { cardInfoResult ->
                cardInfoResult ?: return@Observer
                cardInfoResult.error?.messageResId?.let { showPopupMessage(message = getString(it)) }
                cardInfoResult.error?.errorMessage?.let { showPopupMessage(message = it) }
                cardInfoResult.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
                cardInfoResult.success?.let {
                    println("homeViewModel.cardInfoResult.observe success: ${cardInfoResult.success}")

                    CardBottomSheetCustom(
                        requireActivity(),
                        cardInfoResult.success,
                        creditCardRepository
                    ).show()
                }

            })

        listVprecaViewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> loading.visibility = View.VISIBLE
                else -> loading.visibility = View.GONE
            }
        })
        return binding.root
    }

    @Subscribe
    fun handleReloadCard(event:ReloadCard){
        listVprecaViewModel.getListCard()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        EventBus.getDefault().register(this)
        hideToolbar()
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
        showToolbar()
    }
}