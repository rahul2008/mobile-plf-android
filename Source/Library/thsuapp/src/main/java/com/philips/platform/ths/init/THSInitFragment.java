/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.init;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;

public class THSInitFragment extends THSBaseFragment{
    public static final String TAG = THSInitFragment.class.getSimpleName();
    THSInitPresenter mThsInitPresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_init_fragment, container, false);
        mThsInitPresenter = new THSInitPresenter(this);
        initializeSDK(view);
        ActionBarListener actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar(getString(R.string.ths_terms_and_conditions),true);
        }
        return view;
    }

    private void initializeSDK(ViewGroup view) {
        createCustomProgressBar(view, BIG);
        mThsInitPresenter.initializeAwsdk();
    }

    public void popSelfBeforeTransition() {
        if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }
}
