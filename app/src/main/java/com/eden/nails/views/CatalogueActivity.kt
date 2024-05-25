package com.eden.nails.views

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.eden.nails.R
import com.eden.nails.adapter.ImageListAdapter
import com.eden.nails.base.BaseActivity
import com.eden.nails.databinding.ActivityCatelogueBinding
import com.eden.nails.databinding.BottomSheetSelectDateBinding
import com.eden.nails.databinding.DialogImageViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CatalogueActivity : BaseActivity<ActivityCatelogueBinding>() {

    override fun initControl() {
        binding.loader.visibility = View.VISIBLE
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("catalogue")

        imageRef.listAll().addOnSuccessListener { listResult ->
            val imageUris = mutableListOf<Uri>()

            // Iterate over each item in the list
            listResult.items.forEach { item ->
                // Get the download URL for each item (image)
                item.downloadUrl.addOnSuccessListener { uri ->
                    // Add the download URL to the list of image URIs
                    imageUris.add(uri)

                    if (imageUris.size == listResult.items.size) {
                        binding.loader.visibility = View.GONE
                        binding.rvImageList.adapter = ImageListAdapter(imageUris) {
                            showImageDialog(this, it)
                        }
                    }
                }.addOnFailureListener { exception ->
                    // Handle any errors while retrieving download URLs
                    binding.loader.visibility = View.GONE
                    // Log or display the error
                }
            }
        }.addOnFailureListener { exception ->
            // Handle any errors while listing items in the directory
            binding.loader.visibility = View.GONE
            // Log or display the error
        }

        binding.icBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    fun showImageDialog(context: Context, imageUrl: Uri) {
        val dialogueBinding = DialogImageViewBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(context)
        dialogueBinding.apply {
            loader.visibility = View.VISIBLE
            Glide.with(context)
                .load(imageUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        loader.visibility = View.GONE
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        loader.visibility = View.GONE
                        return false
                    }
                })
                .into(imageView)

            closeButton.setOnClickListener {
                dialog.dismiss()
            }
            btnBookAppointment.setOnClickListener {
                activityLauncher.launch(OurServicesActivity.callOurServicesActivity(this@CatalogueActivity))
                dialog.dismiss()
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        dialog.behavior.isDraggable = false
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(dialogueBinding.root)
        dialog.show()
    }


    override fun getViewBinding(): ActivityCatelogueBinding =
        ActivityCatelogueBinding.inflate(layoutInflater)

    companion object {
        fun callCatalogueActivity(context: Context): Intent {
            return Intent(context, CatalogueActivity::class.java)
        }
    }


}