package com.eden.nails.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.eden.nails.R


@GlideModule
class SampleGlideModule : AppGlideModule()

@SuppressLint("CheckResult")
fun ImageView.loadImageViaGlide(
    value: String? = null,
    drawable: Int? = null,
    bitmap: Bitmap? = null,
    uri: Uri? = null,
    isCircle: Boolean = false,
    showLoading: Boolean = true,
    largeLoading: Boolean = false,
    placeholderRes: Int? = R.mipmap.ic_launcher,
    roundRadius: Int? = null,
    newContext: Context? = null
) {

    val currentContext = newContext ?: context

    if (drawable.notNull() && value.isNull() && bitmap.isNull()) {
        drawable?.let {
            setImageResource(drawable)
        }
    } else {
        val requestOptions = RequestOptions().apply {
            if (isCircle) {
                circleCrop()
            }
            if (roundRadius.nullSafe() > 0) {
                transform(CenterCrop(), RoundedCorners(roundRadius!!))
            }

            error(placeholderRes.nullSafe(R.mipmap.ic_launcher))

            if (showLoading) {
                val circularProgressDrawable = CircularProgressDrawable(currentContext)
                circularProgressDrawable.setColorSchemeColors(
                    ContextCompat.getColor(
                        context,
                        R.color.black
                    )
                )
                circularProgressDrawable.strokeWidth = 5f
                if (largeLoading) {
                    circularProgressDrawable.centerRadius = 60f
                } else {
                    circularProgressDrawable.centerRadius = 30f
                }
                circularProgressDrawable.start()
                placeholder(circularProgressDrawable)
            } else {
                placeholder(placeholderRes.nullSafe(R.mipmap.ic_launcher))
            }

        }

        if (!bitmap.isNull()) {
            Glide.with(currentContext)
                .asBitmap()
                .load(bitmap)
                .thumbnail(0.1f)
                .apply(requestOptions)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this@loadImageViaGlide)

        } else {
            Glide.with(currentContext).load(

                when {
                    !value.isNullOrEmpty() -> value
                    !uri.isNull() -> uri
                    else -> {
                    }
                }
            )
                .thumbnail(0.50f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(requestOptions).into(this@loadImageViaGlide)
        }
    }
}