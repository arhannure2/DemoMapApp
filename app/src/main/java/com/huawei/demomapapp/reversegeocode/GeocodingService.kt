package com.huawei.demomapapp.reversegeocode

import android.util.Log
import com.huawei.demomapapp.constant.ApiConstant
import com.huawei.demomapapp.utils.LogUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*


object GeocodingService {

    val JSON: MediaType = "application/json; charset=utf-8".toMediaTypeOrNull()!!
    @Throws(UnsupportedEncodingException::class)
    fun reverseGeocoding(apiKey: String?) {
        val json = JSONObject()
        val location = JSONObject()
        try {

            //// test lat long 29.365937528099725, 47.96714125349369
            location.put("lng", 47.96714125349369)
            location.put("lat", 29.365937528099725)
            json.put("location", location)
            json.put("language", ApiConstant.ENGLISH_LANGUAGE)
        } catch (e: JSONException) {
            Log.e("error", e.message!!)
        }
        val body: RequestBody = RequestBody.create(JSON, json.toString())
        val client = OkHttpClient()
        val request: Request = Request.Builder().url(
            ApiConstant.ROOT_URL + ApiConstant.connection + URLEncoder.encode(
                apiKey,
                "UTF-8"
            )
        )
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ReverseGeocoding", e.toString())
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                Log.e(
                    "ReverseGeocoding", Objects.requireNonNull(response.body)!!
                        .string()
                )

//                Gson gson = new Gson();
//                //ReverseGeoCodingResponse reverseGeoCodingResponse = gson.fromJson(response.body(), ReverseGeoCodingResponse.class);
//                ReverseGeoCodingResponse reverseGeoCodingResponse = gson.fromJson(response.body().toString(), ReverseGeoCodingResponse.class);
//                LogUtils.INSTANCE.error(LogUtils.INSTANCE.getTAG(),"reverseGeoCodingResponse.getSites().get(0).getFormatAddress:"+reverseGeoCodingResponse.getSites().get(0).getFormatAddress());
//                LogUtils.INSTANCE.error(LogUtils.INSTANCE.getTAG(),"gson.toJson(reverseGeoCodingResponse):"+gson.toJson(reverseGeoCodingResponse));


                if (response.body != null) {
                    // use this to get String
                    //  String res = response.body().string();
                    //instead of
                    //String res = response.body().toString();

                }


//                ///converting map to jason
//                val gson = Gson()
//                val jsonResponse = gson.toJson(Objects.requireNonNull(response.body)!!)
//                ////converting  MAP to POJO model class
//                var reverseGeoCodingResponse: ReverseGeoCodingResponse? = null
//                //val jsonElement = gson.toJsonTree(Objects.requireNonNull(response.body)!!)
//                reverseGeoCodingResponse = gson.fromJson(jsonResponse, ReverseGeoCodingResponse::class.java)
//
//               LogUtils.error(LogUtils.TAG,"reverseGeoCodingResponse!!.sites.get(0).formatAddress:"+reverseGeoCodingResponse.toString())


                try {
                    val jsonObj = JSONObject(response.body!!.toString())

                    LogUtils.error(LogUtils.TAG,"jsonObj.toString():"+jsonObj.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Log.e("TAG", "No valid json")
                }


            }
        })
    }
}