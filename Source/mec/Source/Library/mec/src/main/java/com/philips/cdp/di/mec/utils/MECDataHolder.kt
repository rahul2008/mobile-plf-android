/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.cdp.di.mec.utils

import com.bazaarvoice.bvandroidsdk.BVConversationsClient
import com.philips.cdp.di.ecs.ECSServices
import com.philips.cdp.di.ecs.model.config.ECSConfig
import com.philips.cdp.di.mec.integration.*
import com.philips.cdp.di.mec.paymentServices.MECPayment
import com.philips.cdp.di.mec.paymentServices.MECPayments
import com.philips.cdp.di.mec.screens.address.UserInfo
import com.philips.platform.appinfra.AppInfraInterface
import com.philips.platform.pif.DataInterface.USR.UserDataInterface
import com.philips.platform.pif.DataInterface.USR.UserDataInterfaceException
import com.philips.platform.pif.DataInterface.USR.UserDetailConstants
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState
import com.philips.platform.uappframework.listener.ActionBarListener
import java.util.*

enum class MECDataHolder {

    INSTANCE;

    lateinit var appinfra: AppInfraInterface
    lateinit var actionbarUpdateListener: ActionBarListener
    var mecCartUpdateListener: MECCartUpdateListener? = null
    var mecBannerEnabler: MECBannerConfigurator? = null
    var mecOrderFlowCompletion: MECOrderFlowCompletion? = null
    lateinit var locale: String
    lateinit var propositionId: String
    lateinit var voucherCode: String
    var maxCartCount: Int = 0
    lateinit var userDataInterface: UserDataInterface
    var refreshToken: String? = null
    var blackListedRetailers: List<String>? = null
    lateinit var mecBazaarVoiceInput: MECBazaarVoiceInput
    private var privacyUrl: String? = null
    private var faqUrl: String? = null
    private var termsUrl: String? = null
    var hybrisEnabled: Boolean = true
    var retailerEnabled: Boolean = true
    var voucherEnabled: Boolean = true
    var rootCategory: String = ""
    var config: ECSConfig? = null
    lateinit var eCSServices: ECSServices

    var mutableListOfPayments = mutableListOf<MECPayment>()
    var PAYMENT_HOLDER: MECPayments = MECPayments(mutableListOfPayments, false) //Default empty MECPayments

    fun getPrivacyUrl(): String? {
        return privacyUrl
    }

    fun getFaqUrl(): String? {
        return faqUrl
    }

    fun getTermsUrl(): String? {
        return termsUrl
    }
    fun setPrivacyUrl(privacyUrl: String) {
        this.privacyUrl = privacyUrl
    }

    fun getUserInfo(): UserInfo {

        var firstName = ""
        var lastName = ""

        if (userDataInterface != null && userDataInterface.userLoggedInState == UserLoggedInState.USER_LOGGED_IN) {

            val userDataMap = ArrayList<String>()

            userDataMap.add(UserDetailConstants.GIVEN_NAME)
            userDataMap.add(UserDetailConstants.FAMILY_NAME)
            userDataMap.add(UserDetailConstants.EMAIL)
            try {
                val hashMap = userDataInterface.getUserDetails(userDataMap)
                firstName = hashMap.get(UserDetailConstants.GIVEN_NAME) as String
                lastName = hashMap.get(UserDetailConstants.FAMILY_NAME) as String
            } catch (e: UserDataInterfaceException) {
                e.printStackTrace()
            }

        }

        return UserInfo(firstName, lastName)
    }


    var bvClient: BVConversationsClient? = null

    fun setUpdateCartListener(mActionbarUpdateListener: ActionBarListener, mecCartUpdateListener: MECCartUpdateListener) {
        actionbarUpdateListener = mActionbarUpdateListener
        this.mecCartUpdateListener = mecCartUpdateListener
    }

    fun setPrivacyPolicyUrls(privacyUrl: String, faqUrl: String, termsUrl: String) {
        this.privacyUrl = privacyUrl
        this.faqUrl = faqUrl
        this.termsUrl = termsUrl
    }

}