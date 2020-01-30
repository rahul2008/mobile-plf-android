package com.philips.cdp.di.mec.utils

import android.app.Activity
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.philips.cdp.di.mec.utils.MECConstant.IN_STOCK
import com.philips.cdp.di.mec.utils.MECConstant.LOW_STOCK
import com.philips.platform.uid.thememanager.UIDHelper
import com.philips.platform.uid.view.widget.AlertDialogFragment

class MECutility {


    companion object {

        private var alertDialogFragment: AlertDialogFragment? = null
        val ALERT_DIALOG_TAG = "ALERT_DIALOG_TAG"


        @JvmStatic
        fun showErrorDialog(context: Context, pFragmentManager: FragmentManager,
                            pButtonText: String, pErrorString: String, pErrorDescription: String) {

            if (!(context as Activity).isFinishing) {
                showDLSDialog(UIDHelper.getPopupThemedContext(context), pButtonText, pErrorString, pErrorDescription, pFragmentManager)
            }
        }


        internal fun showDLSDialog(context: Context, pButtonText: String, pErrorString: String, pErrorDescription: String, pFragmentManager: FragmentManager) {
            val builder = AlertDialogFragment.Builder(context)
                    .setMessage(pErrorDescription).setPositiveButton(pButtonText) { dismissAlertFragmentDialog(alertDialogFragment, pFragmentManager) }

            builder.setTitle(pErrorString)
            if (alertDialogFragment != null) {
                dismissAlertFragmentDialog(alertDialogFragment, pFragmentManager)
            }
            alertDialogFragment = builder.create()
            if (alertDialogFragment == null) {
                alertDialogFragment = builder.setCancelable(false).create()
            }

            if (!alertDialogFragment!!.isVisible && isCallingFragmentVisible(pFragmentManager)) {
                alertDialogFragment!!.show(pFragmentManager, ALERT_DIALOG_TAG)
            }

        }

        internal fun dismissAlertFragmentDialog(alertDialogFragment: AlertDialogFragment?, fragmentManager: FragmentManager) {
            var alertDialogFragment = alertDialogFragment

            if (alertDialogFragment == null) {
                alertDialogFragment = fragmentManager.findFragmentByTag(ALERT_DIALOG_TAG) as AlertDialogFragment?
            }
            if (alertDialogFragment != null  && isCallingFragmentVisible(fragmentManager))
                alertDialogFragment.dismiss()
        }

        fun isCallingFragmentVisible(fragmentManager: FragmentManager?): Boolean {

            if (fragmentManager != null) {
                val fragments = fragmentManager.fragments
                if (fragments != null && fragments.size > 0) {
                    val fragment = fragments[fragments.size - 1]
                    return fragment.activity != null && fragment.isAdded //&& fragment.isVisible && fragment.isResumed
                }
            }
            return false
        }

        fun indexOfSubString(ignoreCase: Boolean, str: CharSequence?, subString: CharSequence?): Int {
            if (str == null || subString == null) {
                return -1
            }
            val subStringLen = subString.length
            val max = str.length - subStringLen
            for (i in 0..max) {
                if (regionMatches(ignoreCase, str, i, subString, 0, subStringLen)) {
                    return i
                }
            }
            return -1
        }

        fun regionMatches(ignoreCase: Boolean, str: CharSequence?, strOffset: Int,
                          subStr: CharSequence?, subStrOffset: Int, length: Int): Boolean {
            if (str == null || subStr == null) {
                return false
            }

            if (str is String && subStr is String) {
                return str.regionMatches(strOffset, (subStr as String?)!!, subStrOffset, length, ignoreCase = ignoreCase)
            }

            //SubString length is more than string
            if (subStr.length > str.length) {
                return false
            }

            //Invalid start point
            if (strOffset < 0 || subStrOffset < 0 || length < 0) {
                return false
            }

            //Length can't be greater than diff of string length and offset
            if (str.length - strOffset < length || subStr.length - subStrOffset < length) {
                return false
            }

            //Start comparing
            var strIndex = strOffset
            var subStrIndex = subStrOffset
            var tmpLenth = length

            while (tmpLenth-- > 0) {
                val c1 = str[strIndex++]
                val c2 = subStr[subStrIndex++]

                if (c1 == c2) {
                    continue
                }

                //Same comparison as java framework
                if (ignoreCase && (Character.toUpperCase(c1) == Character.toUpperCase(c2) || Character.toLowerCase(c1) == Character.toLowerCase(c2))) {
                    continue
                }
                return false
            }
            return true
        }

        internal fun isStockAvailable(stockLevelStatus: String?, stockLevel: Int): Boolean {

            /* if (if hybris available ) { // todo
                 return true
             }*/

            if (stockLevelStatus == null) {
                return false
            }

            return if (stockLevelStatus.equals(IN_STOCK, ignoreCase = true) ||
                    stockLevelStatus.equals(LOW_STOCK, ignoreCase = true) || stockLevel > 0) {
                true
            } else false

        }

        fun stockStatus(availability : String): String {
            if(availability.equals("YES")){
                return "available"
            } else if(availability.equals("NO")){
                return "out of stock"
            }
            return ""
        }
    }
}