/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;

public class TextEditBoxFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_texteditbox, container, false);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_tittle_textbox;
    }
}
