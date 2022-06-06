package com.lifecard.vpreca.ui.issuecard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.R
import com.lifecard.vpreca.data.model.CreditCard
import com.lifecard.vpreca.data.model.SelectedData
import com.lifecard.vpreca.databinding.SelectSourceCardItemBinding
import com.lifecard.vpreca.databinding.VprecaCardItemBinding

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
        holder.binding.select = selected[position]
        when (items[position].designId) {
            "001" -> holder.binding.cardItem.setBackgroundResource(R.drawable.bg_first)
            "002" -> holder.binding.cardItem.setBackgroundResource(R.drawable.bg_second)
            "003" -> holder.binding.cardItem.setBackgroundResource(R.drawable.bg_third)
            "004" -> holder.binding.cardItem.setBackgroundResource(R.drawable.bg_fourth)
            "005" -> holder.binding.cardItem.setBackgroundResource(R.drawable.bg_fifth)
            "006" -> holder.binding.cardItem.setBackgroundResource(R.drawable.bg_six)
            "007" -> holder.binding.cardItem.setBackgroundResource(R.drawable.bg_seven)
            else -> holder.binding.cardItem.setBackgroundResource(R.drawable.bg_seven)
        }
    }

    override fun getItemCount() = items.size


}