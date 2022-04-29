package com.lifecard.vpreca.ui.signup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lifecard.vpreca.R


class TermOfUseAdapter(private val dataSet: ArrayList<String>) :
    RecyclerView.Adapter<TermOfUseAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPolicy: TextView

        init {
            // Define click listener for the ViewHolder's View.
            tvPolicy = view.findViewById(R.id.policyItemId)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        var view:View
        if (viewType == 1) {
            // inflate your first item layout & return that viewHolder
            view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.first_row_termofuse, viewGroup, false)
        } else {
            // inflate your second item layout & return that viewHolder
            view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.policy_item, viewGroup, false)
        }
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.tvPolicy.text = dataSet[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size
    override fun getItemViewType(position: Int): Int {
        if (position == 0) return 1;
        else return 2;
    }
}