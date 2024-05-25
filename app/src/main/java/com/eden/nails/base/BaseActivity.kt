package com.eden.nails.base

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.eden.nails.utils.ActivityLauncher
import com.eden.nails.utils.ActivityLauncher.registerActivityForResult
import com.eden.nails.utils.checkInternetConnected
import com.eden.nails.utils.showOkDialog
import com.google.firebase.FirebaseApp


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    abstract fun initControl()
    protected abstract fun getViewBinding(): VB


    protected lateinit var activityLauncher: ActivityLauncher<Intent, ActivityResult>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)

        if (checkInternetConnected()) {
            FirebaseApp.initializeApp(this)
        } else {
            showOkDialog(this, message = "No Internet Connection")
        }


        initControl()
        activityLauncher = registerActivityForResult(this)

        /*  if (isInternetAvailable(this)) {
              // Internet is available, proceed with your network request or action
          } else {
              // Internet is not available, show the custom dialog
              showNoInternetDialog(this)
          }*/

    }


}