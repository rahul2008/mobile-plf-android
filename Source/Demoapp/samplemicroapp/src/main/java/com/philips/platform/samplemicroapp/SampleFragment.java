package com.philips.platform.samplemicroapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.BackEventListener;

import microapp.com.samplemicroapp.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SampleFragment extends Fragment implements BackEventListener {


    public static String TAG = SampleFragment.class.getSimpleName();
    private FragmentLauncher fragmentLauncher;
    private TextView welcomeTextView;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sample, container, false);
        welcomeTextView = (TextView) view.findViewById(R.id.welcome_text);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentLauncher.getActionbarListener().updateActionBar("Sample", true);
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
        this.fragmentLauncher = fragmentLauncher;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            String message = arguments.getString(SampleMicroAppInterface.WELCOME_MESSAGE);
            welcomeTextView.setText(message);
        }
    }
}