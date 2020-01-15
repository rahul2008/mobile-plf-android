/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.screens

import android.app.ProgressDialog
import android.arch.lifecycle.Observer
import android.content.Context
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.MECLauncherActivity
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.screens.catalog.MecPrivacyFragment
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.uappframework.listener.BackEventListener
import com.philips.platform.uid.thememanager.UIDHelper
import com.philips.platform.uid.view.widget.ProgressBar


abstract class MecBaseFragment : Fragment(), BackEventListener, Observer<MecError> {
    private var mContext: Context? = null


    internal var mTitle = ""

    protected val SMALL = 0
    protected val MEDIUM = 1
    protected val BIG = 2
    private var mProgressDialog: ProgressDialog? = null
    private var mMECBaseFragmentProgressBar: ProgressBar? = null

    enum class AnimationType {
        NONE
    }

    override fun handleBackEvent(): Boolean {
        val currentFragment = activity?.supportFragmentManager?.fragments?.last()
        if (currentFragment?.getTag().equals("MECProductCatalogFragment")){

        }else  if (currentFragment?.getTag().equals("WebBuyFromRetailersFragment"))
        {
            setTitleAndBackButtonVisibility(R.string.mec_product_detail_title, true)
        }
        else if (currentFragment?.getTag().equals("MECProductDetailsFragment")){
            setTitleAndBackButtonVisibility(R.string.mec_product_title, true)
        }
        return false
    }

    fun replaceFragment(newFragment: MecBaseFragment,
                        newFragmentTag: String, isReplaceWithBackStack: Boolean) {
        if (MECDataHolder.INSTANCE.actionbarUpdateListener == null || MECDataHolder.INSTANCE.mecListener == null)
            RuntimeException("ActionBarListner and IAPListner cant be null")
        else {
            if (!activity!!.isFinishing) {

                val transaction = activity!!.supportFragmentManager.beginTransaction()
                val simpleName = newFragment.javaClass.simpleName

                if (isReplaceWithBackStack) {
                    transaction.addToBackStack(null)
                }

                transaction.replace(id, newFragment, simpleName)
                transaction.commitAllowingStateLoss()
            }
        }
    }


    fun addFragment(newFragment: MecBaseFragment,
                        newFragmentTag: String, isAddWithBackStack: Boolean) {
        if (MECDataHolder.INSTANCE.actionbarUpdateListener == null || MECDataHolder.INSTANCE.mecListener == null)
            RuntimeException("ActionBarListner and IAPListner cant be null")
        else {
            if (!activity!!.isFinishing) {

                val transaction = activity!!.supportFragmentManager.beginTransaction()
                val simpleName = newFragment.javaClass.simpleName

                if (isAddWithBackStack) {
                    transaction.addToBackStack(null)
                }

                transaction.add(id, newFragment, simpleName)
                transaction.commitAllowingStateLoss()
            }
        }
    }


    protected fun setTitleAndBackButtonVisibility(resourceId: Int, isVisible: Boolean) {
        if (MECDataHolder.INSTANCE.actionbarUpdateListener != null)
            MECDataHolder.INSTANCE.actionbarUpdateListener!!.updateActionBar(resourceId, isVisible)

    }


    protected fun setTitleAndBackButtonVisibility(title: String, isVisible: Boolean) {
        if (MECDataHolder.INSTANCE.actionbarUpdateListener != null)
            MECDataHolder.INSTANCE.actionbarUpdateListener!!.updateActionBar(title, isVisible)
    }


    fun createCustomProgressBar(group: ViewGroup?, size: Int) {
        createCustomProgressBar(group,size,RelativeLayout.CENTER_IN_PARENT)

    }
    fun createCustomProgressBar(group: ViewGroup?, size: Int, gravity: Int) {
        var group = group
        if (context == null) return
        val parentView = view as ViewGroup?
        val layoutViewGroup = group
        if (parentView != null) {
            group = parentView
        }

        when (size) {
            BIG -> context!!.theme.applyStyle(com.philips.cdp.di.mec.R.style.MECCircularPBBig, true)
            SMALL -> context!!.theme.applyStyle(com.philips.cdp.di.mec.R.style.MECCircularPBSmall, true)
            MEDIUM -> context!!.theme.applyStyle(com.philips.cdp.di.mec.R.style.MECCircularPBMedium, true)
            else -> context!!.theme.applyStyle(com.philips.cdp.di.mec.R.style.MECCircularPBMedium, true)
        }

        mMECBaseFragmentProgressBar = ProgressBar(context!!, null, com.philips.cdp.di.mec.R.attr.pth_cirucular_pb)
        val params = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_HORIZONTAL)
        params.addRule(gravity)

        if(gravity == RelativeLayout.ALIGN_PARENT_BOTTOM){
            params.setMargins(0, 0, 0, 130);
        }

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

    fun showProgressDialog() {
        mProgressDialog = ProgressDialog(UIDHelper.getPopupThemedContext(activity!!))
        mProgressDialog!!.getWindow()!!.setGravity(Gravity.CENTER)
        mProgressDialog!!.setCancelable(false)
        mProgressDialog!!.setMessage("Please wait" + "...")

        if (!mProgressDialog!!.isShowing() && !activity!!.isFinishing()) {
            mProgressDialog!!.show()
            mProgressDialog!!.setContentView(R.layout.progressbar_dls)
        }
    }

    fun dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing()) {
            mProgressDialog!!.dismiss()
        }
    }

    protected fun finishActivity() {
        if (activity != null && activity is MECLauncherActivity && !activity!!.isFinishing) {
            activity!!.finish()
        }
    }

    override fun onChanged(mecError: MecError?) {
        hideProgressBar()
        processError(mecError)
    }

    open fun processError(mecError: MecError?){
        fragmentManager?.let { context?.let { it1 -> MECutility.showErrorDialog(it1, it,"OK","Error",mecError!!.exception!!.message.toString()) } }
    }

}