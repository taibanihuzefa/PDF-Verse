package com.example.pdfverse.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@Suppress("unused", "PrivatePropertyName", "LocalVariableName", "PropertyName")
class SharePref(context: Context) {


    private var shareObj: SharedPreferences
    private var shareObjlang: SharedPreferences
    private var editor: SharedPreferences.Editor
    private var editor_lang: SharedPreferences.Editor
    var context: Context
    private val gson = Gson()

    init {
        this.context = context
        val APPMAINPREF = "APP_MAIN_PREF"
        val APPLANGPREF = "APP_LANG_PREF"
        shareObj = context.getSharedPreferences(APPMAINPREF, 0)
        shareObjlang = context.getSharedPreferences(APPLANGPREF, 0)
        editor = shareObj.edit()
        editor_lang = shareObjlang.edit()
    }

    /*references*/

    var isLoggedIn: Boolean
        get() = shareObj.getBoolean("isLoggedIn", false)
        set(value) = shareObj.edit().putBoolean("isLoggedIn", value).apply()


    var selectedCupSize: Int
        get() = shareObj.getInt("selectedCupSize", 4)
        set(value) = shareObj.edit().putInt("selectedCupSize", value).apply()


    var addRecordClicked: String?
        get() = shareObj.getString("addRecordClicked", "")
        set(value) = shareObj.edit().putString("addRecordClicked", value).apply()

    var nextReminderTime: Long
        get() = shareObj.getLong("nextReminderTime", 0)
        set(value) = shareObj.edit().putLong("nextReminderTime", value).apply()

    /**  PDF verse app preferences**/

    var isClickedFrom: String?
        get() = shareObj.getString("isClickedFrom", "")
        set(value) = shareObj.edit().putString("isClickedFrom", value).apply()

    var isPasswordProtected: Boolean
        get() = shareObj.getBoolean("isPasswordProtected", false)
        set(value) = shareObj.edit().putBoolean("isPasswordProtected", value).apply()


    // Save multiple image URIs as a JSON string
    fun setImageUris(uris: List<Uri>) {
        val uriStrings = uris.map { it.toString() }
        val json = gson.toJson(uriStrings)
        editor.putString("imageUris", json)
        editor.apply()
    }

    // Get the saved image URIs
    fun getImageUris(): List<Uri> {
        val json = shareObj.getString("imageUris", null)
        return if (json != null) {
            val uriStrings: List<String> =
                gson.fromJson(json, object : TypeToken<List<String>>() {}.type)
            uriStrings.map { Uri.parse(it) }
        } else {
            emptyList()
        }
    }


//    // setting image uris
//    fun setImageUris(imageUris: List<Uri>?) {
//        if (imageUris == null) {
//            editor.putString("imageUris", null)
//            editor.apply()
//            return
//
////        val type = object : TypeToken<List<Uri>?>() {}.type
////        val gson = Gson()
////        val urisJson = gson.toJson(imageUris, type)
////        editor.putString("imageUris", urisJson)
////        editor.apply()
//        }

//        var imageUris: List<Uri>?
//        get() {
//            val urisString = sharedPreferences.getString(IMAGE_URIS_KEY, null) ?: return null
//            return urisString.split(",").map { Uri.parse(it) }
//        }
//        set(value) {
//            val urisString = value?.joinToString(",") { it.toString() }
//            sharedPreferences.edit().putString(IMAGE_URIS_KEY, urisString).apply()
//        }
//    }


//    // Getting image URIs
//    fun getImageUris(): List<Uri>? {
//        val urisJson = shareObj.getString("imageUris", null) ?: return null
//        val type = object : TypeToken<List<Uri>?>() {}.type
//        val gson = Gson()
//        return gson.fromJson(urisJson, type)
//    }

//    fun getImageUris(): List<Uri> {
//        val urisJson = sharedPreferences.getString("imageUris", null)
//        if (urisJson != null) {
//            val type = object : TypeToken<List<Uri>>() {}.type
//            return gson.fromJson(urisJson, type)
//        }
//        return emptyList()
//    }


//    fun putStringLang(k: String, v: String): Boolean {
//        return if (shareObjlang != null && editor_lang != null) {
//            editor_lang.putString(k, v)
//            editor_lang.apply()
//            editor_lang.commit()
//        } else {
//            context.showToast(context.resources.getString(R.string.null_pref_edit))
//            false
//        }
//    }

//    fun getStringLang(k: String, v: String): String? {
//        return if (shareObjlang != null) {
//            shareObjlang.getString(k, v)
//        } else {
//            context.showToast(context.resources.getString(R.string.null_pref_edit))
//            v
//        }
//    }

//    fun putFCM(k: String, v: String): Boolean {
//        return if (shareObjlang != null && editor_lang != null) {
//            editor_lang.putString(k, v)
//            editor_lang.apply()
//            editor_lang.commit()
//        } else {
//            context.showToast(context.resources.getString(R.string.null_pref_edit))
//            false
//        }
//    }

//    fun getFCM(k: String, v: String): String? {
//        return if (shareObjlang != null) {
//            shareObjlang.getString(k, v)
//        } else {
//            context.showToast(context.resources.getString(R.string.null_pref_edit))
//            v
//        }
//    }

    // ********** clear prefrences **********//
    fun clearPreferences() {
        editor.clear()
        editor.apply()
    }

    fun removeKey(s: String) {
        editor.remove(s)
        editor.apply()
        editor.commit()

    }


}