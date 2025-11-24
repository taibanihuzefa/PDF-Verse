@file:Suppress("NAME_SHADOWING", "SENSELESS_COMPARISON")

package com.example.pdfverse.utils

import android.app.Activity
import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder


object Alert {
//    private fun createAlert(
//        context: Activity?,
//        title: String?, message: String
//    ): AlertDialog.Builder {
//        /* dialog = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) AlertDialog.Builder(
//             ContextThemeWrapper(
//                 context,
//                 R.style.Theme_Material_Light_Dialog_Alert
//             )
//         ) else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) AlertDialog.Builder(
//             ContextThemeWrapper(
//                 context,
//                 R.style.Theme_Holo_Light_Dialog
//             )
//         ) else        AlertDialog.Builder(context)*/
//
//        val dialog: AlertDialog.Builder = AlertDialog.Builder(context!!, R.style.myDialogTheme)
//
//        //  dialog.setIcon(R.mipmap.ic_launcher);
//        if (title != null) dialog.setTitle(title) else dialog.setTitle("Information")
//        dialog.setMessage(message)
//        dialog.setCancelable(false)
//        return dialog
//    }

//    private fun createCustomAlert(
//        context: Activity,
//        title: String?, message: String, layout: Int
//    ): AlertDialog.Builder {
//        val dialog: AlertDialog.Builder =
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) AlertDialog.Builder(
//                ContextThemeWrapper(
//                    context,
//                    R.style.myDialogTheme
//                )
//            ) else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) AlertDialog.Builder(
//                ContextThemeWrapper(
//                    context,
//                    R.style.myDialogTheme
//                )
//            ) else AlertDialog.Builder(context, R.style.myDialogTheme)
//        val inflater = context.layoutInflater
//        val dialogView = inflater.inflate(layout, null)
//        dialog.setView(dialogView)
//        if (title != null) dialog.setTitle(title) else dialog.setTitle("Information")
//        dialog.setMessage(message)
//        dialog.setCancelable(false)
//        return dialog
//    }

//    fun alert(
//        context: Context?, title: String?, message: String,
//        negativeButton: String?, positiveButton: String?,
//        negativeRunnable: Runnable?, positiveRunnable: Runnable?
//    ) {
//        val dialog =
//            createAlert(context as Activity?, title, message)
//        if (negativeButton != null) {
//            dialog.setNegativeButton(
//                negativeButton
//            ) { dialog, which ->
//                dialog.cancel()
//                negativeRunnable?.run()
//            }
//        }
//        if (positiveButton != null) {
//            dialog.setPositiveButton(
//                positiveButton
//            ) { dialog, _ ->
//                dialog.dismiss()
//                positiveRunnable?.run()
//            }
//        }
//        dialog.show()
//    }

//    fun alertCustomView(
//        context: Activity, title: String?, message: String,
//        negativeButton: String?, positiveButton: String?,
//        negativeRunnable: Runnable?, positiveRunnable: Runnable?, layout: Int
//    ) {
//        val dialog =
//            createCustomAlert(context, title, message, layout)
//        if (negativeButton != null) {
//            dialog.setNegativeButton(
//                negativeButton
//            ) { dialog, _ ->
//                dialog.cancel()
//                negativeRunnable?.run()
//            }
//        }
//        if (positiveButton != null) {
//            dialog.setPositiveButton(
//                positiveButton
//            ) { dialog, _ ->
//                dialog.dismiss()
//                positiveRunnable?.run()
//            }
//        }
//        dialog.show()
//    }

    fun showMaterialAlertDialog(
        context: Context?,
        title: Int?,
        message: Int,
        positiveButtonTitle: Int?,
        negativeButtonTitle: Int,
        negativeButtonAction: Runnable?,
        positiveButtonAction: Runnable?
    ) {
        val dialog =
            createMaterialAlert(context as Activity?, title, message)

        if (positiveButtonTitle != null) {
            dialog.setPositiveButton(
                positiveButtonTitle
            ) { dialog, _ ->
                dialog.dismiss()
                positiveButtonAction?.run()
            }
        }
        if (negativeButtonTitle != null) {
            dialog.setNegativeButton(
                negativeButtonTitle
            ) { dialog, _ ->
                dialog.cancel()
                negativeButtonAction?.run()
            }
        }
        dialog.show()
    }

    private fun createMaterialAlert(
        context: Activity?,
        title: Int?,
        message: Int
    ): MaterialAlertDialogBuilder {

        val dialog = MaterialAlertDialogBuilder(context!!)

        //  dialog.setIcon(R.mipmap.ic_launcher);
        if (title != null) dialog.setTitle(title) else dialog.setTitle("Information")
        dialog.setMessage(message)
        dialog.setCancelable(false)
        return dialog
    }
}