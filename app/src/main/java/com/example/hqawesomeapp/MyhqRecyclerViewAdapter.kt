package com.example.hqawesomeapp

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.example.hqawesomeapp.placeholder.PlaceholderContent.PlaceholderItem
import com.example.hqawesomeapp.databinding.FragmentItemBinding


interface HQItemListener{
    fun onItemSelected(position: Int)
}
class MyhqRecyclerViewAdapter(
    private val values: List<PlaceholderItem>,
    private val listener: HQItemListener
) : RecyclerView.Adapter<MyhqRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)


        holder.view.setOnClickListener{
            listener.onItemSelected(position)
        }

    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(val binding: FragmentItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val view: View = binding.root

        fun bind(item: PlaceholderItem){
            binding.hqItem = item
            binding.executePendingBindings()
        }

    }

}