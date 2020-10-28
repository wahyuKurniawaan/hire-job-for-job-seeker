package com.wahyu.hirejobengineer.dashboard.offers

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wahyu.hirejobengineer.R
import com.wahyu.hirejobengineer.dashboard.offers.offersBy.OfferedByActivity
import com.wahyu.hirejobengineer.databinding.ItemRvOffersBinding
import java.text.DecimalFormat

class OffersAdapter : RecyclerView.Adapter<OffersAdapter.OffersHolder>() {

    private val items = mutableListOf<OffersModel>()
    private fun getPhotoImage(file: String): String = "http://34.234.66.114:8080/uploads/$file"
    private val dec = DecimalFormat("#,###")

    fun addList(list: List<OffersModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class OffersHolder(val binding: ItemRvOffersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OffersHolder {
        return OffersHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_rv_offers, parent, false))
    }

    override fun onBindViewHolder(holder: OffersHolder, position: Int) {
        val item = items[position]
        val getPriceFormat = "Rp ${dec.format(item.price)}"
        holder.binding.tvName.text = item.name
        holder.binding.tvDescription.text = item.projectDescription
        holder.binding.tvPrice.text = getPriceFormat
        holder.binding.tvDuration.text = item.duration
        holder.binding.tvRecruiter.text = item.offeredBy
        Picasso.get().load(getPhotoImage(item.image!!)).into(holder.binding.ivImage)

        when (item.status) {
            "waiting for response" -> {
                holder.binding.tvStatusWaiting.visibility = View.VISIBLE
                holder.binding.tvStatusApproved.visibility = View.GONE
                holder.binding.tvStatusRejected.visibility = View.GONE
            }
            "approved" -> {
                holder.binding.tvStatusWaiting.visibility = View.GONE
                holder.binding.tvStatusApproved.visibility = View.VISIBLE
                holder.binding.tvStatusRejected.visibility = View.GONE
            }
            "rejected" -> {
                holder.binding.tvStatusWaiting.visibility = View.GONE
                holder.binding.tvStatusApproved.visibility = View.GONE
                holder.binding.tvStatusRejected.visibility = View.VISIBLE
            }
        }
        holder.binding.root.setOnClickListener {
            val intent = Intent(holder.binding.root.context, OfferedByActivity::class.java)
            intent.putExtra("EMAIL_RECRUITER", item.offeredBy)
            intent.putExtra("ID_OFFER", item.id)
            Toast.makeText(holder.binding.root.context, "${item.offeredBy}", Toast.LENGTH_SHORT).show()
            holder.binding.root.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = items.size
}