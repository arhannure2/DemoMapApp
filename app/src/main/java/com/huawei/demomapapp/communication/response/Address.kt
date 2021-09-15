package com.huawei.demomapapp.communication.response

import java.io.Serializable

data class Address(
    val adminArea: String,
    val country: String,
    val countryCode: String,
    val locality: String,
    val postalCode: String,
    val streetNumber: String,
    val subAdminArea: String,
    val tertiaryAdminArea: String,
    val thoroughfare: String
): Serializable