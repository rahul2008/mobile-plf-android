/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.utility.RALog;

/**
 * AbstractAppFrameworkBaseFragment is the <b>Base class</b> for all fragments.
 */
public abstract class AbstractAppFrameworkBaseFragment extends Fragment{
    private static final String TAG = AbstractAppFrameworkBaseFragment.class.getName();

    protected AbstractUIBasePresenter fragmentPresenter;

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
        String titleText = getActionbarTitle() != null ? getActionbarTitle() : getResources().getString(R.string.af_app_name);
        ((AbstractAppFrameworkBaseActivity) getActivity()).setToolBarTitle(titleText);
    }

    protected void startAppTagging(String pageName) {
        AppFrameworkTagging.getInstance().trackPage(pageName);
    }
}
