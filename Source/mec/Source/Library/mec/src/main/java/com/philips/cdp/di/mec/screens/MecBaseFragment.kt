/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.screens

import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.integration.MECListener
import com.philips.platform.uappframework.listener.ActionBarListener
import com.philips.platform.uappframework.listener.BackEventListener
import com.philips.platform.uid.view.widget.ProgressBar

abstract class MecBaseFragment : Fragment(), BackEventListener {
    private var mContext: Context? = null
     var mActionbarUpdateListener: ActionBarListener? = null
    protected var mECListener: MECListener? = null

    internal var mTitle = ""

    protected val SMALL = 0
    protected val MEDIUM = 1
    protected val BIG = 2
    private var mMECBaseFragmentProgressBar: ProgressBar? = null

    enum class AnimationType {
        NONE
    }

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

    fun showFragment(fragmentTag: String) {
        if (activity != null && !activity!!.isFinishing) {
            activity!!.supportFragmentManager.popBackStackImmediate(fragmentTag, 0)

        }
    }

    fun updateCount(count: Int) {
        if (mECListener != null) {
            mECListener!!.onGetCartCount(count)
           // hideProgressBar()
        }
    }

    fun setCartIconVisibility(shouldShow: Boolean) {
        if (mECListener != null) {

            mECListener!!.updateCartIconVisibility(shouldShow)

        }
    }


    fun setActionBarListener(actionBarListener: ActionBarListener, mecListener: MECListener) {
        mActionbarUpdateListener = actionBarListener
        mECListener = mecListener
    }

    protected fun setTitleAndBackButtonVisibility(resourceId: Int, isVisible: Boolean) {
        mTitle = getString(resourceId)
        if (mActionbarUpdateListener != null)
            mActionbarUpdateListener!!.updateActionBar(resourceId, isVisible)

    }


    protected fun setTitleAndBackButtonVisibility(title: String, isVisible: Boolean) {
        mTitle = title
        if (mActionbarUpdateListener != null)
            mActionbarUpdateListener!!.updateActionBar(title, isVisible)
    }

    fun createCustomProgressBar(group: ViewGroup?, size: Int) {
        var group = group
        if (context == null) return
        val parentView = view as ViewGroup?
        val layoutViewGroup = group
        if (parentView != null) {
            group = parentView
        }

        when (size) {
            BIG -> context!!.theme.applyStyle(R.style.MECCircularPBBig, true)
            SMALL -> context!!.theme.applyStyle(R.style.MECCircularPBSmall, true)
            MEDIUM -> context!!.theme.applyStyle(R.style.MECCircularPBMedium, true)
            else -> context!!.theme.applyStyle(R.style.MECCircularPBMedium, true)
        }

        mMECBaseFragmentProgressBar = ProgressBar(context!!, null, R.attr.pth_cirucular_pb)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        mMECBaseFragmentProgressBar!!.setLayoutParams(params)

        try {
            group!!.addView(mMECBaseFragmentProgressBar)
        } catch (e: Exception) {
            layoutViewGroup?.addView(mMECBaseFragmentProgressBar)
        }

        if (mMECBaseFragmentProgressBar != null) {
            mMECBaseFragmentProgressBar!!.setVisibility(View.VISIBLE)
            if (activity != null) {
                activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }

    fun hideProgressBar() {
        if (mMECBaseFragmentProgressBar != null) {
            mMECBaseFragmentProgressBar!!.setVisibility(View.GONE)
            if (activity != null) {
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        }
    }

}
