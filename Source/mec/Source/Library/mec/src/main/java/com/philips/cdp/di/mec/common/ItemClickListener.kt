package com.philips.cdp.di.mec.common

import java.io.Serializable

interface ItemClickListener : Serializable{

      fun onItemClick(item:Object)
}