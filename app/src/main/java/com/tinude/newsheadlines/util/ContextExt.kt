package com.tinude.newsheadlines.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.tinude.newsheadlines.R

/**
 * Extensions for Contexts
 *
 */
fun Context.toast(message: String?, length: Int = Toast.LENGTH_LONG) {
    if (message?.isNotBlank() == true) {
        Toast.makeText(this, message, length).show()
    }
}
@SuppressLint("ResourceType")
fun Context.openUrlInApp(url: String) {

    try {
        val packageName = "com.android.chrome"

        val activity = (this as? Activity)

        val builder = CustomTabsIntent.Builder()
        builder.setShowTitle(true)
        builder.setInstantAppsEnabled(true)

        val params = CustomTabColorSchemeParams.Builder()
            .setNavigationBarColor(
                ContextCompat.getColor(this, android.graphics.Color.BLACK )
            ).build()
        builder.setDefaultColorSchemeParams(params)

        val customBuilder = builder.build()

        if (packageName != null) {
            customBuilder.intent.setPackage(packageName)
            customBuilder.launchUrl(this, Uri.parse(url))
        } else {
            val i = Intent(ACTION_VIEW, Uri.parse(url))

            activity?.startActivity(i)
        }
    } catch (ex: Exception) {
        ex.localizedMessage
        toast("Please, ensure Google Chrome is installed on your device!")
    }
    fun Context.openUrlInApp2(url: String?) {
        var adjustedUrl = url
        if (url.isNullOrEmpty()) {
            return
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            adjustedUrl = "http://$url"
        }

        val uri = Uri.parse(adjustedUrl)

        val intentBuilder = CustomTabsIntent.Builder()
        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.black))
        intentBuilder.setSecondaryToolbarColor(
            ContextCompat.getColor(this, R.color.black)
        )
        try {
            intentBuilder.build().launchUrl(this, uri)
        } catch (exception: ActivityNotFoundException) {
            exception.printStackTrace()
            toast("You do not have any browser installed.")
        }
    }
}
