package com.lifecard.vpreca.ui.listvpreca

import android.R
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.ColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.VprecaCard
import com.lifecard.vpreca.databinding.CardDetailLayoutBinding
import com.lifecard.vpreca.databinding.FragmentListVprecaBinding
import com.lifecard.vpreca.ui.home.HomeViewModel
import com.lifecard.vpreca.utils.Converter
import com.lifecard.vpreca.utils.hideToolbar
import com.lifecard.vpreca.utils.showToolbar
import dagger.hilt.android.AndroidEntryPoint
import showCustomToast

@AndroidEntryPoint
class ListVprecaFragment : Fragment() {

    companion object {
        fun newInstance() = ListVprecaFragment()
    }

    //    private val viewModel: HomeViewModel by viewModels()
    private val listVprecaViewModel: ListVprecaViewModel by viewModels()
    private var _binding: FragmentListVprecaBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListVprecaBinding.inflate(inflater, container, false)

        val btnBack = binding.appbarListVpreca.btnBack
        btnBack.setOnClickListener(View.OnClickListener { findNavController().navigate(com.lifecard.vpreca.R.id.nav_home) })
        val tvTotalAmount = binding.tvTotalAmount
        val listVpreca = binding.listVprecaCard
        val loading = binding.loading
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(com.lifecard.vpreca.R.id.nav_home)
            }
        })
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
                                    // on below line we are creating a new bottom sheet dialog.
                                    val dialog = BottomSheetDialog(requireContext())

                                    val bindingDialog =
                                        CardDetailLayoutBinding.inflate(inflater, container, false)
                                    dialog.setContentView(bindingDialog.root)
                                    dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
                                    bindingDialog.card = arrPolicy[position]
                                    val card = bindingDialog.cardZone
                                    val btnBack = bindingDialog.btnBack
                                    val btnCopy = bindingDialog.buttonCopy
                                    val btnUsage = bindingDialog.buttonUsage
                                    val btnLock = bindingDialog.buttonLock
                                    val btnRefresh = bindingDialog.buttonReload
                                    var currentLock = arrPolicy[position].vcnSecurityLockFlg

                                    btnRefresh.setOnClickListener(View.OnClickListener {
                                        MaterialAlertDialogBuilder(requireContext()).apply {
                                            setPositiveButton("はい") { dialog, which ->
                                                card.card = arrPolicy[position]
                                                bindingDialog.card = arrPolicy[position]
                                                Toast(context).showCustomToast(
                                                    "再発行しました",
                                                    requireActivity()
                                                )
                                            }
                                            setNegativeButton("いいえ", null)
                                            setMessage("カードを再発行しますよろしいですか？")
                                        }.create().show()
                                    })

                                    btnLock.setOnClickListener(View.OnClickListener {
                                        var newCard:CreditCard
                                        if(currentLock == "1"){
                                            currentLock = "0"
                                            newCard = arrPolicy[position].copy(vcnSecurityLockFlg = "0", cardImageFile = "", cardUnusableDate = "", thumbnailCardImageFile = "")
                                            Toast(context).showCustomToast(
                                                "ロックを解除しました",
                                                requireActivity()
                                            )
                                        }
                                        else{
                                            currentLock = "1"
                                            newCard = arrPolicy[position].copy(vcnSecurityLockFlg = "1", cardImageFile = "", cardUnusableDate = "", thumbnailCardImageFile = "")
                                            Toast(context).showCustomToast(
                                                "ロックしました",
                                                requireActivity()
                                            )
                                        }
                                        card.card = newCard
                                        bindingDialog.card = newCard

                                    })

                                    btnUsage.setOnClickListener(View.OnClickListener {
                                        dialog.dismiss()
                                        val action =
                                            ListVprecaFragmentDirections.actionToCardUsage(
                                                arrPolicy[position]
                                            )
                                        findNavController().navigate(action)
                                    })

                                    btnCopy.setOnClickListener(View.OnClickListener {
                                        if(currentLock == "1"){
                                            MaterialAlertDialogBuilder(requireContext()).apply {
                                                setMessage("ロックを解除してから\n" + "コピーしてください")
                                                setNegativeButton("ok", null)
                                            }.create().show()
                                        }
                                        else{
                                            val clipboardManager =
                                                context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clipData = ClipData.newPlainText(
                                                "text",
                                                arrPolicy[position].precaNumber
                                            )
                                            clipboardManager.setPrimaryClip(clipData)
                                            Toast(context).showCustomToast(
                                                "コピーしました！",
                                                requireActivity()
                                            )
                                        }
                                    })

                                    btnBack.setOnClickListener(View.OnClickListener { dialog.dismiss() })
                                    card.card = arrPolicy[position]
//                                    dialog.setCancelable(false)
                                    dialog.show()
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
                creditCardResult.error?.let {

                    MaterialAlertDialogBuilder(requireContext()).apply {
                        setPositiveButton("ok", null)
                        setMessage(getString(it.messageResId))
                    }.create().show()
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


    override fun onAttach(context: Context) {
        super.onAttach(context)
        hideToolbar()
    }

    override fun onDetach() {
        super.onDetach()
        showToolbar()
    }
}