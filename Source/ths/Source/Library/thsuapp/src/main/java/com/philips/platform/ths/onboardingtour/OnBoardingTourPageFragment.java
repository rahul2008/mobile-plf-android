/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.ths.onboardingtour;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.onboarding.OnBoardingFragment;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uid.view.widget.Label;

/**
 * Welcome fragment contains the screens for onboarding , as of now it supports 3 screens
 * The default content can be resplaced by verticals by changing the xml file 'parent_introduction_fragment_layout'
 */
@SuppressWarnings("unchecked")
public class OnBoardingTourPageFragment extends THSBaseFragment {
    public static final String TAG =  OnBoardingTourPageFragment.class.getSimpleName();

    protected static final String ARG_PAGE_TEXT = "pageText";
    protected static final String ARG_PAGE_TITLE = "pageTitle";
    protected static final String ARG_PAGE_BG_ID = "pageBgId";
    protected static final String ARG_PAGE_IS_AMWELL_LOGO_PRESENT = "isAmwellLogoPresent";
    static final long serialVersionUID = 1139L;


    // Store instance variables
    @StringRes private int textId;
    @DrawableRes private int backgroundId;
    private boolean isAmwellLogoPresent;
    private int titleId;

    @SuppressWarnings("serial")
    public static THSBaseFragment newInstance(@StringRes int title,
                                              @DrawableRes int background, boolean isAmwellLogoPresent, int tileId) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_TEXT, title);
        args.putInt(ARG_PAGE_BG_ID, background);
        args.putInt(ARG_PAGE_TITLE,tileId);
        args.putBoolean(ARG_PAGE_IS_AMWELL_LOGO_PRESENT,isAmwellLogoPresent);
        THSBaseFragment fragmentFirst;
        if(background == 0){
            fragmentFirst = new OnBoardingFragment();
            return fragmentFirst;
        }
        fragmentFirst = new OnBoardingTourPageFragment();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle arguments = getArguments();
        if(arguments!=null) {
            textId = arguments.getInt(ARG_PAGE_TEXT, 0);
            backgroundId = arguments.getInt(ARG_PAGE_BG_ID, R.drawable.ths_welcome);
            isAmwellLogoPresent = arguments.getBoolean(ARG_PAGE_IS_AMWELL_LOGO_PRESENT,false);
            titleId = arguments.getInt(ARG_PAGE_TITLE,0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ths_on_boarding_tour_slide_fragment, null);
        TextView tvOnBoardingText = view.findViewById(R.id.onboarding_page_text);
        ImageView background = view.findViewById(R.id.onboarding_background_image);
        Label pageTitle = view.findViewById(R.id.onboarding_page_title);
        pageTitle.setText(titleId);
        ImageView amwellLogo = view.findViewById(R.id.ths_amwell_logo);
        if(isAmwellLogoPresent) {
            amwellLogo.setVisibility(View.VISIBLE);
        }
        tvOnBoardingText.setText(textId);

        final Drawable drawable = ContextCompat.getDrawable(getActivity(), backgroundId);
        background.setImageDrawable(drawable);
        return view;
    }

    public boolean handleBackEvent() {
        THSTagUtils.doExitToPropositionWithCallBack();
        return false;
    }
}