package com.philips.cdp.di.mec.screens.address

import com.philips.platform.uid.view.widget.InputValidationLayout

class EmptyInputValidator : InputValidationLayout.Validator  {

    override fun validate(msg: CharSequence?): Boolean {
       return !msg?.trim().isNullOrEmpty()
    }
}