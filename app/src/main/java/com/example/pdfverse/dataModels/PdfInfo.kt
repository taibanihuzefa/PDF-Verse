package com.example.pdfverse.dataModels

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PdfInfo(
    val name: String? = null,
    val size: Long? = 0,
    val date: Long? = 0,
    val path: String? = null,
    val extension: String? = null,
    val nameWithoutExtension: String? = null,
    var isEncrypted: Boolean? = false,
    var password: String? = null
) : Parcelable

