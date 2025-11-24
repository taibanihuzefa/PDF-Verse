@file:Suppress("DEPRECATION", "unused")

package com.example.pdfverse.utils

import android.app.Activity
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import com.example.pdfverse.utils.Constants.Companion.DEVICE_HEIGHT
import com.example.pdfverse.utils.Constants.Companion.DEVICE_WIDTH


fun View.setMargin(
    activity: Activity,
    start: Float = 0f,
    end: Float = 0f,
    top: Float = 0f,
    bottom: Float = 0f,
    all: Float = 0f
) {
    val param = this.layoutParams as ViewGroup.MarginLayoutParams
    if (all > 0f) {
        param.setMargins(
            widthPer(activity, all).toInt(),
            heightPer(activity, all).toInt(),
            widthPer(activity, all).toInt(),
            heightPer(activity, all).toInt()
        )
    } else {
        param.setMargins(
            widthPer(activity, start).toInt(),
            heightPer(activity, top).toInt(),
            widthPer(activity, end).toInt(),
            heightPer(activity, bottom).toInt()
        )
    }
    this.layoutParams = param // Tested!! - You need this line for the params to be applied.

}


fun View.setPadding(
    activity: Activity,
    start: Float = 0f,
    end: Float = 0f,
    top: Float = 0f,
    bottom: Float = 0f,
    all: Float = 0f
) {
    if (all > 0f) {
        this.setPadding(
            widthPer(activity, all).toInt(),
            heightPer(activity, all).toInt(),
            widthPer(activity, all).toInt(),
            heightPer(activity, all).toInt()
        )
    } else {
        this.setPadding(
            widthPer(activity, start).toInt(),
            heightPer(activity, top).toInt(),
            widthPer(activity, end).toInt(),
            heightPer(activity, bottom).toInt()
        )
    }
}


private fun heightPer(activity: Activity, value: Float): Float {
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels
    val per = (value / DEVICE_HEIGHT).times(100)
    return (height * per) / 100
}

@JvmName("heightPer1")
fun Activity.heightPer(value: Float): Float {
    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val height = displayMetrics.heightPixels
    val per = (value / DEVICE_HEIGHT).times(100)
    return (height * per) / 100
}


private fun widthPer(activity: Activity, value: Float): Float {
    val displayMetrics = DisplayMetrics()
    activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val width = displayMetrics.widthPixels
    val per = (value / DEVICE_WIDTH).times(100)
    return (width * per) / 100
}

@JvmName("widthPer1")
fun Activity.widthPer(value: Float): Float {
    val displayMetrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(displayMetrics)
    val width = displayMetrics.widthPixels
    val per = (value / DEVICE_WIDTH).times(100)
    return (width * per) / 100
}

fun View.setHeightWidth(
    activity: Activity, height: Float, width: Float
) {
    this.layoutParams.height = heightPer(activity, height).toInt()
    this.layoutParams.width = widthPer(activity, width).toInt()
}


fun View.setHeight(
    activity: Activity, height: Float
) {
    this.layoutParams.height = heightPer(activity, height).toInt()
}


fun View.setWidth(
    activity: Activity, width: Float
) {
    this.layoutParams.width = widthPer(activity, width).toInt()
}

////custom fun
//fun loadOnWebView(activity: Activity, pdfUrl: String?, pdfPageTitle: String? = null) {
//    pdfUrl?.let {
//        val intent = Intent(activity, PdfViewerActivity::class.java)
//        intent.putExtra("pdfUrl", pdfUrl)
//        intent.putExtra("pdfPageTitle", pdfPageTitle)
//        activity.startActivity(intent)
//        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
//    }
//}