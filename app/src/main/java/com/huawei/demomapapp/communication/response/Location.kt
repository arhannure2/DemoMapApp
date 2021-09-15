package com.huawei.demomapapp.communication.response

import java.io.Serializable

data class Location(
    val lat: Double,
    val lng: Double
): Serializable