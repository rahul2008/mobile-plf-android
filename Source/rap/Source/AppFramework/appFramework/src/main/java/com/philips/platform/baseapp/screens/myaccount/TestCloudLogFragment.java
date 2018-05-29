package com.philips.platform.baseapp.screens.myaccount;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.appframework.R;
import com.philips.platform.mya.base.MyaBaseFragment;


public class TestCloudLogFragment extends MyaBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.rap_cloud_logging, container, false);
    }

    @Override
    public int getActionbarTitleResId() {
        return 0;
    }

    @Override
    public String getActionbarTitle(Context context) {
        return "";
    }

    @Override
    public boolean getBackButtonState() {
        return false;
    }
}
