package com.eden.nails.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.eden.nails.R
import com.eden.nails.base.BaseActivity
import com.eden.nails.databinding.ActivityAboutUsBinding

class AboutUsActivity : BaseActivity<ActivityAboutUsBinding>() {
    override fun initControl() {
binding.icBack.setOnClickListener {
    onBackPressedDispatcher.onBackPressed()
}
    }

    override fun getViewBinding(): ActivityAboutUsBinding =
        ActivityAboutUsBinding.inflate(layoutInflater)

    companion object {
        fun callAboutUsActivity(context: Context): Intent {
            return Intent(context, AboutUsActivity::class.java)
        }
    }
}