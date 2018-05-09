/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw;

import com.philips.platform.uappframework.listener.ActionBarListener;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

public abstract class CswBaseFragment extends Fragment {

    private ActionBarListener mActionBarListener;

    public abstract int getTitleResourceId();

    @Override
    public void onResume() {
        super.onResume();
        setCurrentTitle();
    }

    private void setCurrentTitle() {
        if (getUpdateTitleListener() != null) {
            getUpdateTitleListener().updateActionBar(getTitleResourceId(),
                    getFragmentManager().getBackStackEntryCount() > 0);
        }
    }

    public ActionBarListener getUpdateTitleListener() {
        return mActionBarListener;
    }

    public void setUpdateTitleListener(ActionBarListener actionBarListener) {
        mActionBarListener = actionBarListener;
    }
}
