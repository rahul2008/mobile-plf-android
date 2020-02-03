package com.philips.cdp.di.mec.screens.address

import com.philips.platform.uid.view.widget.InputValidationLayout
import java.util.regex.Pattern

class PhoneNumberInputValidator(private val valid_regex_pattern: Pattern) : InputValidationLayout.Validator {

    override fun validate(msg: CharSequence?): Boolean {
        if (msg?.trim().isNullOrEmpty()) {
            val matcher = valid_regex_pattern.matcher(msg)
            return matcher.find()
        }
        return false
    }
}