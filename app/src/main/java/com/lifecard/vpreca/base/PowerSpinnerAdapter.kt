package com.lifecard.vpreca.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.util.rangeTo
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.R
import com.lifecard.vpreca.databinding.PowerSpinnerItemBinding
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.skydoves.powerspinner.PowerSpinnerInterface
import com.skydoves.powerspinner.PowerSpinnerView

class PowerSpinnerAdapter(
    powerSpinnerView: PowerSpinnerView
) : RecyclerView.Adapter<PowerSpinnerAdapter.MySpinnerViewHolder>(),
    PowerSpinnerInterface<CharSequence> {


    /** definition of the non-selected index value. */
    val NO_SELECTED_INDEX: Int = -1

    override var index: Int = powerSpinnerView.selectedIndex
    override val spinnerView: PowerSpinnerView = powerSpinnerView
    override var onSpinnerItemSelectedListener: OnSpinnerItemSelectedListener<CharSequence>? = null

    private val spinnerItems: MutableList<CharSequence> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MySpinnerViewHolder {
        val binding =
            PowerSpinnerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MySpinnerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MySpinnerViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            notifyItemSelected(position)
        }
        holder.binding.text.text = spinnerItems[position]
        if (position == index) {
            holder.itemView.setBackgroundResource(R.color.spinner_selection_bg)
        } else {
            holder.itemView.setBackgroundResource(R.color.spinner_bg)
        }
    }

    // You must call the `spinnerView.notifyItemSelected` method to let `PowerSpinnerView` know the item is changed.
    override fun notifyItemSelected(index: Int) {
        if (index == NO_SELECTED_INDEX) return
        val oldIndex = this.index
        this.index = index
        this.spinnerView.notifyItemSelected(index, this.spinnerItems[index])
        this.onSpinnerItemSelectedListener?.onItemSelected(
            oldIndex = oldIndex,
            oldItem = oldIndex.takeIf { it != NO_SELECTED_INDEX }?.let { spinnerItems[oldIndex] },
            newIndex = index,
            newItem = spinnerItems[index]
        )
        notifyItemChanged(oldIndex)
        notifyItemChanged(this.index)
    }

    override fun setItems(itemList: List<CharSequence>) {
        this.spinnerItems.clear()
        this.spinnerItems.addAll(itemList)
        this.index = NO_SELECTED_INDEX
        notifyDataSetChanged()
    }

    fun getSelectedItem(): Int? {
        return if (index in 0 until itemCount) index else null
    }

    override fun getItemCount(): Int = spinnerItems.size

    class MySpinnerViewHolder(val binding: PowerSpinnerItemBinding) :
        RecyclerView.ViewHolder(binding.root)


}