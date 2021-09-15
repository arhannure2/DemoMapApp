package com.huawei.demomapapp.splash

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


import com.huawei.demomapapp.MainActivity
import com.huawei.demomapapp.R

import com.huawei.demomapapp.utils.GPSUtility


import startNewActivity


/*
Created by ​
Hannure Abdulrahim

 */

class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_splash)

        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                ///////Your Code

                /// if location permission not allowed then show Prominent disclosure location screen else go to login screen

                if (GPSUtility.isLocationPermissionGranted( this@SplashScreenActivity))
                {
                    startNewActivity(MainActivity::class.java)
                }else
                {
                    //display Prominent disclosure location screen
                    startNewActivity(LocationDisclosureActivity::class.java)
                }

               ///finish this activity
                finish()

            }, 3000)
        }


    }



    public override fun onResume() {
        super.onResume()
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

    public override fun onPause() {
        super.onPause()
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }



    private fun proceedToNextActivity(intentNextActivity: Intent)
    {
        intentNextActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intentNextActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intentNextActivity)
       // finish()

    }








}