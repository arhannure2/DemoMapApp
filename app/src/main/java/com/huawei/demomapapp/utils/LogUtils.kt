package com.huawei.demomapapp.utils

/*
Created by ​
Hannure Abdulrahim

 */

import android.util.Log
import com.huawei.demomapapp.BuildConfig


/*
*
* Ensure every Log.d you made by calling LogUtils.debug instead.
* every Log.e you made by calling LogUtils.error instead.
* every Log.w you made by calling LogUtils.warnings instead.
*
*/

object LogUtils {
    var TAG = "###Demo Map App###"



    fun debug(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    fun error(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)
        }
    }

    fun warnings(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
           Log.w(tag, message)
        }
    }

    fun info(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }

}
