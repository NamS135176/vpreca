package com.lifecard.vpreca.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.lifecard.vpreca.BuildConfig
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.UserManager
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.databinding.LayoutDrawerContentBinding
import com.lifecard.vpreca.eventbus.CloseDrawerEvent
import com.lifecard.vpreca.ui.web_direct.WebDirectFragmentArgs
import com.lifecard.vpreca.utils.*
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


data class NavigationItem(var type: Int, var position: Int, var title: String, var show: Boolean)

@AndroidEntryPoint
class DrawerMenuLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {
    @Inject
    lateinit var userManager: UserManager

    @Inject
    lateinit var secureStore: SecureStore

    private var _binding: LayoutDrawerContentBinding
    private var adapter: DrawerMenuAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    private var items: ArrayList<NavigationItem> = arrayListOf(
        NavigationItem(
            type = 0,
            position = 0,
            title = context.getString(R.string.menu_group_vpreca_gift),
            show = false
        ),
        NavigationItem(
            type = 1,
            position = 1,
            title = context.getString(R.string.menu_vpreca_gift_request),
            show = false
        ),
        //Group my account
        NavigationItem(
            type = 0,
            position = 2,
            title = context.getString(R.string.menu_group_my_account),
            show = false
        ),
        NavigationItem(
            type = 1,
            position = 3,
            title = context.getString(R.string.menu_member_info),
            show = false
        ),
        NavigationItem(
            type = 1,
            position = 4,
            title = context.getString(R.string.menu_change_pass),
            show = false
        ),
        NavigationItem(
            type = 1,
            position = 5,
            title = context.getString(R.string.menu_change_phone),
            show = false
        ),
        NavigationItem(
            type = 1,
            position = 6,
            title = context.getString(R.string.menu_credit_card_info),
            show = false
        ),
        NavigationItem(
            type = 1,
            position = 7,
            title = context.getString(R.string.menu_member_setting),
            show = false
        ),
        //Group utilities
        NavigationItem(
            type = 0,
            position = 8,
            title = context.getString(R.string.menu_group_utilities),
            show = false
        ),
        NavigationItem(
            type = 1,
            position = 9,
            title = context.getString(R.string.menu_register_lifecard),
            show = false
        ),
        NavigationItem(
            type = 1,
            position = 10,
            title = context.getString(R.string.menu_campain_info),
            show = false
        ),
        //Group support
        NavigationItem(
            type = 2,
            position = 11,
            title = context.getString(R.string.menu_group_support),
            show = false
        ),
//        NavigationItem(
//            type = 1,
//            position = 12,
//            title = context.getString(R.string.menu_news),
//            show = false
//        ),
//        NavigationItem(
//            type = 1,
//            position = 13,
//            title = context.getString(R.string.menu_faq),
//            show = false
//        ),
//        NavigationItem(
//            type = 1,
//            position = 14,
//            title = context.getString(R.string.menu_attention), show = false
//
//        ),
//        NavigationItem(
//            type = 1,
//            position = 15,
//            title = context.getString(R.string.menu_inquiry),
//            show = false
//        ),
        //Group About VPreca
        NavigationItem(
            type = 2,
            position = 16,
            title = context.getString(R.string.menu_group_about_vpreca),
            show = false
        ),

//        NavigationItem(
//            type = 1,
//            position = 17,
//            title = context.getString(R.string.menu_company),
//            show = false
//        ),
//        NavigationItem(
//            type = 1,
//            position = 18,
//            title = context.getString(R.string.menu_business_info),
//            show = false
//        ),
//        NavigationItem(
//            type = 1,
//            position = 19,
//            title = context.getString(R.string.menu_compliance),
//            show = false
//        ),
//        NavigationItem(
//            type = 1,
//            position = 20,
//            title = context.getString(R.string.menu_privacy_policy),
//            show = false
//        ),
//        NavigationItem(
//            type = 1,
//            position = 21,
//            title = context.getString(R.string.menu_link),
//            show = false
//        ),
//        NavigationItem(
//            type = 1,
//            position = 22,
//            title = context.getString(R.string.menu_laws),
//            show = false
//        ),
//        NavigationItem(
//            type = 1,
//            position = 23,
//            title = context.getString(R.string.menu_settlement),
//            show = false
//        ),
//        NavigationItem(
//            type = 1,
//            position = 24,
//            title = context.getString(R.string.menu_rule),
//            show = false
//        ),
//        NavigationItem(
//            type = 1,
//            position = 25,
//            title = context.getString(R.string.menu_aboutapp),
//            show = false
//        )
    )

    private fun showWebViewActivity(webUrl: String) {
        Utils.openBrowser(context, webUrl)
    }

    private var onItemClickListener = object : OnItemClickListener {
        override fun onClick(position: Int) {
            print("onClick menu at ${position}")
//            Toast.makeText(context, "onClick menu at ${position}", Toast.LENGTH_SHORT).show()
//            closeDrawer()

            when (position) {
                1 -> viewFindNavController().navigate(R.id.nav_gift_card_policy)
                3 -> viewFindNavController().navigate(R.id.nav_change_info_data)
                4 -> viewFindNavController().navigate(R.id.nav_change_pass)
                5 -> viewFindNavController().navigate(R.id.nav_change_phone_first)
                6 -> viewFindNavController().navigate(
                    R.id.nav_web_direct,
                    WebDirectFragmentArgs(screenId = WebDirectScreen.SCREEN_CREDIT_CARD_INFO).toBundle()
                )//menu_member_setting
                7 -> viewFindNavController().navigate(R.id.nav_fingerprint_setting)//menu_credit_card_info
                9 -> showWebViewActivity("https://www.lifecard.co.jp/card/campaign/ol_nyukai/vpc/1604_1/index.html?utm_source=mail&utm_medium=mail&utm_campaign=vpc_my2&argument=xZcLVgDf&dmai=a627cb5ac0f66f")//menu_register_lifecard
                10 -> showWebViewActivity("https://vpc.lifecard.co.jp/campaign/index.html")//menu_campain_info
                11 -> items[position].show = !items[position].show
//                12 -> showWebViewActivity("https://vpc.lifecard.co.jp/news/index.html")//menu_news
//                13 -> showWebViewActivity("https://vpreca.dga.jp/")//menu_faq
//                14 -> showWebViewActivity("https://vpc.lifecard.co.jp/attention/index.html")//menu_attention
//                15 -> showWebViewActivity("https://vpc.lifecard.co.jp/contact/index.html")//menu_inquiry
                12 -> items[position].show = !items[position].show
//                17 -> showWebViewActivity("https://www.lifecard.co.jp/company/")//menu_company
//                18 -> showWebViewActivity("https://www.lifecard.co.jp/company/business/")//menu_business_info
//                19 -> showWebViewActivity("https://www.lifecard.co.jp/efforts/compliance/")//menu_compliance
//                20 -> showWebViewActivity("https://www.lifecard.co.jp/privacy_policy/")//menu_privacy_policy
//                21 -> showWebViewActivity("https://vpc.lifecard.co.jp/links/index.html")//menu_link
//                22 -> showWebViewActivity("https://www.lifecard.co.jp/efforts/privacy_policy/")//menu_laws
//                23 -> showWebViewActivity("https://vpc.lifecard.co.jp/settlement/index.html")//menu_settlement
//                24 -> showWebViewActivity("https://vpc.lifecard.co.jp/rule/index.html")//menu_rule
//                25 -> viewFindNavController().navigate(R.id.nav_aboutapp)
            }
        }
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        _binding = LayoutDrawerContentBinding.inflate(inflater, this, true)

        adapter = DrawerMenuAdapter(items = items, onItemClickListener = onItemClickListener, context)

        val dividerItemDecoration = DividerItemDecoration(
            context,
            VERTICAL
        )
        binding.listMenu.addItemDecoration(dividerItemDecoration)
        binding.listMenu.adapter = adapter

        binding.textAppVersion.text =
            String.format(resources.getString(R.string.menu_app_version), BuildConfig.VERSION_NAME)
        binding.navHeader.buttonCloseDrawer.setOnClickListener(OnClickListener { closeDrawer() })
        binding.buttonLogout.setOnClickListener(OnClickListener {
            userManager.clear()
            secureStore.clear()
            PreferenceHelper.clearDueLogout(context)
            closeDrawer()
            navigateToLogin()
        })
    }

    private fun closeDrawer() {
        EventBus.getDefault().post(CloseDrawerEvent())
    }
}