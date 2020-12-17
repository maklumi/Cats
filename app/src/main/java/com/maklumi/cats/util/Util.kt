package com.maklumi.cats.util

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.maklumi.cats.R

fun lukisanPutaran(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun ImageView.muatturun(uri: String?, putarputar: CircularProgressDrawable) {
    val requestOptions = RequestOptions()
        .placeholder(putarputar)
        .error(R.mipmap.ic_launcher)
    Glide.with(context)
        .setDefaultRequestOptions(requestOptions)
        .load(uri)
        .transition(withCrossFade())
        .into(this)
}

@BindingAdapter("itemCat_url")
fun muatGambar(view: ImageView, url: String?) {
    view.muatturun(url, lukisanPutaran(view.context))
}