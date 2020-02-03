package com.philips.cdp.di.mec.screens.address

import java.util.regex.Pattern

open class MECRegexPattern {

    private val PHONE_NUMBER = (
            "(\\+[0-9]+[\\- \\.]*)?"
                    + "(\\([0-9]+\\)[\\- \\.]*)?"
                    + "([0-9][0-9\\- \\.]+[0-9])")

    val EMPTY_FIELD = "^\\s+$"

    val PHONE_NUMBER_PATTERN = Pattern.compile(PHONE_NUMBER)
    val EMPTY_FIELD_PATTERN = Pattern.compile(EMPTY_FIELD)


    // drop Down Data for salutation todo

    val DROP_DOWN_DATA = arrayOf("Mr.", "Ms.")
}