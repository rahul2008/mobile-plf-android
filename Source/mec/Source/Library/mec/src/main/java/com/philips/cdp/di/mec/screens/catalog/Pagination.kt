package com.philips.cdp.di.mec.screens.catalog

interface Pagination {

    fun  isPaginationSupported():Boolean

    fun  isCategorizedHybrisPagination(): Boolean
}