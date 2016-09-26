/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.List;

public abstract class InAppBaseFragment extends Fragment implements BackEventListener {
    private Context mContext;
    private ActionBarListener mActionbarUpdateListener;
    protected IAPListener mIapListener;
    String mTitle = "";

    public void setActionBarListener(ActionBarListener actionBarListener, IAPListener iapListener) {
        mActionbarUpdateListener = actionBarListener;
        mIapListener = iapListener;
    }

    public enum AnimationType {
        NONE
    }

    protected void setTitleAndBackButtonVisibility(int resourceId, boolean isVisible) {
        mTitle = getString(resourceId);
        if (mActionbarUpdateListener == null) {
            throw new RuntimeException("Please set the ActionBar Listener");
        } else
            mActionbarUpdateListener.updateActionBar(resourceId, isVisible);
    }


    protected void setTitleAndBackButtonVisibility(String title, boolean isVisible) {
        mTitle = title;
        if (mActionbarUpdateListener == null) {
            throw new RuntimeException("Please set the ActionBar Listener");
        } else {
            mActionbarUpdateListener.updateActionBar(title, isVisible);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setCartIconVisibility(false); //Check whether it is required ?
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        NetworkUtility.getInstance().dismissErrorDialog();
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    public void addFragment(InAppBaseFragment newFragment,
                            String newFragmentTag) {
        if (mActionbarUpdateListener == null || mIapListener == null) return;
        newFragment.setActionBarListener(mActionbarUpdateListener, mIapListener);
        if (getActivity() != null && !getActivity().isFinishing()) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(getId(), newFragment, newFragmentTag);
            transaction.addToBackStack(newFragmentTag);
            transaction.commitAllowingStateLoss();

            IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
                    + newFragmentTag + ")");
        }
    }

    public void showFragment(String fragmentTag) {
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().getSupportFragmentManager().popBackStackImmediate(fragmentTag, 0);
        }
    }

    public boolean moveToPreviousFragment() {
        return getFragmentManager().popBackStackImmediate();
    }

    public void showProductCatalogFragment() {
        Fragment fragment = getFragmentManager().findFragmentByTag(ProductCatalogFragment.TAG);
        if (fragment == null) {
            if (getActivity() != null && getActivity() instanceof IAPActivity && !getActivity().isFinishing()) {
                clearFragmentStack();
                addFragment(ProductCatalogFragment.createInstance(new Bundle(),
                        AnimationType.NONE), ProductCatalogFragment.TAG);
            }
        } else {
            getFragmentManager().popBackStack(ProductCatalogFragment.TAG, 0);
        }
    }

    public void clearFragmentStack() {
        if (getActivity() != null && getActivity() instanceof IAPActivity && !getActivity().isFinishing()) {
            FragmentManager fragManager = getActivity().getSupportFragmentManager();
            int count = fragManager.getBackStackEntryCount();
            for (; count >= 0; count--) {
                List<Fragment> fragmentList = fragManager.getFragments();
                if (fragmentList != null && fragmentList.size() > 0) {
                    fragManager.popBackStack();
                }
            }
        }
    }

    public void moveToVerticalAppByClearingStack() {
        clearFragmentStack();
        finishActivity();
    }

    protected boolean isNetworkConnected() {
        if (mContext != null && !NetworkUtility.getInstance().isNetworkAvailable(mContext)) {
            NetworkUtility.getInstance().showErrorDialog(mContext,
                    getFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_you_are_offline), getString(R.string.iap_no_internet));
            return false;
        } else {
            return true;
        }
    }

    public void updateCount(final int count) {
        if (mIapListener != null) {
            mIapListener.onGetCartCount(count);
        }
    }

    public void setCartIconVisibility(final boolean shouldShow) {
        if (mIapListener != null) {
            mIapListener.updateCartIconVisibility(shouldShow);
        }
    }

    protected void finishActivity() {
        if (getActivity() != null && getActivity() instanceof IAPActivity && !getActivity().isFinishing()) {
            getActivity().finish();
        }
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }
}
