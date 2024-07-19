package com.example.taxinfo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.taxinfo.R
import com.example.taxinfo.databinding.HomeLayoutBinding
import com.example.taxinfo.modelClass.TaxDetails
import com.example.taxinfo.viewmodel.TaxViewModel

class HomeAdapter(private val context: Context, private var list : ArrayList<TaxDetails>, private val listener : OnDelete)
    : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater
            .from(context)
            .inflate(R.layout.home_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.binding.taxType.text = item.taxType
        holder.binding.amount.text = item.amount

        holder.binding.delete.setOnClickListener {
            listener.onDeleteListener(item)
            list.remove(item)
            notifyItemRemoved(position)
            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = HomeLayoutBinding.bind(view)
    }
}

interface OnDelete{
    fun onDeleteListener(taxDetails: TaxDetails)
}
