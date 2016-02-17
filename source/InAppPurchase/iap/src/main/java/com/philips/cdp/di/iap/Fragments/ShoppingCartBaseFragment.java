package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPLog;

public class ShoppingCartBaseFragment extends BaseParentFragment {

    public static BaseParentFragment createInstance(AnimationType animType) {
        BaseParentFragment fragment = new ShoppingCartBaseFragment();
        Bundle args = new Bundle();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        addChildFragment(new ShoppingCartHomeFragment(), false, TransitionAnimation.NONE);
        return view;
    }

    @Override
    public boolean handleBackNavigation() {
        IAPLog.i(IAPLog.SHOPPING_BASE_FRAGMENT, "ShoppingBaseFragment = handleBackNavigation");
        //Utility.handleKeyBoard(getActivity());

        FragmentManager childFragmentManager = getChildFragmentManager();

        if (childFragmentManager == null) {
            IAPLog.i(IAPLog.SHOPPING_BASE_FRAGMENT, "ShoppingBaseFragment = handleBackNavigation ==childFragmentManager");
            return false;
        }
        Fragment topFragment = childFragmentManager.findFragmentById(getFragmentContainerId());
        if (topFragment != null && topFragment instanceof ShoppingCartFragment) {
            IAPLog.i(IAPLog.SHOPPING_BASE_FRAGMENT, "ShoppingBaseFragment = handleBackNavigation == topFragment");
            goBackToShoppingCartFragment(childFragmentManager);
        }

        if (childFragmentManager.getBackStackEntryCount() <= 0) {
            IAPLog.i(IAPLog.SHOPPING_BASE_FRAGMENT, "ShoppingBaseFragment = handleBackNavigation == getBackStackEntryCount is more tahn 0");
            return false;
        }
        childFragmentManager.popBackStack();
        return true;
    }

    private void goBackToShoppingCartFragment(FragmentManager childFragmentManager) {
        IAPLog.i(IAPLog.SHOPPING_BASE_FRAGMENT, "RegisterBaseFragment = goBackToShoppingCartFragment");
        int currentLevel = childFragmentManager.getBackStackEntryCount();
        for (int i = currentLevel; i > 1; i--) {
            childFragmentManager.popBackStack();
        }
    }

    @Override
    protected void updateTitle() {

    }
}
