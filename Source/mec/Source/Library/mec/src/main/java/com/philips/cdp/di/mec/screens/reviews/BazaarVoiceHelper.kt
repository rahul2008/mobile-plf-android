package com.philips.cdp.di.mec.screens.reviews

import android.app.Application
import com.bazaarvoice.bvandroidsdk.BVConversationsClient
import com.bazaarvoice.bvandroidsdk.BVLogLevel
import com.bazaarvoice.bvandroidsdk.BVSDK
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class BazaarVoiceHelper {

    private val loggingInterceptor: HttpLoggingInterceptor
        get() {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            return loggingInterceptor
        }

    fun getBazarvoiceClient(context:Application) : BVConversationsClient {

        /*val bvsdk = BVSDK.builderWithConfig(context, Constants.BAZAAR_ENVIRONMENT,bvConfig)
       .logLevel(BVLogLevel.ERROR)
       .okHttpClient(getOkHttpClient(loggingInterceptor))
       .build()*/

         val bvsdk = BVSDK.builder(context, Constants.BAZAAR_ENVIRONMENT)
                .logLevel(BVLogLevel.VERBOSE)
                .okHttpClient(getOkHttpClient(loggingInterceptor))
                 .dryRunAnalytics(false)
                .build()

         var bvClient = BVConversationsClient.Builder(bvsdk).build()
         return bvClient
    }

    private fun getOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
    }
}