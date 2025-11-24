@file:Suppress("DEPRECATION")

package com.example.pdfverse.views.activities

import ImagePagerAdapter
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.canhub.cropper.CropImageView
import com.example.pdfverse.R
import com.example.pdfverse.databinding.ActivityImageCropperBinding
import com.example.pdfverse.dialogs.CustomEditDialog
import com.example.pdfverse.utils.Finish
import com.example.pdfverse.utils.SharePref
import com.example.pdfverse.utils.createPdfFromImages
import com.example.pdfverse.utils.doHide
import com.example.pdfverse.utils.doVisible
import com.example.pdfverse.utils.setMargin
import com.example.pdfverse.utils.setPadding
import com.example.pdfverse.utils.setStatusBarAppearanceAndColor
import com.example.pdfverse.utils.showLog
import com.example.pdfverse.utils.showProgressDialog
import com.example.pdfverse.utils.widthPer


class ImageCropperActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageCropperBinding

    //    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private val myActivity = this
    private var currentImageIndex: Int = 0
    private var imageUris: List<Uri>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageCropperBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        initActivity()
        setStatusBarAppearanceAndColor(R.color.pink)
    }

    private fun initActivity() {
//        homeActivityViewModel = HomeActivityViewModel(application)
//        HomeActivityViewModelFactory(application)
//        binding.homeViewModel = homeActivityViewModel

        setUpView()
        setUpData()
        setUpAdapter()

    }

    private fun setUpAdapter() {
        binding.apply {
            /* set image adapter*/
            imageUris = SharePref(myActivity).getImageUris()
            showLog("imageUris", imageUris.toString())
            val imageAdapter =
                ImagePagerAdapter(myActivity, imageUris!!, object : ImagePagerAdapter.OnViewClick {
                    override fun onViewClick(ivCropImageView: CropImageView) {
                    }
                })
            rcViewImageCrop.apply {
                adapter = imageAdapter
                layoutManager =
                    LinearLayoutManager(myActivity, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(false)
//                addItemDecoration(MyDecoration())
            }


            val count = rcViewImageCrop.adapter?.itemCount ?: 0
            showLog("AdapterCount", count.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpData() {
        binding.apply {

            incImgCropToolbar.apply {
                tvToolbarHeadTitle.text = "Edit Image(s)"
                searchViewIcon.visibility = View.GONE
                ivToolbarBackArrowImg.setOnClickListener {
                    onBackPressed()
                }
            }

            clCrop.setOnClickListener {
                binding.apply {
                    val position =
                        (rcViewImageCrop.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                    val view = rcViewImageCrop.findViewHolderForLayoutPosition(position)?.itemView
                    if (view != null) {
                        val cl = view.findViewById<CropImageView>(R.id.ivCropImageView)
                        val iv = view.findViewById<AppCompatImageView>(R.id.ivAppCompatImageView)

                        if (cl.getCroppedImage() != null) {
                            iv.doHide()
                            cl.doVisible()
                            cl.rotation = 0F
                            (rcViewImageCrop.adapter as ImagePagerAdapter)._updatedImageMap[position] =
                                Pair(cl.croppedImage, cl.rotation)
                        }
                    }
                }
            }

            clRotate.setOnClickListener {
                val position =
                    (rcViewImageCrop.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                showLog("position", position.toString())
                val view = rcViewImageCrop.findViewHolderForLayoutPosition(position)?.itemView
                if (view != null) {
                    val cl = view.findViewById<CropImageView>(R.id.ivCropImageView)
                    val iv = view.findViewById<AppCompatImageView>(R.id.ivAppCompatImageView)
                    var viewCropped: Bitmap? = null
                    val currentRotation: Float

                    if (cl.getCroppedImage() != null) {
                        showLog("cropping", "Image Cropped")
                        iv.doVisible()
                        cl.doHide()
                        viewCropped = cl.getCroppedImage()
                        showLog("viewCropped", viewCropped.toString())
                        iv.setImageBitmap(viewCropped)
                        iv.rotation += 90
                        currentRotation = iv.rotation
//                        cl.rotation = currentRotation

                    } else {
                        showLog("cropping", "Image Not Cropped")
                        cl.doVisible()
                        iv.doHide()
                        viewCropped = cl.getCroppedImage()
                        cl.rotation += 90
                        currentRotation = cl.rotation
//                        iv.rotation = currentRotation
                        /* (rcViewImageCrop.adapter as ImagePagerAdapter)._updatedImageMap[position] =
                             Pair(cl.getCroppedImage(), currentRotation)*/
                    }
                    (rcViewImageCrop.adapter as ImagePagerAdapter)._updatedImageMap[position] =
                        Pair(viewCropped, currentRotation)
                }
            }

            clSave.setOnClickListener {
                CustomEditDialog(myActivity,
                    title = "Enter Pdf Name",
                    fileName = "",
                    customDialogButtons = object : CustomEditDialog.CustomDialogButtons {
                        override fun onYesButtonClick(fileName: String) {
                            myActivity.showProgressDialog()
                            createPdfFromImages(
                                context = myActivity,
                                imageUris = SharePref(myActivity).getImageUris(),
                                fileName = fileName,
                                updatedImageMap = (rcViewImageCrop.adapter as ImagePagerAdapter).getUpdatedImageMap()
                            )
                            onBackPressed()
                        }

                        override fun onNoButtonClick() {
                        }
                    }).show(supportFragmentManager, "")
            }

            ivArrowBackwards.setOnClickListener {
                if (currentImageIndex > 0) {
                    currentImageIndex--
                    rcViewImageCrop.scrollToPosition(currentImageIndex)
//                    rcViewImageCrop.adapter?.notifyItemChanged(currentImageIndex)
                }
                updateButtonState()
            }

            ivArrowForward.setOnClickListener {
                val count = rcViewImageCrop.adapter?.itemCount ?: 0
                if (currentImageIndex < count - 1) {
                    currentImageIndex++
                    showLog("currentImageIndex", currentImageIndex.toString())
                    binding.rcViewImageCrop.scrollToPosition(currentImageIndex)
//                    rcViewImageCrop.adapter?.notifyItemChanged(currentImageIndex)
                }
                updateButtonState()
            }

            // Initial button state update
            updateButtonState()
        }
    }

    private fun updateButtonState() {
        binding.apply {
            ivArrowBackwards.visibility =
                if (currentImageIndex != 0) View.VISIBLE else View.INVISIBLE
            val count = rcViewImageCrop.adapter?.itemCount ?: 0
            ivArrowForward.visibility =
                if (currentImageIndex == count - 1) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun setUpView() {
        binding.apply {

            clMainConstraint.setPadding(myActivity, start = 20f, end = 20f)

            clImgCropActionConstraint.setPadding(myActivity, all = 10f)

            /*-------------Crop----------------*/
            clCrop.setMargin(myActivity, start = 20f)
            ivCrop.apply {
                layoutParams.height = myActivity.widthPer(30f).toInt()
                layoutParams.width = myActivity.widthPer(30f).toInt()
            }
            tvCropText.setMargin(myActivity, top = 5f)


            /*-------------Rotate----------------*/
            ivRotate.apply {
                layoutParams.height = myActivity.widthPer(30f).toInt()
                layoutParams.width = myActivity.widthPer(30f).toInt()
            }
            tvRotateText.setMargin(myActivity, top = 5f)

            /*-------------Save----------------*/
            clSave.setMargin(myActivity, end = 20f)
            ivSave.apply {
                layoutParams.height = myActivity.widthPer(30f).toInt()
                layoutParams.width = myActivity.widthPer(30f).toInt()
            }
            tvSaveText.setMargin(myActivity, top = 5f)

            rcViewImageCrop.apply {
                setMargin(myActivity, top = 100f, bottom = 100f)
            }

            ivArrowBackwards.apply {
                // Set icon tint
                val iconTint = ContextCompat.getColor(myActivity, R.color.black)
                DrawableCompat.setTint(ivArrowBackwards.drawable, iconTint)
                layoutParams.height = myActivity.widthPer(30f).toInt()
                layoutParams.width = myActivity.widthPer(30f).toInt()
            }

            ivArrowForward.apply {
                layoutParams.height = myActivity.widthPer(30f).toInt()
                layoutParams.width = myActivity.widthPer(30f).toInt()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Finish()
    }
}