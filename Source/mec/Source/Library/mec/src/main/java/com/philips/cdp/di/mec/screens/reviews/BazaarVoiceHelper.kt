package com.philips.cdp.di.mec.screens.reviews

import android.app.Application
import com.bazaarvoice.bvandroidsdk.*
import com.philips.cdp.di.mec.integration.MECBazaarVoiceInput
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

        var bvConfigBuilder = BVConfig.Builder()
        bvConfigBuilder.clientId("philipsglobal")
        bvConfigBuilder.apiKeyConversations("caAyWvBUz6K3xq4SXedraFDzuFoVK71xMplaDk1oO5P4E")

        val bvsdk = BVSDK.builderWithConfig(context, BazaarEnvironment.PRODUCTION,bvConfigBuilder.build())
       .logLevel(BVLogLevel.ERROR)
       .okHttpClient(getOkHttpClient(loggingInterceptor)).dryRunAnalytics(false)
       .build()

         /*val bvsdk = BVSDK.builder(context, Constants.BAZAAR_ENVIRONMENT)
                .logLevel(BVLogLevel.VERBOSE)
                .okHttpClient(getOkHttpClient(loggingInterceptor))
                 .dryRunAnalytics(false)
                .build()*/

         var bvClient = BVConversationsClient.Builder(bvsdk).build()
         return bvClient
    }

    private fun getOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
    }
}