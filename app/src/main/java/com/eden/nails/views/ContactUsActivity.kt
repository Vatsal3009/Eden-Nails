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
import com.eden.nails.databinding.ActivityContactUsBinding
import com.eden.nails.utils.openAppRating
import com.eden.nails.utils.openInstagram
import com.eden.nails.utils.openWhatsAppChat

class ContactUsActivity : BaseActivity<ActivityContactUsBinding>() {
    override fun initControl() {

        binding.icInstagram.setOnClickListener {
            openInstagram(this)
        }
        binding.btnRateUs.setOnClickListener {
            openAppRating(this)
        }

        binding.icWhatsapp.setOnClickListener {
            openWhatsAppChat(this)
        }

        binding.icBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun getViewBinding(): ActivityContactUsBinding =
        ActivityContactUsBinding.inflate(layoutInflater)

    companion object {
        fun callContactUsActivity(context: Context): Intent {
            return Intent(context, ContactUsActivity::class.java)
        }
    }

}