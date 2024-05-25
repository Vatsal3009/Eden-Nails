package com.eden.nails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eden.nails.databinding.ItemHomeBinding
import com.eden.nails.model.HomeData

class HomeAdapter(
    private val items: ArrayList<HomeData>,
   private val callBack: (HomeData) -> Unit
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(
        private val binding: ItemHomeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(banner: HomeData) {

            binding.tvTitle.text = banner.title.toString()
            binding.icImage.setImageResource(banner.image)
            binding.root.setOnClickListener {
                callBack(banner)
            }
        }
    }
}