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
import android.view.View;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.IAPBackButtonListener;
import com.philips.cdp.di.iap.activity.IAPFragmentListener;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.tagging.Tagging;

import java.util.List;

public abstract class BaseAnimationSupportFragment extends Fragment implements IAPBackButtonListener, EventListener {
    private IAPFragmentActionLayout mFragmentLayout;
    private Context mContext;

    private View.OnClickListener mCartIconListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            if (NetworkUtility.getInstance().isNetworkAvailable(mContext)) {
                addFragment(ShoppingCartFragment.createInstance(new Bundle(),
                        BaseAnimationSupportFragment.AnimationType.NONE), ShoppingCartFragment.TAG);
            } else {
                NetworkUtility.getInstance().showErrorDialog(getActivity(), getActivity()
                                .getSupportFragmentManager(), getString(R.string.iap_ok),
                        getString(R.string.iap_you_are_offline), getString(R.string.iap_no_internet));
            }
        }
    };

    protected boolean isNetworkNotConnected() {
        if (!NetworkUtility.getInstance().isNetworkAvailable(getContext())) {
            NetworkUtility.getInstance().showErrorDialog(getContext(), getFragmentManager(), getString(R.string.iap_ok), getString(R.string.iap_you_are_offline), getString(R.string.iap_no_internet));
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mContext = context;
        EventHelper.getInstance().registerEventNotification(IAPConstant.UNKNOWN_HOST, this);
        if (mFragmentLayout == null) {
            mFragmentLayout = new IAPFragmentActionLayout(getContext(), getActivity().getSupportFragmentManager());
        }
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
        mFragmentLayout.getCartContainer().setOnClickListener(mCartIconListener);
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
            FragmentManager fragManager = getActivity().getSupportFragmentManager();
            int count = fragManager.getBackStackEntryCount();
            for (; count >= 0; count--) {
                List<Fragment> fragmentList = fragManager.getFragments();
                if (fragmentList != null && fragmentList.size() > 0) {
                    if (fragmentList.get(fragmentList.size() - 1) instanceof IAPFragmentListener) {
                        fragManager.popBackStackImmediate();
                    }
                }
            }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventHelper.getInstance().unregisterEventNotification(IAPConstant.UNKNOWN_HOST, this);
    }

    @Override
    public void onEventReceived(String event) {
        if (event.equalsIgnoreCase(IAPConstant.UNKNOWN_HOST)) {
            addFragment(NoNetworkConnectionFragment.createInstance(new Bundle(), BaseAnimationSupportFragment.AnimationType.NONE),
                    NoNetworkConnectionFragment.TAG);
        }
    }
}
