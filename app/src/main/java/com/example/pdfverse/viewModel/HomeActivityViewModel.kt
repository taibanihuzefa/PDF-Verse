package com.example.pdfverse.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pdfverse.dataModels.PdfInfo
import com.example.pdfverse.utils.getAllFilesFromDirectory

class HomeActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val _filesListData = MutableLiveData<List<PdfInfo>>()
    val filesListData: LiveData<List<PdfInfo>> get() = _filesListData

    fun getFilesFromDirectory(view: View) {
        val pdfList = getAllFilesFromDirectory(view.context)
        _filesListData.value = pdfList
    }
}