package com.eden.nails.adapter

import android.animation.ObjectAnimator
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ScrollCaptureCallback
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.eden.nails.R
import com.eden.nails.databinding.ItemOurServiceTitleBinding
import com.eden.nails.databinding.ItemOurServicesBinding
import com.eden.nails.model.Service
import com.eden.nails.model.ServiceListModel


class OurServicesTitleAdapter(
    private val items: ArrayList<ServiceListModel>,
    val callback: (ServiceListModel) -> Unit
) : RecyclerView.Adapter<OurServicesTitleAdapter.ViewHolder>() {

    lateinit var ourServicesAdapter: OurServicesAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemOurServiceTitleBinding.inflate(
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
        private val binding: ItemOurServiceTitleBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bindData(banner: ServiceListModel) {

            binding.tvTitle.text = banner.serviceTitle
            if (banner.isExpanded){
                binding.rvOurServices.visibility = View.VISIBLE
                binding.ivArrow.setImageResource(R.drawable.ic_arrow_up)
            }else{
                binding.rvOurServices.visibility = View.GONE
                binding.ivArrow.setImageResource(R.drawable.ic_arrow_down)
            }
            ourServicesAdapter = OurServicesAdapter(banner.serviceList as ArrayList<Service>)
            binding.rvOurServices.adapter = ourServicesAdapter

            binding.root.setOnClickListener {
                callback.invoke(banner)
               // binding.cbService.performClick()
            }

        }
    }


}