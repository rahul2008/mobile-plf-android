package com.philips.cdp.ui.catalog.cardviewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.ui.catalog.R;

public final class CardsTestFragment extends Fragment {

    public static CardsTestFragment newInstance() {
        CardsTestFragment fragment = new CardsTestFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = getActivity().getLayoutInflater().inflate(R.layout.uikit_card_view, null, false);
        return layout;
    }

}
