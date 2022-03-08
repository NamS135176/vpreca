package com.lifecard.vpreca.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.databinding.FragmentHomeBinding
import com.lifecard.vpreca.ui.login.LoginViewModel
import com.lifecard.vpreca.ui.login.LoginViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this, HomeViewModelFactory())
                .get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textNoCard = binding.textNoCard
        val cardContainer = binding.listCard
        val viewPager = cardContainer.cardList
        val tabDots = cardContainer.tabDots

        binding.textLastLogin.text =
            getString(R.string.home_text_last_login, SimpleDateFormat("yyyy M/d").format(Date()))

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
                        val pagerAdapter = CardSlidePagerAdapter(requireActivity(), creditCardResult.success)
                        viewPager.adapter = pagerAdapter

                        TabLayoutMediator(tabDots, viewPager) { tab, position ->

                        }.attach()
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
        _binding = null
    }

    private inner class CardSlidePagerAdapter(fa: FragmentActivity, private val listCard: List<CreditCard>) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = listCard.size

        override fun createFragment(position: Int): Fragment = CardSlideFragment(listCard[position])
    }
}