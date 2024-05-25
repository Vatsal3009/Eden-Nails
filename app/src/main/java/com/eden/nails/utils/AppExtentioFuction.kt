package com.eden.nails.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AlertDialog
import com.eden.nails.R
import com.eden.nails.model.Service
import java.math.BigDecimal

inline fun <T> justTry(tryBlock: () -> T) = try {
    tryBlock()
} catch (e: Throwable) {
    e.printStackTrace()
}


/**Null value check*/
fun String?.nullSafe(defaultValue: String = ""): String {
    return this ?: defaultValue
}

fun Int?.nullSafe(defaultValue: Int = 0): Int {
    return this ?: defaultValue
}

fun Float?.nullSafe(defaultValue: Float = 0.0f): Float {
    return this ?: defaultValue
}

fun Long?.nullSafe(defaultValue: Long = 0L): Long {
    return this ?: defaultValue
}

fun Double?.nullSafe(defaultValue: Double = 0.0): Double {
    return this ?: defaultValue
}

fun BigDecimal?.nullSafe(defaultValue: BigDecimal = BigDecimal(0)): BigDecimal {
    return this ?: defaultValue
}

fun Boolean?.nullSafe(defaultValue: Boolean = false): Boolean {
    return this ?: defaultValue
}

fun Any?.isNull() = this == null

fun Any?.notNull() = this != null

fun constructWhatsappMessage(selectedServices: List<Service>, chosenDate: String?): String {
    val servicesMessage = StringBuilder()
    for (service in selectedServices) {
        if (service.isServiceSelected) {
            servicesMessage.append("- ")
            servicesMessage.append(service.title)
           // servicesMessage.append(": ₹${service.price}")
            servicesMessage.append("\n")
        }
    }
    // Remove the trailing comma and space
    if (servicesMessage.isNotEmpty()) {
        servicesMessage.delete(servicesMessage.length - 2, servicesMessage.length)
    }

    val dateMessage =
        chosenDate ?: "No date selected" // If no date is selected, display a default message

    return "Hello Mansi,\n\nI would like to schedule the following nail services for $dateMessage.\n\n$servicesMessage.\n\nThank you!"
}

fun openInstagram(activity:Activity){
    val instagramUsername = "eden.nails._"
    val uri = Uri.parse("http://instagram.com/_u/$instagramUsername")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.instagram.android")

    try {
        activity.startActivity(intent)
    } catch (e: Exception) {
        // Instagram app not installed, fallback to browser
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/$instagramUsername"))
        activity.startActivity(webIntent)
    }
}

fun openAppRating(context: Context) {
    val appPackageName = context.packageName
    try {
        // Open app in Google Play Store
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$appPackageName")
            )
        )
    } catch (e: android.content.ActivityNotFoundException) {
        // If Google Play Store app is not installed, open in web browser
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
            )
        )
    }
}

fun openWhatsAppChat(context: Context) {
    // Create a Uri with the WhatsApp URL
    val uri = Uri.parse("https://wa.me/$+917567155240")

    // Create an Intent with ACTION_VIEW and the Uri
    val intent = Intent(Intent.ACTION_VIEW, uri)

    // Set the package to com.whatsapp to ensure it opens in WhatsApp if it's installed
    intent.setPackage("com.whatsapp")

    // Check if there's an activity to handle this intent before starting it
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        // If WhatsApp is not installed, you can show a message or take alternative action
        // For example, open WhatsApp in the browser
        val browserIntent = Intent(Intent.ACTION_VIEW, uri)
        context.startActivity(browserIntent)
    }
}
fun Context.checkInternetConnected(): Boolean {
    var isConnected = false
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                isConnected = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
    } else {
        cm?.run {
            @Suppress("DEPRECATION")
            cm.activeNetworkInfo?.run {
                if (type == ConnectivityManager.TYPE_WIFI) {
                    isConnected = true
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    isConnected = true
                }
            }
        }
    }
    return isConnected
}

fun showOkDialog(
    context: Context,
    title: String = context.getString(R.string.app_name),
    message: String,
    isFinish: Boolean = false
) {
    val alertDialog = AlertDialog.Builder(context)
    alertDialog.setTitle(title)
    alertDialog.setMessage(message)
    alertDialog.setPositiveButton("Ok") { dialog, _ ->
        if (isFinish) {
            val activity = context as Activity
            activity.finishAffinity()
        } else {
            dialog.dismiss()
        }

    }
    alertDialog.show()
}

