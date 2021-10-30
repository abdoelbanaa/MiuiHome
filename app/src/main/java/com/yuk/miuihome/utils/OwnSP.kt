package com.yuk.miuihome.utils

import android.content.Context
import android.content.SharedPreferences
import com.yuk.miuihome.Config.SpName
import com.yuk.miuihome.HomeContext

object OwnSP {
    val ownSP: SharedPreferences by lazy {
        HomeContext.context.createDeviceProtectedStorageContext()
            .getSharedPreferences(SpName, Context.MODE_PRIVATE)
    }
    private val ownEditor: SharedPreferences.Editor = ownSP.edit()
    fun set(key: String, any: Any) {
        when (any) {
            is Int -> ownEditor.putInt(key, any)
            is Float -> ownEditor.putFloat(key, any)
            is String -> ownEditor.putString(key, any)
            is Boolean -> ownEditor.putBoolean(key, any)
            is Long -> ownEditor.putLong(key, any)
        }
        ownEditor.apply()
    }

    fun remove(key: String) {
        ownEditor.remove(key)
        ownEditor.apply()
    }
}