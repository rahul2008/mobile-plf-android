package com.philips.cdp.di.mec.screens.reviews

import android.app.Application
import android.content.Context
import com.bazaarvoice.bvandroidsdk.BVConversationsClient
import com.bazaarvoice.bvandroidsdk.BVLogLevel
import com.bazaarvoice.bvandroidsdk.BVSDK
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class App : Application() {
    var bvClient: BVConversationsClient? = null
        private set

    override fun onCreate() {
        super.onCreate()

        val bvsdk = BVSDK.builder(this, Constants.BAZAAR_ENVIRONMENT)
                .logLevel(BVLogLevel.VERBOSE)
                .okHttpClient(getOkHttpClient(loggingInterceptor))
                .build()

        bvClient = BVConversationsClient.Builder(bvsdk).build()
    }

    companion object {

        operator fun get(context: Context): App {
            return context.applicationContext as App
        }

        private fun getOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
            return OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()
        }

        private val loggingInterceptor: HttpLoggingInterceptor
            get() {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                return loggingInterceptor
            }
    }
}