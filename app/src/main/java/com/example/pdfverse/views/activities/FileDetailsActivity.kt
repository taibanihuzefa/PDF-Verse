package com.example.pdfverse.views.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdfverse.R
import com.example.pdfverse.adapters.FileDetailsActivityListAdapter
import com.example.pdfverse.databinding.ActivityFileDetailsBinding
import com.example.pdfverse.dialogs.CustomAppDialog
import com.example.pdfverse.dialogs.CustomEditDialog
import com.example.pdfverse.utils.StartActivity
import com.example.pdfverse.utils.addPasswordToPdf
import com.example.pdfverse.utils.addTextWatermarkToPdf
import com.example.pdfverse.utils.compressAndZipPdf
import com.example.pdfverse.utils.doHide
import com.example.pdfverse.utils.formatFileSize
import com.example.pdfverse.utils.getFileFromFileName
import com.example.pdfverse.utils.getFileIcon
import com.example.pdfverse.utils.getNumberOfPages
import com.example.pdfverse.utils.removePasswordFromPdf
import com.example.pdfverse.utils.renameFile
import com.example.pdfverse.utils.setMargin
import com.example.pdfverse.utils.setPadding
import com.example.pdfverse.utils.setStatusBarAppearanceAndColor
import com.example.pdfverse.utils.sharePdfFile
import com.example.pdfverse.utils.showLog
import com.example.pdfverse.utils.showProgressDialog
import com.example.pdfverse.utils.showToast
import com.example.pdfverse.utils.splitPdf
import com.example.pdfverse.utils.unzipCompressToPdfFile
import com.example.pdfverse.utils.viewPdfFile
import com.example.pdfverse.viewModel.FileDetailsActivityViewModel
import com.example.pdfverse.viewModelFactory.FileDetailsActivityViewModelFactory
import java.io.File


@Suppress("DEPRECATION")
class FileDetailsActivity : AppCompatActivity() {
    //
//    private lateinit var textStrings: Array<String>
//    private lateinit var imageResourceIds: TypedArray
    private lateinit var binding: ActivityFileDetailsBinding
    private lateinit var fileDetailsActivityViewModel: FileDetailsActivityViewModel
    private val myActivity = this
    private var pdfInfo: File? = null
    private var newFileName: String? = null
    private var isEncrypted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileDetailsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        initActivity()

        setStatusBarAppearanceAndColor(R.color.pink)
    }

    private fun initActivity() {
        fileDetailsActivityViewModel = FileDetailsActivityViewModel(application)
        FileDetailsActivityViewModelFactory(application)
        binding.fileDetailsViewModel = fileDetailsActivityViewModel

        val fileName = newFileName ?: intent.getStringExtra("pdfName")!!
        isEncrypted = intent.getBooleanExtra("isEncrypted", false)
        showLog("fileName", fileName)
        pdfInfo = getFileFromFileName(myActivity, fileName)
        fileDetailsActivityViewModel.getDataFromIntent(binding.root, fileName)



        setUpView()
        setUpObserver()
        setUpData()
    }

    override fun onResume() {
        super.onResume()
        myActivity.showProgressDialog(500L)
        val fileName = newFileName ?: intent.getStringExtra("pdfName")!!
        isEncrypted = intent.getBooleanExtra("isEncrypted", false)
        showLog("fileName", fileName)
        pdfInfo = getFileFromFileName(myActivity, fileName)
        fileDetailsActivityViewModel.getDataFromIntent(binding.root, fileName)
    }

    private fun setUpObserver() {
        binding.apply {
            fileDetailsActivityViewModel.filesListData.observe(lifecycleOwner!!) { fileInfo ->
                /**document history adapter list**/
                // Use the pdfInfo object as needed
                ivFileInfoIcon.setImageResource(getFileIcon(fileInfo.extension))
                if (fileInfo.extension == "zip") {
                    ivFileInfoEyeIcon.doHide()
                    tvFileInfoEyeIcon.doHide()
                }
                tvFileInfoTitle.text = newFileName ?: fileInfo.name
                //                            if (newFileName.isNotEmpty())  else fileInfo.name
                tvFileInfoDesc.text = formatFileSize(fileInfo.length())
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpData() {
        binding.apply {
            showLog("pdfInfoNew", pdfInfo?.name!!)
            incFileDetailsToolbar.apply {
                tvToolbarHeadTitle.text = "Select Option"
                searchViewIcon.visibility = View.GONE
                ivToolbarBackArrowImg.setOnClickListener {
                    onBackPressed()
                }
            }

            ivFileInfoEyeIcon.setOnClickListener {
                showLog("ivFileInfoEyeIcon", "clicked")
                showLog("Received PdfInfo", pdfInfo?.name!!)
                viewPdfFile(myActivity, pdfInfo!!)
            }

            ivFileInfoShareIcon.setOnClickListener {
                sharePdfFile(myActivity, pdfInfo!!)
            }

            val imageResourceIds = if (pdfInfo?.extension == "zip") {
                resources.obtainTypedArray(R.array.file_details_list_icon_for_zip)
            } else if (isEncrypted) {
                resources.obtainTypedArray(R.array.file_details_list_icon_for_passProtect)
            } else {
                resources.obtainTypedArray(R.array.file_details_list_icon)
            }

            val textStrings = if (pdfInfo?.extension == "zip") {
                resources.getStringArray(R.array.file_details_list_text_for_zip)
            } else if (isEncrypted) {
                resources.getStringArray(R.array.file_details_list_text_for_passProtect)
            } else {
                resources.getStringArray(R.array.file_details_list_text)
            }

            /* set up the adapter for the pdf details list  */
            val rcViewFileDetailsList = FileDetailsActivityListAdapter(
                myActivity,
                imageResourceIds,
                textStrings,
                object : FileDetailsActivityListAdapter.OnFileDetailsOptionClick {
                    override fun onFileDetailsOptionClick(imageResourceIds: Int) {
                        when (imageResourceIds) {
                            R.drawable.ic_rename_icon -> {
                                showLog("Rename", "Rename")
                                CustomEditDialog(
                                    myActivity,
                                    "Enter ${pdfInfo?.extension} file name",
                                    pdfInfo?.nameWithoutExtension!!,
                                    object : CustomEditDialog.CustomDialogButtons {
                                        override fun onYesButtonClick(fileName: String) {
                                            pdfInfo?.let { fileInfo ->
                                                if (renameFile(
                                                        pdfInfo!!, "$fileName.${fileInfo.extension}"
                                                    )
                                                ) {
                                                    myActivity.showToast("File renamed successfully")
                                                    newFileName = "$fileName.${fileInfo.extension}"
                                                    onResume()
                                                } else {
                                                    myActivity.showToast("File rename failed")
                                                }
                                            }
                                        }

                                        override fun onNoButtonClick() {

                                        }

                                    }).show(myActivity.supportFragmentManager, "")
                            }

                            R.drawable.ic_delete_icon -> {
                                showLog("Delete", "Delete")

                                CustomAppDialog(
                                    myActivity,
                                    title = "Alert",
                                    message = "Are you sure you want to delete this file?",
                                    customDialogButtons = object :
                                        CustomAppDialog.CustomDialogButtons {
                                        override fun onYesButtonClick() {
                                            if (com.example.pdfverse.utils.deleteFile(pdfInfo!!)) {
                                                myActivity.showToast("File deleted successfully")
//                                                myActivity.StartActivity(HomeActivity::class.java)
                                                onBackPressed()
                                            } else {
                                                myActivity.showToast("File deletion failed")
                                            }
                                        }

                                        override fun onNoButtonClick() {
                                        }

                                    }).show(
                                    myActivity.supportFragmentManager, ""
                                )
                            }

                            R.drawable.ic_reorder_icon -> {
                                showLog("ReOrder", "ReOrder")

                                val numberOfPages =
                                    getNumberOfPages(context = myActivity, file = pdfInfo)
                                showLog("numberOfPages", numberOfPages.toString())

                                if (numberOfPages <= 1) {
                                    myActivity.showToast("This file contains only one page")
                                } else {
                                    val extras = Bundle().apply {
                                        // Put the list of image URIs into the Bundle
                                        putString("selectedFileName", pdfInfo?.name)
                                        // Add more data as needed
                                    }
                                    myActivity.StartActivity(
                                        ReorderPagesActivity::class.java, extras = extras
                                    )
                                }
                            }

                            R.drawable.ic_watermark_icon -> {
                                showLog("Watermark", "Watermark")
                                CustomEditDialog(
                                    context = myActivity,
                                    title = "Enter watermark text",
                                    fileName = "",
                                    customDialogButtons = object :
                                        CustomEditDialog.CustomDialogButtons {
                                        override fun onYesButtonClick(fileName: String) {
                                            addTextWatermarkToPdf(
                                                context = myActivity,
                                                file = pdfInfo!!,
                                                watermarkText = fileName
                                            )
                                            onBackPressed()
                                        }

                                        override fun onNoButtonClick() {
                                        }
                                    }).show(supportFragmentManager, "")

                            }

                            R.drawable.ic_lock_icon -> {
                                showLog("Lock", "Lock")

                                if (isEncrypted) {
                                    CustomAppDialog(
                                        context = myActivity,
                                        title = "Alert",
                                        message = "Are you sure you want to remove the password on this file?",
                                        positiveButtonText = "Remove Password",
                                        customDialogButtons = object :
                                            CustomAppDialog.CustomDialogButtons {
                                            override fun onYesButtonClick() {
                                                removePasswordFromPdf(
                                                    context = myActivity,
                                                    file = pdfInfo!!,
                                                )
                                                onBackPressed()
                                            }

                                            override fun onNoButtonClick() {
                                            }
                                        }).show(
                                        supportFragmentManager, ""
                                    )
                                } else {
                                    CustomEditDialog(
                                        context = myActivity,
                                        title = "Enter password",
                                        fileName = "",
                                        customDialogButtons = object :
                                            CustomEditDialog.CustomDialogButtons {
                                            override fun onYesButtonClick(fileName: String) {/* makePdfPasswordProtected(
                                                     context = myActivity, file = file, fileName
                                                 )*/

                                                addPasswordToPdf(
                                                    context = myActivity,
                                                    file = pdfInfo!!,
                                                    password = fileName
                                                )
                                                onBackPressed()
                                            }

                                            override fun onNoButtonClick() {
                                            }
                                        }).show(
                                        supportFragmentManager, ""
                                    )
                                }
                            }

                            R.drawable.ic_split_icon -> {
                                showLog("Split", "Split")/* val file = File(
                                     getExternalFilesDir(null), "Documents/PDFs/${pdfInfo?.name}"
                                 )*/
                                val numberOfPages =
                                    getNumberOfPages(context = myActivity, file = pdfInfo)
                                showLog("numberOfPages", numberOfPages.toString())

                                if (numberOfPages <= 1) {
                                    myActivity.showToast("This file contains only one page")
                                } else {
                                    CustomAppDialog(
                                        myActivity,
                                        title = "Alert",
                                        message = "This file contains $numberOfPages pages. \n Are you sure you want split this file in $numberOfPages files?",
                                        customDialogButtons = object :
                                            CustomAppDialog.CustomDialogButtons {
                                            override fun onYesButtonClick() {
                                                splitPdf(context = myActivity, file = pdfInfo)
                                                onBackPressed()
                                            }

                                            override fun onNoButtonClick() {
                                            }

                                        }).show(
                                        myActivity.supportFragmentManager, ""
                                    )
                                }
                            }

                            R.drawable.ic_compress_icon -> {
                                showLog("Compress", "Compress")/* val file = File(
                                     getExternalFilesDir(null), "Documents/PDFs/${pdfInfo?.name}"
                                 )*/
                                compressAndZipPdf(context = myActivity, file = pdfInfo)
                                onBackPressed()
                            }

                            R.drawable.ic_unzip_icon -> {
                                showLog("unzip", "unzip")

                                /* val zipFIle = File(
                                     getExternalFilesDir(null), "Documents/PDFs/${pdfInfo?.name}"
                                 )*/
                                unzipCompressToPdfFile(context = myActivity, zipFile = pdfInfo!!)
                                onBackPressed()
                            }
                        }
                    }
                })
            rcvFileDetailsList.layoutManager = LinearLayoutManager(myActivity)
            rcvFileDetailsList.adapter = rcViewFileDetailsList
        }
    }

    private fun setUpView() {
        binding.apply {
            //
            clMainConstraint.setPadding(myActivity, start = 20f, end = 20f)

            //
//            incFileDetailsToolbar.setMargin(myActivity, start = 20f, top = 20f, end = 20f)

            //
            clFileInfoConstraint.setMargin(myActivity, top = 20f)
            clFileInfoConstraint.setPadding(myActivity, start = 10f, end = 10f)

            tvFileInfoTitle.setMargin(myActivity, start = 5f, end = 5f)

            //
            tvFileInfoDesc.setMargin(myActivity, start = 5f)

            //
            clFileInfoConstraintBottom.setMargin(myActivity, top = 24f)

            //
//            ivFileInfoEyeIcon.setMargin(myActivity, start = 50f)
//            ivFileInfoShareIcon.setMargin(myActivity, start = 60f)

            ivFileInfoEyeIcon.setMargin(myActivity, end = 50f)

            //
            dvdFileInfo.setMargin(myActivity, top = 20f)

            //
            rcvFileDetailsList.setMargin(myActivity, top = 20f)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
//        Finish()
        finish()
    }
}