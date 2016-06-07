/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.actionlayout;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPBackButtonListener;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.tagging.Tagging;

import java.util.List;

public class IAPActionLayout implements ActionLayoutCallBack {

    protected View mBackButton;
    protected ViewGroup mMainLayout;

    protected FragmentManager v4FragManager;

    public IAPActionLayout(Context context, FragmentManager v4FragManager) {
        this.v4FragManager = v4FragManager;
        getCustomView(context);
    }

    @Override
    public View getCustomView(Context context) {
        View v = null;
        if (context instanceof Activity) {
            v = ((Activity) context).findViewById(R.id.ratingthememain);
            mMainLayout = (ViewGroup) v;
        }
        if (v == null) {
            v = View.inflate(context, R.layout.iap_action_bar, null);
            mMainLayout = (ViewGroup) v.findViewById(R.id.ratingtheme);
        }
        mBackButton = v.findViewById(R.id.arrow);
        return v;
    }

    @Override
    public void setBackGroundDrawable(final Drawable drawable) {
        mMainLayout.setBackground(drawable);
    }

    @Override
    public void setBackButtonDrawable(final Drawable drawable) {
        mBackButton.setBackground(drawable);
    }

    @Override
    public boolean onHWBackPressed() {
        IAPLog.i(IAPLog.LOG, "OnBackpressed Called");
        Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.BACK_BUTTON_PRESS);
        return dispatchBackToFragments();
    }

    public boolean dispatchBackToFragments() {
        List<Fragment> fragments = v4FragManager.getFragments();
        IAPLog.i(IAPLog.LOG, "OnBackpressed dispatchBackToFragments Called = " + fragments);
        boolean isBackHandled = false;
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible() && (fragment instanceof IAPBackButtonListener)) {

                isBackHandled = ((IAPBackButtonListener) fragment).onBackPressed();
                IAPLog.i(IAPLog.LOG, "OnBackpressed dispatchBackToFragments Called");
            }
        }
        return isBackHandled;
    }
}