package com.yuk.miuihome.module

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.yuk.miuihome.utils.Config
import com.yuk.miuihome.utils.OwnSP
import com.yuk.miuihome.utils.ktx.callMethod
import com.yuk.miuihome.utils.ktx.callStaticMethod
import com.yuk.miuihome.utils.ktx.findClass
import com.yuk.miuihome.utils.ktx.hookAfterMethod
import de.robv.android.xposed.XposedBridge

class ModifyAppReturnBlur {

    fun init() {
        if (OwnSP.ownSP.getBoolean("appReturnAmin", false)) {
            val value = (OwnSP.ownSP.getFloat("animationLevel", -1f) * 500).toLong()
            val handler = Handler(Looper.getMainLooper())
            val blurClass = "com.miui.home.launcher.common.BlurUtils".findClass()
            val launcherClass = "com.miui.home.launcher.Launcher".findClass()
            val navStubViewClass = "com.miui.home.recents.NavStubView".findClass()
            launcherClass.hookAfterMethod("onCreate", Bundle::class.java
            ) {
                val activity = it.thisObject as Activity
                val view: View = activity.findViewById(activity.resources.getIdentifier("recents_container", "id", Config.hookPackage))
                val runnable = Runnable { blurClass.callStaticMethod("fastBlur", 0.0f, activity.window, true, value) }
                navStubViewClass.hookAfterMethod("performAppToHome"
                ) {
                    val isFolderShowing = activity.callMethod("isFolderShowing") as Boolean
                    val isInEditing = activity.callMethod("isInEditing") as Boolean
                    val isUserBlurWhenOpenFolder = blurClass.callStaticMethod("isUserBlurWhenOpenFolder") as Boolean
                    //XposedBridge.log("IsFolderShowing now is $isFolderShowing")
                    //XposedBridge.log("IsInEditing now is $isInEditing")
                    //XposedBridge.log("IsUserBlurWhenOpenFolder now is $isUserBlurWhenOpenFolder")
                    if (view.visibility == View.GONE && !isInEditing) {
                        if ((isUserBlurWhenOpenFolder && !isFolderShowing) or (!isUserBlurWhenOpenFolder && isFolderShowing))
                            handler.postDelayed(runnable, 100)
                    }
                }
            }
        }
    }
}