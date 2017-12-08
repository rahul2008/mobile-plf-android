/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.uappdemo.screens.introscreen.welcomefragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp.uikit.customviews.CircleIndicator;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.uappdemo.screens.base.UappOnBoardingBaseFragment;
import com.philips.platform.uappdemo.screens.base.UappBasePresenter;
import com.philips.platform.uappdemo.screens.introscreen.LaunchActivity;
import com.philips.platform.uappdemo.screens.introscreen.pager.WelcomePagerAdapter;
import com.philips.platform.uappdemo.UappDemoUiHelper;
import com.philips.platform.uappdemolibrary.R;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.shamanland.fonticon.FontIconView;



/**
 * <b></b>Introduction screen are the screen that acts as the Welcome screens. It may be used to make the user learn about the functionality of the app</b>
 * <br>
 * <p/>
 * <b>To use the Introduction screen flow, start the mActivity with IntroudctionScreenActivity as the Intent</b><br>
 * <pre>&lt;To make the start , skip ,left and right button visibility in each screen, please use the onPageSelected
 *
 */
public class WelcomeFragment extends UappOnBoardingBaseFragment implements View.OnClickListener, UappWelcomeView, BackEventListener {

    public static String TAG = LaunchActivity.class.getSimpleName();

    private FontIconView leftArrow;
    private FontIconView rightArrow;
    private TextView doneButton;
    private TextView skipButton;
    private CircleIndicator indicator;
    private UappBasePresenter presenter;
    private ViewPager pager;

    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            BaseFlowManager targetFlowManager = getTargetFlowManager();
            targetFlowManager.getBackState(targetFlowManager.getCurrentState());
            targetFlowManager.clearStates();
            getActivity().finish();
        } else {
            pager.arrowScroll(View.FOCUS_LEFT);
        }
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new WelcomeFragmentPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        UappDemoUiHelper.getInstance().getLoggingInterface().log(LoggingInterface.LogLevel.INFO, TAG,
                " IntroductionScreen Activity Created ");
        View view = inflater.inflate(R.layout.ufw_welcome_fragment, container, false);

        pager = (ViewPager) view.findViewById(R.id.ufw_welcome_pager);
        pager.setAdapter(new WelcomePagerAdapter(getActivity().getSupportFragmentManager()));
        leftArrow = (FontIconView) view.findViewById(R.id.ufw_welcome_leftarrow);
        rightArrow = (FontIconView) view.findViewById(R.id.ufw_welcome_rightarrow);
        doneButton = (TextView) view.findViewById(R.id.ufw_welcome_start_registration_button);
        skipButton = (TextView) view.findViewById(R.id.ufw_welcome_skip_button);
        doneButton.setOnClickListener(this);
        skipButton.setOnClickListener(this);

        indicator = (CircleIndicator) view.findViewById(R.id.ufw_welcome_indicator);
        indicator.setViewPager(pager);
        indicator.setFillColor(Color.WHITE);
        indicator.setStrokeColor(Color.WHITE);
        leftArrow.setVisibility(FontIconView.GONE);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    leftArrow.setVisibility(FontIconView.GONE);
                } else {
                    leftArrow.setVisibility(FontIconView.VISIBLE);
                }

                if (position == 2) {
                    rightArrow.setVisibility(FontIconView.GONE);
                    skipButton.setVisibility(TextView.GONE);
                    doneButton.setVisibility(TextView.VISIBLE);
                } else {
                    rightArrow.setVisibility(FontIconView.VISIBLE);
                    skipButton.setVisibility(TextView.VISIBLE);
                    doneButton.setVisibility(TextView.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.arrowScroll(View.FOCUS_RIGHT);
            }
        });

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.arrowScroll(View.FOCUS_LEFT);
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        if (presenter != null) {
            presenter.onEvent(v.getId());
        }
    }

    @Override
    public boolean handleBackEvent() {
        onBackPressed();
        return true;
    }

    @Override
    public BaseFlowManager getTargetFlowManager() {
        return UappDemoUiHelper.getInstance().getFlowManager();
    }
}