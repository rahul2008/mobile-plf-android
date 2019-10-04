/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.screens

import android.support.v4.app.Fragment

import com.philips.cdp.di.mec.utils.MECUtility
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState
import com.philips.platform.uappframework.listener.BackEventListener

abstract class InAppBaseFragment : Fragment(), BackEventListener {


    protected val isUserLoggedIn: Boolean
        get() = MECUtility.getInstance().userDataInterface != null && MECUtility.getInstance().userDataInterface.userLoggedInState == UserLoggedInState.USER_LOGGED_IN


    override fun handleBackEvent(): Boolean {
        return false
    }
}
