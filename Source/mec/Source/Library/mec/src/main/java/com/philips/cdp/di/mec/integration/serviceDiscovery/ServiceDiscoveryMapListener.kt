package com.philips.cdp.di.mec.integration.serviceDiscovery

import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService
import java.util.ArrayList

class ServiceDiscoveryMapListener : ServiceDiscoveryInterface.OnGetServiceUrlMapListener {


    private val IAP_PRIVACY_URL = "iap.privacyPolicy"


    override fun onError(error: ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES?, message: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSuccess(urlMap: MutableMap<String, ServiceDiscoveryService>?) {
        val collection = urlMap?.values


        val list = ArrayList<ServiceDiscoveryService>()
        if (collection != null) {
            list.addAll(collection)
        }

        val discoveryService = urlMap?.get(IAP_PRIVACY_URL)!!
        val privacyUrl = discoveryService.configUrls
        if (privacyUrl != null) {
            MECDataHolder.INSTANCE.setPrivacyUrl(privacyUrl)
        }
    }
}