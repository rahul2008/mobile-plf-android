/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.ths.onboardingtour;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;

import java.io.Serializable;
import java.util.List;

/**
 * Welcome fragment contains the screens for onboarding , as of now it supports 3 screens
 * The default content can be resplaced by verticals by changing the xml file 'parent_introduction_fragment_layout'
 */
public class OnBoardingTourPageFragment extends THSBaseFragment {
    public static final String TAG =  OnBoardingTourPageFragment.class.getSimpleName();

    protected static final String ARG_PAGE_TITLE = "pageTitle";
    protected static final String ARG_PAGE_BG_ID = "pageBgId";

    // Store instance variables
    @StringRes private int titleId;
    @DrawableRes private int backgroundId;
    private List<OnBoardingSpanValue> spanValues;

    public static OnBoardingTourPageFragment newInstance(@StringRes int title,
                                                         @DrawableRes int background, List<OnBoardingSpanValue> spanIndexPairs) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_TITLE, title);
        args.putInt(ARG_PAGE_BG_ID, background);
        args.putSerializable("INDEX_PAIRS", (Serializable) spanIndexPairs);

        OnBoardingTourPageFragment fragmentFirst = new OnBoardingTourPageFragment();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();
        if(arguments!=null) {
            titleId = arguments.getInt(ARG_PAGE_TITLE, 0);
            backgroundId = arguments.getInt(ARG_PAGE_BG_ID, R.drawable.ths_welcome);
            spanValues = (List<OnBoardingSpanValue>) arguments.getSerializable("INDEX_PAIRS");
        }
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_on_boarding_tour_slide_fragment, null);

        TextView tvOnBoaringText = (TextView) view.findViewById(R.id.onboarding_page_text);
        SpannableStringBuilder spannableStringBuilder = getSpannableStringBuilder();
        View background = view.findViewById(R.id.welcome_slide_fragment_layout);

        tvOnBoaringText.setText(spannableStringBuilder);
        background.setBackground(ContextCompat.getDrawable(getActivity(), backgroundId));
        return view;
    }

    @NonNull
    protected SpannableStringBuilder getSpannableStringBuilder() {
        Typeface centraleSansBold = Typeface.createFromAsset(getActivity().getAssets(), "fonts/centralesansbold.ttf");
        Typeface centraleSansBook = Typeface.createFromAsset(getActivity().getAssets(), "fonts/centralesansbook.ttf");

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getString(titleId));
        if(spanValues != null && !spanValues.isEmpty()) {
            for(OnBoardingSpanValue spanValue : spanValues) {
                switch (spanValue.getOnBoardingTypeface()) {
                    case BOLD:
                        spannableStringBuilder.setSpan(new OnBoardingTypefaceSpan(centraleSansBold), spanValue.getStartIndex(), spanValue.getEndIndex(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        break;
                    case BOOK:
                        spannableStringBuilder.setSpan(new OnBoardingTypefaceSpan(centraleSansBook), spanValue.getStartIndex(), spanValue.getEndIndex(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        break;
                }
            }
        }
        return spannableStringBuilder;
    }
}