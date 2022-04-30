package com.haidev.storyapp.util

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

@BindingAdapter("srcGlide")
fun ImageView.setGlideImage(imageUrl: String?) {
    imageUrl?.let {
        if (it.substringAfterLast(".").equals("svg", true))
            GlideToVectorYou.justLoadImage(
                getParentActivity(),
                Uri.parse(
                    it
                ),
                this
            )
        else
            Glide.with(context).load(it).into(this)
    }
}