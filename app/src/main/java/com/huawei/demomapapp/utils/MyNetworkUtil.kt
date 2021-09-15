package com.huawei.demomapapp.utils

/*
Created by ​
Hannure Abdulrahim

 */

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings


import android.util.Log
import android.view.ContextThemeWrapper
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import com.huawei.demomapapp.R



class MyNetworkUtil {

    internal lateinit var connectivityManager: ConnectivityManager

    val isOnline: Boolean
        get() {
            var result = false
            try {

                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val networkCapabilities = connectivityManager.activeNetwork ?: return false
                    val actNw =
                            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                    result = when {
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                } else {
                    connectivityManager.run {
                        connectivityManager.activeNetworkInfo?.run {
                            result = when (type) {
                                ConnectivityManager.TYPE_WIFI -> true
                                ConnectivityManager.TYPE_MOBILE -> true
                                ConnectivityManager.TYPE_ETHERNET -> true
                                else -> false
                            }

                        }
                    }
                }

                return result



            } catch (e: Exception) {
                println("CheckConnectivity Exception: " + e.message)
                Log.v("connectivity", e.toString())
            }

            return result
        }

    companion object {
         val instance = MyNetworkUtil()

        internal lateinit var context: Context

        fun getInstance(ctx: Context): MyNetworkUtil {
            context = ctx.applicationContext
            return instance
        }



        /// inside companion object
        /// name argument used if not provided then default will be used
        //// argument order is not important here in if name argument used
        fun   showCustomNetworkError(title:String="Network Error!", message:String="Please check your network connection.", positiveButtonText:String="Okay", negativeButtonText:String="Cancel", contextNetworkError: Context, @StyleRes style: Int = R.style.MyAlertDialogTheme, @DrawableRes icon:Int=android.R.drawable.ic_dialog_alert) {


            ///without cutom theme
            //AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom)).apply {}
            /// sample style
            // R.style.AlertDialog_AppCompat_Light
            // R.style.AlertDialogCustom //  if you dont pass it will take as default

            AlertDialog.Builder(ContextThemeWrapper(contextNetworkError, style)).apply {
                setTitle(title)
                setMessage(message)
                setIcon(icon)
                setPositiveButton(positiveButtonText) { _, _ ->
                    //contextNetworkError.toast("todo")
                    //startActivity(Intent(WifiManager.ACTION_PICK_WIFI_NETWORK))
                    contextNetworkError.startActivity(Intent(Settings.ACTION_SETTINGS))

                }
                setNegativeButton(negativeButtonText) { _, _ ->

                }
            }.create().show()


        }

    }





}
