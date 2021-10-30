package com.yuk.miuihome.module

import com.yuk.miuihome.utils.OwnSP.ownSP
import com.yuk.miuihome.utils.ktx.hookBeforeMethod

class ModifyBlurLevel {

    fun init() {
        if (ownSP.getBoolean("simpleAnimation", false)) {
            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("getBlurType") {
                it.result = 0
            }

            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUseCompleteBlurOnDev") {
                it.result = false
            }
        } else {
            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("getBlurType") {
                when (ownSP.getString("blurLevel", "")) {
                    "COMPLETE" -> {
                        it.result = 2
                    }
                    "SIMPLE" -> {
                        it.result = 1
                    }
                    "NONE" -> {
                        it.result = 0
                    }
                }
            }

            "com.miui.home.launcher.common.BlurUtils".hookBeforeMethod("isUseCompleteBlurOnDev") {
                when (ownSP.getString("blurLevel", "")) {
                    "TEST" -> {
                        it.result = true
                    }
                }
            }

            "com.miui.home.launcher.common.DeviceLevelUtils".hookBeforeMethod("isLowLevelOrLiteDevice") {
                when (ownSP.getString("blurLevel", "")) {
                    "TEST" -> {
                        it.result = false
                    }
                    "COMPLETE" -> {
                        it.result = false
                    }
                }
            }
        }
    }
}