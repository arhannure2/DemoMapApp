package com.huawei.demomapapp.communication.response

import java.io.Serializable

data class Site(
    val address: Address,
    val aoiFlag: Boolean,
    val formatAddress: String,
    val location: Location,
    val name: String,
    val poi: Poi,
    val siteId: String,
    val viewport: Viewport
): Serializable