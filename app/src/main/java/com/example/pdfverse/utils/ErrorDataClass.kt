@file:Suppress("unused", "UNUSED_PARAMETER", "UnusedReceiverParameter")

package com.example.pdfverse.utils

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.pdfverse.views.activities.HomeActivity

data class ErrorDataClass(
    var status: Int = 0,
    var message: String = "",
    var statusState: String = "",
)

fun Context.errorCodeHandle(responseCode: Int, errorDataClass: ErrorDataClass) {
    when (responseCode) {
        400 -> {
            showLog("TAG", "errorCode:1 >>>> $responseCode")
//            logout()
        }

        401 -> {
            logout()
            showLog("TAG", "errorCode:1 >>>> $responseCode")
            //this.showToast(errorDataClass.message)
        }
//        422 -> {
//            showLog("TAG", "errorCode: 2>>>> $responseCode")
//            this.showToast(errorDataClass.message)
//        }
//        403 -> {
//            showLog("TAG", "errorCode:3 >>>> $responseCode")
//            this.showToast(errorDataClass.message)
//        }
//        404 -> {
//            showLog("TAG", "errorCode: 4>>>> $responseCode")
//            this.showToast(errorDataClass.message)
//        }
//        405 -> {
//            showLog("TAG", "errorCode: 5>>>> $responseCode")
//            this.showToast(errorDataClass.message)
//        }
        410 -> {
            showLog("TAG", "errorCode: 6>>>> $responseCode")
            //this.showToast(errorDataClass.message)
        }


        500 -> {
            showLog("TAG", "errorCode:7>>>> $responseCode")
            // this.showToast(errorDataClass.message)
        }

    }
}

fun Context.showToast(message: Int) {
    showLog("call toast", "call toast")
    Handler(Looper.getMainLooper()).post {
        //   Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Toast.makeText(applicationContext, getString(message), Toast.LENGTH_SHORT).show()

    }
}

fun Context.showToast(message: String) {
    showLog("call toast", "call toast")
    Handler(Looper.getMainLooper()).post {
        //   Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

    }
}

fun logout() {
    SharePref(App.context!!).clearPreferences() // clear preferences
    App.context!!.startActivity(
        Intent(
            App.context!!, HomeActivity::class.java
        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    )
}