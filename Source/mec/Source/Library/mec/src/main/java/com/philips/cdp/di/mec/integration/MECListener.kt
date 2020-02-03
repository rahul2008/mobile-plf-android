/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration


import com.philips.cdp.di.mec.common.MecError

import java.util.ArrayList


interface MECListener {

    fun onGetCartCount(count: Int)

    fun onGetCompleteProductList(productList: ArrayList<String>)


    fun onFailure(exception: Exception)

}
