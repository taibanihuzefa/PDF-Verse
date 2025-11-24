@file:Suppress(
    "FunctionName",
    "FunctionName",
    "FunctionName",
    "LocalVariableName",
    "LocalVariableName",
    "LocalVariableName",
    "LocalVariableName",
    "LocalVariableName",
    "LocalVariableName",
    "LocalVariableName",
    "LocalVariableName",
    "LocalVariableName",
    "LocalVariableName",
    "DEPRECATION",
    "unused",
    "PARAMETER_NAME_CHANGED_ON_OVERRIDE",
    "UNUSED_PARAMETER",
    "UNUSED_VARIABLE"
)

package com.example.pdfverse.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowInsetsController
import android.webkit.MimeTypeMap
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.pdfverse.R
import com.example.pdfverse.dataModels.PdfInfo
import com.example.pdfverse.dialogs.CustomAppDialog
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.geom.Rectangle
import com.itextpdf.kernel.pdf.EncryptionConstants
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.WriterProperties
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.VerticalAlignment
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.pdmodel.PDPage
import com.tom_roush.pdfbox.pdmodel.PDPageContentStream
import com.tom_roush.pdfbox.pdmodel.common.PDRectangle
import com.tom_roush.pdfbox.pdmodel.encryption.AccessPermission
import com.tom_roush.pdfbox.pdmodel.encryption.StandardProtectionPolicy
import com.tom_roush.pdfbox.pdmodel.graphics.image.LosslessFactory
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Math.toRadians
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.math.log10
import kotlin.math.pow


private lateinit var dialog: Dialog

/**
 * This Extension has been using for showing Toast messages*/
//fun Context.showToast(message: String) {
//    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//}


//fun Context.showToast(message: String) {
//
////    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
////        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
////    } else {
////    val inflater: LayoutInflater = (this as Activity).layoutInflater
////    val layout: View = inflater.inflate(
////        R.layout.custom_toast_layout,
////        findViewById((R.id.clCustomProgress))
////    )
//
////    val toastTextView = layout.findViewById<AppCompatTextView>(R.id.toastTextView)
////    val toastImageView = layout.findViewById<AppCompatImageView>(R.id.ivAppLogo)
//
////    toastTextView.text = message
//    val toast = Toast(this)
//    toast.duration = Toast.LENGTH_SHORT
//    toast.view = layout // set the inflated layout
//    toast.show() // display the custom Toast
////    }
//}
/** to set the status bar color **/
//fun setStatusBarColor(window: Window, @ColorRes colorResId: Int) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//        // For Android 11 and above
//        window.insetsController?.setSystemBarsAppearance(
//            0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//        )
//    } else {
//        // For Android versions below Android 11
//        @Suppress("DEPRECATION") window.decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//    }
//    window.statusBarColor = ContextCompat.getColor(window.context, colorResId)
//}

fun Activity.setStatusBarAppearanceAndColor(colorResId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // For Android 11 and above
        window.insetsController?.setSystemBarsAppearance(
            0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else {
        // For Android versions below Android 11
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
    window.statusBarColor = ContextCompat.getColor(this, colorResId)
}

// Utility function to change ImageView icon color
fun ImageView.setIconColor(
    context: Context, colorResId: Int, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN
) {
    this.setColorFilter(ContextCompat.getColor(context, colorResId), mode)
}

/** to use in the fragment **/
//fun Fragment.setStatusBarColor(@ColorRes colorResId: Int) {
//    activity?.window?.let { window ->
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            // For Android 11 and above
//            window.insetsController?.setSystemBarsAppearance(
//                0, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//            )
//        } else {
//            // For Android versions below Android 11
//            @Suppress("DEPRECATION") window.decorView.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        }
//        window.statusBarColor = ContextCompat.getColor(requireContext(), colorResId)
//    }
//}


/**
 * This Extension has been using for making Status bar transparent*/
//fun Activity.makeStatusBarTransparent() {
//    if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
//    }
//    if (Build.VERSION.SDK_INT >= 19) {
//        window.decorView.systemUiVisibility =
//            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//    }
//    if (Build.VERSION.SDK_INT >= 21) {
//        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
//        window.statusBarColor = Color.TRANSPARENT
//    }/* window.insetsController?.setSystemBarsAppearance(
//         WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
//         WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//     )*/
//    val windowInsetController = ViewCompat.getWindowInsetsController(window.decorView)
//    windowInsetController?.isAppearanceLightStatusBars = true// Set status bar text color to dark
//}

//private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
//    val win = activity.window
//    val winParams = win.attributes
//    if (on) {
//        winParams.flags = winParams.flags or bits
//    } else {
//        winParams.flags = winParams.flags and bits.inv()
//    }
//    win.attributes = winParams
//}

/**
 * This Extension has been using for making views visible*/
fun View.doVisible() {
    this.visibility = View.VISIBLE
}

/**
 * This Extension has been using for hiding the views*/
fun View.doHide() {
    this.visibility = View.GONE
}

/**
 * This Extension has been using for making views Invisible */
fun View.doInVisible() {
    this.visibility = View.INVISIBLE
}

/**
 * This Extension has been using for showing alert dialog*/
//fun Activity.showAlertDialog() {
//    var dialog = CustomDialog(this)
//    dialog.show()
//}

/** Set text to the EditText**/
//fun EditText.setTextToEditText(text: String?) {
//    this.text = Editable.Factory.getInstance().newEditable(text ?: "")
//    this.setSelection(this.text.length)
//}


/**
 * This Extension has been using for starting activities with animation */
// Assume this function is in a file named ActivityExtensions.kt

fun Activity.StartActivity(
    activity: Class<*>,
    finishActivity: Boolean = false,
    finishAffinity: Boolean = false,
    extras: Bundle? = null
) {
    val intent = Intent(this, activity)
    extras?.let { intent.putExtras(it) } // Add extras if provided
    startActivity(intent)
    if (finishActivity) {
        finish()
    }
    if (finishAffinity) {
        finishAffinity()
    }
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
}


fun Double.roundOff(): Int {
    val decimalPart = this - this.toInt()
    return if (decimalPart >= 0.5) {
        this.toInt() + 1
    } else {
        this.toInt()
    }
}

/**
 * This Extension has been using for finishing activities with animation */
fun Activity.Finish() {
    this.finish()
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
}

fun Activity.fadeInFadeOutAnimation() {
    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
}

fun Context.showProgressDialog(delayMillis: Long? = null) {
    val dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.layout_custom_progress)
    dialog.show()
    // Close the dialog after 0.5 seconds default
    android.os.Handler().postDelayed({
        dialog.dismiss()
    }, delayMillis ?: 500L)
}

/*fun getFileNameFromUri(context: Context, uri: Uri): String {
    var fileName = ""
    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex != -1) {
                fileName = it.getString(displayNameIndex)
            }
        }
    }
    return fileName
}*/

fun formatMillisToDateTime(millis: Long): String {
    val date = Date(millis)
    val format = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.getDefault())
    return format.format(date)
}

fun renameFile(oldFile: File, newFileName: String): Boolean {
    val newFile = File(oldFile.parent, newFileName)
    return oldFile.renameTo(newFile)
}

fun deleteFile(file: File): Boolean {
    return file.delete()
}

fun sharePdfFile(context: Context, file: File) {
    try {
        val uri: Uri = FileProvider.getUriForFile(
            context, context.applicationContext.packageName + ".fileProvider", file
        )
        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mimeType
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        // Check if there's an app that can handle this intent
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(Intent.createChooser(intent, "Share PDF using"))
        } else {
            context.showToast("No application available to share this file")
        }
    } catch (e: Exception) {
        context.showToast("Error sharing file: ${e.message}")
    }
}

fun viewPdfFile(context: Context, file: File) {
    try {
        val uri: Uri = FileProvider.getUriForFile(
            context, context.applicationContext.packageName + ".fileProvider", file
        )

        val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(file.extension)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, mimeType)
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        // Check if there's an app that can handle this intent
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            context.showToast("No application available to view this file")
        }
    } catch (e: Exception) {
        context.showToast("Error opening file: ${e.message}")
    }
}

/*fun getFileIcon(extension: String): Int {
    return when (extension.toLowerCase(Locale.ROOT)) {
        "pdf" -> R.drawable.ic_pdf_icon
        "zip" -> R.drawable.ic_zip_icon
        "doc", "docx" -> R.drawable.ic_doc_icon
        "xlsx", "xls" -> R.drawable.ic_xlsx_icon
        "txt" -> R.drawable.ic_txt_icon
        else -> R.drawable.ic_doc_icon
    }
}*/

@DrawableRes
fun getFileIcon(extension: String) = when (extension.lowercase(Locale.ROOT)) {
    "pdf" -> R.drawable.ic_pdf_icon
    in listOf("zip", "rar", "7z") -> R.drawable.ic_zip_icon
    in listOf("doc", "docx") -> R.drawable.ic_doc_icon
    in listOf("xls", "xlsx", "csv") -> R.drawable.ic_xlsx_icon
    in listOf("txt", "log", "md") -> R.drawable.ic_txt_icon
    else -> R.drawable.ic_doc_icon
}

fun getAllFilesFromDirectory(context: Context): List<PdfInfo> {

    val pdfDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
    val pdfFiles = pdfDir.listFiles() ?: return emptyList()

    return pdfFiles.map { file ->
        PdfInfo(
            name = file.name,
            size = file.length(),
            date = file.lastModified(),
            path = file.absolutePath,
            extension = file.extension,
            nameWithoutExtension = file.nameWithoutExtension
        )
    }
}

fun openAppSetting(context: Context) {
    val intent = Intent()
    /*To open directly app permission page of the app use ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, but it only works android 11(R) or higher  */
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", context.packageName, null)
//        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    } else {
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", context.packageName, null)
    }
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    context.startActivity(intent)
}

fun getFileFromFileName(context: Context, fileName: String): File {
    val pdfFile = File(context.getExternalFilesDir(null), "Documents/PDFs/$fileName")
    return if (pdfFile.exists()) pdfFile else throw FileNotFoundException("File not found")
}

fun getNumberOfPages(context: Context, pdfUri: Uri? = null, file: File? = null): Int {
    // Open the PDF file from URI or File
    val inputStream = when {
        pdfUri != null -> context.contentResolver.openInputStream(pdfUri)
        file != null -> FileInputStream(file)
        else -> null
    }
    // Load PDF document
    val pdfReader = PdfReader(inputStream)
    val pdfDocument = PdfDocument(pdfReader)
    return pdfDocument.numberOfPages
}

@SuppressLint("DefaultLocale")
fun formatFileSize(sizeInBytes: Long): String {
    if (sizeInBytes <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(sizeInBytes.toDouble()) / log10(1024.0)).toInt()
    return String.format(
        "%.2f %s", sizeInBytes / 1024.0.pow(digitGroups.toDouble()), units[digitGroups]
    )
}


// Utility function to get the file name from Uri
@SuppressLint("Range")
fun getFileNameFromUri(context: Context, uri: Uri): String {
    var result: String? = null
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = uri.path
        val cut = result?.lastIndexOf('/')
        if (cut != -1) {
            result = result?.substring(cut!! + 1)
        }
    }
    return result ?: "watermarked"
}

fun checkAndSaveFile(
    context: Context, outputFile: File, saveAction: (File) -> Unit
) {
    if (outputFile.exists()) {
        // Show dialog to ask user for action
        val activity = context as? AppCompatActivity
        activity.let {
            CustomAppDialog(
                context,
                title = "File Exists",
                message = "The destination already has a file named \"${outputFile.name}\"",
                positiveButtonText = "Replace the file in the destination",
                negativeButtonText = "Ignore and save the file",
                customDialogButtons = object : CustomAppDialog.CustomDialogButtons {
                    override fun onYesButtonClick() {
                        // Replace the file
                        saveAction(outputFile)
                    }

                    override fun onNoButtonClick() {
                        // Save as new file with incremented name
                        val newFile = getNewFileWithIncrementedName(outputFile)
                        saveAction(newFile)
                    }
                }).show(it!!.supportFragmentManager, "")
        }
    } else {
        // Save the file directly
        saveAction(outputFile)
    }
}

fun getNewFileWithIncrementedName(file: File): File {
    var fileName = file.nameWithoutExtension
    val fileExtension = file.extension
    var counter = 1

    var newFile = File(file.parent, "$fileName($counter).$fileExtension")

    while (newFile.exists()) {
        counter++
        newFile = File(file.parent, "$fileName($counter).$fileExtension")
    }

    return newFile
}

fun isPdfEncrypted(context: Context, file: File): Boolean {
    return try {
        val document = PDDocument.load(file, Constants.OWNER_PASSWORD)
        if (document.isEncrypted) {
            document.close()
            showLog("PDF Encryption Check", "File is encrypted: ${document.isEncrypted}")
            return true
        } else {
            document.close()
            showLog("PDF Encryption Check", "File is not encrypted: ${document.isEncrypted}")
            false
        }
    } catch (e: Exception) {
        e.printStackTrace()
        showLog("PDF Encryption Check", "Error checking PDF encryption: ${e.message}")
        false/* // Open the PDF file from the provided File
         val inputStream = FileInputStream(file)
         val readerProperties = ReaderProperties()
         readerProperties.setPassword(password?.toByteArray())
         val pdfReader = PdfReader(inputStream, readerProperties)
         val pdfDocument = PdfDocument(pdfReader)

         val isEncrypted = pdfReader.isEncrypted
         pdfReader.close()*/

        // Log the result
    }
}

fun getPdfPageAsBitmap(file: File, pageIndex: Int): Bitmap? {
    var bitmap: Bitmap? = null
    try {
        val parcelFileDescriptor =
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        val pdfRenderer = PdfRenderer(parcelFileDescriptor)
        val page = pdfRenderer.openPage(pageIndex)

        bitmap = Bitmap.createBitmap(
            page.width, page.height, Bitmap.Config.ARGB_8888
        )
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()
        pdfRenderer.close()
        parcelFileDescriptor.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return bitmap
}


/*fun createPdfFromImages(
    context: Context, imageUris: List<Uri>, fileName: String
) {
    try {

        // Create a new PDF document
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }*//*
                val pdfFile = File(context.getExternalFilesDir(null), "Documents/PDFs/$fileName.pdf")
        *//*

        val outputFile = File(outputDir, "$fileName.pdf")
        val pdfWriter = PdfWriter(outputFile)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        showLog("imageUris", imageUris.toString())

        for ((index, uri) in imageUris.withIndex()) {
            if (index != 0) {
                pdfDocument.addNewPage()
            }

            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val imageData = stream.toByteArray()
            val image = Image(ImageDataFactory.create(imageData))

            // Calculate the scale factor to fit the image within the page size
            val scaleFactor = minOf(
                PageSize.A4.width / image.imageWidth, PageSize.A4.height / image.imageHeight
            )

            // Scale the image
            image.scaleAbsolute(
                image.imageWidth * scaleFactor, image.imageHeight * scaleFactor
            )

            image.apply {
                setMargins(0f, 20f, 0f, 0f)
            }

//            image.setFixedPosition(0f, 50f)


//            // Center the image on the page
//            val offsetX = (PageSize.A4.width - image.imageScaledWidth) / 2
//            val offsetY = (PageSize.A4.height - image.imageScaledHeight) / 2
//            image.setFixedPosition(offsetX, offsetY)

            // Add the image to the document
            document.add(image)
        }

//        document.close()
        // Close the document and writer
        document.close()
        pdfWriter.close()
        pdfDocument.close()

        showLog("PdfUtils", "PDF created successfully at: ${outputFile.absolutePath}")
        context.showToast("PDF created successfully")
    } catch (e: Exception) {
        showLog("PdfUtils", "Error creating PDF: ${e.message}")
        context.showToast("Error creating PDF")
    }
}*/

/*fun createPdfFromImages(
    context: Context, imageUris: List<Uri>, fileName: String
) {
    try {
        // Create a new PDF document
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        val outputFile = File(outputDir, "$fileName.pdf")
        val document = PDDocument()

        showLog("imageUris", imageUris.toString())

        for (uri in imageUris) {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            val page = PDPage(PDRectangle.A4)
            document.addPage(page)

            val contentStream = PDPageContentStream(document, page)

            // Convert bitmap to a format compatible with PDFBox
            val image = LosslessFactory.createFromImage(document, bitmap)

            val margin = 20f

            // Calculate the available width and height within the margins
            val availableWidth = page.mediaBox.width - 2 * margin
            val availableHeight = page.mediaBox.height - 2 * margin

            // Calculate the scale factor to fit the image within the available space
            val scaleFactor = minOf(
                availableWidth / image.width, availableHeight / image.height
            )

            // Scale the image
            val scaledWidth = image.width * scaleFactor
            val scaledHeight = image.height * scaleFactor

            // Calculate position to center the image within the available space
            val offsetX = margin + (availableWidth - scaledWidth) / 2
            val offsetY = margin + (availableHeight - scaledHeight) / 2

            // Draw the image on the page
            contentStream.drawImage(image, offsetX, offsetY, scaledWidth, scaledHeight)
            contentStream.close()
        }

        // Save and close the document
        document.save(outputFile)
        document.close()

        showLog("PdfUtils", "PDF created successfully at: ${outputFile.absolutePath}")
        context.showToast("PDF created successfully")
    } catch (e: Exception) {
        showLog("PdfUtils", "Error creating PDF: ${e.message}")
        context.showToast("Error creating PDF")
    }
}*/

/*fun createPdfFromImages(
    context: Context,
    imageUris: List<Uri>,
    fileName: String,
    modifiedImages: Map<Int, Pair<Bitmap?, Float>>
) {
    try {
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        val outputFile = File(outputDir, "$fileName.pdf")
        val document = PDDocument()

        for ((index, uri) in imageUris.withIndex()) {
            val bitmap = modifiedImages[index] ?: MediaStore.Images.Media.getBitmap(
                context.contentResolver,
                uri
            )
            val page = PDPage(PDRectangle.A4)
            document.addPage(page)

            val contentStream = PDPageContentStream(document, page)
            val image = LosslessFactory.createFromImage(document, bitmap)
            val margin = 10f
            val availableWidth = page.mediaBox.width - 2 * margin
            val availableHeight = page.mediaBox.height - 2 * margin
            val scaleFactor = minOf(
                availableWidth / image.width, availableHeight / image.height
            )
            val scaledWidth = image.width * scaleFactor
            val scaledHeight = image.height * scaleFactor
            val offsetX = margin + (availableWidth - scaledWidth) / 2
            val offsetY = margin + (availableHeight - scaledHeight) / 2
            contentStream.drawImage(image, offsetX, offsetY, scaledWidth, scaledHeight)
            contentStream.close()
        }

        document.save(outputFile)
        document.close()

        showLog("PdfUtils", "PDF created successfully at: ${outputFile.absolutePath}")
        context.showToast("PDF created successfully")
    } catch (e: Exception) {
        showLog("PdfUtils", "Error creating PDF: ${e.message}")
        context.showToast("Error creating PDF")
    }
}*/

fun createPdfFromImages(
    context: Context,
    imageUris: List<Uri>,
    fileName: String,
    updatedImageMap: Map<Int, Pair<Bitmap?, Float>>
) {
    try {
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Ensure unique file name
        val outputFile = getUniqueFile(outputDir, fileName, "pdf")

        PDDocument().use { document ->
            for ((index, uri) in imageUris.withIndex()) {
                val (modifiedImage, rotation) = updatedImageMap[index] ?: run {
                    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    Pair(bitmap, 0F)
                }

                val rotatedImage = modifiedImage?.let { img ->
                    val matrix = Matrix().apply { postRotate(rotation) }
                    Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
                } ?: modifiedImage

                val page = PDPage(PDRectangle.A4)
                document.addPage(page)

                PDPageContentStream(document, page).use { contentStream ->
                    val image = LosslessFactory.createFromImage(document, rotatedImage)

                    val margin = 20f

                    // Calculate the available width and height within the margins
                    val availableWidth = page.mediaBox.width - 2 * margin
                    val availableHeight = page.mediaBox.height - 2 * margin

                    val imageWidth = rotatedImage?.width?.toFloat() ?: 0F
                    val imageHeight = rotatedImage?.height?.toFloat() ?: 0F

                    val scale = minOf(availableWidth / imageWidth, availableHeight / imageHeight)

                    // Scale the image
                    val scaledWidth = imageWidth * scale
                    val scaledHeight = imageHeight * scale

                    // Calculate position to center the image within the available space
                    val xOffset = margin + (availableWidth - scaledWidth) / 2
                    val yOffset = margin + (availableHeight - scaledHeight) / 2

                    /*   val xOffset = (availableWidth - scaledWidth) / 2
                       val yOffset = (availableHeight - scaledHeight) / 2*/

                    contentStream.drawImage(
                        image, xOffset, yOffset, scaledWidth, scaledHeight
                    )
                }
            }

            document.save(FileOutputStream(outputFile))
        }

        context.showToast("PDF created successfully")
        showLog("PdfUtils", "PDF created successfully at: ${outputFile.absolutePath}")
    } catch (e: Exception) {
        showLog("PdfUtils", "Error creating PDF: ${e.message}")
        context.showToast("Error creating PDF")
    }
}

/**
 * Returns a unique file by appending a count to the file name if a file with the same name exists.
 */
fun getUniqueFile(dir: File, baseName: String, extension: String): File {
    var count = 0
    var uniqueFile: File
    do {
        val fileName = if (count == 0) "$baseName.$extension" else "$baseName($count).$extension"
        uniqueFile = File(dir, fileName)
        count++
    } while (uniqueFile.exists())
    return uniqueFile
}


/*fun compressAndZipPdf(context: Context, pdfUri: Uri) {
    try {
        // Get the name of the selected PDF file
        val pdfFileName = getFileNameFromUri(context, pdfUri).substringBeforeLast(".")
        showLog("pdfFileName", pdfFileName)
        // Define the directory to save the compressed ZIP file
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        // Define the temporary compressed PDF file
        val compressedFile = File(outputDir, "${pdfFileName}_temp.pdf")
        // Define the ZIP file with the same name as the original PDF file
        val zipFile = File(outputDir, "$pdfFileName.zip")

        // Ensure the compressed file and zip file are not leftover from previous runs
        if (compressedFile.exists()) {
            compressedFile.delete()
        }
        if (zipFile.exists()) {
            zipFile.delete()
        }

        // Open input stream for the selected PDF file
        context.contentResolver.openInputStream(pdfUri)?.use { inputStream ->
            // Compress the PDF
            val pdfReader = PdfReader(inputStream)
            val pdfWriter = PdfWriter(FileOutputStream(compressedFile)).apply {
                setCompressionLevel(CompressionConstants.BEST_COMPRESSION)
            }
            val pdfDoc = PdfDocument(pdfReader, pdfWriter)
            pdfDoc.close()
            pdfReader.close()
            pdfWriter.close()
        }

        // Create ZIP file
        FileOutputStream(zipFile).use { fos ->
            ZipOutputStream(fos).use { zos ->
                zos.putNextEntry(ZipEntry(compressedFile.name))
                FileInputStream(compressedFile).use { fis ->
                    val buffer = ByteArray(1024)
                    var len: Int
                    while (fis.read(buffer).also { len = it } > 0) {
                        zos.write(buffer, 0, len)
                    }
                }
                zos.closeEntry()
            }
        }

        // Delete the temporary compressed PDF file
        if (compressedFile.exists()) {
            compressedFile.delete()
        }

        Log.d("PDF Compression", "PDF compressed and zipped to: ${zipFile.absolutePath}")
    } catch (e: Exception) {
        Log.e("PDF Compression Error", e.message ?: "")
    }
}*/

/*fun compressAndZipPdf(context: Context, pdfUri: Uri? = null, file: File? = null) {
    try {
        // Get the name of the selected PDF file
        val pdfFileName = when {
            pdfUri != null -> getFileNameFromUri(context, pdfUri).substringBeforeLast(".")
            file != null -> file.nameWithoutExtension
            else -> throw IllegalArgumentException("Both pdfUri and file are null.")
        }
        showLog("pdfFileName", pdfFileName)

        // Define the directory to save the compressed ZIP file
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Define the temporary compressed PDF file
        val compressedFile = File(outputDir, "${pdfFileName}_temp.pdf")
        // Define the ZIP file with the same name as the original PDF file
        val zipFile = File(outputDir, "$pdfFileName.zip")

        // Ensure the compressed file and zip file are not leftover from previous runs
        if (compressedFile.exists()) {
            compressedFile.delete()
        }
        if (zipFile.exists()) {
            zipFile.delete()
        }

        // Open input stream for the selected PDF file
        val inputStream = when {
            pdfUri != null -> context.contentResolver.openInputStream(pdfUri)
            file != null -> FileInputStream(file)
            else -> null
        }

        inputStream?.use {
            // Compress the PDF
            val pdfReader = PdfReader(it)
            val pdfWriter = PdfWriter(FileOutputStream(compressedFile)).apply {
                setCompressionLevel(CompressionConstants.BEST_COMPRESSION)
            }
            val pdfDoc = PdfDocument(pdfReader, pdfWriter)
            pdfDoc.close()
            pdfReader.close()
            pdfWriter.close()
        }

        // Create ZIP file
        FileOutputStream(zipFile).use { fos ->
            ZipOutputStream(fos).use { zos ->
                zos.putNextEntry(ZipEntry(compressedFile.name))
                FileInputStream(compressedFile).use { fis ->
                    val buffer = ByteArray(1024)
                    var len: Int
                    while (fis.read(buffer).also { len = it } > 0) {
                        zos.write(buffer, 0, len)
                    }
                }
                zos.closeEntry()
            }
        }

        // Delete the temporary compressed PDF file
        if (compressedFile.exists()) {
            compressedFile.delete()
        }

        showLog("PDF Compression", "PDF compressed and zipped to: ${zipFile.absolutePath}")
        context.showToast("PDF compressed and zipped successfully")
    } catch (e: Exception) {
        showLog("PDF Compression Error", e.message ?: "")
        context.showToast("Error compressing and zipping PDF")

    }
}*/


fun compressAndZipPdf(context: Context, pdfUri: Uri? = null, file: File? = null) {
    try {
        // Get the name of the selected PDF file
        val pdfFileName = when {
            pdfUri != null -> getFileNameFromUri(context, pdfUri).substringBeforeLast(".")
            file != null -> file.nameWithoutExtension
            else -> throw IllegalArgumentException("Both pdfUri and file are null.")
        }
        showLog("pdfFileName", pdfFileName)

        // Define the directory to save the compressed ZIP file
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Define the temporary compressed PDF file
        val compressedFile = File(outputDir, "${pdfFileName}_temp.pdf")
        // Ensure a unique file name for the ZIP file
        var zipFile = File(outputDir, "$pdfFileName.zip")
        var count = 1
        while (zipFile.exists()) {
            zipFile = File(outputDir, "$pdfFileName($count).zip")
            count++
        }

        // Ensure the compressed file is not leftover from previous runs
        if (compressedFile.exists()) {
            compressedFile.delete()
        }

        // Open input stream for the selected PDF file
        val inputStream = when {
            pdfUri != null -> context.contentResolver.openInputStream(pdfUri)
            file != null -> FileInputStream(file)
            else -> null
        }

        inputStream?.use { input ->
            // Load the document with PDFBox
            PDDocument.load(input).use { document ->
                // Compress the PDF by reducing image quality, etc.
                for (page in document.pages) {
                    if (page.mediaBox != PDRectangle.A4) {
                        page.mediaBox = PDRectangle.A4
                    }
                }
                // Save the compressed PDF
                document.save(compressedFile)
            }
        }

        // Create ZIP file
        FileOutputStream(zipFile).use { fos ->
            ZipOutputStream(fos).use { zos ->
                zos.putNextEntry(ZipEntry(compressedFile.name))
                FileInputStream(compressedFile).use { fis ->
                    val buffer = ByteArray(1024)
                    var len: Int
                    while (fis.read(buffer).also { len = it } > 0) {
                        zos.write(buffer, 0, len)
                    }
                }
                zos.closeEntry()
            }
        }

        // Delete the temporary compressed PDF file
        if (compressedFile.exists()) {
            compressedFile.delete()
        }

        showLog("PDF Compression", "PDF compressed and zipped to: ${zipFile.absolutePath}")
        context.showToast("PDF compressed and zipped successfully")
    } catch (e: Exception) {
        showLog("PDF Compression Error", e.message ?: "")
        context.showToast("Error compressing and zipping PDF")
    }
}


fun unzipCompressToPdfFile(context: Context, zipFile: File) {
    try {
        // Open the ZIP file from the provided File object
        val inputStream = FileInputStream(zipFile)

        // Define the output directory
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Create a ZipInputStream to read the ZIP file
        val zipInputStream = ZipInputStream(BufferedInputStream(inputStream))
        var zipEntry: ZipEntry?
        val buffer = ByteArray(1024)

        // Iterate through each entry in the ZIP file
        while (zipInputStream.nextEntry.also { zipEntry = it } != null) {
            zipEntry?.let {
                // Check if the entry is a PDF file
                if (!it.isDirectory && it.name.endsWith(".pdf", true)) {
                    var outputFile = File(outputDir, it.name.replace("_temp.pdf", ".pdf"))

                    // Ensure unique file name
                    var baseName = outputFile.nameWithoutExtension
                    var extension = outputFile.extension
                    var count = 1
                    while (outputFile.exists()) {
                        outputFile = File(outputDir, "$baseName($count).$extension")
                        count++
                    }

                    // Write the extracted PDF to the output directory
                    FileOutputStream(outputFile).use { fileOutputStream ->
                        var len: Int
                        while (zipInputStream.read(buffer).also { len = it } > 0) {
                            fileOutputStream.write(buffer, 0, len)
                        }
                    }
                    showLog("UnzipPdf", "Extracted: ${it.name}")
                }
            }
        }

        // Close the ZipInputStream and InputStream
        zipInputStream.close()
        inputStream.close()

        context.showToast("PDF files unzipped successfully")
    } catch (e: Exception) {
        e.printStackTrace()
        showLog("UnzipPdf", "Error unzipping PDF: ${e.message}")
        context.showToast("Error unzipping PDF")
    }
}

/*fun unzipCompressToPdfFile(context: Context, zipFile: File) {
    try {
        // Open the ZIP file from the provided File object
        val inputStream = FileInputStream(zipFile)

        // Define the output directory
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Create a ZipInputStream to read the ZIP file
        val zipInputStream = ZipInputStream(BufferedInputStream(inputStream))
        var zipEntry: ZipEntry?
        val buffer = ByteArray(1024)

        // Iterate through each entry in the ZIP file
        while (zipInputStream.nextEntry.also { zipEntry = it } != null) {
            zipEntry?.let {
                // Check if the entry is a PDF file
                if (!it.isDirectory && it.name.endsWith(".pdf", true)) {
                    val outputFile = File(outputDir, it.name)
                    FileOutputStream(outputFile).use { fileOutputStream ->
                        var len: Int
                        while (zipInputStream.read(buffer).also { len = it } > 0) {
                            fileOutputStream.write(buffer, 0, len)
                        }
                    }
                    showLog("UnzipPdf", "Extracted: ${it.name}")
                }
            }
        }

        // Close the ZipInputStream and InputStream
        zipInputStream.close()
        inputStream.close()

        context.showToast("PDF files unzipped successfully")
    } catch (e: Exception) {
        e.printStackTrace()
        showLog("UnzipPdf", "Error unzipping PDF: ${e.message}")
        context.showToast("Error unzipping PDF")
    }
}*/

fun makePdfPasswordProtected(context: Context, file: File, password: String) {
    try {
        // Open the PDF file from the provided File
        val inputStream = FileInputStream(file)

        // Define the output directory
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Set the output file name
        val outputFileName = file.name.replace(".pdf", "_protected.pdf")
        val outputFile = File(outputDir, outputFileName)

        // Set up the reader and writer with password protection
        val reader = PdfReader(inputStream)
        val writerProperties = WriterProperties().setStandardEncryption(
            password.toByteArray(),
            password.toByteArray(),
            EncryptionConstants.ALLOW_PRINTING,
            EncryptionConstants.ENCRYPTION_AES_128
        )
        val writer = PdfWriter(FileOutputStream(outputFile), writerProperties)
        val pdfDoc = PdfDocument(reader, writer)

        // Close the documents
        pdfDoc.close()
        reader.close()
        writer.close()

        showLog("PasswordProtectPdf", "PDF password protected successfully.")
        context.showToast("PDF password protected successfully")
    } catch (e: Exception) {
        e.printStackTrace()
        showLog("PasswordProtectPdf", "Error protecting PDF: ${e.message}")
        context.showToast("Error protecting PDF")
    }
}

/*fun removePasswordFromPdf(context: Context, file: File, password: String) {
    try {
        // Open the PDF file from the provided File
        val inputStream = FileInputStream(file)

        // Define the output directory
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

//        // Set the output file name
        val outputFileName = file.name.replace("_protected.pdf", ".pdf")
//        val outputFile = File(outputDir, outputFileName)

//        // Set up the reader with the password and writer without password
//        val reader = PdfReader(inputStream, PdfReader.unethicalReading)
//        reader.setUnethicalReading(true)
//        reader.decryptOnOpening(password.toByteArray())
//
//        val writer = PdfWriter(FileOutputStream(outputFile))
//        val pdfDoc = PdfDocument(reader, writer)


//        // Get the output file name by removing "_protected" suffix
//        val fileNameWithoutExtension = file.nameWithoutExtension
//        val outputFileName = if (fileNameWithoutExtension.endsWith("_protected")) {
//            fileNameWithoutExtension.removeSuffix("_protected") + ".pdf"
//        } else {
//            fileNameWithoutExtension + "_unprotected.pdf"
//        }
        val outputFile = File(outputDir, outputFileName)

        // Set up the reader with the password and writer without password
        val reader = PdfReader(inputStream)
        reader.setUnethicalReading(true)
        val writer = PdfWriter(FileOutputStream(outputFile))
        val pdfDoc = PdfDocument(reader, writer)


        // Close the documents
        pdfDoc.close()
        reader.close()
        writer.close()

        showLog("RemovePasswordPdf", "Password removed from PDF successfully.")
        context.showToast("Password removed from PDF successfully")
    } catch (e: Exception) {
        e.printStackTrace()
        showLog("RemovePasswordPdf", "Error removing password from PDF: ${e.message}")
        context.showToast("Error removing password from PDF")
    }
}*/

fun makePdfWithReorderPages(context: Context, file: File, pages: List<Int>) {
    try {
        // Check if the file exists and can be read
        if (!file.exists() || !file.canRead()) {
            showLog(
                "PDF Reorder Error", "File does not exist or cannot be read: ${file.absolutePath}"
            )
            context.showToast("File does not exist or cannot be read")
            return
        }

        // Read the original PDF document
        val pdfReader = PdfReader(file)
        val originalPdfDocument = PdfDocument(pdfReader)

        // Create a temporary output file
        val outputDir = file.parentFile
        if (!outputDir!!.exists()) {
            outputDir.mkdirs()
        }
        val tempFile = File(outputDir, "temp_" + file.name)
        val pdfWriter = PdfWriter(FileOutputStream(tempFile))
        val newPdfDocument = PdfDocument(pdfWriter)

        // Copy pages to the new document in the new order
        for (pageIndex in pages) {
            if (pageIndex < 0 || pageIndex >= originalPdfDocument.numberOfPages) {
                showLog("PDF Reorder Error", "Invalid page index: $pageIndex")
                context.showToast("Invalid page index: $pageIndex")
                originalPdfDocument.close()
                newPdfDocument.close()
                pdfWriter.close()
                tempFile.delete()
                return
            }
            originalPdfDocument.copyPagesTo(pageIndex + 1, pageIndex + 1, newPdfDocument)
        }

        // Close the documents
        originalPdfDocument.close()
        newPdfDocument.close()
        pdfWriter.close()

        // Replace the original file with the new reordered file
        if (file.delete()) {
            if (tempFile.renameTo(file)) {
                // Notify the user
                showLog("PDF Reorder", "Reordered PDF saved to: ${file.absolutePath}")
                context.showToast("Reordered PDF saved successfully")
            } else {
                showLog("PDF Reorder Error", "Failed to rename temp file to original file")
                context.showToast("Failed to save reordered PDF")
            }
        } else {
            showLog("PDF Reorder Error", "Failed to delete original file")
            context.showToast("Failed to save reordered PDF")
        }

    } catch (e: Exception) {
        e.printStackTrace()
        showLog("PDF Reorder Error", "Error reordering PDF: ${e.message}")
        context.showToast("Error reordering PDF")
    }
}

fun addPasswordToPdf(context: Context, file: File, password: String) {
    try {

        val document = PDDocument.load(file, Constants.OWNER_PASSWORD)
        if (!document.isEncrypted) {
            val accessPermission = AccessPermission()
            val standardProtectionPolicy =
                StandardProtectionPolicy(Constants.OWNER_PASSWORD, password, accessPermission)
            standardProtectionPolicy.encryptionKeyLength = 128
            standardProtectionPolicy.permissions = accessPermission
            document.protect(standardProtectionPolicy)
            document.save(file)
            document.close()
        }
        context.showToast("Password added successfully")
    } catch (e: Exception) {
        e.printStackTrace()
        context.showToast("Error adding password: ${e.message}")
    }
}

fun removePasswordFromPdf(context: Context, file: File) {
    try {
        val document = PDDocument.load(file, Constants.OWNER_PASSWORD)
        document.isAllSecurityToBeRemoved = true
        document.save(file)
        document.close()

        context.showToast("Password removed successfully")
    } catch (e: Exception) {
        e.printStackTrace()
        context.showToast("Error removing password: ${e.message}")
    }
}

/*fun makePdfWithReorderPages(context: Context, file: File, pages: List<Int>) {
    try {
        // Check if the file exists and can be read
        if (!file.exists() || !file.canRead()) {
            showLog(
                "PDF Reorder Error", "File does not exist or cannot be read: ${file.absolutePath}"
            )
            context.showToast("File does not exist or cannot be read")
            return
        }

        // Read the original PDF document
        val pdfReader = PdfReader(file)
        val originalPdfDocument = PdfDocument(pdfReader)

        // Create a new output file
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val outputFile = File(outputDir, "Reordered_" + file.name)
        val pdfWriter = PdfWriter(FileOutputStream(outputFile))
        val newPdfDocument = PdfDocument(pdfWriter)

        // Copy pages to the new document in the new order
        for (pageIndex in pages) {
            if (pageIndex < 0 || pageIndex >= originalPdfDocument.numberOfPages) {
                showLog("PDF Reorder Error", "Invalid page index: $pageIndex")
                context.showToast("Invalid page index: $pageIndex")
                originalPdfDocument.close()
                newPdfDocument.close()
                pdfWriter.close()
                return
            }
            originalPdfDocument.copyPagesTo(pageIndex + 1, pageIndex + 1, newPdfDocument)
        }

        // Close the documents
        originalPdfDocument.close()
        newPdfDocument.close()
        pdfWriter.close()

        // Notify the user
        showLog("PDF Reorder", "Reordered PDF saved to: ${outputFile.absolutePath}")
        context.showToast("Reordered PDF saved successfully")

    } catch (e: Exception) {
        e.printStackTrace()
        showLog("PDF Reorder Error", "Error reordering PDF: ${e.message}")
        context.showToast("Error reordering PDF")
    }
}*/

// Example usage
// makePdfWithReorderPages(context = myActivity, file = selectedFile, pages = listOf(0, 1, 2, 3, 4)) // Example with pages list

/*fun makePdfWithReorderPages(context: Context, file: File, pages: List<Int>) {
    try {
        if (!file.exists() || !file.canRead()) {
            showLog(
                "PDF Reorder Error", "File does not exist or cannot be read: ${file.absolutePath}"
            )
            context.showToast("File does not exist or cannot be read")
            return
        }

        // Create a new PDF document in memory
        val baos = ByteArrayOutputStream()
        val pdfWriter = PdfWriter(baos)
        val newPdfDocument = PdfDocument(pdfWriter)

        // Read the original PDF document
        val pdfReader = PdfReader(file)
        val originalPdfDocument = PdfDocument(pdfReader)

        // Copy pages to the new document in the new order
        for (pageIndex in pages) {
            if (pageIndex < 0 || pageIndex >= originalPdfDocument.numberOfPages) {
                showLog("PDF Reorder Error", "Invalid page index: $pageIndex")
                context.showToast("Invalid page index: $pageIndex")
                originalPdfDocument.close()
                newPdfDocument.close()
                pdfWriter.close()
                return
            }
            originalPdfDocument.copyPagesTo(pageIndex + 1, pageIndex + 1, newPdfDocument)
        }

        // Close the documents
        originalPdfDocument.close()
        newPdfDocument.close()
        pdfWriter.close()

        // Create a new PdfReader from the reordered document
        val reader = PdfReader(baos.toByteArray())
        val startToc = 1 // or any starting point you require
        val n = reader.numberOfPages

        // Reorder pages as per specific logic
        reader.selectPages(String.format("1,%s-%s,2-%s,%s", startToc, n - 1, startToc - 1, n))

        // Write reordered document to output file
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val outputFile = File(outputDir, file.name)
        val stamper = PdfWriter(FileOutputStream(outputFile))

        val reorderedPdfDocument = PdfDocument(reader, stamper)
        reorderedPdfDocument.close()

        // Notify the user
        showLog("PDF Reorder", "Reordered PDF saved to: ${outputFile.absolutePath}")
        context.showToast("Reordered PDF saved successfully")
    } catch (e: Exception) {
        e.printStackTrace()
        showLog("PDF Reorder Error", "Error reordering PDF: ${e.message}")
        context.showToast("Error reordering PDF")
    }
}*/

/*fun compressAndZipPdf(context: Context, pdfUri: Uri? = null, file: File? = null) {
    try {
        // Get the name of the selected PDF file
        val pdfFileName = when {
            pdfUri != null -> getFileNameFromUri(context, pdfUri).substringBeforeLast(".")
            file != null -> file.nameWithoutExtension
            else -> throw IllegalArgumentException("Both pdfUri and file are null.")
        }
        showLog("pdfFileName", pdfFileName)

        // Define the directory to save the compressed ZIP file
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Define the temporary compressed PDF file
        val compressedFile = File(outputDir, "${pdfFileName}_temp.pdf")

        // Open input stream for the selected PDF file
        val inputStream = when {
            pdfUri != null -> context.contentResolver.openInputStream(pdfUri)
            file != null -> FileInputStream(file)
            else -> null
        }

        inputStream?.use {
            // Compress the PDF
            val pdfReader = PdfReader(it)
            val pdfWriter = PdfWriter(FileOutputStream(compressedFile)).apply {
                setCompressionLevel(CompressionConstants.BEST_COMPRESSION)
            }
            val pdfDoc = PdfDocument(pdfReader, pdfWriter)
            pdfDoc.close()
            pdfReader.close()
            pdfWriter.close()
        }

        // Define the ZIP file with the same name as the original PDF file
        val zipFile = File(outputDir, "$pdfFileName.zip")

        // Create ZIP file using the checkAndSaveFile function
        checkAndSaveFile(context, zipFile) { fileToSave ->
            FileOutputStream(fileToSave).use { fos ->
                ZipOutputStream(fos).use { zos ->
                    zos.putNextEntry(ZipEntry(compressedFile.name))
                    FileInputStream(compressedFile).use { fis ->
                        val buffer = ByteArray(1024)
                        var len: Int
                        while (fis.read(buffer).also { len = it } > 0) {
                            zos.write(buffer, 0, len)
                        }
                    }
                    zos.closeEntry()
                }
            }
        }

        // Delete the temporary compressed PDF file
        if (compressedFile.exists()) {
            compressedFile.delete()
        }

        Log.d("PDF Compression", "PDF compressed and zipped to: ${zipFile.absolutePath}")
    } catch (e: Exception) {
        Log.e("PDF Compression Error", e.message ?: "")
    }
}*/

/*fun compressAndZipPdf(context: Context, pdfUri: Uri? = null, file: File? = null) {
    try {
        // Get the name of the selected PDF file
        val pdfFileName = when {
            pdfUri != null -> getFileNameFromUri(context, pdfUri).substringBeforeLast(".")
            file != null -> file.nameWithoutExtension
            else -> throw IllegalArgumentException("Both pdfUri and file are null.")
        }
        showLog("pdfFileName", pdfFileName)

        // Define the directory to save the compressed ZIP file
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Define the temporary compressed PDF file
        val compressedFile = File(outputDir, "${pdfFileName}_temp.pdf")
        if (compressedFile.exists()) {
            compressedFile.delete()
        }

        // Open input stream for the selected PDF file
        val inputStream = when {
            pdfUri != null -> context.contentResolver.openInputStream(pdfUri)
            file != null -> FileInputStream(file)
            else -> null
        }

        inputStream?.use {
            // Compress the PDF
            val pdfReader = PdfReader(it)
            val pdfWriter = PdfWriter(FileOutputStream(compressedFile)).apply {
                setCompressionLevel(CompressionConstants.BEST_COMPRESSION)
            }
            val pdfDoc = PdfDocument(pdfReader, pdfWriter)
            pdfDoc.close()
            pdfReader.close()
            pdfWriter.close()
        }

        // Define the ZIP file with the same name as the original PDF file
        val zipFile = File(outputDir, "$pdfFileName.zip")

        // Check and save the ZIP file using the checkAndSaveFile function
        checkAndSaveFile(context, zipFile) { fileToSave ->
            FileOutputStream(fileToSave).use { fos ->
                ZipOutputStream(fos).use { zos ->
                    zos.putNextEntry(ZipEntry(compressedFile.name))
                    FileInputStream(compressedFile).use { fis ->
                        val buffer = ByteArray(1024)
                        var len: Int
                        while (fis.read(buffer).also { len = it } > 0) {
                            zos.write(buffer, 0, len)
                        }
                    }
                    zos.closeEntry()
                }
            }
        }

        // Delete the temporary compressed PDF file
        if (compressedFile.exists()) {
            compressedFile.delete()
        }

        showLog("PDF Compression", "PDF compressed and zipped to: ${zipFile.absolutePath}")
        context.showToast("PDF compressed and zipped successfully")
    } catch (e: Exception) {
        showLog("PDF Compression Error", e.message ?: "")
        context.showToast("Error compressing and zipping PDF")
    }
}*/

/*fun compressAndZipPdf(context: Context, pdfUri: Uri? = null, file: File? = null) {
    try {
        // Get the name of the selected PDF file
        val pdfFileName = when {
            pdfUri != null -> getFileNameFromUri(context, pdfUri).substringBeforeLast(".")
            file != null -> file.nameWithoutExtension
            else -> throw IllegalArgumentException("Both pdfUri and file are null.")
        }
        showLog("pdfFileName", pdfFileName)

        // Define the directory to save the compressed ZIP file
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Define the temporary compressed PDF file
        val compressedFile = File(outputDir, "${pdfFileName}_temp.pdf")
       if (compressedFile.exists()) {
            compressedFile.delete()
        }

        // Open input stream for the selected PDF file
        val inputStream = when {
            pdfUri != null -> context.contentResolver.openInputStream(pdfUri)
            file != null -> FileInputStream(file)
            else -> null
        }

        inputStream?.use {
            // Compress the PDF
            val pdfReader = PdfReader(it)
            val pdfWriter = PdfWriter(FileOutputStream(compressedFile)).apply {
                setCompressionLevel(CompressionConstants.BEST_COMPRESSION)
            }
            val pdfDoc = PdfDocument(pdfReader, pdfWriter)
            pdfDoc.close()
            pdfReader.close()
            pdfWriter.close()
        }

        // Define the ZIP file with the same name as the original PDF file
        val zipFile = File(outputDir, "$pdfFileName.zip")

        // Check and save the ZIP file using the checkAndSaveFile function
        checkAndSaveFile(context, zipFile) { fileToSave ->
            FileOutputStream(fileToSave).use { fos ->
                ZipOutputStream(fos).use { zos ->
                    zos.putNextEntry(ZipEntry(compressedFile.name))
                    FileInputStream(compressedFile).use { fis ->
                        val buffer = ByteArray(1024)
                        var len: Int
                        while (fis.read(buffer).also { len = it } > 0) {
                            zos.write(buffer, 0, len)
                        }
                    }
                    zos.closeEntry()
                }
            }
        }

        // Delete the temporary compressed PDF file
        if (compressedFile.exists()) {
            compressedFile.delete()
        }

        Log.d("PDF Compression", "PDF compressed and zipped to: ${zipFile.absolutePath}")
        context.showToast("PDF compressed and zipped successfully to: ${zipFile.absolutePath}")
    } catch (e: Exception) {
        Log.e("PDF Compression Error", e.message ?: "")
        context.showToast("Error compressing and zipping PDF: ${e.message}")
    }
}*/

/*fun addTextWatermarkToPdf(
    context: Context, pdfUri: Uri? = null, file: File? = null, watermarkText: String
) {
    try {
        // Get input stream from URI
        val inputStream = when {
            pdfUri != null -> context.contentResolver.openInputStream(pdfUri)
            file != null -> FileInputStream(file)
            else -> null
        }

        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
//        = context.contentResolver.openInputStream(pdfUri)
        val pdfReader = PdfReader(inputStream)

        val fileName = when {
            pdfUri != null -> getFileNameFromUri(context, pdfUri)
            file != null -> file.name
            else -> null
        }


        val outputFileName = fileName!!.replace(".pdf", "_watermarked.pdf")
//        val outputFile = File(context.getExternalFilesDir(null), "Documents/PDFs/$outputFileName")
        val outputFile = File(outputDir, outputFileName)
        // Function to save the watermarked PDF
        val saveWatermarkedPdf: (File) -> Unit = { fileToSave ->
            val pdfWriter = PdfWriter(FileOutputStream(fileToSave))
            val pdfDocument = PdfDocument(pdfReader, pdfWriter)
            val document = Document(pdfDocument)

            val numberOfPages = pdfDocument.numberOfPages

            for (i in 1..numberOfPages) {
                val pdfPage = pdfDocument.getPage(i)
                val pageSize: Rectangle = pdfPage.pageSize

                // Define the properties for the watermark text
                val paragraph = Paragraph(watermarkText).setFontSize(50f)
                    .setFontColor(ColorConstants.LIGHT_GRAY).setBold().setOpacity(0.5f)
                    .setRotationAngle(Math.toRadians(45.0)).setFixedPosition(
                        (pageSize.width - 400) / 2, // Adjust x position to center
                        (pageSize.height - 300) / 2, // Adjust y position to center
                        400f
                    )

                // Add the watermark text to the page
                document.add(
                    paragraph.setFixedPosition(
                        i, (pageSize.width - 100) / 2, (pageSize.height - 200) / 2, 400f
                    )
                )
            }

            document.close()
            pdfDocument.close()
            pdfReader.close()
            pdfWriter.close()

            context.showToast("Watermark added successfully")
        }
        // Check and save the file
        checkAndSaveFile(context, outputFile, saveWatermarkedPdf)
    } catch (e: Exception) {
        showLog("PdfUtils", "Error adding watermark: ${e.message}")
        context.showToast("Error adding watermark")
    }
}*/

fun addTextWatermarkToPdf(
    context: Context,
    pdfUri: Uri? = null,
    file: File? = null,
    watermarkText: String = "CONFIDENTIAL"
) {
    try {
        // Step 1: Open input stream
        val inputStream: InputStream = when {
            pdfUri != null -> context.contentResolver.openInputStream(pdfUri)!!
            file != null -> file.inputStream()
            else -> throw IllegalArgumentException("Either pdfUri or file must be provided")
        }

        // Step 2: Create output directory
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs").apply {
            mkdirs()
        }

        // Step 3: Determine file name
        val fileName = when {
            pdfUri != null -> getFileNameFromUri(context, pdfUri)
            file != null -> file.name
            else -> "watermarked.pdf"
        }

        val outputFile =
            File(outputDir, fileName.replace(".pdf", "_watermarked.pdf", ignoreCase = true))

        // Step 4: Process PDF with iText7
        val pdfReader = PdfReader(inputStream)
        val pdfWriter = PdfWriter(FileOutputStream(outputFile))
        val pdfDocument = PdfDocument(pdfReader, pdfWriter)
        val document = Document(pdfDocument)

        // Use built-in Helvetica Bold (no need for assets)
        val boldFont = PdfFontFactory.createFont("Helvetica-Bold")

        repeat(pdfDocument.numberOfPages) { pageNumber ->
            val pageIndex = pageNumber + 1
            val pageSize: Rectangle = pdfDocument.getPage(pageIndex).pageSize

            val watermark = Paragraph(watermarkText)
                .setFont(boldFont)
                .setFontSize(48f)
                .setFontColor(ColorConstants.LIGHT_GRAY)
                .setOpacity(0.3f) // 0.3 looks best
                .setRotationAngle(toRadians(45.0))

            // Best method: showTextAligned (perfect centering + rotation)
            document.showTextAligned(
                watermark,
                pageSize.width / 2,      // X center
                pageSize.height / 2,     // Y center
                pageIndex,
                TextAlignment.CENTER,
                VerticalAlignment.MIDDLE,
                toRadians(45.0).toFloat()
            )
        }

        // Step 5: Clean up
        document.close()
        pdfDocument.close()
        pdfWriter.close()
        pdfReader.close()
        inputStream.close()

        context.showToast("Watermark added successfully!")
        Log.d("PdfUtils", "Watermarked PDF saved: ${outputFile.absolutePath}")

    } catch (e: Exception) {
        Log.e("PdfUtils", "Error adding watermark", e)
        context.showToast("Failed to add watermark: ${e.message}")
    }
}

fun splitPdf(context: Context, pdfUri: Uri? = null, file: File? = null) {
    try {
        // Open the PDF file from URI or File
        val inputStream = when {
            pdfUri != null -> context.contentResolver.openInputStream(pdfUri)
            file != null -> FileInputStream(file)
            else -> null
        }

        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }

        // Load PDF document
        val pdfReader = PdfReader(inputStream)
        val pdfDocument = PdfDocument(pdfReader)
        val numberOfPages = pdfDocument.numberOfPages

        val fileName = when {
            pdfUri != null -> getFileNameFromUri(context, pdfUri)
            file != null -> file.name
            else -> null
        }

        // Split and save each page as a new PDF
        for (i in 1..numberOfPages) {
            val outputFileName = fileName!!.replace(".pdf", "_split($i).pdf")
            val outputFile = File(outputDir, outputFileName)

            val pdfWriter = PdfWriter(FileOutputStream(outputFile))
            val newDocument = PdfDocument(pdfWriter)
            pdfDocument.copyPagesTo(i, i, newDocument)
            newDocument.close()
        }

        pdfDocument.close()
        pdfReader.close()

        showLog("SplitPdf", "Splitted Pdf Successfully to ${outputDir.absolutePath}")
        context.showToast("Pdf splitted successfully")
    } catch (e: Exception) {
        e.printStackTrace()
        showLog("SplitPdf", "Error splitting PDF: ${e.message}")
        context.showToast("Error splitting PDF")
    }
}

/*fun mergePdfs(context: Context, pdfUris: List<Uri>, mergePdfName: String) {
    try {
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val outputFile = File(outputDir, "$mergePdfName.pdf")

        checkAndSaveFile(context, outputFile) { fileToSave ->
            val pdfWriter = PdfWriter(FileOutputStream(fileToSave))
            val pdfDocument = PdfDocument(pdfWriter)
            val document = Document(pdfDocument)

            for (pdfUri in pdfUris) {
                context.contentResolver.openInputStream(pdfUri)?.use { inputStream ->
                    val pdfReader = PdfReader(inputStream)
                    val tempPdfDocument = PdfDocument(pdfReader)
                    tempPdfDocument.copyPagesTo(1, tempPdfDocument.numberOfPages, pdfDocument)
                    tempPdfDocument.close()
                }
            }

            document.close()
            pdfDocument.close()
            pdfWriter.close()

            showLog("MergePdf", "Merged Pdf Successfully to: ${fileToSave.absolutePath}")
            context.showToast("Pdf merged successfully")
        }
    } catch (e: Exception) {
        e.printStackTrace()
        showLog("MergePdf", "Error merging PDF: ${e.message}")
        context.showToast("Error merging PDF: ${e.message}")
    }
}*/

fun mergePdfs(context: Context, pdfUris: List<Uri>, mergePdfName: String) {
    try {
        val outputDir = File(context.getExternalFilesDir(null), "Documents/PDFs")
        if (!outputDir.exists()) {
            outputDir.mkdirs()
        }
        val outputFile = File(outputDir, "$mergePdfName.pdf")

        val pdfWriter = PdfWriter(FileOutputStream(outputFile))
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        for (pdfUri in pdfUris) {
            context.contentResolver.openInputStream(pdfUri)?.use { inputStream ->
                val pdfReader = PdfReader(inputStream)
                val tempPdfDocument = PdfDocument(pdfReader)
                tempPdfDocument.copyPagesTo(1, tempPdfDocument.numberOfPages, pdfDocument)
                tempPdfDocument.close()
            }
        }

        document.close()
        pdfDocument.close()
        pdfWriter.close()

        showLog("MergePdf", "Merged Pdf Successfully to: ${outputFile.absolutePath}")
        context.showToast("Pdf merged successfully")
    } catch (e: Exception) {
        e.printStackTrace()
        showLog("MergePdf", "Error merging PDF: ${e.message}")
        context.showToast("Error merging PDF")
    }
}


/** for popup menu**/
fun View.showPopupMenu(menuResId: Int, menuItemClickListener: (itemId: Int) -> Unit) {
    // Creating the instance of PopupMenu
    val popupMenu = PopupMenu(context, this)

    // Inflating the Popup using xml file
    popupMenu.menuInflater.inflate(menuResId, popupMenu.menu)

    // Registering the popup menu item click listener
    popupMenu.setOnMenuItemClickListener { item ->
        menuItemClickListener.invoke(item.itemId)
        true
    }
    // Showing the popup menu
    popupMenu.show()
}

//fun View.animate(@AnimRes animResId: Int): Animation? {
//    clearAnimation()
//    val animation = AnimationUtils.loadAnimation(context, animResId)
//    startAnimation(animation)
//    return animation
//}

//fun Animation.then(action: () -> Unit) {
//    setAnimationListener(object : Animation.AnimationListener {
//        override fun onAnimationRepeat(animation: Animation?) {}
//        override fun onAnimationStart(animation: Animation?) {}
//        override fun onAnimationEnd(animation: Animation?) {
//            action()
//        }
//    })
//}

/**
 *This Extension has been using for check empty String
 * */
fun Any?.checkEmptyString(): Boolean {
    return if (this == null) true
    else TextUtils.isEmpty(this.toString().trim())
}

/**
 * This Extension has been using for validating email id
 * */
//fun Any?.checkEmailPattern(): Boolean {
//    return !Patterns.EMAIL_ADDRESS.matcher(this.toString()).matches()
//}


/**
 * This Extension has been using for hiding Progress dialog
 * */

fun Context.showProgress() {
    dialog = Dialog(this)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.layout_custom_progress)
    dialog.show()
}

fun hideProgress() {
    if (::dialog.isInitialized && dialog.isShowing) {
        dialog.dismiss()
    }
}

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
    observe(owner, object : Observer<T> {
        override fun onChanged(t: T) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}


/**
 * this function is use for set top layout height
 * */
fun View.setTopBar(
    activity: Activity,
) {
    this.setHeight(activity, 283f)
//    view.layoutParams.height = activity.widthPer(283f).toInt()
}


fun View.setToolBarBack(
    activity: Activity,
) {
    this.layoutParams.height = activity.widthPer(44f).toInt()
    this.layoutParams.width = activity.widthPer(44f).toInt()
}

fun View.setTopViewLogo(
    activity: Activity,
) {
    this.layoutParams.height = activity.widthPer(150f).toInt()
    this.layoutParams.width = activity.widthPer(150f).toInt()
    setMargin(activity, top = 100f)
}

fun View.setRightTopViewLogo(
    activity: Activity,
) {
    this.layoutParams.height = activity.widthPer(100f).toInt()
    this.layoutParams.width = activity.widthPer(100f).toInt()
    setMargin(activity, top = 20f, end = 30f)
}


/**
 * This Extension has been using for load image
 * */
fun ImageView.loadImage(imageUrl: String?) {
    if (imageUrl != null) {
        Glide.with(context).load(imageUrl).placeholder(R.drawable.ic_photo_to_pdf_icon)
            .error(R.drawable.ic_photo_to_pdf_icon).into(this)
    }
}

//fun ImageView.loadImage(imageUrl: Int) {
//    if (imageUrl != null) {
//        Glide.with(context).load(imageUrl)
//            .placeholder(R.drawable.ic_launcher_playstore).error(R.drawable.ic_launcher_playstore)
//            .into(this)
//    }
//}


/**
 * this function is use for set common navigation margin
 * */
fun View.setNavigateButtonMargin(
    activity: Activity,
) {
    this.setMargin(activity, start = 12f)
}

/**
 * this function is use for set navigation button height and width
 * */
fun View.setNavigateButton(
    activity: Activity,
) {
    this.layoutParams.height = activity.heightPer(17f).toInt()
    this.layoutParams.width = activity.widthPer(23f).toInt()

    this.setMargin(activity, start = 10f, end = 10f, top = 14f, bottom = 14f)
}

/**
 * this function is use for set navigation button height and width
 * */
fun Activity.setNavigateMenu(
    view: View,
    logoImage: ImageView,
    menuImageView: ImageView,
    isShowMenu: Boolean = false,
) {
    view.setMargin(this, all = 20f)
    logoImage.layoutParams.width = this.widthPer(50f).toInt()
    logoImage.layoutParams.height = this.widthPer(50f).toInt()

    if (isShowMenu) {
        menuImageView.doVisible()
        menuImageView.layoutParams.width = this.widthPer(30f).toInt()
        menuImageView.layoutParams.height = this.widthPer(30f).toInt()
    } else {
        menuImageView.doHide()
    }
}

fun requestPermissions(
    context: Context?, title: Int?, message: Int, positiveButton: Int?, negativeButton: Int
) {
    Alert.showMaterialAlertDialog(
        context, title, message, positiveButton, negativeButton, null
    ) {
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context!!.packageName)
        } else {
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.data = Uri.fromParts("package", context!!.packageName, null)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}

fun EditText.setCursor() {
    val password = this.text.toString().length
    if (password > 0) {
        this.setSelection(this.text!!.length)
    }
}

/**
 * This function is use for open website
 * */
fun Context.openWebSite(webUrl: String) {
    try {
        this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)))
    } catch (e: Exception) {
        showLog("openWebSite >>>", e.toString())
    }
}

/**
 * This function is use for set underline
 * */
fun TextView.makeUnderLine() {
    this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

/**
 * This function is use for make a phone call
 * */
fun makePhoneCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    context.startActivity(intent)
}

/**
 * This function is use for load data in webView
 * */
@SuppressLint("SetJavaScriptEnabled")
fun WebView.loadWebViewData(context: Context, content: String) {

    this.setBackgroundColor(Color.TRANSPARENT)
    this.settings.javaScriptEnabled = true
    this.clearCache(true)
    this.clearHistory()
    this.settings.loadWithOverviewMode = true
    this.settings.loadsImagesAutomatically = true

    this.settings.javaScriptCanOpenWindowsAutomatically = true
    this.settings.setSupportZoom(false)
    this.settings.builtInZoomControls = false // allow pinch to zoom
    this.settings.displayZoomControls = false // disable the default zoom controls on the page
    this.settings.useWideViewPort = false
    this.settings.mediaPlaybackRequiresUserGesture = true

//    this.loadUrl(content)

    this.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null)
    this.webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?,
        ): Boolean {
            context.openWebSite(request?.url.toString())
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            hideProgress()
        }
    }

    this.webChromeClient = object : WebChromeClient() {
        private var mCustomView: View? = null
        private var mCustomViewCallback: CustomViewCallback? = null
        private var mOriginalOrientation = 0
        private var mOriginalSystemUiVisibility = 0
        override fun onShowCustomView(view: View, callback: CustomViewCallback) {
            //do stuff
            if (mCustomView != null) {
                onHideCustomView()
                return
            }
            mCustomView = view
            mOriginalSystemUiVisibility = (context as Activity).window.decorView.systemUiVisibility
            mOriginalOrientation = context.requestedOrientation
            mCustomViewCallback = callback
            (context.window.decorView as FrameLayout).addView(
                mCustomView, FrameLayout.LayoutParams(-1, -1)
            )
            context.window.decorView.systemUiVisibility = 3846 or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        override fun onHideCustomView() {
            //do stuff
            ((context as Activity).window.decorView as FrameLayout).removeView(mCustomView)
            mCustomView = null
            context.window.decorView.systemUiVisibility = mOriginalSystemUiVisibility
            context.requestedOrientation = mOriginalOrientation
            mCustomViewCallback?.onCustomViewHidden()
            mCustomViewCallback = null
        }
    }
}

//fun String.createPartFromString(): RequestBody {
//    return this.toRequestBody("text/plain".toMediaTypeOrNull())
//}

fun setUpHyperLabel(
    textView: TextView, fullString: String,
//    initialFont: Typeface,
    initialColor: Int, subString: String, subStringColor: Int,
//    subStringFont: Typeface,
    clickHandler: () -> Unit
) {
    val spannableStringBuilder = SpannableStringBuilder(fullString)

    // Set initial attributes
    spannableStringBuilder.setSpan(
        ForegroundColorSpan(initialColor), 0, fullString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    spannableStringBuilder.setSpan(
        StyleSpan(Typeface.NORMAL), 0, fullString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    textView.text = spannableStringBuilder

    // Set attributes for the specified substring
    val subStringStart = fullString.indexOf(subString)
    if (subStringStart != -1) {
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(subStringColor),
            subStringStart,
            subStringStart + subString.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableStringBuilder.setSpan(
            StyleSpan(Typeface.BOLD),  // Change to the appropriate style
            subStringStart, subStringStart + subString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    // Set click handler
    textView.movementMethod = LinkMovementMethod.getInstance()
    textView.setOnClickListener {
        clickHandler()
    }

    // Apply the styled text to the TextView
    textView.text = spannableStringBuilder
}

fun openSendEmail(context: Context, description: String, value: Array<String> = arrayOf()) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, value)
        intent.putExtra(Intent.EXTRA_TEXT, description)
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        showLog("TAG", "setUpObservers: $e")

    }
}

@SuppressLint("QueryPermissionsNeeded")
fun openSendEmailWithChooser(
    context: Context, description: String, value: Array<String> = arrayOf()
) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, value)
        intent.putExtra(Intent.EXTRA_TEXT, description)

        val chooserIntent = Intent.createChooser(intent, "Choose app")
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(chooserIntent)
        } else {
            // Handle case where no email app is available
            context.showToast("No email app found")
        }
    } catch (e: ActivityNotFoundException) {
        showLog("TAG", "setUpObservers: $e")
        // Handle exception if no activity found to handle the intent
    }
}


/**
 * This function use for go to google play store
 * */
fun openPlayStore(context: Context, packageName: String) {
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW, "market://details?id=$packageName".toUri()
            )
        )
    } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=$packageName".toUri()
            )
        )
    }
}


/** Default function **/

fun showLog(key: String, value: String) {/*if (BuildConfig.DEBUG) {*/
    Log.e(key, value)
    // }
}

/** this function is used to change only time from the old timestamp**/
fun updateTimeInTimestamp(oldTimeMillis: Long, newTime: String): Long {
    // Parse the new time string to extract hour, minute, and AM/PM components
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val newTimeCalendar = Calendar.getInstance().apply {
        time = timeFormat.parse(newTime)!!
    }
    val newHour = newTimeCalendar.get(Calendar.HOUR_OF_DAY)
    val newMinute = newTimeCalendar.get(Calendar.MINUTE)

    // Convert the old timestamp to a Calendar object
    val calendar = Calendar.getInstance().apply {
        timeInMillis = oldTimeMillis
    }

    // Set the new time components to the Calendar object
    calendar.apply {
        set(Calendar.HOUR_OF_DAY, newHour)
        set(Calendar.MINUTE, newMinute)
    }

    // Convert the updated Calendar object back to a timestamp
    return calendar.timeInMillis
}

/** this method is used to get the time stamp time **/
fun createTimestampFromTimePicker(timeString: String): Long {
    // Get the current date and time
    val calendar = Calendar.getInstance()

    // Parse the time string obtained from the time picker (e.g., "10:30 AM")
    val timeParts = timeString.split(":")
    val hour = timeParts[0].toInt()
    val minute = timeParts[1].split(" ")[0].toInt()
    val amPm = timeParts[1].split(" ")[1]

    // Adjust the hour based on AM/PM
    if (amPm.equals("PM", ignoreCase = true) && hour < 12) {
        calendar.set(Calendar.HOUR_OF_DAY, hour + 12)
    } else {
        calendar.set(Calendar.HOUR_OF_DAY, hour)
    }

    // Set the minute
    calendar.set(Calendar.MINUTE, minute)

    // Set the second and millisecond to zero
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    // Return the timestamp
    return calendar.timeInMillis
}


/** this method is used to get the time stamp from date and time **/
fun createTimestampFromDateAndTime(dateString: String, timeString: String): Long {
    // Parse the date string (e.g., "09-04-2024") into Calendar instance
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.time = dateFormat.parse(dateString) ?: Date()

    // Parse the time string (e.g., "10:30 AM")
    val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val time = timeFormat.parse(timeString) ?: Date()
    val timeCalendar = Calendar.getInstance()
    timeCalendar.time = time

    // Set the hour and minute from the parsed time
    calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
    calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))

    // Set the second and millisecond to zero
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    // Return the timestamp
    return calendar.timeInMillis
}

fun createTimestampForSelectedDateAndCurrentTime(selectedDateMillis: Long): Long {
    val calendar = Calendar.getInstance()

    // Set the date to the selected date
    calendar.timeInMillis = selectedDateMillis

    // Set the time to the current time
    val currentTime = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY))
    calendar.set(Calendar.MINUTE, currentTime.get(Calendar.MINUTE))
    calendar.set(Calendar.SECOND, currentTime.get(Calendar.SECOND))
    calendar.set(Calendar.MILLISECOND, currentTime.get(Calendar.MILLISECOND))

    // Return the timestamp in milliseconds
    return calendar.timeInMillis
}

@RequiresApi(Build.VERSION_CODES.O)
fun createTimestampWithSelectedTimeAndCurrentDate(timeInMillis: Long): Long {
    // Get the current date and time in LocalDateTime
    val currentDateAndTime = LocalDateTime.now()

    // Create a LocalDateTime object with the current date and time from the parameter
    val targetDateTime = LocalDateTime.ofEpochSecond(timeInMillis / 1000, 0, ZoneOffset.UTC)

    // Replace the date part with the current date and keep the time part

    return targetDateTime.withYear(currentDateAndTime.year).withMonth(currentDateAndTime.monthValue)
        .withDayOfMonth(currentDateAndTime.dayOfMonth).toEpochSecond(ZoneOffset.UTC) * 1000
}

fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return dateFormat.format(Date())
}

fun getTimeFromMillis(milliseconds: Long): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = milliseconds
    return dateFormat.format(calendar.time)
}

fun getOnlyTimeFromMillis(millis: Long): String {
    // Convert milliseconds to seconds
    val seconds = millis / 1000

    // Convert seconds to LocalTime
    val time = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalTime.ofSecondOfDay(seconds)
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    // Create a formatter for 12-hour format with am/pm
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")

    // Format the time using the formatter
    return time.format(formatter)
}


fun isValidPassword(password: String?): Boolean {
    val pattern: Pattern
    val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[.@#$%^&+=!*])(?=\\S+$).{8,}$"
    pattern = Pattern.compile(PASSWORD_PATTERN)
    val matcher: Matcher = pattern.matcher(password.toString())
    return matcher.matches()
}

@SuppressLint("DefaultLocale")
fun showTimePickerDialog(
    context: Context,
    title: Int,
    hours: Int,
    min: Int,
    supportFragmentManager: FragmentManager,
    textView: TextView
) {
    // Create a MaterialTimePicker.Builder and set the provided time
    val builder = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_12H)
        .setHour(if (hours > 12) hours - 12 else hours).setMinute(min).setTitleText(title)

    // If it's PM, set the PM time
    if (hours >= 12) {
        builder.setHour(hours)
    } else {
        builder.setHour(hours)
    }
    // Build the MaterialTimePicker
    val timePicker = builder.build()

    // Handle positive button click
    timePicker.addOnPositiveButtonClickListener {
        val hour = if (timePicker.hour > 12) timePicker.hour - 12 else timePicker.hour
        val minute = timePicker.minute
        val amPm = if (timePicker.hour >= 12) "pm" else "am"
        val selectedTime = String.format("%02d:%02d %s", hour, minute, amPm)
        textView.text = selectedTime
    }

    // Show the MaterialTimePicker
    timePicker.show(supportFragmentManager, "TimePicker")
}

