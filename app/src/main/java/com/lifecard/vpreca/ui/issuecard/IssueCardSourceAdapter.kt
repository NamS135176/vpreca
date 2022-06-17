package com.lifecard.vpreca.ui.issuecard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.SelectedData
import com.lifecard.vpreca.data.model.getBackgroundCard
import com.lifecard.vpreca.databinding.SelectSourceCardItemBinding

class IssueCardSourceAdapter(private var items: List<CreditCard>,private var selected:List<SelectedData>) :
    RecyclerView.Adapter<IssueCardSourceAdapter.ViewHolder>() {


    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int, binding: SelectSourceCardItemBinding)
    }

    fun setOnClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            SelectSourceCardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, mListener)
    }

    class ViewHolder(var binding: SelectSourceCardItemBinding, listener : OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cardInfo.setOnClickListener { listener.onItemClick(adapterPosition, binding) }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.card = items[position]
        holder.binding.cardInclude.card = items[position]
        holder.binding.select = selected[position]
        holder.binding.cardInclude.cardInfo.setBackgroundResource(items[position].getBackgroundCard())
    }

    override fun getItemCount() = items.size


}