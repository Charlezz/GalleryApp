package com.charlezz.galleryapp.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.charlezz.galleryapp.databinding.VhGalleryBinding

class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    val items = ArrayList<GalleryData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = VhGalleryBinding.inflate(
                inflater,
                parent,
                false)

        return GalleryViewHolder(binding)
    }

    fun addItem(item: GalleryData, update: Boolean) {
        items.add(item)
        if (update) {
            notifyItemChanged(items.lastIndex)
        }
    }

    fun clearItems() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.binding.galleryData = items[position]
        holder.binding.executePendingBindings()
    }

    class GalleryViewHolder(binding: VhGalleryBinding) : RecyclerView.ViewHolder(binding.root) {
        val binding = binding
    }
}