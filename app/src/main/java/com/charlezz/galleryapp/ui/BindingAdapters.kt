package com.charlezz.galleryapp.ui

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(iv: ImageView, path: String) {
        Glide.with(iv).load(path).into(iv)
    }

}