package com.huawei.demomapapp.communication.response

import java.io.Serializable

data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
): Serializable