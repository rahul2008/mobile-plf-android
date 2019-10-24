/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.screens

import android.support.v4.app.Fragment
import com.philips.cdp.di.mec.integration.MECListener
import com.philips.platform.uappframework.listener.ActionBarListener
import com.philips.platform.uappframework.listener.BackEventListener

abstract class MecBaseFragment : Fragment(), BackEventListener {

     var mActionbarUpdateListener: ActionBarListener? = null
    protected var mECListener: MECListener? = null

    override fun handleBackEvent(): Boolean {
        return false
    }


    fun addFragment(newFragment: MecBaseFragment,
                    newFragmentTag: String, isReplaceWithBackStack: Boolean) {
        if (mActionbarUpdateListener == null || mECListener == null)
            RuntimeException("ActionBarListner and IAPListner cant be null")
        else {

            newFragment.setActionBarListener(mActionbarUpdateListener!!, mECListener!!)
            if (activity != null && !activity!!.isFinishing) {
                val transaction = activity!!.supportFragmentManager.beginTransaction()
                val simpleName = newFragment.javaClass.simpleName
                if (isReplaceWithBackStack) {
                    transaction.replace(id, newFragment, simpleName)
                    transaction.addToBackStack(newFragmentTag)
                } else {
                    transaction.replace(id, newFragment, simpleName)
                    //transaction.addToBackStack(null);
                }
                transaction.commitAllowingStateLoss()
            }
        }
    }


    fun setActionBarListener(actionBarListener: ActionBarListener, mecListener: MECListener) {
        mActionbarUpdateListener = actionBarListener
        mECListener = mecListener
    }

}
