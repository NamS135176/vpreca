package com.lifecard.vpreca.ui.custom

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.SelectedData
import com.lifecard.vpreca.data.model.getBackgroundCard
import com.lifecard.vpreca.databinding.MenuItemTestBinding
import com.lifecard.vpreca.databinding.SelectSourceCardItemBinding

class ListMenuAdapter(private var items: List<String>) :
    RecyclerView.Adapter<ListMenuAdapter.ViewHolder>() {


    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, binding: MenuItemTestBinding)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MenuItemTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, mListener)
    }

    class ViewHolder(var binding: MenuItemTestBinding, listener : OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { listener.onItemClick(adapterPosition, binding) }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.text.text = items[position]
    }

    override fun getItemCount() = items.size


}