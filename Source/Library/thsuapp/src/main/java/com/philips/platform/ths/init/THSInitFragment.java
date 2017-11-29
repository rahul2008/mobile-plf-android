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
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import static com.philips.platform.ths.utility.THSConstants.THS_INIT_PAGE;


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
            actionBarListener.updateActionBar(getString(R.string.ths_welcome),true);
        }
        return view;
    }

    private void initializeSDK(ViewGroup view) {
        createCustomProgressBar(view, BIG);
        mThsInitPresenter.initializeAwsdk();
    }

    @Override
    public void onResume() {
        super.onResume();
        // entry to THS, start tagging
        THSManager.getInstance().getThsTagging().collectLifecycleInfo(this.getActivity());
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_INIT_PAGE, null, null);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
       //do nothing , here connection error will be handled by other call backs
    }
}
