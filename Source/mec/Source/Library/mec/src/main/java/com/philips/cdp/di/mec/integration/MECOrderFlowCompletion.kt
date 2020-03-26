package com.philips.cdp.di.mec.integration

/**
 * Created by philips on 4/26/19.
 */
interface MECOrderFlowCompletion {
    fun onOrderPlace() {}
    fun onOrderCancel() {}
    fun shouldMoveToProductList(): Boolean
}