package com.huawei.demomapapp.communication

import com.huawei.demomapapp.constant.ApiConstant


/*
Created by ​
Hannure Abdulrahim

 */

object ApiPostUtils {

    val apiPostService: ApiPostService
        get() = RetrofitClient.getClient(ApiConstant.ROOT_URL)!!.create(ApiPostService::class.java)
}