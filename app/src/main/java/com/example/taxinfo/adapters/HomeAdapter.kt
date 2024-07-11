package com.example.taxinfo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taxinfo.R
import com.example.taxinfo.databinding.HomeLayoutBinding
import com.example.taxinfo.modelClass.TaxDetails
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Runnable

class HomeAdapter(private val context: Context, private var list : List<TaxDetails>)
    : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

        val updateList = Runnable {
            list = list
            notifyDataSetChanged()
        }
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
            val ref =  FirebaseDatabase.getInstance().getReference("Tax Details")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = HomeLayoutBinding.bind(view)
    }
}
