package com.philips.platform.samplemicroapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.uappframework.listener.BackEventListener;

import microapp.com.samplemicroapp.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SampleFragment extends Fragment implements BackEventListener{


    public static String TAG = SampleFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample, container, false);
        return view;
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

}