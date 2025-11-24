@file:Suppress("DEPRECATION")

package com.example.pdfverse.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.pdfverse.R
import com.example.pdfverse.adapters.ReorderPagesActivityAdapter
import com.example.pdfverse.databinding.ActivityReorderPagesBinding
import com.example.pdfverse.utils.Finish
import com.example.pdfverse.utils.ItemMoveCallback
import com.example.pdfverse.utils.doHide
import com.example.pdfverse.utils.getFileFromFileName
import com.example.pdfverse.utils.makePdfWithReorderPages
import com.example.pdfverse.utils.setMargin
import com.example.pdfverse.utils.setPadding
import com.example.pdfverse.utils.setStatusBarAppearanceAndColor
import com.example.pdfverse.utils.showLog
import com.example.pdfverse.utils.showProgressDialog
import java.io.File

class ReorderPagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReorderPagesBinding

    //    private lateinit var homeActivityViewModel: HomeActivityViewModel
    private val myActivity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReorderPagesBinding.inflate(layoutInflater)
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
        setUpObserver()
    }

    override fun onResume() {
        super.onResume()
        showProgressDialog(500L)
    }

    private fun setUpObserver() {
        binding.apply {

            val fileName = intent?.extras?.getString("selectedFileName")
            showLog("selectedFileName", fileName!!)
            val selectedFile = getFileFromFileName(myActivity, fileName)

            val rcReorderPages = ReorderPagesActivityAdapter(myActivity, selectedFile)
            rcvPageList.layoutManager = GridLayoutManager(myActivity, 2)
            rcvPageList.adapter = rcReorderPages

            val callback = ItemMoveCallback(rcReorderPages)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(rcvPageList)


            btnSave.setOnClickListener {
                saveReorderedPages(getFileFromFileName(myActivity, fileName), rcReorderPages.pages)
            }

        }
    }

    private fun setUpData() {
        binding.apply {
            incFileDetailsToolbar.searchViewIcon.doHide()
            incFileDetailsToolbar.tvToolbarHeadTitle.text = "Reorder Pages"
            incFileDetailsToolbar.ivToolbarBackArrowImg.setOnClickListener {
                onBackPressed()
            }

        }
    }

    private fun saveReorderedPages(selectedFile: File, pages: List<Int>) {
        showLog("pageList", pages.toString())
        showLog("selectedFile.absolutePath", selectedFile.absolutePath)
        makePdfWithReorderPages(context = myActivity, file = selectedFile, pages = pages)
        onBackPressed()

    }

    private fun setUpView() {
        binding.apply {
            //
            clMainConstraint.setPadding(myActivity, start = 20f, end = 20f)

            //
            rcvPageList.setMargin(myActivity, top = 25f, bottom = 20f)

            //
            btnSave.setMargin(myActivity, bottom = 20f)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Finish()
    }

}