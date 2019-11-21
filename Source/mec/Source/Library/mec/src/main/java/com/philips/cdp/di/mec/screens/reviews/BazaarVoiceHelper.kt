package com.philips.cdp.di.mec.screens.reviews

import android.app.Application
import com.bazaarvoice.bvandroidsdk.*
import com.philips.cdp.di.mec.integration.MECBazaarVoiceInput
import com.philips.cdp.di.mec.integration.MECLaunchInput
import com.philips.cdp.di.mec.utils.MECDataHolder
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

        var bazaarClientId = MECDataHolder.INSTANCE.mecBazaarVoiceInput.getBazaarVoiceClientID()
        var bazaarApiConversationKey = MECDataHolder.INSTANCE.mecBazaarVoiceInput.getBazaarVoiceConversationAPIKey()
        var bazaarEnvironment = MECDataHolder.INSTANCE.mecBazaarVoiceInput.getBazaarVoiceEnvironment()

        var bvConfigBuilder = BVConfig.Builder()
        bvConfigBuilder.clientId(bazaarClientId)
        bvConfigBuilder.apiKeyConversations(bazaarApiConversationKey)
        val bvsdk = BVSDK.builderWithConfig(context, if (bazaarEnvironment.equals(BazaarVoiceEnvironment.PRODUCTION)) BazaarEnvironment.PRODUCTION else BazaarEnvironment.STAGING,bvConfigBuilder.build())
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