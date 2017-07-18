package com.philips.platform.ths.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;

public class THSProviderNotAvailableFragment extends THSBaseFragment{
    public static final String TAG = THSProviderNotAvailableFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_no_available_provider, container, false);
        return view;
    }
}
