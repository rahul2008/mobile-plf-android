package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.utils.IAPLog;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class EmptyCartFragment extends BaseNoAnimationFragment implements View.OnClickListener {

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        IAPLog.d(IAPLog.LOG, "EmptyCartFragment onCreateView");
        View rootView = inflater.inflate(R.layout.iap_empty_shopping_cart, container, false);
        Button button = (Button) rootView.findViewById(R.id.continues);
        return rootView;
    }

    @Override
    protected void updateTitle() {

    }

    @Override
    public void onClick(final View v) {

    }
}
