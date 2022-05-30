package com.lifecard.vpreca.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.copyCardLockInverse
import com.lifecard.vpreca.databinding.FragmentHomeBinding
import com.lifecard.vpreca.ui.card.CardDetailBottomSheetDialog
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val homeViewModel: HomeViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var latestPage = 0

    private val pageChangeCallback = object : SimpleOnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            latestPage = position
            val buttonSlideLeft = binding.listCard.buttonSlideLeft
            val buttonSlideRight = binding.listCard.buttonSlideRight
            val buttonLock = binding.listCard.buttonLock
            val buttonInfo = binding.listCard.buttonInfo

            binding.listCard.cardList.adapter?.let {
                val adapter = it as CardSlidePagerAdapter
                val itemCount = adapter.itemCount
                if (itemCount == 1) {
                    buttonSlideLeft.visibility = View.INVISIBLE
                    buttonSlideRight.visibility = View.INVISIBLE
                } else if (position < 1) {
                    buttonSlideLeft.visibility = View.INVISIBLE
                    buttonSlideRight.visibility = View.VISIBLE
                } else if (position >= itemCount - 1) {
                    buttonSlideLeft.visibility = View.VISIBLE
                    buttonSlideRight.visibility = View.INVISIBLE
                } else {
                    buttonSlideLeft.visibility = View.VISIBLE
                    buttonSlideRight.visibility = View.VISIBLE
                }
                val currentCreditCard = adapter.getItem(position)
                binding.listCard.currentCard = currentCreditCard
                buttonInfo.setOnClickListener(View.OnClickListener {
                    CardDetailBottomSheetDialog(requireActivity(), currentCreditCard).show()
                })
                buttonLock.setOnClickListener(View.OnClickListener {
                    homeViewModel.inverseLockStatus(currentCreditCard, position)
                })
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textNoCard = binding.textNoCard
        val cardContainer = binding.listCard
        val viewPager = cardContainer.cardList
        val tabDots = cardContainer.tabDots
        var pagerAdapter: CardSlidePagerAdapter? = null
        val buttonSlideLeft = binding.listCard.buttonSlideLeft
        val buttonSlideRight = binding.listCard.buttonSlideRight
        val buttonSeeAllCard = binding.buttonSeeAllCard
        val textBalance = binding.textBalance
        val btnIssueCard = binding.buttonAddNewCard
        val btnBalance = binding.buttonCardNoBalance

        btnBalance.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_balance_amount_menu) })

        btnIssueCard.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_main) })

        buttonSeeAllCard.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_list_vpreca) })

        binding.textLastLogin.text =
            getString(R.string.home_text_last_login, SimpleDateFormat("yyyy M/d").format(Date()))

        binding.listCard.buttonUsage.setOnClickListener(View.OnClickListener {
//            findNavController().navigate(R.id.action_to_card_usage)
            val card = pagerAdapter?.getItem(viewPager.currentItem)
            card?.let {
                val action =
                    HomeFragmentDirections.actionToCardUsage(it)
                findNavController().navigate(action)
            }
        })


        buttonSlideLeft.setOnClickListener(View.OnClickListener {
            viewPager.setCurrentItem(max(viewPager.currentItem - 1, 0), true)
        })
        buttonSlideRight.setOnClickListener(View.OnClickListener {
            viewPager.adapter?.let {
                viewPager.setCurrentItem(
                    min(viewPager.currentItem + 1, it.itemCount - 1),
                    true
                )
            }

        })

        homeViewModel.creditCardResult.observe(viewLifecycleOwner, Observer { creditCardResult ->
            creditCardResult ?: return@Observer
            creditCardResult.success?.let {
                println("homeViewModel.creditCardResult.observe success: ${creditCardResult.success}")
                // The pager adapter, which provides the pages to the view pager widget.
                when (creditCardResult.success.size) {
                    0 -> {
                        textNoCard.visibility = View.VISIBLE
                        cardContainer.root.visibility = View.GONE
                    }
                    else -> {
                        textNoCard.visibility = View.GONE
                        cardContainer.root.visibility = View.VISIBLE
                        pagerAdapter =
                            CardSlidePagerAdapter(
                                requireActivity(),
                                ArrayList(creditCardResult.success)
                            )
                        viewPager.adapter = pagerAdapter
                        if (latestPage > 0 && latestPage < pagerAdapter!!.itemCount) {
                            viewPager.setCurrentItem(latestPage, false)
                        }

                        viewPager.registerOnPageChangeCallback(pageChangeCallback)

                        TabLayoutMediator(tabDots, viewPager) { tab, position ->

                        }.attach()

                        val sumBalance: Int = creditCardResult.success.sumOf {
                            try {
                                it.publishAmount.toInt()
                            } catch (e: Exception) {
                                0
                            }
                        }
                        textBalance.text = Converter.convertCurrency(sumBalance)
                    }
                }
            }
            creditCardResult.error?.let {
                println("homeViewModel.creditCardResult.observe err: ${getString(it.messageResId!!)}")
                //show dialog
                MaterialAlertDialogBuilder(requireContext()).apply {
                    setPositiveButton(R.string.button_ok, null)
                    setMessage(getString(it.messageResId))
                }.create().show()
            }
        })
        setLightStatusBar()

        binding.buttonAddNewCard.setOnClickListener(View.OnClickListener {
            fragmentFindNavController().navigate(R.id.nav_issue_card_main)
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unlockDrawer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.listCard.cardList.unregisterOnPageChangeCallback(pageChangeCallback)
        _binding = null
        clearLightStatusBar()

        lockDrawer()
    }

    private lateinit var lifecycleObserver: DefaultLifecycleObserver

    override fun onAttach(context: Context) {
        super.onAttach(context)
        lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                showToolbar()
                owner.lifecycle.removeObserver(lifecycleObserver)
            }
        }
        requireActivity().lifecycle.addObserver(lifecycleObserver)
    }

    private inner class CardSlidePagerAdapter(
        fa: FragmentActivity,
        val listCard: MutableList<CreditCard>
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = listCard.size

        override fun createFragment(position: Int): Fragment = CardSlideFragment(listCard[position])

        fun getItem(position: Int): CreditCard {
            return listCard[position]
        }

        fun updateCardAtIndex(card: CreditCard, index: Int) {
            try {
                listCard[index] = card
                notifyItemChanged(index)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}