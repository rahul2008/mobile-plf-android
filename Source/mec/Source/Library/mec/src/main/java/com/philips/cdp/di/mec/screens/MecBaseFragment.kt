/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.screens

import android.app.ProgressDialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.common.MECLauncherActivity
import com.philips.cdp.di.mec.common.MecError
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogCategorizedFragment
import com.philips.cdp.di.mec.screens.catalog.MECProductCatalogFragment
import com.philips.cdp.di.mec.utils.MECDataHolder
import com.philips.cdp.di.mec.utils.MECutility
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState
import com.philips.platform.uappframework.listener.BackEventListener
import com.philips.platform.uid.thememanager.UIDHelper
import com.philips.platform.uid.view.widget.ProgressBar
import com.philips.platform.uid.view.widget.ProgressBarWithLabel


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
        // assuming that user come back to Product detail page only from Retailer and Shopping cart screen
       /* if (currentFragment?.getTag().equals(WebBuyFromRetailersFragment.TAG) )
        {
            setTitleAndBackButtonVisibility(R.string.mec_product_detail_title, true)
            setCartIconVisibility(true)
        }*/

        return false
    }

    fun replaceFragment(newFragment: MecBaseFragment,
                        newFragmentTag: String, isReplaceWithBackStack: Boolean) {
        if (MECDataHolder.INSTANCE.actionbarUpdateListener == null || MECDataHolder.INSTANCE.mecListener == null)
            RuntimeException("ActionBarListner and MECListner cant be null")
        else {
            if (null!=activity && !activity!!.isFinishing) {

                val transaction = activity!!.supportFragmentManager.beginTransaction()
                val simpleName = newFragment.javaClass.simpleName



                transaction.replace(id, newFragment, simpleName)
                if (isReplaceWithBackStack) {
                    transaction.addToBackStack(simpleName)
                }
                transaction.commitAllowingStateLoss()
            }
        }
    }


    fun addFragment(newFragment: MecBaseFragment,
                        newFragmentTag: String, isAddWithBackStack: Boolean) {
        if (MECDataHolder.INSTANCE.actionbarUpdateListener == null || MECDataHolder.INSTANCE.mecListener == null)
            RuntimeException("ActionBarListner and MECListner cant be null")
        else {
            if (null!=activity && !activity!!.isFinishing) {
                val transaction = activity!!.supportFragmentManager.beginTransaction()
                val simpleName = newFragment.javaClass.simpleName
                transaction.add(id, newFragment, simpleName)
                if (isAddWithBackStack) {
                    transaction.addToBackStack(simpleName)
                }
                transaction.commitAllowingStateLoss()
            }
        }
    }

    fun showProductCatalogFragment(fragmentTag: String) {
        val fragment = fragmentManager!!.findFragmentByTag(MECProductCatalogFragment.TAG)
        if (fragment == null) {
            val fragment = fragmentManager!!.findFragmentByTag(MECProductCatalogCategorizedFragment.TAG)
            if (fragment == null) {
                fragmentManager!!.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                replaceFragment(MECProductCatalogFragment(),  MECProductCatalogFragment.TAG, true)
            }else{
                fragmentManager!!.popBackStack(MECProductCatalogCategorizedFragment.TAG, 0)
            }
        } else {
            fragmentManager!!.popBackStack(MECProductCatalogFragment.TAG, 0)
        }
    }

    fun showFragment(fragmentTag: String) {
        if (activity != null && !activity!!.isFinishing) {
            activity!!.supportFragmentManager.popBackStackImmediate(fragmentTag, 0)

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

    fun updateCount(count: Int) {
        if (MECDataHolder.INSTANCE.mecListener  != null) {
            MECDataHolder.INSTANCE.mecListener .onUpdateCartCount(count)
        }
    }

    fun setCartIconVisibility(shouldShow: Boolean) {
        if (MECDataHolder.INSTANCE.mecListener != null) {
            if (isUserLoggedIn() && MECDataHolder.INSTANCE.hybrisEnabled) {
                MECDataHolder.INSTANCE.mecListener.updateCartIconVisibility(shouldShow)
            } else {
                MECDataHolder.INSTANCE.mecListener.updateCartIconVisibility(false)
            }
        }
    }

    protected fun isUserLoggedIn(): Boolean {
        return (MECDataHolder.INSTANCE.userDataInterface != null && MECDataHolder.INSTANCE.userDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN)

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

    fun showProgressBar(mecProgressBar: FrameLayout?) {
        mecProgressBar?.visibility = View.VISIBLE
        if (activity != null) {
            activity?.getWindow()?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    fun showProgressBarWithText(mecProgressBar: FrameLayout?, text: String){
        mecProgressBar?.visibility = View.VISIBLE
        val mecProgressBarText = mecProgressBar?.findViewById(R.id.mec_progress_bar_text) as ProgressBarWithLabel
        mecProgressBarText?.setText(text)
        if (activity != null) {
            activity?.getWindow()?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    fun dismissProgressBar(mecProgressBar: FrameLayout?){
        mecProgressBar?.visibility = View.GONE
        if (activity != null) {
            activity?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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
        processError(mecError,true)
    }

    open fun processError(mecError: MecError?, showDialog: Boolean) {
        MECutility.tagAndShowError(mecError,showDialog,fragmentManager,context)
    }

    abstract fun getFragmentTag():String

    fun <T> LiveData<T>.reObserve(owner: LifecycleOwner, observer: Observer<T>) {
        removeObserver(observer)
        observe(owner, observer)
    }


}
