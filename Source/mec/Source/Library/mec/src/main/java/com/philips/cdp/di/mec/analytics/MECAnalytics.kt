package com.philips.cdp.di.mec.analytics

import android.app.Activity
import android.util.Log
import com.philips.cdp.di.ecs.model.products.ECSProduct
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant.country
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant.currency
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant.productListKey
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant.sendData
import com.philips.platform.appinfra.tagging.AppTaggingInterface
import com.philips.cdp.di.mec.integration.MECDependencies
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.platform.appinfra.BuildConfig
import java.util.*
import kotlin.collections.HashMap


class MECAnalytics {

    companion object {

        var mAppTaggingInterface: AppTaggingInterface? = null
        var previousPageName = "uniquePageName";
        var countryCode = ""
        var currencyCode= ""

        @JvmStatic
        fun initMECAnalytics(dependencies: MECDependencies) {
            mAppTaggingInterface = dependencies.appInfra.tagging.createInstanceForComponent(MECAnalyticsConstant.COMPONENT_NAME, BuildConfig.VERSION_NAME)
            countryCode = dependencies.appInfra.serviceDiscovery.homeCountry
           // currencyCode = Currency.getInstance(countryCode).currencyCode
        }

        @JvmStatic
        fun trackPage(currentPage: String) {
            if (mAppTaggingInterface != null && currentPage != null) {
                var map = HashMap<String, String>()
                if (currentPage != previousPageName) {
                    previousPageName = currentPage
                    Log.v("MEC_LOG", "trackPage" + currentPage);
                    mAppTaggingInterface!!.trackPageWithInfo(currentPage, addCountryAndCurrency(map))
                }
            }
        }


        @JvmStatic
        fun trackAction(state: String, key: String, value: Any) {
            val valueObject = value as String
            Log.v("MEC_LOG", "trackAction" + valueObject);
            if (mAppTaggingInterface != null)
                mAppTaggingInterface!!.trackActionWithInfo(state, key, valueObject)
        }

        @JvmStatic
        fun trackMultipleActions(state: String, map: Map<String, String>) {
            if (mAppTaggingInterface != null)
                Log.v("MEC_LOG", "trackMtlutipleAction " )
                mAppTaggingInterface!!.trackActionWithInfo(state, addCountryAndCurrency(map))
        }


        @JvmStatic
        fun pauseCollectingLifecycleData() {
            if (mAppTaggingInterface != null)
                mAppTaggingInterface!!.pauseLifecycleInfo()
        }


        @JvmStatic
        fun collectLifecycleData(activity: Activity) {
            if (mAppTaggingInterface != null)
                mAppTaggingInterface!!.collectLifecycleInfo(activity)
        }

        private fun addCountryAndCurrency(map: Map<String,String>) :Map<String,String>{
            //var newMap = map.toMap<String,>()
            var newMap = HashMap(map)
            newMap.put(country,countryCode)
            newMap.put(currency,currencyCode)
            return newMap;

        }

        @JvmStatic
         fun tagActions(ctn : String) {
            var map = HashMap<String, String>()
            map.put(MECAnalyticsConstant.specialEvents, MECAnalyticsConstant.prodView)
            map.put(MECAnalyticsConstant.mecProduct,ctn)
            MECAnalytics.trackMultipleActions(MECAnalyticsConstant.sendData,map)
        }

        @JvmStatic
        fun tagProductList(productList :MutableList<ECSProduct>){
            val mutableProductIterator = productList.iterator()
            var productList :String=""
            for(product in mutableProductIterator){
                productList+=","+getProductInfo(product)
            }
            productList=productList.substring(1,productList.length-1)
            Log.v("MEC_LOG", "prodList : "+productList )
            trackAction(sendData,productListKey,productList)
        }

        @JvmStatic
        fun getProductInfo(product :ECSProduct):String{
            var protuctDetail: String= MECDataHolder.INSTANCE.rootCategory
            protuctDetail+= ";"+product.code
            protuctDetail+= ";"+ (if (product.stock !=null && product.stock.stockLevel!=null ) product.stock.stockLevel else 0)
            protuctDetail+= ";"+ (if (product.discountPrice!=null) product.discountPrice.value else 0  )
            return protuctDetail
        }

        fun setCurrencyString(localeString: String) {
            try {
                val localeArray = localeString.split("_".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val locale = Locale(localeArray[0], localeArray[1])
                val currency = Currency.getInstance(locale)
                currencyCode = currency.currencyCode
            } catch (e: Exception) {

            }

        }

    }



}