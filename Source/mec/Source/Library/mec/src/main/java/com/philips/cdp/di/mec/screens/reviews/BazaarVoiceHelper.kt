/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.cdp.di.mec.screens.reviews

import android.app.Application
import com.bazaarvoice.bvandroidsdk.*
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
        val bvsdk = BVSDK.builderWithConfig(context, if (bazaarEnvironment.equals(MECBazaarVoiceEnvironment.PRODUCTION)) BazaarEnvironment.PRODUCTION else BazaarEnvironment.STAGING,bvConfigBuilder.build())
       .logLevel(BVLogLevel.ERROR)
       .okHttpClient(getOkHttpClient(loggingInterceptor)).dryRunAnalytics(false)
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