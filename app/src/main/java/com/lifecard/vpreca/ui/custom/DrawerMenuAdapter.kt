package com.lifecard.vpreca.ui.custom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.databinding.LayoutDrawerGroupItemBinding
import com.lifecard.vpreca.databinding.LayoutDrawerItemBinding

class DrawerMenuAdapter(
    private var items: ArrayList<NavigationItem>,
    private var onItemClickListener: OnItemClickListener
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
        if (holder.itemViewType == 0) {//group
            holder.headerBinding?.text?.text = items[position].title
        } else {
            println("sdhfkjsdhfskdfh")
////            holder?.regularItemBinding?.item = items[position]
            holder.regularItemBinding?.text?.text = items[position].title
////            holder.regularItemBinding?.c
            holder.regularItemBinding?.root?.setOnClickListener(View.OnClickListener {
                onItemClickListener.onClick(position)
//                val isInvisible: Boolean = items[position].show
//                holder.regularItemBinding?.tvTest?.visibility = if(isInvisible) View.VISIBLE else View.GONE
//
//                items[position].show = !items[position].show
//                notifyItemChanged(position)
            })
        }
    }

}


interface OnItemClickListener {
    fun onClick(position: Int)
}