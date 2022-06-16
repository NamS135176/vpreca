package com.lifecard.vpreca.ui.balance_amount

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.SelectedData
import com.lifecard.vpreca.data.model.getBackgroundCard
import com.lifecard.vpreca.databinding.SelectSourceBalanceItemBinding

class BalanceSelectSourceAdapter(
    private var items: List<CreditCard>,
    private var selected: List<SelectedData>
) : RecyclerView.Adapter<BalanceSelectSourceAdapter.ViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, binding: SelectSourceBalanceItemBinding)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            SelectSourceBalanceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding, mListener)
    }

    class ViewHolder(var binding: SelectSourceBalanceItemBinding, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cardInfo.setOnClickListener { listener.onItemClick(adapterPosition, binding) }
        }

    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.card = items[position]
        holder.binding.cardInclude.card = items[position]
        holder.binding.select = selected[position]
        holder.binding.cardInclude.cardInfo.setBackgroundResource(items[position].getBackgroundCard())
    }
}