package com.wahyu.hirejobengineer.dashboard.home.detail.portofolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.wahyu.hirejobengineer.R
import com.wahyu.hirejobengineer.databinding.ItemRvPortofolioBinding

class PortofolioAdapter : RecyclerView.Adapter<PortofolioAdapter.PortofolioHolder>() {

    private val items = mutableListOf<PortofolioModel>()

    private fun getPhotoImage(file: String): String = "http://34.234.66.114:8080/uploads/$file"

    fun addList(list: List<PortofolioModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    class PortofolioHolder(val binding: ItemRvPortofolioBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortofolioHolder {
        return PortofolioHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_rv_portofolio, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PortofolioHolder, position: Int) {
        val item = items[position]
        Picasso.get().load(getPhotoImage(item.portoImage!!)).into(holder.binding.ivImage)
        holder.binding.root.setOnClickListener {
            val ft = (holder.binding.root.context as AppCompatActivity).supportFragmentManager.beginTransaction()
            DialogPortofolioFragment(item.appName, item.description, item.publishLink, item.repoLink, item.type).show(ft, "Dialog")
        }
    }

    override fun getItemCount(): Int = items.size
}