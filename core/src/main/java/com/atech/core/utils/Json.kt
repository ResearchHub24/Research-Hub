package com.atech.core.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun <T> fromJSON(json: String, type: Class<T>): T? =
    try {
        Gson().fromJson(json, type)
    } catch (e: Exception) {
        Log.d("AAA", "fromJSON: ${e.message}")
        null
    }

inline fun <reified T> fromJsonList(json: String): List<T> {
    val gson = Gson()
    val type = object : TypeToken<List<T>>() {}.type
    return gson.fromJson(json, type)
}


fun <T> toJSON(obj: T): String =
    Gson().toJson(obj)
