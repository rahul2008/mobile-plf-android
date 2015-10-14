package com.philips.cdp.uikit.hamburger;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.uikit.R;

public class WhatsHotFragment extends Fragment {

    public WhatsHotFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_whats_hot, container, false);

        return rootView;
    }
}
