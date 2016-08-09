/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.Logger;

/**
 * AppFrameworkBaseActivity is the App level settings class for controlling the behavior of apps.
 */
public abstract class AppFrameworkBaseActivity extends UiKitActivity {
    public static final String SHARED_PREFERENCES = "SharedPref";
    public static final String DONE_PRESSED = "donePressed";
    private static String TAG = AppFrameworkBaseActivity.class.getSimpleName();
    private static SharedPreferences mSharedPreference = null;
    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ProductSelectionLogger.i(Constants.ACTIVITY, "onCreate");
        fragmentManager = getSupportFragmentManager();
    }

    protected void showFragment(Fragment fragment, String fragmentTag) {
        int containerId = R.id.frame_container;

            try {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(containerId, fragment, fragmentTag);
                fragmentTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                Logger.e(TAG, "IllegalStateException" + e.getMessage());
                e.printStackTrace();
            }
    }

    protected void showFragment(Fragment fragment, String fragmentTag, boolean addToBackstack) {
        int containerId = R.id.frame_container;

        try {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerId, fragment, fragmentTag);
            fragmentTransaction.addToBackStack(fragmentTag);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            Logger.e(TAG, "IllegalStateException" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.i(Constants.ACTIVITY, " onConfigurationChanged ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.i(Constants.ACTIVITY, " onResume ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Logger.i(Constants.ACTIVITY, " onPause ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.i(Constants.ACTIVITY, "onDestroy ");
    }

    protected boolean backstackFragment() {
        fragmentManager.popBackStack();
        return true;
    }

    private void removeCurrentFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFrag = fragmentManager
                .findFragmentById(R.id.mainContainer);

        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }
        transaction.commit();
    }

    private void removeFragmentByTag(String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFrag = fragmentManager
                .findFragmentByTag(tag);

        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }
        transaction.commit();
    }

    protected boolean findFragmentByTag(String tag) {
        Fragment currentFrag = getSupportFragmentManager().findFragmentByTag(tag);

        return (currentFrag != null);
    }

    protected void setIntroScreenDonePressed() {
        if (mSharedPreference == null) {
            mSharedPreference = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = mSharedPreference.edit();
        editor.putBoolean(DONE_PRESSED, true);
        editor.commit();
    }

    protected Boolean getIntroScreenDonePressed() {
        if (mSharedPreference == null) {
            mSharedPreference = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
        }
        return mSharedPreference.getBoolean(DONE_PRESSED, false);
    }
}
