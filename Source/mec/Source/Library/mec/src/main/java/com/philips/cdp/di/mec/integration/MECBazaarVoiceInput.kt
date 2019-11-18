package com.philips.cdp.di.mec.integration

import com.philips.cdp.di.mec.screens.reviews.BazaarVoiceEnvironment

open class MECBazaarVoiceInput {

    open fun getBazaarVoiceClientID (): String{

        return "philipsglobal";
    }

    open fun getBazaarVoiceConversationAPIKey (): String{

        return "ca23LB5V0eOKLe0cX6kPTz6LpAEJ7SGnZHe21XiWJcshc"
    }

    open fun getBazaarVoiceEnvironment() : BazaarVoiceEnvironment{
        return BazaarVoiceEnvironment.STAGING
    }
}