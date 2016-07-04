/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.Logger;

/**
 * AppFrameworkBaseActivity is the App level settings class for controlling the behavior of apps.
 *
 * @author: ritesh.jha@philips.com
 * @since: June 17, 2016
 */
public abstract class AppFrameworkBaseActivity extends UiKitActivity {
    public static final String SHARED_PREFERENCES = "SharedPref";
    public static final String DONE_PRESSED = "donePressed";
    private static String TAG = AppFrameworkBaseActivity.class.getSimpleName();
    private FragmentManager fragmentManager = null;
    private static SharedPreferences mSharedPreference = null;

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
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
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
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            fragmentManager.popBackStack();
            removeCurrentFragment();
        }
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

    public static void setIntroScreenDonePressed() {
        SharedPreferenceUtility.getInstance().writePreferenceBoolean(DONE_PRESSED,true);
    }

    public static Boolean getIntroScreenDonePressed() {
        return SharedPreferenceUtility.getInstance().getPreferenceBoolean(DONE_PRESSED);
    }
}
