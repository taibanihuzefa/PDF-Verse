package com.example.pdfverse.viewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pdfverse.viewModel.HomeActivityViewModel

@Suppress("UNCHECKED_CAST")
class HomeActivityViewModelFactory(private var application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeActivityViewModel(application) as T
    }
}