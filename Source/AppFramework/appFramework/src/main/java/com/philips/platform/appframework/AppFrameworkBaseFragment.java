/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import com.philips.platform.modularui.statecontroller.UIBasePresenter;

/**
 * AppFrameworkBaseFragment is the <b>Base class</b> for all fragments.
 */
public abstract class AppFrameworkBaseFragment extends Fragment{

    private static String tag = AppFrameworkBaseFragment.class.getSimpleName();
    private static FragmentActivity mFragmentActivityContext = null;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private FragmentManager fragmentManager = null;
    private Thread mUiThread = Looper.getMainLooper().getThread();
    private TextView mActionBarTitle = null;
    protected UIBasePresenter fragmentPresenter;



    public abstract String getActionbarTitle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tag = this.getClass().getSimpleName();
        mFragmentActivityContext = getActivity();
        fragmentManager = mFragmentActivityContext.getSupportFragmentManager();
    }


     @Override
    public void onResume() {
        super.onResume();
        setActionbarTitle();
    }

    /**
     * Updating action bar title. The text has to be updated at each fragment
     * selection/creation.
     */
    private void setActionbarTitle() {
        if (mActionBarTitle == null) {
            mActionBarTitle = (TextView) getActivity().findViewById(R.id.iap_header_title);
        }
        String titleText = null;
        if (getActionbarTitle() == null) {
            titleText = getResources().getString(R.string.app_name);
        } else {
            titleText = getActionbarTitle();
        }
        mActionBarTitle.setText(titleText);
    }
}
