/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration


interface MECFetchCartListener : CartListener {

    fun onGetCartCount(count: Int)
}
