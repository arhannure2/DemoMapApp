package com.huawei.demomapapp.communication.response

data class Poi(
    val hwPoiTypes: List<String>,
    val icon: String,
    val internationalPhone: String,
    val poiTypes: List<String>,
    val rating: Double
)