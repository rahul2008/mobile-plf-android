package com.philips.platform.catalogapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.catalogapp.R;

public class NotificationBarFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return View.inflate(getActivity(), R.layout.uid_notification_bg_white, null);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_notification_bar;
    }
}
