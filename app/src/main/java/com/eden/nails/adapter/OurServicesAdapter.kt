package com.eden.nails.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eden.nails.R
import com.eden.nails.databinding.ItemOurServicesBinding
import com.eden.nails.model.Service


class OurServicesAdapter(
    private val items: ArrayList<Service>
) : RecyclerView.Adapter<OurServicesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOurServicesBinding.inflate(
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
        private val binding: ItemOurServicesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(banner: Service) {

            if (banner.specialOffer=="1"){
                binding.tvOfferPrice.visibility = View.VISIBLE
                binding.tvOffer.visibility = View.VISIBLE
            }else{
                binding.tvOfferPrice.visibility = View.GONE
                binding.tvOffer.visibility = View.GONE
            }

            binding.tvTitle.text = banner.title.toString()
            binding.tvPrice.text =
                itemView.context.getString(R.string.price, banner.price.toString())

            binding.tvOfferPrice.text =
                itemView.context.getString(R.string.price, banner.offerPrice.toString())

            binding.tvOfferPrice.setPaintFlags(binding.tvOfferPrice.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG)
            binding.cbService.isChecked = banner.isServiceSelected
            binding.cbService.setOnCheckedChangeListener { _, isChecked ->
                banner.isServiceSelected = isChecked
            }

            binding.root.setOnClickListener {
                binding.cbService.performClick()
            }

        }
    }

}