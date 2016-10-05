/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.introscreen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.philips.cdp.uikit.customviews.CircleIndicator;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.shamanland.fonticon.FontIconView;

/**
 * <b></b>Introduction screen are the screen that acts as the Welcome screens. It may be used to make the user learn about the functionality of the app</b>
 * <br>
 * <p/>
 * <b>To use the Introduction screen flow, start the mActivity with IntroudctionScreenActivity as the Intent</b><br>
 * <pre>&lt;To make the start , skip ,left and right button visibility in each screen, please use the onPageSelected
 *
 */
public class WelcomeScreenFragment extends Fragment implements View.OnClickListener, WelcomeView {

    private static String TAG = WelcomeActivity.class.getSimpleName();
    private FontIconView appframework_leftarrow, appframework_rightarrow;
    private TextView startRegistrationScreenButton, appframeworkSkipButton;
    private CircleIndicator indicator;
    private UIBasePresenter presenter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.INFO, TAG, " IntroductionScreen Activity Created ");
        presenter = new WelcomePresenter(this);
        View view = inflater.inflate(R.layout.af_welcome_activity, container, false);
        final ViewPager mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new WelcomePagerAdapter(getActivity().getSupportFragmentManager()));

        appframework_leftarrow = (FontIconView) view.findViewById(R.id.appframework_leftarrow);
        appframework_rightarrow = (FontIconView) view.findViewById(R.id.appframework_rightarrow);
        startRegistrationScreenButton = (TextView) view.findViewById(R.id.start_registration_button);
        appframeworkSkipButton = (TextView) view.findViewById(R.id.appframework_skip_button);
        startRegistrationScreenButton.setOnClickListener(this);
        appframeworkSkipButton.setOnClickListener(this);

        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.setFillColor(Color.WHITE);
        indicator.setStrokeColor(Color.WHITE);
        appframework_leftarrow.setVisibility(FontIconView.GONE);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        appframework_leftarrow.setVisibility(FontIconView.GONE);
                        appframework_rightarrow.setVisibility(FontIconView.VISIBLE);
                        startRegistrationScreenButton.setVisibility(TextView.GONE);
                        appframeworkSkipButton.setVisibility(TextView.VISIBLE);
                        break;
                    case 1:
                        appframework_leftarrow.setVisibility(FontIconView.VISIBLE);
                        appframework_rightarrow.setVisibility(FontIconView.VISIBLE);
                        startRegistrationScreenButton.setVisibility(TextView.GONE);
                        appframeworkSkipButton.setVisibility(TextView.VISIBLE);
                        break;
                    case 2:
                        appframework_leftarrow.setVisibility(FontIconView.VISIBLE);
                        appframework_rightarrow.setVisibility(FontIconView.GONE);
                        startRegistrationScreenButton.setVisibility(TextView.VISIBLE);
                        appframeworkSkipButton.setVisibility(TextView.GONE);
                        break;
                    default:
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        appframework_rightarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mPager.getCurrentItem() < mPager.getRight())
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
            }
        });

        appframework_leftarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() > mPager.getLeft())
                    mPager.setCurrentItem(mPager.getCurrentItem() - 1, true);
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        presenter.onClick(v.getId(), getActivity());
    }

    @Override
    public void showActionBar() {
        final WelcomeActivity welcomeActivity = (WelcomeActivity) getActivity();
        welcomeActivity.showActionBar();
    }

    @Override
    public void hideActionBar() {
        final WelcomeActivity welcomeActivity = (WelcomeActivity) getActivity();
        welcomeActivity.hideActionBar();
    }

    @Override
    public void loadWelcomeFragment() {
        final WelcomeActivity welcomeActivity = (WelcomeActivity) getActivity();
        welcomeActivity.loadWelcomeFragment();
    }

    @Override
    public void finishActivityAffinity() {
        final WelcomeActivity welcomeActivity = (WelcomeActivity) getActivity();
        welcomeActivity.finishAffinity();
    }

    @Override
    public ActionBarListener getActionBarListener() {
        return (WelcomeActivity) getActivity();
    }

    @Override
    public int getContainerId() {
        return R.id.fragment_frame_container;
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }
}
