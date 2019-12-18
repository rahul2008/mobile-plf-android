package com.philips.cdp.di.mec.screens.catalog


import android.os.Bundle
import com.philips.cdp.di.ecs.constants.NetworkConstants

import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.MECConstant

class MecPrivacyFragment : WebFragment() {
        private var mUrl: String? = null

         override fun isJavaScriptEnable(): Boolean {
            return true
        }

         override fun getWebUrl(): String? {
             mUrl = arguments!!.getString(MECConstant.MEC_PRIVACY_URL)
             return mUrl
        }

        override fun onResume() {
            super.onResume()
                setTitleAndBackButtonVisibility("Privacy", true)
        }
    }
