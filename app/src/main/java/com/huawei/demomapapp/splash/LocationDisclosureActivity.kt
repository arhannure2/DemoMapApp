package com.huawei.demomapapp.splash

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.huawei.demomapapp.MainActivity
import com.huawei.demomapapp.R
import com.huawei.demomapapp.utils.GPSUtility
import com.huawei.demomapapp.utils.LogUtils.TAG
import toastShort


/*
Created by ​
Hannure Abdulrahim


on 8/19/2021.
 */
class LocationDisclosureActivity : AppCompatActivity() {

    private var tvNoThanks: MaterialTextView? = null
    private var tvLocationTurnOn: MaterialButton? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_location_disclosure)

        // method call to initialize the views
        initViews()

        // method call to initialize the listeners
        initListeners()



    }


    public override fun onResume() {
        super.onResume()
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

    public override fun onPause() {
        super.onPause()
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

    private fun gotoMainActivity()
    {


        val i = Intent(this@LocationDisclosureActivity, MainActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(i)
        // close this activity
        finish()

    }

//    /**
//     * Callback received when a permissions request has been completed.
//     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == GPSUtility.LOCATION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission granted!", Toast.LENGTH_SHORT).show()
                gotoMainActivity()

            }
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 1) {
//            if (grantResults.size > 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSION successful")
//                gotoMainActivity()
//            } else {
//                Log.i(TAG, "onRequestPermissionsResult: apply LOCATION PERMISSSION  failed")
//            }
//        }
//        if (requestCode == 2) {
//            if (grantResults.size > 2 && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                Log.i(
//                    TAG,
//                    "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION successful"
//                )
//                gotoMainActivity()
//            } else {
//                Log.i(TAG, "onRequestPermissionsResult: apply ACCESS_BACKGROUND_LOCATION  failed")
//            }
//        }
//    }


    /**
     * method to initialize the views
     */
    private fun initViews() {

        tvNoThanks = findViewById<View>(R.id.tvNoThanks) as MaterialTextView
        tvLocationTurnOn = findViewById<View>(R.id.tvLocationTurnOn) as MaterialButton
    }

    /**
     * method to initialize the click listeners
     */
    private fun initListeners() {

        tvNoThanks!!.setOnClickListener {
            finish()
        }

        tvLocationTurnOn!!.setOnClickListener {


            /// here ask ofr permission
            if (GPSUtility.askForPermission(Manifest.permission.ACCESS_FINE_LOCATION, GPSUtility.LOCATION, this@LocationDisclosureActivity)) {
                gotoMainActivity()

            }else{
                //Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
            }

//            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
//                Log.i(TAG, "sdk < 28 Q")
//                if (ActivityCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED
//                    && ActivityCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    val strings = arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    )
//                    ActivityCompat.requestPermissions(this, strings, 1)
//                }
//            } else {
//                if (ActivityCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                        this,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                        this,
//                        "android.permission.ACCESS_BACKGROUND_LOCATION"
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    val strings = arrayOf(
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        "android.permission.ACCESS_BACKGROUND_LOCATION"
//                    )
//                    ActivityCompat.requestPermissions(this, strings, 2)
//                }
//            }




        }

    }



}