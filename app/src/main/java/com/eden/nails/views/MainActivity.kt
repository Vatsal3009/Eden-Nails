package com.eden.nails.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.eden.nails.R
import com.eden.nails.adapter.BannerAdapter
import com.eden.nails.adapter.HomeAdapter
import com.eden.nails.base.BaseActivity
import com.eden.nails.databinding.ActivityMainBinding
import com.eden.nails.model.HomeData
import com.eden.nails.utils.justTry
import com.eden.nails.views.OurServicesActivity.Companion.callOurServicesActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    private var autoScrollTime = 1500L
    private var currentPage = 0
    private var totalPage = 0
    private val handler = Handler(Looper.getMainLooper())
    private val update = object : Runnable {
        override fun run() {
            justTry {
                if (currentPage == totalPage) {
                    currentPage = 0
                }
                binding.viewPager.setCurrentItem(currentPage++, true)
                handler.postDelayed(this, autoScrollTime)
            }
        }
    }
    private val pageCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            currentPage = position
        }
    }

    override fun initControl() {
        binding.loader.visibility = View.VISIBLE
        FirebaseApp.initializeApp(this)
        loadBanner()
        loadHomeList()
        binding.btnShareApp.setOnClickListener {
            shareApp()
        }
    }

    private fun shareApp() {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Eden Nails")
            var shareMessage = "Let me recommend you this application\n\n"
            shareMessage =
                "${shareMessage}https://play.google.com/store/apps/details?id=${getPackageName()}".trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            //e.toString();
        }
    }

    private fun loadHomeList() {
        val homeList = ArrayList<HomeData>()
        homeList.clear()
        homeList.add(HomeData(1, R.drawable.ic_our_services, "Our Services"))
        homeList.add(HomeData(2, R.drawable.ic_catelogue, "Catalogue"))
        homeList.add(HomeData(3, R.drawable.ic_create_design, "Contact Us"))
        homeList.add(HomeData(4, R.drawable.ic_new_abount_us, "About Us"))

        binding.rvHome.adapter = HomeAdapter(homeList) {
            when (it.type) {
                1 -> {
                    activityLauncher.launch(callOurServicesActivity(this))
                }

                2 -> {
                    activityLauncher.launch(CatalogueActivity.callCatalogueActivity(this))
                }
                3 -> {
                    activityLauncher.launch(ContactUsActivity.callContactUsActivity(this))
                }
                4 -> {
                    activityLauncher.launch(AboutUsActivity.callAboutUsActivity(this))
                }
            }
        }
    }

    private fun loadBanner() {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("banner")
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
                        binding.icBackground.visibility = View.GONE
                        binding.viewPager.adapter = BannerAdapter(imageUris)
                        if (imageUris.size > 0) {
                            binding.viewPager. offscreenPageLimit = imageUris.size
                        }
                        totalPage = imageUris.size
                        binding.viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
                        binding.viewPager.registerOnPageChangeCallback(pageCallback)

                        binding.indicatorView.setViewPager2(binding.viewPager)
                        handler.postDelayed(update, autoScrollTime)
                    }
                }.addOnFailureListener { exception ->
                    // Handle any errors while retrieving download URLs
                    // Log or display the error
                }
            }
        }.addOnFailureListener { exception ->
            // Handle any errors while listing items in the directory
            // Log or display the error
        }
    }

    companion object {
        fun callMainActivity(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

}