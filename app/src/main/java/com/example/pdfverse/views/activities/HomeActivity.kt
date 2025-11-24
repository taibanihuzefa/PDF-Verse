@file:Suppress("DEPRECATION")

package com.example.pdfverse.views.activities

import android.app.Activity
import android.content.ClipData
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdfverse.R
import com.example.pdfverse.adapters.HomeActivityHistoryAdapter
import com.example.pdfverse.databinding.ActivityHomeBinding
import com.example.pdfverse.dialogs.CustomAppDialog
import com.example.pdfverse.dialogs.CustomEditDialog
import com.example.pdfverse.utils.GlobalMethods
import com.example.pdfverse.utils.SharePref
import com.example.pdfverse.utils.StartActivity
import com.example.pdfverse.utils.addTextWatermarkToPdf
import com.example.pdfverse.utils.compressAndZipPdf
import com.example.pdfverse.utils.doHide
import com.example.pdfverse.utils.doVisible
import com.example.pdfverse.utils.getNumberOfPages
import com.example.pdfverse.utils.hideProgress
import com.example.pdfverse.utils.mergePdfs
import com.example.pdfverse.utils.openAppSetting
import com.example.pdfverse.utils.setMargin
import com.example.pdfverse.utils.setPadding
import com.example.pdfverse.utils.setStatusBarAppearanceAndColor
import com.example.pdfverse.utils.showLog
import com.example.pdfverse.utils.showProgress
import com.example.pdfverse.utils.showProgressDialog
import com.example.pdfverse.utils.showToast
import com.example.pdfverse.utils.splitPdf
import com.example.pdfverse.utils.widthPer
import com.example.pdfverse.viewModel.HomeActivityViewModel
import com.example.pdfverse.viewModelFactory.HomeActivityViewModelFactory
import java.io.File

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private var isClickedFrom: String = ""

    private val myActivity = this
    private var imageUris = mutableListOf<Uri>()
    private var imageStr: Uri? = null
    private var imageFile: File? = null

    //    private var captureIMageUrl: String = ""
    //    private var isCameraImg = false

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                showLog("ResultStatus", "Result OK")
                showLog("result.data", result.data.toString())
                handleActivityResult(result)
            } else {
                imageUris.clear()
                showLog("ResultStatus", "Result not OK")
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        initActivity()
        setStatusBarAppearanceAndColor(R.color.pink)
    }

    private fun initActivity() {
        homeActivityViewModel = HomeActivityViewModel(application)
        HomeActivityViewModelFactory(application)
        binding.homeViewModel = homeActivityViewModel


        createDirectory()
        setUpView()
        setUpData()
        setUpObserver()
    }

    override fun onResume() {
        myActivity.showProgress()
        super.onResume()
        showLog("onResume()", "onResume()")
        homeActivityViewModel.getFilesFromDirectory(binding.root)
        hideProgress()
    }

    private fun setUpObserver() {
        binding.apply {
//            homeActivityViewModel.getFilesFromDirectory(binding.root)
            homeActivityViewModel.filesListData.observe(lifecycleOwner!!) { fileList ->
                /**document history adapter list**/
                showLog("fileList", fileList.toString())
                if (fileList.isNotEmpty()) {
                    rcViewList.doVisible()
                    tvNoFilesFound.doHide()
                    val filesLists = fileList.sortedByDescending { it.date }
                    val rcViewHistory = HomeActivityHistoryAdapter(
                        myActivity,
                        filesLists,
                        object : HomeActivityHistoryAdapter.OnHomeHistoryOptionClick {
                            override fun onHomeHistoryOptionClick(
                                pdfName: String?, isEncrypted: Boolean
                            ) {
                                val extras = Bundle().apply {
                                    // Put the list of image URIs into the Bundle
                                    putString("pdfName", pdfName)
                                    putBoolean("isEncrypted", isEncrypted)
                                    // Add more data as needed
                                }
                                showLog("pdfInfo", pdfName.toString())
                                myActivity.StartActivity(
                                    FileDetailsActivity::class.java, extras = extras
                                )
                            }
                        })

                    rcViewList.layoutManager = LinearLayoutManager(myActivity)
                    rcViewList.adapter = rcViewHistory
                    showLog("itemCount", rcViewHistory.itemCount.toString())

                    tvRcViewSubTitle.apply {
                        if (filesLists.size > 4) doVisible() else doHide()
                    }
                } else {
//                    showLog("No Data", "No Data")
                    rcViewList.doHide()
                    tvNoFilesFound.doVisible()
                }
            }
        }
    }

    private fun setUpData() {
        binding.apply {
            //
            tvRcViewSubTitle.setOnClickListener {
                myActivity.showProgressDialog(500L)
                myActivity.StartActivity(FileHistoryListActivity::class.java)
            }

            cvPhotoToPdf.setOnClickListener {
                showLog("cvPhotoToPdf", "cvPhotoToPdf")
                handleCvClick("cameraToPdf")
            }


            cvImageToPdf.setOnClickListener {
                showLog("cvImageToPdf", "cvImageToPdf")
                handleCvClick("imageToPdf")
            }

            cvCompress.setOnClickListener {
                showLog("cvCompress", "cvCompress")
                handleCvClick("compress")
            }

            cvWatermark.setOnClickListener {
                showLog("cvWatermark", "cvWatermark")
                handleCvClick("watermark")
            }

            cvMerge.setOnClickListener {
                showLog("cvMerge", "cvMerge")
                handleCvClick("merge")
            }

            cvSplit.setOnClickListener {
                showLog("cvSplit", "cvSplit")
                handleCvClick("split")
            }
        }
    }

    private fun setUpView() {
        binding.apply {
            clMainConstraint.setPadding(myActivity, start = 20f, end = 20f)
            //
            clActionBarConstraint.setMargin(myActivity, top = 20f)
            //
            tvActionBarHeadTitle.setMargin(myActivity, start = 10f)
            //
            tvSubHeadTitle.setMargin(myActivity, top = 18f)
            //
            tvSectionHeadTitle.setMargin(myActivity, top = 18f)

            //card view PhotoToPdf
            cvPhotoToPdf.apply {
                this.layoutParams.height = myActivity.widthPer(100f).toInt()
                this.layoutParams.width = myActivity.widthPer(100f).toInt()
            }
            ivPhotoToPdf.setMargin(myActivity, top = 17f)
            tvPhotoToPdf.setMargin(myActivity, top = 11f, bottom = 8f)

            //card view ImageToPdf
            cvImageToPdf.apply {
                this.layoutParams.height = myActivity.widthPer(100f).toInt()
                this.layoutParams.width = myActivity.widthPer(100f).toInt()
                setMargin(myActivity, start = 15f)
            }


            ivImageToPdf.setMargin(myActivity, top = 17f)
            tvImageToPdf.setMargin(myActivity, top = 11f, bottom = 8f)

            //card view compress
            cvCompress.apply {
                this.layoutParams.height = myActivity.widthPer(100f).toInt()
                this.layoutParams.width = myActivity.widthPer(100f).toInt()
                setMargin(myActivity, start = 15f)
            }

            ivCompress.setMargin(myActivity, top = 17f)
            tvCompress.setMargin(myActivity, top = 11f, bottom = 8f)

            //card view Watermark
            cvWatermark.apply {
                this.layoutParams.height = myActivity.widthPer(100f).toInt()
                this.layoutParams.width = myActivity.widthPer(100f).toInt()
                setMargin(myActivity, top = 16f)
            }

            ivWatermark.setMargin(myActivity, top = 17f)
            tvWatermark.setMargin(myActivity, top = 11f, bottom = 8f)

            //card view Merge
            cvMerge.apply {
                this.layoutParams.height = myActivity.widthPer(100f).toInt()
                this.layoutParams.width = myActivity.widthPer(100f).toInt()
                setMargin(myActivity, start = 15f)
            }

            ivMerge.setMargin(myActivity, top = 17f)
            tvMerge.setMargin(myActivity, top = 11f, bottom = 8f)

            //card view compress
            cvSplit.apply {
                this.layoutParams.height = myActivity.widthPer(100f).toInt()
                this.layoutParams.width = myActivity.widthPer(100f).toInt()
                setMargin(myActivity, start = 15f)
            }

            ivSplit.setMargin(myActivity, top = 17f)
            tvSplit.setMargin(myActivity, top = 11f, bottom = 8f)

            //recycler view list constraint
            clRcViewConstraint.setMargin(myActivity, top = 20f)

            tvRcViewSubTitle.setMargin(myActivity, end = 10f)

            dvdRcView.setMargin(myActivity, top = 10f)

            rcViewList.setMargin(myActivity, top = 10f)

        }
    }

    private fun handleCvClick(action: String) {
        isClickedFrom = action
        when (action) {
            "cameraToPdf" -> {
                checkPermissionAndOpenCamera()
            }

            "imageToPdf", "compress", "watermark", "merge", "split" -> {
                GlobalMethods.checkGalleryPermission(myActivity, resultLauncher, action)
            }
        }
    }

    private fun checkPermissionAndOpenCamera() {
        imageFile = GlobalMethods.createImageFile(myActivity).first
        imageStr = FileProvider.getUriForFile(
            myActivity, myActivity.applicationContext.packageName + ".fileProvider", imageFile!!
        )
        GlobalMethods.checkCameraPermission(myActivity, resultLauncher, imageStr!!)
    }

    private fun createDirectory() {
        val storedDirectory = File(getExternalFilesDir(null), "Documents/PDFs")
        if (!storedDirectory.exists()) {
            storedDirectory.mkdirs()
        }
    }

    private fun handleActivityResult(result: ActivityResult) {
        try {
            showLog("result.data", result.data.toString())
            val clipData = when (isClickedFrom) {
                "cameraToPdf" -> {
                    imageStr.let {
                        ClipData.newUri(
                            contentResolver, "URI", it
                        )
                    }
                }

                "imageToPdf", "merge" -> {
                    result.data?.clipData
                }

                "compress", "watermark", "split" -> {/*for selecting the single pdf */
                    result.data?.data?.let {
                        ClipData.newUri(
                            contentResolver, "URI", it
                        )
                    }
                }

                else -> null
            }

            collectUrisFromClipDataOrSingleData(clipData, result.data?.data)

            // Proceed if we have any image URIs or PDF URI
            if (imageUris.isNotEmpty()) {
                when (isClickedFrom) {
                    "cameraToPdf", "imageToPdf", "watermark", "merge", "split" -> {
                        if (isClickedFrom == "cameraToPdf" || isClickedFrom == "split") {
                            openCustomAppDialog(isClickedFrom)
                        } else if (isClickedFrom == "merge") {
                            showLog("imageUris count", imageUris.size.toString())
                            if (imageUris.size <= 1) {
                                myActivity.showToast("You have selected only one file to merge")
                                imageUris.clear()
                            } else {
                                openEditDialog(isClickedFrom)
                            }
                        } else if (isClickedFrom == "imageToPdf") {/* cropImage(imageUris[0])*/
                            SharePref(myActivity).setImageUris(imageUris)
                            myActivity.StartActivity(
                                ImageCropperActivity::class.java
                            )
                            imageUris.clear()
                        } else {
                            openEditDialog(isClickedFrom)
                        }
                    }

                    "compress" -> {
                        compressAndZipPdf(context = myActivity, pdfUri = imageUris[0])
                        imageUris.clear()
                    }
                }
            } else {
                showLog("No Data Found", "No URIs received")
            }
        } catch (e: Exception) {
            showLog("Image Exception", e.message ?: "")
        }
    }

    private fun collectUrisFromClipDataOrSingleData(clipData: ClipData?, singleData: Uri?) {
        if (clipData != null) {
            showLog("clipData", clipData.itemCount.toString())
            showLog("clipData", clipData.toString())
            for (i in 0 until clipData.itemCount) {
                imageUris.add(clipData.getItemAt(i).uri)
            }
        } else {
            showLog("clipData", "clip data is empty")
            singleData?.let {
                imageUris.add(it)
                showLog("imageUris", imageUris.toString())
            }
        }
    }

    private fun openCustomAppDialog(isClickedFrom: String) {
        when (isClickedFrom) {
            "cameraToPdf" -> {
                CustomAppDialog(
                    myActivity,
                    "Alert",
                    "Do You Want to Capture More Images?",
                    customDialogButtons = object : CustomAppDialog.CustomDialogButtons {
                        override fun onYesButtonClick() {
//                            imageUris = mutableListOf(imageStr!!)
                            checkPermissionAndOpenCamera()
//                            GlobalMethods.openCamera(resultLauncher, imageStr!!)
                        }

                        override fun onNoButtonClick() {
                            showLog("imageUris", imageUris.toString())
                            SharePref(myActivity).setImageUris(imageUris)
                            myActivity.StartActivity(
                                ImageCropperActivity::class.java
                            )
                            imageUris.clear()
//                            openEditDialog(isClickedFrom)
                        }
                    }).show(
                    myActivity.supportFragmentManager, ""
                )
            }

            "split" -> {
                val numberOfPages = getNumberOfPages(context = myActivity, pdfUri = imageUris[0])
                showLog("numberOfPages", numberOfPages.toString())

                if (numberOfPages <= 1) {
                    myActivity.showToast("This file contains only one page")
                    imageUris.clear()
                } else {
                    CustomAppDialog(
                        myActivity,
                        title = "Alert",
                        message = "This file contains $numberOfPages pages. \n Are you sure you want split this file in $numberOfPages files?",
                        customDialogButtons = object : CustomAppDialog.CustomDialogButtons {
                            override fun onYesButtonClick() {
                                splitPdf(
                                    context = myActivity, pdfUri = imageUris[0]
                                )
                                showLog("Split Done", "Split Done")
                                imageUris.clear()
                                onResume()
                            }

                            override fun onNoButtonClick() {
                                imageUris.clear()
                            }
                        }).show(
                        myActivity.supportFragmentManager, ""
                    )
                }
            }
        }
    }

    private fun openEditDialog(isClickedFrom: String) {
        val title = when (isClickedFrom) {
            "imageToPdf", "merge" -> "Enter Pdf Name"
            "watermark" -> "Enter Watermark Text"
            else -> "Enter Name"
        }/* val extras = Bundle().apply {
             // Put the list of image URIs into the Bundle
             putStringArrayList("imagesList", ArrayList(imageUris.map { it.toString() }))
             // Add more data as needed
         }*/

        CustomEditDialog(myActivity, title, "", object : CustomEditDialog.CustomDialogButtons {
            override fun onYesButtonClick(fileName: String) {
                when (isClickedFrom) {
                    "watermark" -> {
                        addTextWatermarkToPdf(
                            context = myActivity, pdfUri = imageUris[0], watermarkText = fileName
                        )
                    }

                    "merge" -> {
                        mergePdfs(myActivity, imageUris, fileName)
//                        mergePdfs(myActivity)
                    }
                }
                imageUris.clear()
                onResume()
            }

            override fun onNoButtonClick() {
                imageUris.clear()
            }
        }).show(myActivity.supportFragmentManager, "")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == GlobalMethods.CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                isCameraImg = true
                showLog("PERMISSION_GRANTED", "PERMISSION_GRANTED")
                GlobalMethods.openCamera(resultLauncher, imageStr!!)
            } else {
                showLog("PERMISSION_DENIED", "PERMISSION_DENIED")
                CustomAppDialog(
                    context = myActivity,
                    title = "Permission Denied",
                    message = "Without this permission the app is not be able to work properly. Please allow the permission from the settings",
                    positiveButtonText = "Settings",
                    customDialogButtons = object : CustomAppDialog.CustomDialogButtons {
                        override fun onYesButtonClick() {
                            openAppSetting(myActivity)
                        }

                        override fun onNoButtonClick() {

                        }
                    }).show(
                    supportFragmentManager, ""
                )
            }
        } else {
            showLog("code not match", "code not match")
        }

        if (requestCode == GlobalMethods.GALLERY_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                isCameraImg = true
                showLog("PERMISSION_GRANTED", "PERMISSION_GRANTED")
                when (isClickedFrom) {
                    "imageToPdf" -> {
                        GlobalMethods.chooseImageGallery(resultLauncher)
                    }

                    "compress", "watermark", "split" -> {
                        GlobalMethods.choosePDFsFromGallery(resultLauncher)
                    }

                    "merge" -> {
                        GlobalMethods.chooseMultiplePDFsFromGallery(resultLauncher)
                    }

                    else -> {}
                }
//                GlobalMethods.choosePDFsFromGallery(resultLauncher)
            } else {
                showLog("PERMISSION_DENIED", "PERMISSION_DENIED")
                CustomAppDialog(
                    context = myActivity,
                    title = "Permission Denied",
                    message = "Without this permission the app is not be able to work properly. Please allow the permission from the settings",
                    positiveButtonText = "Settings",
                    customDialogButtons = object : CustomAppDialog.CustomDialogButtons {
                        override fun onYesButtonClick() {
                            openAppSetting(myActivity)
                        }

                        override fun onNoButtonClick() {

                        }
                    }).show(
                    supportFragmentManager, ""
                )
            }
        } else {
            showLog("code not match", "code not match")
        }
    }


    private var backPressedTime: Long = 0

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
            finishAffinity()
            overridePendingTransition(
                R.anim.fade_in, R.anim.fade_out
            )// This finishes the current activity and all parent activities
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}