package com.lifecard.vpreca.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.lifecard.vpreca.BuildConfig
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.UserRepository
import com.lifecard.vpreca.data.source.SecureStore
import com.lifecard.vpreca.databinding.LayoutDrawerContentBinding
import com.lifecard.vpreca.eventbus.CloseDrawerEvent
import com.lifecard.vpreca.ui.webview.WebViewFragment
import com.lifecard.vpreca.utils.navigateToLogin
import com.lifecard.vpreca.utils.viewFindNavController
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject


data class NavigationItem(var type: Int, var position: Int, var title: String)

@AndroidEntryPoint
class DrawerMenuLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {
    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var secureStore: SecureStore

    private var _binding: LayoutDrawerContentBinding
    private var adapter: DrawerMenuAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding
//    var navController: NavController? = null

    private var items: ArrayList<NavigationItem> = arrayListOf(
        NavigationItem(
            type = 0,
            position = 0,
            title = context.getString(R.string.menu_group_vpreca_gift)
        ),
        NavigationItem(
            type = 1,
            position = 1,
            title = context.getString(R.string.menu_vpreca_gift_request)
        ),
        //Group my account
        NavigationItem(
            type = 0,
            position = 2,
            title = context.getString(R.string.menu_group_my_account)
        ),
        NavigationItem(
            type = 1,
            position = 3,
            title = context.getString(R.string.menu_member_info)
        ),
        NavigationItem(
            type = 1,
            position = 4,
            title = context.getString(R.string.menu_change_pass)
        ),
        NavigationItem(
            type = 1,
            position = 5,
            title = context.getString(R.string.menu_change_phone)
        ),
        NavigationItem(
            type = 1,
            position = 6,
            title = context.getString(R.string.menu_credit_card_info)
        ),
        NavigationItem(
            type = 1,
            position = 7,
            title = context.getString(R.string.menu_member_setting)
        ),
        //Group utilities
        NavigationItem(
            type = 0,
            position = 8,
            title = context.getString(R.string.menu_group_utilities)
        ),
        NavigationItem(
            type = 1,
            position = 9,
            title = context.getString(R.string.menu_register_lifecard)
        ),
        NavigationItem(
            type = 1,
            position = 10,
            title = context.getString(R.string.menu_campain_info)
        ),
        //Group support
        NavigationItem(
            type = 0,
            position = 11,
            title = context.getString(R.string.menu_group_support)
        ),
        NavigationItem(
            type = 1,
            position = 12,
            title = context.getString(R.string.menu_news)
        ),
        NavigationItem(
            type = 1,
            position = 13,
            title = context.getString(R.string.menu_faq)
        ),
        NavigationItem(
            type = 1,
            position = 14,
            title = context.getString(R.string.menu_inquiry)
        ),
        //Group About VPreca
        NavigationItem(
            type = 0,
            position = 15,
            title = context.getString(R.string.menu_group_about_vpreca)
        ),
        NavigationItem(
            type = 1,
            position = 16,
            title = context.getString(R.string.menu_attention)
        ),
        NavigationItem(
            type = 1,
            position = 17,
            title = context.getString(R.string.menu_company)
        ),
        NavigationItem(
            type = 1,
            position = 18,
            title = context.getString(R.string.menu_business_info)
        ),
        NavigationItem(
            type = 1,
            position = 19,
            title = context.getString(R.string.menu_compliance)
        ),
        NavigationItem(
            type = 1,
            position = 20,
            title = context.getString(R.string.menu_privacy_policy)
        ),
        NavigationItem(
            type = 1,
            position = 21,
            title = context.getString(R.string.menu_link)
        ),
        NavigationItem(
            type = 1,
            position = 22,
            title = context.getString(R.string.menu_laws)
        ),
        NavigationItem(
            type = 1,
            position = 23,
            title = context.getString(R.string.menu_settlement)
        ),
        NavigationItem(
            type = 1,
            position = 24,
            title = context.getString(R.string.menu_rule)
        )
    )

    private fun showWebViewActivity(webUrl: String) {
        val navController = viewFindNavController()
        navController.navigate(R.id.nav_webview, WebViewFragment.createBundle(webUrl))
    }

    private var onItemClickListener = object : OnItemClickListener {
        override fun onClick(position: Int) {
            print("onClick menu at ${position}")
//            Toast.makeText(context, "onClick menu at ${position}", Toast.LENGTH_SHORT).show()
            closeDrawer()

            when (position) {
                1 -> viewFindNavController().navigate(R.id.nav_gift_card_policy)
                3 -> viewFindNavController().navigate(R.id.nav_change_info_data)
                4 -> viewFindNavController().navigate(R.id.nav_change_pass)
                5 -> Toast.makeText(context, "Not yet supported)", Toast.LENGTH_SHORT)
                    .show()//menu_change_phone
                6 -> Toast.makeText(context, "Not yet supported)", Toast.LENGTH_SHORT)
                    .show()//menu_credit_card_info
                7 -> Toast.makeText(
                    context,
                    "Not yet supported (FaceID/TouchID)",
                    Toast.LENGTH_SHORT
                ).show()//menu_member_setting -> show faceId/TouchID
                9 -> showWebViewActivity("https://www.lifecard.co.jp/card/campaign/ol_nyukai/vpc/1604_1/index.html?utm_source=mail&utm_medium=mail&utm_campaign=vpc_my2&argument=xZcLVgDf&dmai=a627cb5ac0f66f")//menu_register_lifecard
                10 -> showWebViewActivity("https://vpc.lifecard.co.jp/campaign/index.html")//menu_campain_info
                12 -> showWebViewActivity("https://vpc.lifecard.co.jp/news/index.html")//menu_news
                13 -> showWebViewActivity("https://vpreca.dga.jp/")//menu_faq
                14 -> showWebViewActivity("https://vpc.lifecard.co.jp/contact/index.html")//menu_inquiry
                16 -> showWebViewActivity("https://vpc.lifecard.co.jp/attention/index.html")//menu_attention
                17 -> showWebViewActivity("https://www.lifecard.co.jp/company/")//menu_company
                18 -> showWebViewActivity("https://www.lifecard.co.jp/company/business/")//menu_business_info
                19 -> showWebViewActivity("https://www.lifecard.co.jp/efforts/compliance/")//menu_compliance
                20 -> showWebViewActivity("https://www.lifecard.co.jp/privacy_policy/")//menu_privacy_policy
                21 -> showWebViewActivity("https://vpc.lifecard.co.jp/links/index.html")//menu_link
                22 -> showWebViewActivity("https://www.lifecard.co.jp/efforts/privacy_policy/")//menu_laws
                23 -> showWebViewActivity("https://vpc.lifecard.co.jp/settlement/index.html")//menu_settlement
                24 -> showWebViewActivity("https://vpc.lifecard.co.jp/rule/index.html")//menu_rule
            }
        }
    }

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        _binding = LayoutDrawerContentBinding.inflate(inflater, this, true)

        adapter = DrawerMenuAdapter(items = items, onItemClickListener = onItemClickListener)

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
            userRepository.clear()
            secureStore.clear()
            closeDrawer()
            navigateToLogin()
        })
    }

    private fun closeDrawer() {
        EventBus.getDefault().post(CloseDrawerEvent())
    }
}