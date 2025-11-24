package com.example.pdfverse.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.WindowManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File


object GlobalMethods {

    fun removeTheBackgroundDiv(myActivity: Activity) {
        // Remove dim background from the activity
        val window = myActivity.window
        val layoutParams = window.attributes
        layoutParams.dimAmount = 1.0f
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }


    const val CAMERA_PERMISSION_CODE = 1001
    fun checkCameraPermission(
        context: Context,
        resultLauncher: ActivityResultLauncher<Intent>,
        imageStr: Uri,

        ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context, Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permissions = arrayOf(Manifest.permission.CAMERA)
                ActivityCompat.requestPermissions(
                    context as Activity, permissions, CAMERA_PERMISSION_CODE
                )

            } else {
                showLog("PERMISSION_GRANTED", "PERMISSION_GRANTED")
                openCamera(resultLauncher, imageStr)
            }
        } else {
            showLog("PERMISSION_GRANTED", "PERMISSION_GRANTED")
            openCamera(resultLauncher, imageStr)
        }
    }

    fun openCamera(
        resultLauncher: ActivityResultLauncher<Intent>, imageStr: Uri
    ) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageStr)
        resultLauncher.launch(intent)
    }

    const val GALLERY_PERMISSION_CODE = 1002

    fun checkGalleryPermission(
        context: Context,
        resultLauncher: ActivityResultLauncher<Intent>,
        isComeFrom: String,
    ) {
        val permissions = when {

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            Build.VERSION.SDK_INT <= Build.VERSION_CODES.R -> arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            else -> arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (permissions.any {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_DENIED
            }) {
            ActivityCompat.requestPermissions(
                context as Activity, permissions, GALLERY_PERMISSION_CODE
            )
        } else {
            when (isComeFrom) {
                "imageToPdf" -> {
                    chooseImageGallery(resultLauncher)
                }

                "compress", "watermark", "split" -> {
                    choosePDFsFromGallery(resultLauncher)
                }

                "merge" -> {
                    chooseMultiplePDFsFromGallery(resultLauncher)
                }

                else -> {
                }
            }
        }
    }

    fun chooseImageGallery(resultLauncher: ActivityResultLauncher<Intent>) {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.type = "image/*"
        resultLauncher.launch(intent)

//        val intent = Intent()
//        intent.setType("image/*")
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//        intent.setAction(Intent.ACTION_GET_CONTENT)
//        resultLauncher.launch(intent)
    }

    fun choosePDFsFromGallery(resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent()
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        resultLauncher.launch(intent)
    }

    fun chooseMultiplePDFsFromGallery(resultLauncher: ActivityResultLauncher<Intent>) {
        val intent = Intent()
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        resultLauncher.launch(intent)
    }


    fun createImageFile(context: Context): Pair<File, String> {
        val imageFileName = "JPEG_" + System.currentTimeMillis() + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, ".jpg", storageDir
        )

        val mCurrentPhotoPath = image.absolutePath
        return Pair(image, mCurrentPhotoPath)
    }

}

