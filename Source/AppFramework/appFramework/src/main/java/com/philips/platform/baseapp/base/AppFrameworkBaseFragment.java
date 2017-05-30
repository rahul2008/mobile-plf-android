/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.utility.RALog;

/**
 * AppFrameworkBaseFragment is the <b>Base class</b> for all fragments.
 */
public abstract class AppFrameworkBaseFragment extends Fragment{
    private static final String TAG = AppFrameworkBaseFragment.class.getName();

    protected UIBasePresenter fragmentPresenter;

    private TextView actionBarTitle = null;

    public abstract String getActionbarTitle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


     @Override
    public void onResume() {
         RALog.d(TAG," onResume called");
         super.onResume();
        setActionbarTitle();
    }

    /**
     * Updating action bar title. The text has to be updated at each fragment
     * selection/creation.
     */
    private void setActionbarTitle() {
        RALog.d(TAG," setActionbarTitle called");

        if (actionBarTitle == null) {
            actionBarTitle = (TextView) getActivity().findViewById(R.id.af_actionbar_title);
        }
        String titleText = null;
        if (getActionbarTitle() == null) {
            titleText = getResources().getString(R.string.af_app_name);
        } else {
            titleText = getActionbarTitle();
        }
        if (actionBarTitle != null)
            actionBarTitle.setText(titleText);
    }
}
