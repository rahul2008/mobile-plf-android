/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.screens

import android.support.v4.app.Fragment
import com.philips.platform.uappframework.listener.BackEventListener

abstract class InAppBaseFragment : Fragment(), BackEventListener {

    override fun handleBackEvent(): Boolean {
        return false
    }
}
