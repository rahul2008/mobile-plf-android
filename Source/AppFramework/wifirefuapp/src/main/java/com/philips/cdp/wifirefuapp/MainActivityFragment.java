package com.philips.cdp.wifirefuapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.BackEventListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MainActivityFragment extends Fragment implements BackEventListener {


    public static String TAG = MainActivityFragment.class.getSimpleName();
    private FragmentLauncher fragmentLauncher;
    private TextView welcomeTextView;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
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
            String message = arguments.getString(WifiCommLibUappInterface.WELCOME_MESSAGE);
            welcomeTextView.setText(message);
        }
    }
}