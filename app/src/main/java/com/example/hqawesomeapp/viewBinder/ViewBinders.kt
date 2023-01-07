package com.example.hqawesomeapp.viewBinder

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("scrUrl")
fun ImageView.bindScrUrl(
    url: String
) {
    Glide
        .with(this)
        .load(url)
        .centerInside()
        .into(this)
}