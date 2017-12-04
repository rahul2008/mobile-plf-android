/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.init;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSUtilities;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.utility.THSConstants.THS_INIT_PAGE;


public class THSInitFragment extends THSBaseFragment {
    public static final String TAG = THSInitFragment.class.getSimpleName();
    THSInitPresenter mThsInitPresenter;
    @IdRes
    int progressBarID = 1989898;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_init_fragment, container, false);
        mThsInitPresenter = new THSInitPresenter(this);
        initializeSDK(view);

        ActionBarListener actionBarListener = getActionBarListener();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(getString(R.string.ths_welcome), true);
        }
        return view;
    }

    private void initializeSDK(ViewGroup view) {
        addCustomProgreeBar(view);
        mThsInitPresenter.initializeAwsdk();
    }

    protected void addCustomProgreeBar(ViewGroup view) {
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.ths_custom_progress_bar, null, false);
        RelativeLayout.LayoutParams pbParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pbParams.addRule(RelativeLayout.CENTER_IN_PARENT, v.getId());
        v.setLayoutParams(pbParams);
        view.addView(v);

        Label label = new Label(getContext());
        label.setText(R.string.ths_loading_text);
        RelativeLayout.LayoutParams paramsLabel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsLabel.addRule(RelativeLayout.BELOW, v.getId());
        paramsLabel.addRule(RelativeLayout.CENTER_HORIZONTAL);
        label.setGravity(Gravity.CENTER);
        label.setLayoutParams(paramsLabel);
        label.setTextColor(THSUtilities.getAttributeColor(getContext(), R.attr.uidLoginScreenDefaultTextColor));
        view.addView(label);
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
