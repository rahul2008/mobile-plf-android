package com.philips.cdp.digitalcare.homefragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.digitalcare.R;

/**
 * Created by philips on 6/21/17.
 */

public class TestFragment extends DigitalCareBaseFragment {


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.consumercare_locate_search1, container, false);
        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
