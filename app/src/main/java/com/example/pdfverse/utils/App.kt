package com.example.pdfverse.utils

import android.app.Application
import android.content.Context

class App : Application() {
    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    companion object {
        var instance: App? = null
            private set
        val context: Context?
            get() = instance
    }
}