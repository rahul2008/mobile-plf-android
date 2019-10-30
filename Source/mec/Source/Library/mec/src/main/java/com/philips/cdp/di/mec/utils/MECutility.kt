package com.philips.cdp.di.mec.utils

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentManager
import android.view.View
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
            if (alertDialogFragment != null && alertDialogFragment.isVisible && isCallingFragmentVisible(fragmentManager))
                alertDialogFragment.dismiss()
        }

        internal fun isCallingFragmentVisible(fragmentManager: FragmentManager?): Boolean {

            if (fragmentManager != null) {
                val fragments = fragmentManager.fragments
                if (fragments != null && fragments.size > 0) {
                    val fragment = fragments[fragments.size - 1]
                    return fragment.activity != null && fragment.isAdded //&& fragment.isVisible && fragment.isResumed
                }
            }
            return false
        }
    }
}