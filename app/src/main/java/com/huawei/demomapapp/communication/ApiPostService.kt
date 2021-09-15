package  com.huawei.demomapapp.communication


import com.huawei.demomapapp.communication.response.ReverseGeoCodingResponse
import com.huawei.demomapapp.constant.ApiConstant
import retrofit2.Call
import retrofit2.http.*


/*
Created by ​
Hannure Abdulrahim

 */

interface ApiPostService {

    /*
     * On top of the method is the @POST annotation, which indicates that we want to execute a POST request when this method is called.
     * The argument value for the @POST annotation is the endpoint—which is /posts. So the full URL will be http://jsonplaceholder.typicode.com/posts.
      *
     *
     here FULL URL will be
     https://makan.place/makanwebserives_test/mkapi/login_update.php

    * */

    /*//@POST("/posts")
    @POST("login_update.php")
    @FormUrlEncoded
    fun savePost(@Field("api_key") api_key: String,
                 @Field("username") username: String,
                 @Field("password") password: String): Call<Post>*/




    @POST(ApiConstant.REVERSE_GEOCODING_URL)
    @Headers("Content-Type: application/json")

    fun reverseGeoCoding(@Body body:String): Call<ReverseGeoCodingResponse>


}