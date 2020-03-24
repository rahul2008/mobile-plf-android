/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration


interface MECCartUpdateListener :CartListener {

     fun onUpdateCartCount( count: Int)
     fun shouldShowCart(shouldShow: Boolean)


}
