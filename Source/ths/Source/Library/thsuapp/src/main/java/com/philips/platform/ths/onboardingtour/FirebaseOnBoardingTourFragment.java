/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.ths.onboardingtour;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ths.R;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.DotNavigationIndicator;

import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.ON_BOARDING_PAGE_1;

/**
 * <b></b>Introduction screen are the screen that acts as the Welcome screens. It may be used to make the user learn about the functionality of the app</b>
 * <br>
 * <p/>
 * <b>To use the Introduction screen flow, start the mActivity with IntroudctionScreenActivity as the Intent</b><br>
 * <pre>&lt;To make the start , skip ,left and right button visibility in each screen, please use the onPageSelected
 */
public class FirebaseOnBoardingTourFragment extends OnBoardingTourFragment implements View.OnClickListener {

    public static String TAG = FirebaseOnBoardingTourFragment.class.getSimpleName();

    protected OnBoardingTourPresenter presenter;
    protected ViewPager pager;

    List<OnBoardingTourContentModel> onBoardingTourContentModelList;
    private OnBoardingTourPagerAdapter onBoardingTourPagerAdapter;
    private Button startNowButton;
    private Button termsConditionsButton;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new OnBoardingTourPresenter(this);

        onBoardingTourContentModelList = presenter.createOnBoardingContent();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_firebase_on_boarding_tour_fragment, container, false);
        ActionBarListener actionBarListener = getActionBarListener();
        if (actionBarListener != null) {
            actionBarListener.updateActionBar(R.string.ths_Welcome_nav_title, false);
        }

        pager = view.findViewById(R.id.welcome_pager);
        onBoardingTourPagerAdapter = new OnBoardingTourPagerAdapter(getActivity().getSupportFragmentManager(), onBoardingTourContentModelList, getFragmentLauncher(), getActionBarListener());
        pager.setAdapter(onBoardingTourPagerAdapter);

        startNowButton = view.findViewById(R.id.btn_startnow);
        termsConditionsButton = view.findViewById(R.id.btn_termsConditions);

        termsConditionsButton.setOnClickListener(this);
        startNowButton.setOnClickListener(this);

        DotNavigationIndicator indicator = view.findViewById(R.id.welcome_indicator);
        indicator.setViewPager(pager);

        addPageChangeListener();

        THSTagUtils.doTrackPageWithInfo(ON_BOARDING_PAGE_1 + "_alt Onboarding", null, null);
        return view;
    }

    protected void addPageChangeListener() {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == (pager.getAdapter().getCount() - 1)) {
                    startNowButton.setVisibility(View.GONE);
                    termsConditionsButton.setVisibility(View.VISIBLE);
                } else {
                    startNowButton.setVisibility(View.VISIBLE);
                    termsConditionsButton.setVisibility(View.GONE);
                }

                startAppTagging(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    protected void startAppTagging(int position) {
        String pageTitle = onBoardingTourPagerAdapter.getPageTitle(position);
        THSTagUtils.doTrackPageWithInfo(pageTitle, null, null);
    }

    @Override
    public void onClick(View v) {

        int componentID = v.getId();

        if (componentID == R.id.btn_startnow) {
            presenter.onEvent(v.getId());
        }
        if (componentID == R.id.btn_termsConditions) {
            this.popSelfBeforeTransition();
            presenter.onEvent(v.getId());
        }

    }

    public boolean handleBackEvent() {
        THSTagUtils.doExitToPropositionWithCallBack();
        return true;
    }

    public OnBoardingTourPagerAdapter getOnBoardingTourPagerAdapter() {
        return onBoardingTourPagerAdapter;
    }

    public void onResume() {
        super.onResume();

        final Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.uid_toolbar);
        if (toolbar != null) {
            final Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ths_cross_icon);
            toolbar.setNavigationIcon(drawable);
        }
    }

    protected void setCurrent(){
        pager.setCurrentItem(pager.getAdapter().getCount() - 1,false);
    }

}
