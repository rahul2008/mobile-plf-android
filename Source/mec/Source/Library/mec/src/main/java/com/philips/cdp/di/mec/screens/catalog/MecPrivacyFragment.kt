package com.philips.cdp.di.mec.screens.catalog

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philips.cdp.di.ecs.constants.NetworkConstants

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.screens.MecBaseFragment

class MecPrivacyFragment : WebFragment() {
        private var mUrl: String? = null
        private var termsUrl:String? = null
        private var faqUrl:String? = null

         override fun isJavaScriptEnable(): Boolean {
            return true
        }

         override fun getWebUrl(): String? {
            return "Privacy"
        }

        fun createInstance(args: Bundle, animType: MecBaseFragment.AnimationType): MecPrivacyFragment {
            val fragment = MecPrivacyFragment()
            args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal)
            fragment.setArguments(args)
            return fragment
        }

        override fun onResume() {
            super.onResume()
                setTitleAndBackButtonVisibility("Privacy", true)
        }
    }
