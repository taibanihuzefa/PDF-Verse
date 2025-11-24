@file:Suppress("DEPRECATION")

package com.example.pdfverse.views.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pdfverse.R
import com.example.pdfverse.adapters.FileHistoryListActivityAdapter
import com.example.pdfverse.dataModels.PdfInfo
import com.example.pdfverse.databinding.ActivityFileHistoryListBinding
import com.example.pdfverse.utils.Finish
import com.example.pdfverse.utils.StartActivity
import com.example.pdfverse.utils.doHide
import com.example.pdfverse.utils.doVisible
import com.example.pdfverse.utils.hideProgress
import com.example.pdfverse.utils.setIconColor
import com.example.pdfverse.utils.setMargin
import com.example.pdfverse.utils.setPadding
import com.example.pdfverse.utils.setStatusBarAppearanceAndColor
import com.example.pdfverse.utils.showLog
import com.example.pdfverse.utils.showProgress
import com.example.pdfverse.utils.showProgressDialog
import com.example.pdfverse.viewModel.FileHistoryListActivityViewModel
import com.example.pdfverse.viewModelFactory.FileDetailsActivityViewModelFactory


class FileHistoryListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFileHistoryListBinding
    private lateinit var fileHistoryListActivityViewModel: FileHistoryListActivityViewModel
    private val myActivity = this
    private var isAlphabetFilterOn = false
    private var isCalenderFilterOn = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFileHistoryListBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        setContentView(binding.root)

        initActivity()

        setStatusBarAppearanceAndColor(R.color.pink)
    }

    private fun initActivity() {
        fileHistoryListActivityViewModel = FileHistoryListActivityViewModel(application)
        FileDetailsActivityViewModelFactory(application)
        binding.fileHistoryListViewModel = fileHistoryListActivityViewModel


        setUpView()
        setUpData()
        setUpObserver()
    }

    override fun onResume() {
        super.onResume()
        myActivity.showProgress()
        fileHistoryListActivityViewModel.getFilesFromDirectory(binding.root)
        hideProgress()
    }

    private fun setUpObserver() {
        binding.apply {
            fileHistoryListActivityViewModel.getFilesFromDirectory(binding.root)
            fileHistoryListActivityViewModel.filesListData.observe(lifecycleOwner!!) { fileList ->
                /**document history adapter list**/
                if (fileList.isNotEmpty()) {
                    val sortedList = updateFilters(fileList)
                    rcViewFileHistoryList.setHasFixedSize(true)
                    val rcViewHistoryList = FileHistoryListActivityAdapter(myActivity,
                        sortedList,
                        object : FileHistoryListActivityAdapter.OnHomeHistoryOptionClick {
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
                                //Clear the text from EditText view
                                incFileHistoryListToolbar.searchViewIcon.setQuery("", false)
                                //Collapse the action view
                                incFileHistoryListToolbar.searchViewIcon.onActionViewCollapsed();
                                //Collapse the search widget
                                incFileHistoryListToolbar.searchViewIcon.clearFocus();
                            }
                        })
                    rcViewFileHistoryList.layoutManager = LinearLayoutManager(myActivity)
                    rcViewFileHistoryList.adapter = rcViewHistoryList

                    incFileHistoryListToolbar.searchViewIcon.setOnQueryTextListener(object :
                        SearchView.OnQueryTextListener,
                        android.widget.SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            rcViewHistoryList.filter.filter(newText)
                            return false
                        }
                    })

                } else {
                    rcViewFileHistoryList.doHide()
                    tvNoFilesFound.doVisible()
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpData() {
        binding.apply {
            incFileHistoryListToolbar.apply {
                tvToolbarHeadTitle.text = "History"
                ivToolbarBackArrowImg.setOnClickListener {
                    onBackPressed()
                }
//                searchViewIcon.visibility = View.GONE
            }

            ivAlphabetFilter.setOnClickListener {
                myActivity.showProgressDialog(500L)
                isAlphabetFilterOn = !isAlphabetFilterOn
                showLog("isAlphabetFilterOn", isAlphabetFilterOn.toString())
                fileHistoryListActivityViewModel.getFilesFromDirectory(binding.root)
            }

            ivCalenderFilter.setOnClickListener {
                myActivity.showProgressDialog(500L)
                isCalenderFilterOn = !isCalenderFilterOn
                showLog("isCalenderFilterOn", isCalenderFilterOn.toString())
                fileHistoryListActivityViewModel.getFilesFromDirectory(binding.root)
            }
        }
    }

    private fun setUpView() {
        binding.apply {
            //
            clMainConstraint.setPadding(myActivity, start = 20f, end = 20f)

            //
            ivCalenderFilter.apply {
                setMargin(myActivity, end = 20f, top = 20f)
            }

            //
            ivAlphabetFilter.apply {
                setMargin(myActivity, end = 15f)
            }

            //
            rcViewFileHistoryList.setMargin(myActivity, top = 10f)
        }
    }

    private fun updateFilters(fileList: List<PdfInfo>): List<PdfInfo> {
        binding.apply {
            return when {
                isAlphabetFilterOn && isCalenderFilterOn -> {
                    ivAlphabetFilter.setIconColor(myActivity, R.color.pink)
                    ivCalenderFilter.setIconColor(myActivity, R.color.pink)
                    showLog("Filters", "Both filters are ON")
                    // Perform action for both filters being ON
                    fileList.sortedWith(compareBy<PdfInfo> { it.name }.thenByDescending { it.date })
                }

                isAlphabetFilterOn && !isCalenderFilterOn -> {
                    ivAlphabetFilter.setIconColor(myActivity, R.color.pink)
                    ivCalenderFilter.setIconColor(myActivity, R.color.black)
                    showLog("Filters", "Alphabet filter ON, Calendar filter OFF")
                    // Perform action for alphabet filter ON, calendar filter OFF
                    fileList.sortedBy { it.name }
                }

                !isAlphabetFilterOn && isCalenderFilterOn -> {
                    ivAlphabetFilter.setIconColor(myActivity, R.color.black)
                    ivCalenderFilter.setIconColor(myActivity, R.color.pink)
                    showLog("Filters", "Alphabet filter OFF, Calendar filter ON")
                    // Perform action for alphabet filter OFF, calendar filter ON
                    fileList.sortedByDescending { it.date }
                }

                else -> {
                    ivAlphabetFilter.setIconColor(myActivity, R.color.black)
                    ivCalenderFilter.setIconColor(myActivity, R.color.black)
                    showLog("Filters", "Both filters are OFF")
                    // Perform action for both filters being OFF
                    fileList
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        Finish()
    }
}