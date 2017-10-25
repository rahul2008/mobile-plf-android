/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.ths.onboardingtour;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uid.view.widget.Label;

/**
 * Welcome fragment contains the screens for onboarding , as of now it supports 3 screens
 * The default content can be resplaced by verticals by changing the xml file 'parent_introduction_fragment_layout'
 */
public class OnBoardingTourPageFragment extends THSBaseFragment {
    public static final String TAG =  OnBoardingTourPageFragment.class.getSimpleName();

    private static final String ARG_PAGE_TITLE = "pageTitle";
    private static final String ARG_PAGE_BG_ID = "pageBgId";

    // Store instance variables
    @StringRes private int titleId;
    @DrawableRes private int backgroundId;

    public static OnBoardingTourPageFragment newInstance(@StringRes int title,
                                                         @DrawableRes int background) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_TITLE, title);
        args.putInt(ARG_PAGE_BG_ID, background);

        OnBoardingTourPageFragment fragmentFirst = new OnBoardingTourPageFragment();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titleId = getArguments().getInt(ARG_PAGE_TITLE, 0);
        backgroundId = getArguments().getInt(ARG_PAGE_BG_ID, R.drawable.ths_welcome);
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_on_boarding_tour_slide_fragment, null);

        Label largeText = (Label) view.findViewById(R.id.welcome_slide_large_text);
       // Label smallText = (Label) view.findViewById(R.id.welcome_slide_small_text);
        View background = view.findViewById(R.id.welcome_slide_fragment_layout);

        largeText.setText(titleId);

       /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            smallText.setText(Html.fromHtml(getString(subtitleId), Html.FROM_HTML_MODE_LEGACY));
        } else {
            smallText.setText(Html.fromHtml(getString(subtitleId)));
        }*/
        background.setBackground(ContextCompat.getDrawable(getActivity(), backgroundId));

        return view;
    }
}