package com.philips.cdp.di.iap.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.di.iap.Fragments.BaseAnimationSupportFragment;
import com.philips.cdp.di.iap.Fragments.BaseParentFragment;
import com.philips.cdp.di.iap.Fragments.EmptyCartFragment;
import com.philips.cdp.di.iap.Fragments.ShoppingCartFragment;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.uikit.UiKitActivity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BaseFragmentActivity extends UiKitActivity {

    public void addFragmentAndRemoveUnderneath(BaseAnimationSupportFragment newFragment,
                                               boolean addOnTopOfSameFragment) {
        String topFragmentTag = getTopFragmentTag();

        if (isFragmentAlreadyOnTop(newFragment, getTopFragment())) {
            IAPLog.d(IAPLog.LOG, " fragment already on top (" + topFragmentTag
                    + ")");
            if (!addOnTopOfSameFragment) {
                return;
            }
            IAPLog.d(IAPLog.LOG, "Adding same fragment on top of existing one ("
                    + topFragmentTag + ")");
        }

        newFragment.setUnderlyingFragmentTag(topFragmentTag);
        String newFragmentTag = generateFragmentTag(newFragment);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.fl_mainFragmentContainer, newFragment, newFragmentTag);
        transaction.commitAllowingStateLoss();

        IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
                + newFragmentTag + ")");
    }

    public void removeFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            IAPLog.e(IAPLog.LOG, "No fragment found to remove (" + tag + ")");
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();

        IAPLog.d(IAPLog.LOG, "Fragment removed (" + tag + ")");
    }

    protected String getTopFragmentTag() {
        Fragment topFragment = getTopFragment();
        return topFragment == null ? null : topFragment.getTag();
    }

    public String generateFragmentTag(BaseAnimationSupportFragment newFragment) {
        if (newFragment == null) {
            return null;
        }
        return newFragment.getClass().getPackage() + "." + newFragment.getClass().getSimpleName()
                + newFragment.hashCode();
    }

    public boolean isFragmentAlreadyOnTop(BaseAnimationSupportFragment newFragment,
                                          Fragment topFragment) {
        if (topFragment == null) {
            return false;
        }
        if (newFragment == null) {
            return false;
        }
        return (newFragment.getClass().equals(topFragment.getClass()));
    }

    protected Fragment getTopFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fl_mainFragmentContainer);
    }

    @Override
    public void onBackPressed() {
        IAPLog.d(IAPLog.LOG, "onBackPressed");

        Utility.hideKeypad(this);

        boolean backNavigationHandled = false;
        Fragment topFragment = getTopFragment();
        if (topFragment instanceof BaseParentFragment) {
            backNavigationHandled = ((BaseParentFragment) topFragment).handleBackNavigation();
        }
        if (backNavigationHandled) {
            return;
        }
        if (topFragment instanceof EmptyCartFragment || topFragment instanceof ShoppingCartFragment) {
            this.finish();
        }
        super.onBackPressed();
    }
}
