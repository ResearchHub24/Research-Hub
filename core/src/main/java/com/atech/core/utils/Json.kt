package com.atech.core.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun <T> fromJSON(json: String, type: Class<T>): T? =
    try {
        Gson().fromJson(json, type)
    } catch (e: Exception) {
        Log.d(AppErrors.ERROR.name, "fromJSON: ${e.message}")
        null
    }

inline fun <reified T> fromJsonList(json: String): List<T> {
    try {
        val gson = Gson()
        val type = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(json, type)
    } catch (e: Exception) {
        Log.d(AppErrors.ERROR.name, "fromJsonList: ${e.message}")
        return emptyList()
    }
}


fun <T> toJSON(obj: T): String =
    Gson().toJson(obj)
