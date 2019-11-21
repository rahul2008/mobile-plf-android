package com.philips.cdp.di.mec.integration

import com.philips.cdp.di.mec.screens.reviews.BazaarVoiceEnvironment

open class MECBazaarVoiceInput {

    open fun getBazaarVoiceClientID (): String{

        return "philipsglobal";
    }

    open fun getBazaarVoiceConversationAPIKey (): String{

        return "caAyWvBUz6K3xq4SXedraFDzuFoVK71xMplaDk1oO5P4E"
    }

    open fun getBazaarVoiceEnvironment() : BazaarVoiceEnvironment{
        return BazaarVoiceEnvironment.PRODUCTION
    }
}