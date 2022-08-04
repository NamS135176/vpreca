package com.lifecard.vpreca.ui.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.*
import com.lifecard.vpreca.ui.issuecard.IssueCardSourceAdapter
import com.lifecard.vpreca.utils.Converter
import com.lifecard.vpreca.utils.Utils
import com.lifecard.vpreca.utils.showAlertMessage
import com.lifecard.vpreca.utils.viewFindNavController

class DrawerMenuAdapter(
    private var items: ArrayList<NavigationItem>,
    private var onItemClickListener: OnItemClickListener,
    private var context: Context
) :
    RecyclerView.Adapter<DrawerMenuAdapter.NavigationItemViewHolder>() {

    class NavigationItemViewHolder : RecyclerView.ViewHolder {
        var headerBinding: LayoutDrawerGroupItemBinding? = null
        var regularItemBinding: LayoutDrawerItemBinding? = null
        var menuItemBinding: LayoutDrawerMenuItemBinding? = null

        constructor(binding: LayoutDrawerGroupItemBinding) : super(binding.root) {
            headerBinding = binding
        }

        constructor(binding: LayoutDrawerItemBinding) : super(binding.root) {
            regularItemBinding = binding
        }

        constructor(binding: LayoutDrawerMenuItemBinding) : super(binding.root) {
            menuItemBinding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationItemViewHolder {
        if (viewType == 0) {//case group
            val binding = LayoutDrawerGroupItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return NavigationItemViewHolder(binding)
        } else if (viewType == 1) {
            val binding =
                LayoutDrawerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return NavigationItemViewHolder(binding)
        } else {
            val binding =
                LayoutDrawerMenuItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return NavigationItemViewHolder(binding)
        }

    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    private fun showWebViewActivity(webUrl: String) {
        Utils.openBrowser(context, webUrl)
    }

    override fun onBindViewHolder(holder: NavigationItemViewHolder, position: Int) {
        if (holder.itemViewType == 0) {//group
            holder.headerBinding?.text?.text = items[position].title
        } else if (holder.itemViewType == 1) {
            holder.regularItemBinding?.text?.text = items[position].title
            holder.regularItemBinding?.root?.setOnClickListener(View.OnClickListener {
                onItemClickListener.onClick(position)
                notifyItemChanged(position)
            })
        } else {
            val items: ArrayList<String>
            if(this.items[position].position == 11){
                items = arrayListOf(
                    context.getString(R.string.menu_news),
                    context.getString(R.string.menu_faq),
                    context.getString(R.string.menu_attention),
                    context.getString(R.string.menu_inquiry)
                )
            }
            else{
                items = arrayListOf(
                    context.getString(R.string.menu_company),
                    context.getString(R.string.menu_business_info),
                    context.getString(R.string.menu_compliance),
                    context.getString(R.string.menu_privacy_policy),
                    context.getString(R.string.menu_link),
                    context.getString(R.string.menu_laws),
                    context.getString(R.string.menu_settlement),
                    context.getString(R.string.menu_rule),
                    context.getString(R.string.menu_aboutapp),
                )
            }
            holder.menuItemBinding?.text?.text = this.items[position].title

            val linearLayoutManager =
                LinearLayoutManager(context)
            linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            val adapter = ListMenuAdapter(items)
//            val dividerItemDecoration = DividerItemDecoration(
//                context,
//                LinearLayoutCompat.VERTICAL
//            )
//            holder.menuItemBinding?.tvTest?.addItemDecoration(dividerItemDecoration)
            holder.menuItemBinding?.tvTest?.layoutManager = linearLayoutManager
            holder.menuItemBinding?.tvTest?.adapter = adapter
            adapter.setOnClickListener(object : ListMenuAdapter.OnItemClickListener {
                override fun onItemClick(position: Int, binding: MenuItemTestBinding) {
                    when(items[position]){
                        context.getString(R.string.menu_news) -> showWebViewActivity("https://vpc.lifecard.co.jp/news/index.html")//menu_news
                        context.getString(R.string.menu_faq) -> showWebViewActivity("https://vpreca.dga.jp/")//menu_faq
                        context.getString(R.string.menu_attention) -> showWebViewActivity("https://vpc.lifecard.co.jp/attention/index.html")//menu_attention
                        context.getString(R.string.menu_inquiry) -> showWebViewActivity("https://vpc.lifecard.co.jp/contact/index.html")//menu_inquiry
                        context.getString(R.string.menu_company) -> showWebViewActivity("https://www.lifecard.co.jp/company/")//menu_company
                        context.getString(R.string.menu_business_info) -> showWebViewActivity("https://www.lifecard.co.jp/company/business/")//menu_business_info
                        context.getString(R.string.menu_compliance) -> showWebViewActivity("https://www.lifecard.co.jp/efforts/compliance/")//menu_compliance
                        context.getString(R.string.menu_privacy_policy) -> showWebViewActivity("https://www.lifecard.co.jp/privacy_policy/")//menu_privacy_policy
                        context.getString(R.string.menu_link) -> showWebViewActivity("https://vpc.lifecard.co.jp/links/index.html")//menu_link
                        context.getString(R.string.menu_laws) -> showWebViewActivity("https://www.lifecard.co.jp/efforts/privacy_policy/")//menu_laws
                        context.getString(R.string.menu_settlement) -> showWebViewActivity("https://vpc.lifecard.co.jp/settlement/index.html")//menu_settlement
                        context.getString(R.string.menu_rule) -> showWebViewActivity("https://vpc.lifecard.co.jp/rule/index.html")//menu_rule
                        context.getString(R.string.menu_aboutapp) -> holder.menuItemBinding?.root?.viewFindNavController()?.navigate(R.id.nav_aboutapp)
                    }
                }
            })

            if (this.items[position].show) {
                holder.menuItemBinding?.tvTest?.visibility = View.VISIBLE
                holder.menuItemBinding?.imgArrow?.setImageResource(R.drawable.ic_arrow_up_fill_blue)
            } else {
                holder.menuItemBinding?.tvTest?.visibility = View.GONE
                holder.menuItemBinding?.imgArrow?.setImageResource(R.drawable.ic_arrow_down_fill_blue)
            }

            holder.menuItemBinding?.root?.setOnClickListener(View.OnClickListener {
                onItemClickListener.onClick(position)
                notifyItemChanged(position)
            })
        }

    }
}


interface OnItemClickListener {
    fun onClick(position: Int)
}