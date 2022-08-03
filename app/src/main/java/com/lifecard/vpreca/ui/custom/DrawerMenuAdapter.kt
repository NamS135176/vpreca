package com.lifecard.vpreca.ui.custom

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.LayoutDrawerGroupItemBinding
import com.lifecard.vpreca.databinding.LayoutDrawerItemBinding
import com.lifecard.vpreca.databinding.MenuItemTestBinding
import com.lifecard.vpreca.databinding.SelectSourceCardItemBinding
import com.lifecard.vpreca.ui.issuecard.IssueCardSourceAdapter
import com.lifecard.vpreca.utils.Converter
import com.lifecard.vpreca.utils.showAlertMessage

class DrawerMenuAdapter(
    private var items: ArrayList<NavigationItem>,
    private var onItemClickListener: OnItemClickListener,
    private var context : Context
) :
    RecyclerView.Adapter<DrawerMenuAdapter.NavigationItemViewHolder>() {

    class NavigationItemViewHolder : RecyclerView.ViewHolder {
        var headerBinding: LayoutDrawerGroupItemBinding? = null
        var regularItemBinding: LayoutDrawerItemBinding? = null

        constructor(binding: LayoutDrawerGroupItemBinding) : super(binding.root) {
            headerBinding = binding
        }

        constructor(binding: LayoutDrawerItemBinding) : super(binding.root) {
            regularItemBinding = binding
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
        }
        val binding =
            LayoutDrawerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NavigationItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].type
    }

    override fun onBindViewHolder(holder: NavigationItemViewHolder, position: Int) {
//        if (holder.itemViewType == 0) {//group

        holder.headerBinding?.text?.text = items[position].title
//        holder.headerBinding?.imgArrow?.setImageResource(R.drawable.ic_menu_arrow)
        val items: ArrayList<String> = arrayListOf("jskdhfskdf", "dkjshfksd")
        val linearLayoutManager =
            LinearLayoutManager(context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        val adapter = ListMenuAdapter(items)

        holder.headerBinding?.tvTest?.layoutManager = linearLayoutManager
        holder.headerBinding?.tvTest?.adapter = adapter
        adapter.setOnClickListener(object : ListMenuAdapter.OnItemClickListener{
            override fun onItemClick(position: Int, binding: MenuItemTestBinding) {

            }
        })

        if (this.items[position].show) {
                holder.headerBinding?.tvTest?.visibility = View.VISIBLE
                holder.headerBinding?.imgArrow?.setImageResource(R.drawable.ic_arrow_toggle)
            } else {
                holder.headerBinding?.tvTest?.visibility = View.GONE
                holder.headerBinding?.imgArrow?.setImageResource(R.drawable.ic_menu_arrow)
            }

        holder.headerBinding?.root?.setOnClickListener(View.OnClickListener {
            onItemClickListener.onClick(position)
            notifyItemChanged(position)
//            if (holder.headerBinding?.tvTest?.visibility == View.GONE) {
//                holder.headerBinding?.tvTest?.visibility = View.VISIBLE
//                holder.headerBinding?.imgArrow?.setImageResource(R.drawable.ic_arrow_toggle)
//            } else {
//                holder.headerBinding?.tvTest?.visibility = View.GONE
//                holder.headerBinding?.imgArrow?.setImageResource(R.drawable.ic_menu_arrow)
//            }
        })
//        } else {
//            println("sdhfkjsdhfskdfh")
//////            holder?.regularItemBinding?.item = items[position]
//            holder.regularItemBinding?.text?.text = items[position].title
//////            holder.regularItemBinding?.c
//            holder.regularItemBinding?.root?.setOnClickListener(View.OnClickListener {
//                onItemClickListener.onClick(position)
////                val isInvisible: Boolean = items[position].show
////                holder.regularItemBinding?.tvTest?.visibility = if(isInvisible) View.VISIBLE else View.GONE
////
////                items[position].show = !items[position].show
////                notifyItemChanged(position)
//            })
//        }
    }

}


interface OnItemClickListener {
    fun onClick(position: Int)
}