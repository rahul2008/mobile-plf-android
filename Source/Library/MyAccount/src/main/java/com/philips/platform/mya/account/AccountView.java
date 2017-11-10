/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.account;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.MyaBaseFragment;
import com.philips.platform.mya.R;
import com.philips.platform.uid.view.widget.Button;

public class AccountView extends MyaBaseFragment implements
        AccountInterface, View.OnClickListener {

    private Button myaFragmentLaunch;

    private AccountPresenter accountPresenter;

    public AccountView() {
    }

    @Override
    protected void setViewParams(Configuration config, int width) {

    }

    @Override
    protected void handleOrientation(View view) {
    }

    @Override
    public int getTitleResourceId() {
        return R.string.mya_account;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_fragment_account, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        accountPresenter = new AccountPresenter(this, getContext());

        myaFragmentLaunch = (Button) getView().findViewById(R.id.mya_account_permissions);
        myaFragmentLaunch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.mya_account_permissions) {
            getMyaFragment().launchCswFragment();
        }
    }


}
