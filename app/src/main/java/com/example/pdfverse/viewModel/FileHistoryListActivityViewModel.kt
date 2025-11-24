package com.example.pdfverse.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pdfverse.dataModels.PdfInfo
import com.example.pdfverse.utils.getAllFilesFromDirectory
import com.example.pdfverse.utils.showToast

class FileHistoryListActivityViewModel(application: Application) : AndroidViewModel(application) {
    private var _filesListData = MutableLiveData<List<PdfInfo>>()
    val filesListData: LiveData<List<PdfInfo>> get() = _filesListData


    fun getFilesFromDirectory(view: View) {
        val pdfList = getAllFilesFromDirectory(view.context)

        if (pdfList.isNotEmpty()) {
            _filesListData.value = pdfList
        } else {
            (view.context).showToast("No files found")
        }
    }
}