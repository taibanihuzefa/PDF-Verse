package com.example.pdfverse.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pdfverse.utils.getFileFromFileName
import com.example.pdfverse.utils.showLog
import java.io.File

class FileDetailsActivityViewModel(application: Application) : AndroidViewModel(application) {/*  private var _filesListData = MutableLiveData<List<PdfInfo>>()
      val filesListData: LiveData<List<PdfInfo>> get() = _filesListData*/

    private var _filesListData = MutableLiveData<File>()
    val filesListData: LiveData<File> get() = _filesListData

//    private var _fileData = MutableLiveData<File>()
//    val fileData: LiveData<File> get() = _fileData


    fun getDataFromIntent(view: View, fileName: String? = null) {
        val pdfFile = getFileFromFileName(view.context, fileName!!)
        pdfFile.let {
            _filesListData.value = pdfFile
            showLog("File", "File found")
            showLog("File found", pdfFile.name)
        }


        /* pdfInfo?.let {
             _filesListData.value = listOf(it)
         }*/
    }

    /* fun getFile(view: View, fileName: String) {
         val pdfList = getFileFromFileName(view.context, fileName)
         if (pdfList.exists()) {
             _filesListData.value = pdfList
             showLog("File", "File found")
         } else {
             showLog("File", "File not found")
         }
     }*/
}