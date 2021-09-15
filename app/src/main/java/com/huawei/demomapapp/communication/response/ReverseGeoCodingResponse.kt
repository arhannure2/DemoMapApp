package com.huawei.demomapapp.communication.response

import java.io.Serializable

data class ReverseGeoCodingResponse(
    val returnCode: String,
    val returnDesc: String,
    val sites: List<Site>
): Serializable