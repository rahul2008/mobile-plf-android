package com.philips.multiproduct.homefragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.philips.multiproduct.R;


public class SupportHomeFragment extends BaseFragment {

    private static final String TAG = SupportHomeFragment.class.getSimpleName();
    private LinearLayout mOptionParent = null;
    private FrameLayout.LayoutParams mParams = null;
    private int ButtonMarginTop = 0;
    private int RegisterButtonMarginTop = 0;
    private boolean mIsFirstScreenLaunch = false;
    private View mView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_digi_care, container,
                false);
        mIsFirstScreenLaunch = true;

        return mView;
    }

    @Override
    public void setViewParams(Configuration config) {

    }

    @Override
    public String getActionbarTitle() {
        return null;
    }

    @Override
    public String setPreviousPageName() {
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}