package com.lifecard.vpreca.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.CreditCardRepository
import com.lifecard.vpreca.data.Result
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.databinding.FragmentHomeBinding
import com.lifecard.vpreca.eventbus.ReloadCard
import com.lifecard.vpreca.exception.ApiException
import com.lifecard.vpreca.exception.NoConnectivityException
import com.lifecard.vpreca.ui.card.CardBottomSheetCustom
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.max
import kotlin.math.min


@AndroidEntryPoint
class HomeFragment : Fragment(), CoroutineScope {
    @Inject
    lateinit var creditCardRepository: CreditCardRepository

    @Inject
    lateinit var userManager: UserManager

    private var _binding: FragmentHomeBinding? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    private val homeViewModel: HomeViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var latestPage = 0
    private var forceReloadCard: Boolean = false

    private val pageChangeCallback = object : SimpleOnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            latestPage = position
            val buttonSlideLeft = binding.listCard.buttonSlideLeft
            val buttonSlideRight = binding.listCard.buttonSlideRight
            val buttonLock = binding.listCard.buttonLock
            val buttonInfo = binding.listCard.buttonInfo
            val buttonReload = binding.listCard.buttonReload

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
                var currentCreditCard = adapter.getItem(position)
                binding.listCard.currentCard = currentCreditCard
                buttonInfo.setOnClickListener(View.OnClickListener {
                    homeViewModel.creditCardSelectDataChanged(currentCreditCard)
                })
                buttonLock.setOnClickListener(View.OnClickListener {
                    launch {
                        currentCreditCard = currentCreditCard.copyCardLockInverse()
                        val res = creditCardRepository.updateCard(currentCreditCard)
                        if (res is Result.Success) {
                            homeViewModel.updateCard(currentCreditCard, position)
                            val toastMessage = when (currentCreditCard.isCardLock()) {
                                true -> "ロックしました"
                                else -> "ロックを解除しました"
                            }
                            Toast(context).showCustomToast(
                                message = toastMessage,
                                requireActivity()
                            )
                        } else if (res is Result.Error) {
                            when (res.exception) {
                                is NoConnectivityException -> showInternetTrouble()
                                is ApiException -> showPopupMessage("", res.exception.message)
                                else -> showPopupMessage(
                                    "",
                                    getString(R.string.get_list_card_failure)
                                )

                            }
                        }
                    }
                })
                buttonReload.setOnClickListener(View.OnClickListener {
                    if (!adapter.getItem(position).isCardLock() && adapter.getItem(position)
                            .isAvailable()
                    ) {
                        MaterialAlertDialogBuilder(requireContext()).apply {
                            setPositiveButton("はい") { _, _ ->

                                launch {
                                    val res = creditCardRepository.republishCard(currentCreditCard)
                                    if (res is Result.Success) {
                                        binding.listCard.currentCard = res.data
                                        homeViewModel.updateList(res.data, position)

                                        showToast("再発行しました")
                                    } else if (res is Result.Error) {
                                        when (res.exception) {
                                            is NoConnectivityException -> showInternetTrouble()
                                            is ApiException -> showPopupMessage(
                                                "",
                                                res.exception.message
                                            )
                                            else -> showPopupMessage(
                                                "",
                                                getString(R.string.get_list_card_failure)
                                            )
                                        }
                                    }
                                }
                            }
                            setNegativeButton("いいえ", null)
                            setMessage("カードを再発行しますよろしいですか？")
                        }.create().show()
                    }

                })
            }
        }
    }

    @Subscribe
    fun handleReloadCard(event: ReloadCard) {
        if (isRemoving) {
            forceReloadCard = true//will reload on method onViewCreated
        } else {
            homeViewModel.loadCard(true)
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
        val buttonSlideLeft = binding.listCard.buttonSlideLeft
        val buttonSlideRight = binding.listCard.buttonSlideRight
        val buttonSeeAllCard = binding.buttonSeeAllCard
        val textBalance = binding.textBalance
        val btnIssueCard = binding.buttonAddNewCard
        val btnBalance = binding.buttonCardNoBalance

        val params = buttonSlideLeft.layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(
            resources.getDimensionPixelOffset(R.dimen.my_page_card_arrow_direction_ml),
            calculateMarginTopArrowIcon(),
            0,
            0
        )
        buttonSlideLeft.layoutParams = params

        val params1 = buttonSlideRight.layoutParams as ViewGroup.MarginLayoutParams
        params1.setMargins(
            0,
            calculateMarginTopArrowIcon(),
            resources.getDimensionPixelOffset(R.dimen.my_page_card_arrow_direction_ml),
            0
        )
        buttonSlideRight.layoutParams = params1

        btnBalance.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_balance_amount_menu) })

        btnIssueCard.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_issue_card_main) })

        buttonSeeAllCard.setOnClickListener(View.OnClickListener { findNavController().navigate(R.id.nav_list_vpreca) })

        binding.textLastLoginDate.text = SimpleDateFormat("yyyy M/d").format(Date())

        binding.listCard.buttonUsage.setOnClickListener(View.OnClickListener {
            val pagerAdapter = viewPager.adapter as CardSlidePagerAdapter
            val card = pagerAdapter.getItem(viewPager.currentItem)
            val action =
                HomeFragmentDirections.actionToCardUsage(card)
            findNavController().navigate(action)
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
                        textBalance.text = "¥0"
                        buttonSeeAllCard.visibility = View.INVISIBLE
                    }
                    else -> {
                        textNoCard.visibility = View.GONE
                        cardContainer.root.visibility = View.VISIBLE

                        setupViewPager(creditCardResult.success)

                        val sumBalance: Int = creditCardResult.success.sumOf {
                            try {
                                it.publishAmount?.toInt()!!
                            } catch (e: Exception) {
                                0
                            }
                        }
                        textBalance.text = Converter.convertCurrency(sumBalance)
                        if(sumBalance > 0){
                            buttonSeeAllCard.visibility = View.VISIBLE
                        }else {
                            buttonSeeAllCard.visibility = View.INVISIBLE
                        }
                    }
                }
            }
            creditCardResult.error?.let { error ->

                error.messageResId?.let { showPopupMessage(message = getString(it)) }
                error.message?.let { showPopupMessage(message = it) }
            }
            creditCardResult.networkTrouble?.let {
                if (it) {
                    showInternetTrouble()
                }
            }
        })
        homeViewModel.cardInfoResult.observe(
            viewLifecycleOwner,
            Observer { cardInfoResult ->
                cardInfoResult ?: return@Observer
                cardInfoResult.success?.let {
                    CardBottomSheetCustom(
                        requireActivity(),
                        cardInfoResult.success,
                        creditCardRepository
                    ).show()
                    homeViewModel.clearCardInfoResult()
                }
                cardInfoResult.error?.let { error ->
                    error.messageResId?.let { showPopupMessage(message = getString(it)) }
                    error.message?.let { showPopupMessage(message = it) }
                }
                cardInfoResult.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                    homeViewModel.clearCardInfoResult()
                }
            })
        homeViewModel.suspendDealResult.observe(
            viewLifecycleOwner,
            Observer { suspendDealResult ->
                suspendDealResult ?: return@Observer
                suspendDealResult.success?.let {
                    println("BalanceAmountViewModel.suspendDealResult.observe success: ${suspendDealResult.success}")
                    val sumBalance: Int = suspendDealResult.success.sumOf {
                        try {
                            if (it.suspendReasonType == "11" && it.adjustEndFlg == "0") {
                                it.unadjustDifferenceAmount.toInt()
                            } else 0
                        } catch (e: Exception) {
                            0
                        }
                    }
                    if (sumBalance > 0) {
                        btnBalance.isEnabled = true
                        btnBalance.visibility = View.VISIBLE
                    }
                }
                suspendDealResult.error?.let { error ->

                    error.messageResId?.let { showPopupMessage(message = getString(it)) }
                    error.message?.let { showPopupMessage(message = it) }
                }
                suspendDealResult.networkTrouble?.let {
                    if (it) {
                        showInternetTrouble()
                    }
                }
            })
        homeViewModel.loading.observe(viewLifecycleOwner, Observer {
            when (it) {
                true -> showLoadingDialog()
                else -> hideLoadingDialog()
            }
        })
        setLightStatusBar()
        return root
    }

    private fun setupViewPager(listCard: List<CreditCard>) {
        val tabDots = binding.listCard.tabDots
        val viewPager = binding.listCard.cardList
        val pagerAdapter =
            CardSlidePagerAdapter(requireActivity(), listCard)
        viewPager.adapter = pagerAdapter

        viewPager.offscreenPageLimit = 3


        val pageMargin =
            resources.getDimensionPixelOffset(R.dimen.home_card_item_page_margin)
        val pageOffset =
            resources.getDimensionPixelOffset(R.dimen.home_card_item_page_offset)
        println("pageMargin = $pageMargin - pageOffset = $pageOffset")
        viewPager.setPageTransformer { page, position ->
            val myOffset: Float = position * -(2 * pageOffset + pageMargin)
            if (position < -1) {
                page.translationX = -myOffset
            } else {
                page.translationX = myOffset
            }
        }
        if (latestPage > 0 && latestPage < pagerAdapter!!.itemCount) {
            viewPager.setCurrentItem(latestPage, false)
        }

        viewPager.registerOnPageChangeCallback(pageChangeCallback)

        TabLayoutMediator(tabDots, viewPager) { _, _ -> }.attach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (forceReloadCard) {
            homeViewModel.loadCard(true)
            forceReloadCard = false
        }
        homeViewModel.loadCardIfEmptyData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val viewPager = binding.listCard.cardList
        viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
        clearLightStatusBar()

        lockDrawer()
    }

    private lateinit var lifecycleObserver: DefaultLifecycleObserver

    override fun onAttach(context: Context) {
        super.onAttach(context)
        unlockDrawer()

        lifecycleObserver = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                showToolbar()
                owner.lifecycle.removeObserver(lifecycleObserver)
            }
        }
        requireActivity().lifecycle.addObserver(lifecycleObserver)
        EventBus.getDefault().register(this)

        requireActivity().onBackPressedDispatcher.addCallback(this, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
//                userManager.clear()
                closeApp()
            }
        })
    }

    override fun onDetach() {
        super.onDetach()
        EventBus.getDefault().unregister(this)
    }

    private fun calculateMarginTopArrowIcon(): Int {
        val displayMetrics = DisplayMetrics()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity?.display
            display?.getRealMetrics(displayMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = activity?.windowManager?.defaultDisplay
            @Suppress("DEPRECATION")
            display?.getMetrics(displayMetrics)
        }

        val width = displayMetrics.widthPixels
        val marginHorizontal =
            resources.getDimensionPixelOffset(R.dimen.home_card_item_page_margin_offset)
        val arrowHeight =
            resources.getDimensionPixelOffset(R.dimen.my_page_card_arrow_direction_height)

        val creditCardWidth = width - 2 * marginHorizontal
        val creditCardHeight = creditCardWidth * 270 / 425

        return (creditCardHeight - arrowHeight) / 2
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