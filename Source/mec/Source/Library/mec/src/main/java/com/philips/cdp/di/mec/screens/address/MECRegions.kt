package com.philips.cdp.di.mec.screens.address

import com.philips.cdp.di.ecs.model.address.Region
import com.philips.cdp.di.ecs.model.region.ECSRegion

class MECRegions(private val regionList: List<ECSRegion>){

    fun getRegionNames(): Array<String> ?{

        val rowItems : Array<String> = Array(regionList.size) { "it = $it" }

        if (regionList != null) {
            for (i in regionList.indices) {
                rowItems[i] = regionList[i].name
            }
        }
        return rowItems
    }

    fun getRegion(regionName:String):Region{

        val region = Region()
        for (ecsRegion in regionList){

            if(ecsRegion.name.equals(regionName,true)){

                region.isocodeShort = ecsRegion.getIsocode()
            }
        }
        return region
    }
}