package com.lifecard.vpreca.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.tabs.TabLayoutMediator
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.databinding.FragmentHomeBinding
import com.lifecard.vpreca.utils.Converter
import com.lifecard.vpreca.utils.SimpleOnPageChangeCallback
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val homeViewModel: HomeViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val pageChangeCallback = object : SimpleOnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            val buttonSlideLeft = binding.listCard.buttonSlideLeft
            val buttonSlideRight = binding.listCard.buttonSlideRight
            val buttonLock = binding.listCard.buttonLock

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
        val textBalance = binding.textBalance

        viewPager.apply {
            clipToPadding = false   // allow full width shown with padding
            clipChildren = false    // allow left/right item is not clipped
            offscreenPageLimit = 2
        }
        // increase this offset to show more of left/right
        val offsetPx = 50
        viewPager.setPadding(offsetPx, 0, offsetPx, 0)

// increase this offset to increase distance between 2 items
        val pageMarginPx = 20
        val marginTransformer = MarginPageTransformer(pageMarginPx)
        viewPager.setPageTransformer(marginTransformer)

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
                            CardSlidePagerAdapter(requireActivity(), creditCardResult.success)
                        viewPager.adapter = pagerAdapter

                        viewPager.registerOnPageChangeCallback(pageChangeCallback)

                        TabLayoutMediator(tabDots, viewPager) { tab, position ->

                        }.attach()

                        val sumBalance: Int = creditCardResult.success.sumOf {
                            try {
                                it.chargeBalance.toInt()
                            } catch (e: Exception) {
                                0
                            }
                        }
                        textBalance.text = Converter.convertCurrency(sumBalance)
                    }
                }

            }
            creditCardResult.error?.let {
                println("homeViewModel.creditCardResult.observe err: ${getString(creditCardResult.error)}")
            }
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.listCard.cardList.unregisterOnPageChangeCallback(pageChangeCallback)
        _binding = null
    }

    private inner class CardSlidePagerAdapter(
        fa: FragmentActivity,
        val listCard: List<CreditCard>
    ) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = listCard.size

        override fun createFragment(position: Int): Fragment = CardSlideFragment(listCard[position])

        fun getItem(position: Int): CreditCard {
            return listCard[position]
        }
    }
}