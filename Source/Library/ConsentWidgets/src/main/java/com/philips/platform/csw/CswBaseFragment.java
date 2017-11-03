/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.platform.csw;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;


public abstract class CswBaseFragment extends Fragment {

    protected int mLeftRightMarginPort;

    protected int mLeftRightMarginLand;

    public abstract int getTitleResourceId();

    public String getTitleResourceText() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLeftRightMarginPort = (int) getResources().getDimension(com.philips.cdp.registration.R.dimen.reg_layout_margin_port);
        mLeftRightMarginLand = (int) getResources().getDimension(com.philips.cdp.registration.R.dimen.reg_layout_margin_land);
    }

    @Override
    public void onResume() {
        super.onResume();
        setCurrentTitle();
    }

    private void setCurrentTitle() {
        CswFragment fragment = (CswFragment) getParentFragment();
        if (null != fragment) {
            if (fragment.getFragmentCount() > 1) {
                if (null != fragment.getUpdateTitleListener()) {
                    fragment.getUpdateTitleListener().updateActionBar(
                            getTitleResourceId(), true);
                    String titleText = getTitleResourceText();
                    if (titleText != null && titleText.length() > 0) {
                        fragment.getUpdateTitleListener().updateActionBar(titleText, false);
                    }
                }
            } else {
                if (null != fragment.getUpdateTitleListener()) {
                    fragment.getUpdateTitleListener().updateActionBar(
                            getTitleResourceId(), false);
                    String titleText = getTitleResourceText();
                    if (titleText != null && titleText.length() > 0) {
                        fragment.getUpdateTitleListener().updateActionBar(titleText, false);
                    }
                }
            }
            fragment.setResourceID(getTitleResourceId());
        }
    }

}
