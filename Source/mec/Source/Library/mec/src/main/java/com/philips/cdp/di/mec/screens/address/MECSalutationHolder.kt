package com.philips.cdp.di.mec.screens.address

import com.philips.cdp.di.ecs.util.ECSConfiguration
import java.util.regex.Pattern

open class MECSalutationHolder {

    val DROP_DOWN_DATA = arrayOf("Mr.", "Ms.")

    val locale = ECSConfiguration.INSTANCE.country
}