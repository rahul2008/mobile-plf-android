/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.ecs.model.retailers

import java.io.Serializable

class StoreEntity : Serializable {
    val name: String? = null
    val availability: String? = null
    val isPhilipsStore: String? = null
    val philipsOnlinePrice: String? = null
    val logoHeight: Int = 0
    val logoWidth: Int = 0
    val buyURL: String? = null
    val logoURL: String? = null

    companion object {

        private const val serialVersionUID = -4549397314749988036L
    }
}
