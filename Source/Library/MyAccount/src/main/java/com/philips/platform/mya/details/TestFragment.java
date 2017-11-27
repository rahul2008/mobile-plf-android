package com.philips.platform.mya.details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.R;
import com.philips.platform.mya.base.mvp.MyaBaseFragment;


public class TestFragment extends MyaBaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        return view;
    }

    @Override
    public int getActionbarTitleResId() {
        return 0;
    }

    @Override
    public String getActionbarTitle(Context context) {
        return null;
    }

    @Override
    public boolean getBackButtonState() {
        return false;
    }
}
