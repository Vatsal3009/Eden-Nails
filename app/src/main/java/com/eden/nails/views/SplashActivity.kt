package com.eden.nails.views

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.eden.nails.R
import com.eden.nails.base.BaseActivity
import com.eden.nails.databinding.ActivitySplashBinding
import com.eden.nails.views.MainActivity.Companion.callMainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun getViewBinding(): ActivitySplashBinding =
        ActivitySplashBinding.inflate(layoutInflater)

    override fun initControl() {
        lifecycleScope.launch {
            delay(2000)
            activityLauncher.launch(callMainActivity(this@SplashActivity))
            finishAffinity()
        }
    }
}