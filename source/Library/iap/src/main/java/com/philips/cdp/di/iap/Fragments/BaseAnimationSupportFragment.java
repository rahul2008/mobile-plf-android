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
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.List;

public abstract class BaseAnimationSupportFragment extends Fragment implements BackEventListener{
    private Context mContext;
    private ActionBarListener mActionbarUpdateListener;
    private String mTitle = "";

    public void setActionBarListener(ActionBarListener actionBarListener) {
        mActionbarUpdateListener = actionBarListener;
    }

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
        if (getContext() != null && !NetworkUtility.getInstance().isNetworkAvailable(getContext())) {
            NetworkUtility.getInstance().showErrorDialog(getContext(), getFragmentManager(), getString(R.string.iap_ok), getString(R.string.iap_you_are_offline), getString(R.string.iap_no_internet));
            return true;
        }
        return false;
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mContext = context;
//        if (mFragmentLayout == null) {
//            mFragmentLayout = new IAPFragmentActionLayout(getContext(), getActivity().getSupportFragmentManager());
//        }
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
       // setBackButtonVisibility(true);
        //setCartIconVisibility(false);
        //mFragmentLayout.getCartContainer().setOnClickListener(mCartIconListener);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        NetworkUtility.getInstance().dismissErrorDialog();
        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    public void addFragment(BaseAnimationSupportFragment newFragment,
                            String newFragmentTag) {
        newFragment.setActionBarListener(mActionbarUpdateListener);
        if (getActivity() != null && !getActivity().isFinishing()) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(getId(), newFragment, newFragmentTag);
            transaction.addToBackStack(newFragmentTag);
            transaction.commitAllowingStateLoss();

            IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
                    + newFragmentTag + ")");
        }
    }

//    public void removeFragment() {
//        if (getActivity() != null && !getActivity().isFinishing()) {
//            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//            Fragment currentFragment = getActivity().getSupportFragmentManager()
//                    .findFragmentById(getId());
//            if (currentFragment != null) {
//                transaction.remove(currentFragment);
//            }
//            transaction.commit();
//        }
//    }

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

//    public void launchShoppingCart() {
//        if (getActivity() != null && !getActivity().isFinishing()) {
//            FragmentManager manager = getActivity().getSupportFragmentManager();
//            FragmentTransaction transaction = manager.beginTransaction();
//            transaction.replace(getId(), new ShoppingCartFragment());
//            transaction.addToBackStack(ShoppingCartFragment.TAG);
//            transaction.commitAllowingStateLoss();
//        }
//    }


    protected void setTitleAndBackButtonVisibility(int resourceId, boolean isVisible) {
        // mFragmentLayout.setHeaderTitle(resourceId);
        mTitle = getString(resourceId);
        mActionbarUpdateListener.updateActionBar(resourceId, isVisible);
    }

    protected void setTitleAndBackButtonVisibility(String title, boolean isVisible) {
        //  mFragmentLayout.setHeaderTitle(title);
        mTitle = title;
        mActionbarUpdateListener.updateActionBar(title, isVisible);
    }

//    protected void setBackButtonVisibility(final boolean isVisible) {
//        //  mFragmentLayout.setBackButtonVisibility(isVisible);
//        mActionbarUpdateListener.updateActionBar(mTitle, isVisible);
//    }

    protected void finishActivity() {
//        IAPAnalytics.trackPage(Tagging.getLaunchingPageName());
        if (getActivity() != null && !getActivity().isFinishing()) {
            getActivity().finish();
        }
    }

    @Override
    public boolean handleBackEvent() {
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
                    fragManager.popBackStack();
                }
            }
        }
    }

    public void updateCount(final int count) {
        // mFragmentLayout.updateCount(count);
    }

    public void setCartIconVisibility(final boolean visibility) {
        if (!ControllerFactory.getInstance().shouldDisplayCartIcon()) {
            //  mFragmentLayout.setCartIconVisibility(View.GONE);
            mActionbarUpdateListener.updateActionBar(mTitle, false);
        } else {
            //  mFragmentLayout.setCartIconVisibility(visibility);
            mActionbarUpdateListener.updateActionBar(mTitle, visibility);
        }
    }
}
