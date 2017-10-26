/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.ths.onboardingtour;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.DotNavigationIndicator;
import com.philips.platform.uid.view.widget.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * <b></b>Introduction screen are the screen that acts as the Welcome screens. It may be used to make the user learn about the functionality of the app</b>
 * <br>
 * <p/>
 * <b>To use the Introduction screen flow, start the mActivity with IntroudctionScreenActivity as the Intent</b><br>
 * <pre>&lt;To make the start , skip ,left and right button visibility in each screen, please use the onPageSelected
 */
public class OnBoardingTourFragment extends THSBaseFragment implements View.OnClickListener, View.OnLongClickListener {

    public static String TAG = OnBoardingTourFragment.class.getSimpleName();

    private ImageView rightArrow, leftArrow;
    private Label doneButton;
    private Label skipButton;
    private DotNavigationIndicator indicator;
    private OnBoardingTourPresenter presenter;
    private ViewPager pager;
    //private Button environmentSelection;

    List<OnBoardingTourContentModel> onBoardingTourContentModelList;


    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new OnBoardingTourPresenter(this);

        //Just for testing
        OnBoardingTourContentModel onBoardingTourContentModel1 = new OnBoardingTourContentModel(R.string.onboarding_one_text, R.mipmap.onboarding_tour_one);
        OnBoardingTourContentModel onBoardingTourContentModel2 = new OnBoardingTourContentModel(R.string.onboarding_two_text, R.mipmap.onboarding_tour_two);
        OnBoardingTourContentModel onBoardingTourContentModel3 = new OnBoardingTourContentModel(R.string.onboarding_three_text, R.mipmap.onboarding_tour_three);
        OnBoardingTourContentModel onBoardingTourContentModel4 = new OnBoardingTourContentModel(R.string.onboarding_four_text, R.mipmap.onboarding_tour_four);

        onBoardingTourContentModelList = new ArrayList<>();
        onBoardingTourContentModelList.add(onBoardingTourContentModel1);
        onBoardingTourContentModelList.add(onBoardingTourContentModel2);
        onBoardingTourContentModelList.add(onBoardingTourContentModel3);
        onBoardingTourContentModelList.add(onBoardingTourContentModel1);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        startLogging();
        View view = inflater.inflate(R.layout.ths_on_boarding_tour_fragment, container, false);

        pager = (ViewPager) view.findViewById(R.id.welcome_pager);
        pager.setAdapter(new OnBoardingTourPagerAdapter(getActivity().getSupportFragmentManager(), onBoardingTourContentModelList, getActivity()));

        rightArrow = (ImageView) view.findViewById(R.id.welcome_rightarrow);
        leftArrow = (ImageView) view.findViewById(R.id.welcome_leftarrow);
        doneButton = (Label) view.findViewById(R.id.welcome_start_registration_button);
        skipButton = (Label) view.findViewById(R.id.welcome_skip_button);

        doneButton.setOnClickListener(this);
        skipButton.setOnClickListener(this);
        leftArrow.setOnClickListener(this);
        rightArrow.setOnClickListener(this);

        indicator = (DotNavigationIndicator) view.findViewById(R.id.welcome_indicator);
        indicator.setViewPager(pager);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 0) {
                    skipButton.setVisibility(View.VISIBLE);
                    leftArrow.setVisibility(View.GONE);
                    rightArrow.setVisibility(View.VISIBLE);
                } else {
                    if (position == (pager.getAdapter().getCount() - 1)) {
                        rightArrow.setVisibility(View.GONE);
                        doneButton.setVisibility(View.VISIBLE);
                    } else {
                        rightArrow.setVisibility(View.VISIBLE);
                        doneButton.setVisibility(View.GONE);
                    }
                    skipButton.setVisibility(View.GONE);
                    leftArrow.setVisibility(View.VISIBLE);
                }
                setEnviromentSelectionVisibility(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        startAppTagging();
        return view;
    }

    private void setEnviromentSelectionVisibility(int position) {

    }

    protected void startAppTagging() {

    }

    protected void startLogging() {

    }

    @Override
    public void onClick(View v) {

        int componentID = v.getId();

        if (componentID == R.id.welcome_rightarrow) {
            pager.arrowScroll(View.FOCUS_RIGHT);
        }
        if (componentID == R.id.welcome_leftarrow) {
            pager.arrowScroll(View.FOCUS_LEFT);
        }
        if (componentID == R.id.welcome_start_registration_button) {
            //Start a amwell Fragment

        }
        if (componentID == R.id.welcome_skip_button) {
            //start Amwell Fragemnt .
        }
    }


    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {

        }
        return false;
    }
}