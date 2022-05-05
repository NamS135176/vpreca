package com.lifecard.vpreca.ui.custom

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.lifecard.vpreca.BuildConfig
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.LayoutDrawerContentBinding
import com.lifecard.vpreca.eventbus.CloseDrawerEvent
import org.greenrobot.eventbus.EventBus


data class NavigationItem(var type: Int, var position: Int, var title: String)

class DrawerMenuLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var _binding: LayoutDrawerContentBinding
    private var adapter: DrawerMenuAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
            title = context.getString(R.string.menu_group_utilities)
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
            title = context.getString(R.string.menu_laws)
        ),
        NavigationItem(
            type = 1,
            position = 22,
            title = context.getString(R.string.menu_settlement)
        ),
        NavigationItem(
            type = 1,
            position = 23,
            title = context.getString(R.string.menu_rule)
        )
    )

    private var onItemClickListener = object : OnItemClickListener {
        override fun onClick(position: Int) {
            print("onClick menu at ${position}")
            Toast.makeText(context, "onClick menu at ${position}", Toast.LENGTH_SHORT).show()
            closeDrawer()
            when (position) {
                0 -> {

                }
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
    }

    private fun closeDrawer() {
        EventBus.getDefault().post(CloseDrawerEvent())
    }
}