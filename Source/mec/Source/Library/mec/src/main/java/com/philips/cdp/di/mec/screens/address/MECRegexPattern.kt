package com.philips.cdp.di.mec.screens.address

import com.philips.cdp.di.ecs.util.ECSConfiguration
import java.util.regex.Pattern

open class MECRegexPattern {

    private val PHONE_NUMBER = (
            "(\\+[0-9]+[\\- \\.]*)?"
                    + "(\\([0-9]+\\)[\\- \\.]*)?"
                    + "([0-9][0-9\\- \\.]+[0-9])")


    val PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER)


    // drop Down Data for salutation todo

    val DROP_DOWN_DATA = arrayOf("Mr.", "Ms.")

    val locale = ECSConfiguration.INSTANCE.country
}