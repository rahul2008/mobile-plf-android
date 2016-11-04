package com.philips.platform.catalogapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;

public class AlertDialogFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        return view;
    }

    @Override
    public int getPageTitle() {
        return R.string.page_tittle_alertDialog;
    }
}
