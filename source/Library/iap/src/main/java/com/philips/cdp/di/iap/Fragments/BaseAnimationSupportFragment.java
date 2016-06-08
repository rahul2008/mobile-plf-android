/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.

Project           : InAppPurchase
----------------------------------------------------------------------------*/

package com.philips.cdp.di.iap.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.activity.IAPBackButtonListener;
import com.philips.cdp.di.iap.activity.IAPFragmentListener;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.tagging.Tagging;

public abstract class BaseAnimationSupportFragment extends Fragment implements IAPBackButtonListener {
    private IAPFragmentListener mActivityListener;
    private IAPFragmentActionLayout mFragmentLayout;


    protected boolean isNetworkNotConnected() {
        if (!getIAPActivity().getNetworkUtility().isNetworkAvailable(getContext())) {
            getIAPActivity().getNetworkUtility().showErrorDialog(getContext(), getFragmentManager(), getString(R.string.iap_ok), getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if(mFragmentLayout == null) {
            mFragmentLayout = new IAPFragmentActionLayout(getContext(), getActivity().getSupportFragmentManager());
        }
        mActivityListener = (IAPFragmentListener) context;
    }

    public enum AnimationType {
        /**
         * No animation for Fragment
         */
        NONE,
    }

    @Override
    public void onResume() {
        super.onResume();
        setBackButtonVisibility(View.VISIBLE);
        setCartIconVisibility(View.GONE);
    }

    public void addFragment(BaseAnimationSupportFragment newFragment,
                            String newFragmentTag) {

        if (getActivity() != null && !getActivity().isFinishing()) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(getId(), newFragment, newFragmentTag);
            transaction.addToBackStack(newFragmentTag);
            transaction.commitAllowingStateLoss();

            IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
                    + newFragmentTag + ")");
        }
    }

    public void removeFragment() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment currentFragment = getActivity().getSupportFragmentManager()
                    .findFragmentById(getId());
            if (currentFragment != null) {
                transaction.remove(currentFragment);
            }
            transaction.commit();
        }
    }

    private void clearStackAndLaunchProductCatalog() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            clearFragmentStack();
            manager.beginTransaction().replace(getId(),
                    ProductCatalogFragment.createInstance(new Bundle(), AnimationType.NONE),
                    ProductCatalogFragment.TAG).addToBackStack(ProductCatalogFragment.TAG)
                    .commitAllowingStateLoss();
        }
    }

    public void launchProductCatalog() {
        Fragment fragment = getFragmentManager().findFragmentByTag(ProductCatalogFragment.TAG);
        if (fragment == null) {
            clearStackAndLaunchProductCatalog();
        } else {
            getFragmentManager().popBackStack(ProductCatalogFragment.TAG, 0);
        }
    }

    public void launchShoppingCart() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            FragmentManager manager = getActivity().getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(getId(), new ShoppingCartFragment());
            transaction.addToBackStack(ShoppingCartFragment.TAG);
            transaction.commitAllowingStateLoss();
        }
    }

    protected void setTitle(int resourceId) {
        mFragmentLayout.setHeaderTitle(resourceId);
//        mActivityListener.setHeaderTitle(resourceId);
    }

    protected void setTitle(String title) {
        mFragmentLayout.setHeaderTitle(title);
        //mActivityListener.setHeaderTitle(title);
    }

    protected void setBackButtonVisibility(final int isVisible) {
        mFragmentLayout.setBackButtonVisibility(isVisible);
//        mActivityListener.setBackButtonVisibility(isVisible);
    }

    protected void finishActivity() {
        IAPAnalytics.trackPage(Tagging.getLaunchingPageName());
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().finish();
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    public boolean moveToFragment(String tag) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            return getActivity().getSupportFragmentManager().popBackStackImmediate(tag, 0);
        }
        return false;
    }

    public boolean moveToPreviousFragment() {
        return getFragmentManager().popBackStackImmediate();
    }

    public void clearFragmentStack() {
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().getSupportFragmentManager().popBackStackImmediate(null, 0);
        }

    }

    public void updateCount(final int count) {
        mFragmentLayout.updateCount(count);
        //mActivityListener.updateCount(count);
    }

    public void setCartIconVisibility(final int visibility) {
        if (!ControllerFactory.getInstance().shouldDisplayCartIcon()) {
            mFragmentLayout.setCartIconVisibility(View.GONE);
            //mActivityListener.setCartIconVisibility(View.GONE);
        } else {
            mFragmentLayout.setCartIconVisibility(visibility);
//            mActivityListener.setCartIconVisibility(visibility);
        }
    }

    public IAPActivity getIAPActivity() {
        Activity activity = getActivity();
        if (activity != null && (activity instanceof IAPActivity)) {
            return (IAPActivity) activity;
        }
        return null;
    }
}
