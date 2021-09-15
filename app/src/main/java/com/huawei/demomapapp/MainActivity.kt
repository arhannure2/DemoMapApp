package com.huawei.demomapapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.material.textview.MaterialTextView
import com.huawei.demomapapp.communication.ApiPostService
import com.huawei.demomapapp.communication.ApiPostUtils
import com.huawei.demomapapp.communication.response.ReverseGeoCodingResponse
import com.huawei.demomapapp.constant.ApiConstant
import com.huawei.demomapapp.reversegeocode.GeocodingService

import com.huawei.demomapapp.utils.GPSUtility
import com.huawei.demomapapp.utils.LogUtils
import com.huawei.demomapapp.utils.MyNetworkUtil
import com.huawei.hms.location.*
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.MapView
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.LatLng
import com.huawei.hms.maps.model.Marker
import com.huawei.hms.maps.model.MarkerOptions
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import toast
import java.io.IOException


class MainActivity : AppCompatActivity() , OnMapReadyCallback{
    // HUAWEI map
    private var hMap: HuaweiMap? = null

    private var mMapView: MapView? = null

    private var mLocationRequest: LocationRequest? = null
    // Define a location provider client.
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    var LOCATION_UPDATE_INTERVEL: Long =  5 * 1000L // 5 sec

    var previousLocation: Location? = null

    // Define a device setting client.
    // Create a fusedLocationProviderClient object.
    private val settingsClient: SettingsClient? = null

    private var textViewLat: MaterialTextView? = null
    private var textViewLong: MaterialTextView? = null
    private var textViewAddressDescription: MaterialTextView? = null
    private var apiPostService: ApiPostService? = null




    companion object {
        private const val TAG = "MapViewDemoActivity"
        private const val MAPVIEW_BUNDLE_KEY = "MapViewBundleKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate:")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // method call to initialize the views
        initViews()

        // method call to initialize the listeners
       // initListeners()



        mMapView = findViewById(R.id.mapView)
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle =
                savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
        }
        mMapView?.apply {
            onCreate(mapViewBundle)
            getMapAsync(this@MainActivity)
        }

        setLocationRequest()
        getLastLocation()
        startLocationUpdates()

        /////Check whether user has internet connection or not
        if (MyNetworkUtil.getInstance(this@MainActivity).isOnline) {

            ////Check for location permission
            /////Now check GPS is on or not , if not ask for GPS to on it

            if (GPSUtility.askForPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    GPSUtility.LOCATION,
                    this@MainActivity
                )
            ) {
                if (GPSUtility.isLocationEnabled(this@MainActivity)) {
                    //////Here Location permission is granted and GPS is ON , ok to proceed
                    //// here go to current location on map


                        //// testing Reverse Geocoding via doc
                    GeocodingService.reverseGeocoding(ApiConstant.SECRET_API_KEY)
                  ///////Testing  reverseGeocoding api call via Retrofit
                    //reverseGeoCodingPost()

                   if(previousLocation!=null)
                   {

                       val update = CameraUpdateFactory.newLatLng(previousLocation!! as LatLng)
                       val zoom = CameraUpdateFactory.zoomTo(15f)

                       hMap!!.moveCamera(update)
                       hMap!!.animateCamera(zoom)
                   }



                } else {
                    ////call setting to enable GPS
                    GPSUtility.showGPSDisabledAlertToUser(this@MainActivity)
                }

            }


        } else {
            LogUtils.error(LogUtils.TAG, "CheckConnection =>" + "\"########You are offline!!!!")

            MyNetworkUtil.showCustomNetworkError(contextNetworkError = this@MainActivity)


        }

    }



    /**
     * method to initialize the views
     */
    private fun initViews() {

        textViewLat = findViewById<View>(R.id.tvLatitude) as MaterialTextView
        textViewLong = findViewById<View>(R.id.tvLongitude) as MaterialTextView
        textViewAddressDescription = findViewById<View>(R.id.tvAddressDescription) as MaterialTextView
    }

    var markertemp : Marker? = null

    override fun onMapReady(map: HuaweiMap) {
        Log.d(TAG, "onMapReady: ")
        hMap = map

        val location = hMap!!.cameraPosition.target as LatLng
        /// call here location to address conversion Huawei api
        toast("Location :" + location.latitude + "," + location.longitude)
        // Enable the my-location layer.
        hMap!!.setMyLocationEnabled(true)
        // Enable the my-location icon.
        hMap!!.getUiSettings().setMyLocationButtonEnabled(true);
        markertemp = hMap!!.addMarker(
            MarkerOptions().position(hMap!!.cameraPosition.target)
                .title("Title:HERE")
        ) as Marker

    }


    override fun onStart() {
        super.onStart()
        mMapView?.onStart()

//        if(markertemp!=null) {
//            markertemp!!.remove()
//            markertemp = null
//        }
//
//        hMap!!.clear()
    }

    override fun onStop() {
        super.onStop()
        mMapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
        stopLocationUpdates()
    }

    override fun onPause() {
        mMapView?.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }


    private fun setLocationRequest() {
        mLocationRequest = LocationRequest.create()
        //mLocationRequest!!.setInterval(1 * 1000);// 1s
        mLocationRequest!!.setInterval(LOCATION_UPDATE_INTERVEL);//  sec
        //mLocationRequest!!.interval = 500// 0.5s (half sec.)

        //make it 7 seconds
        //mLocationRequest!!.interval = OGoConstant.LOCATION_UPDATE_PERIOD_MSEC
        //mLocationRequest!!.setFastestInterval(100);
        //mLocationRequest!!.interval = OGoConstant.LOCATION_UPDATE_PERIOD_MSEC
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY


    }

     var mLocationCallback: LocationCallback = object : LocationCallback() {
         override fun onLocationResult(locationResult: LocationResult?) {

             if (locationResult != null) {

                 val locations: List<Location> = locationResult.getLocations()

                 if (!locations.isEmpty()) {
                     for (location in locations) {
                         Log.i(
                             TAG,
                             "onLocationResult location[Longitude,Latitude,Accuracy]:" + location.longitude + "," + location.latitude + "," + location.accuracy
                         )
                     }
                 }

                 val mLastLocation = locationResult?.lastLocation

                 previousLocation = mLastLocation
                 toast("mLastLocation:" + mLastLocation!!.latitude + "," + mLastLocation!!.longitude)
                 textViewLat!!.text= mLastLocation!!.latitude.toString()
                 textViewLong!!.text= mLastLocation!!.longitude.toString()
             }





         }

         override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
             super.onLocationAvailability(locationAvailability)
             if (locationAvailability != null) {
                 val flag: Boolean = locationAvailability.isLocationAvailable()
                 Log.i(TAG, "onLocationAvailability isLocationAvailable:$flag")
                 toast("isLocationAvailable:" + flag)
             }
         }


     }




         /**
          * Provides a simple way of getting a device's location and is well suited for
          * applications that do not require a fine-grained location and that do not need location
          * updates. Gets the best and most recent location currently available, which may be null
          * in rare cases when a location is not available.
          * <p>
          * Note: this method should be called after location permission has been granted.
          */
         @SuppressWarnings("MissingPermission")
         private fun getLastLocation() {


             fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@MainActivity)
             val addOnCompleteListener =
                     fusedLocationProviderClient!!.lastLocation.addOnCompleteListener {

                         if (it.isSuccessful() && it.getResult() != null) {
                             val mLastLocation = it.getResult()

                             LogUtils.error(
                                 LogUtils.TAG,
                                 ": ${javaClass.simpleName} : getLastLocation : mLastLocation.latitude " + mLastLocation.latitude
                             )
                             LogUtils.error(
                                 LogUtils.TAG,
                                 " : ${javaClass.simpleName} : getLastLocation : mLastLocation.longitude  " + mLastLocation.longitude
                             )
                         } else {
                             LogUtils.error(LogUtils.TAG, " LOCATION : no location detected")


                         }
                     }

         }


         /**
          * Note: this method should be called after location permission has been granted.
          */
         @SuppressWarnings("MissingPermission")
         private fun startLocationUpdates() {
             fusedLocationProviderClient!!.requestLocationUpdates(
                 mLocationRequest!!,
                 mLocationCallback!!,
                 null
             )

         }

         private fun stopLocationUpdates() {
             if (fusedLocationProviderClient != null)
                 fusedLocationProviderClient!!.removeLocationUpdates(mLocationCallback!!)
         }

    private fun reverseGeoCodingPost()
    {
        apiPostService = ApiPostUtils.apiPostService
        val paramObject = JSONObject()
        val location = JSONObject()
        try {

            //// test lat long 29.365937528099725, 47.96714125349369
            location.put("lng", 47.96714125349369)
            location.put("lat", 29.365937528099725)
            paramObject.put("location", location)
            paramObject.put("language", ApiConstant.ENGLISH_LANGUAGE)
        } catch (e: JSONException) {
            Log.e("error", e.message!!)
        }


        /// //// test lat long 29.365937528099725, 47.96714125349369
        apiPostService!!.reverseGeoCoding(paramObject.toString()).enqueue(object :
            Callback<ReverseGeoCodingResponse> {

            override fun onResponse(call: Call<ReverseGeoCodingResponse>, response: Response<ReverseGeoCodingResponse>) {

                LogUtils.error(LogUtils.TAG, "response.raw().toString() =>" + response.raw().toString())

                if (response.isSuccessful) {

                    showReverseGeoCodingResponse(response.body()!!.toString())



                }
            }

            override fun onFailure(call: Call<ReverseGeoCodingResponse>, t: Throwable) {

                if (t is IOException) {
                    Toast.makeText(this@MainActivity, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                    // logging probably not necessary
                } else {
                    Toast.makeText(this@MainActivity, "conversion issue! big problems :(", Toast.LENGTH_SHORT).show();
                    // todo log to some central bug tracking service
                    Toast.makeText(this@MainActivity, " Unable to submit postOrdersHistory to API.!", Toast.LENGTH_LONG).show();
                }

                LogUtils.error("TAG", "Unable to submit postOrdersHistory to API." + t.printStackTrace())
                LogUtils.error("TAG", "Unable to submit postOrdersHistory to API.")

                // showProgress(false)

            }
        })





    }

    fun showReverseGeoCodingResponse(response: String) {
        LogUtils.error("TAG>>>>RESPONSE>>>>", response)
    }










}



