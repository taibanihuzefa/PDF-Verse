package com.example.pdfverse.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pdfverse.viewModel.FileDetailsActivityViewModel

@Suppress("UNCHECKED_CAST")
class FileDetailsActivityViewModelFactory(private var application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FileDetailsActivityViewModel(application) as T
    }
}