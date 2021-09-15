package com.huawei.demomapapp.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.huawei.demomapapp.R
import com.huawei.hmf.tasks.OnCompleteListener
import com.huawei.hmf.tasks.Task

import com.huawei.hms.common.ApiException
import com.huawei.hms.common.ResolvableApiException
import com.huawei.hms.location.*



/*
Created by ​
Hannure Abdulrahim


on 6/12/2018.
 */
object GPSUtility {

    val LOCATION = 0x1
    val GPS_SETTINGS = 0x7//GPS Setting

    val MY_PERMISSIONS_REQUEST_LOCATION = 99

    ///Ask user for location permission

    fun askForPermission(permission: String, requestCode: Int?, context: Context): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again


                ActivityCompat.requestPermissions(context, arrayOf(permission), requestCode!!)


            } else {

                ActivityCompat.requestPermissions(context, arrayOf(permission), requestCode!!)
            }
            return false


        } else {

            //Toast.makeText(context, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show()
            return true

        }
    }



    fun isLocationPermissionGranted(context: Context): Boolean {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }



    fun checkLocationPermission(context: Context): Boolean {
        if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(context as Activity)
                        .setTitle("title_location_permission")
                        .setMessage("text_location_permission")
                        .setPositiveButton("ok", DialogInterface.OnClickListener { dialogInterface, i ->
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(context as Activity,
                                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    MY_PERMISSIONS_REQUEST_LOCATION)
                        })
                        .create()
                        .show()


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context as Activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION)
            }
            return false
        } else {
            return true
        }
    }




    //// This method will generate dialog box if GPS is not enable and ask user to enable it.
     fun askForGPS(context: Context) {
        val RESULT_CODE = 101//GPS Setting
        var mLocationRequest: LocationRequest?=null
        mLocationRequest = LocationRequest.create()
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest.setInterval(30 * 1000)
        mLocationRequest.setFastestInterval(5 * 1000)
        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        builder.setAlwaysShow(true)

        ////Check result in  onActivityResult in RESULT_CODE
        ///latest
        val task = LocationServices.getSettingsClient(context).checkLocationSettings(builder.build())
        task.addOnCompleteListener(object: OnCompleteListener<LocationSettingsResponse> {
            override fun onComplete(task: Task<LocationSettingsResponse>) {
                try
                {
                    val response = task.getResult()
                    // All location settings are satisfied. The client can initialize location
                    // requests here.
                }
                catch (exception: ApiException) {
                    when (exception.getStatusCode()) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                            // Location settings are not satisfied. But could be fixed by showing the
                            // user a dialog.
                            try
                            {
                                // Cast to a resolvable exception.
                                val resolvable = exception as ResolvableApiException
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(
                                    context as Activity,
                                    RESULT_CODE)
                            }
                            catch (e:Throwable) {
                                // Ignore the error.
                            }
                            catch (e:Throwable) {
                                // Ignore, should be an impossible error.
                            }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                    }// Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.
                }
            }
        })


        fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent) {
            val states = LocationSettingsStates.fromIntent(data)
            when (requestCode) {
                RESULT_CODE -> when (resultCode) {
                    Activity.RESULT_OK ->
                        // All required changes were successfully made
                        Toast.makeText(context, " "+ states.isLocationPresent(), Toast.LENGTH_SHORT).show()
                    Activity.RESULT_CANCELED ->
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(context, " "+ context.getString(R.string.cancel), Toast.LENGTH_SHORT).show()
                    else -> {}
                }
            }
        }



    }





    fun isLocationEnabled(context: Context): Boolean {
        lateinit var locationManager: LocationManager
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


//    fun isPlayServicesAvailable(context: Context): Boolean {
//        val googleApiAvailability = GoogleApiAvailability.getInstance()
//        val status = googleApiAvailability.isGooglePlayServicesAvailable(context)
//        return ConnectionResult.SUCCESS == status
//    }


    /*
   * This is method which  will open setting to enable GPS ,
   * This will not automatically enable after clicking on 'Enable GPS' , user has to click manually
   * to enable manually please check method GPSUtility.askForGPS(...)
   *
   *
   * */
    fun showGPSDisabledAlertToUser(context: Context) {
        LogUtils.error(LogUtils.TAG, "showGPSDisabledAlertToUser called!")

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
            .setCancelable(false)
            .setPositiveButton("Enable GPS"
            ) { dialog, id ->
                val callGPSSettingIntent = Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(callGPSSettingIntent)
            }
        alertDialogBuilder.setNegativeButton("Cancel",
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, id: Int) {
                    dialog.cancel()
                }
            })
        val alert = alertDialogBuilder.create()
        alert.show()


    }




}