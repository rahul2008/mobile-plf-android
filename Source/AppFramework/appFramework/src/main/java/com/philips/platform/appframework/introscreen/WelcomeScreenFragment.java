/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.introscreen;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp.uikit.customviews.CircleIndicator;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.introscreen.pager.WelcomePagerAdapter;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.shamanland.fonticon.FontIconView;

public class WelcomeScreenFragment extends Fragment implements View.OnClickListener {

    private static String TAG = WelcomeActivity.class.getSimpleName();

    private FontIconView leftArrow;
    private FontIconView rightArrow;
    private TextView doneButton;
    private TextView skipButton;
    private CircleIndicator indicator;
    private UIBasePresenter presenter;

    // This is not ideal solution, but more of a workaround
    // ideally each activity and each fragment should have their own presenter responsible
    // only for handling business logic of one specific component
    // Current welcome screen clearly violates Single Responsibility Principle and
    // doing correct MVP pattern here is not possible at this state.
    // TODO make clear separation of responsibilities between activity and fragment
    public void setPresenter(UIBasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.INFO, TAG,
                " IntroductionScreen Activity Created ");
        View view = inflater.inflate(R.layout.af_welcome_fragment, container, false);

        final ViewPager pager = (ViewPager) view.findViewById(R.id.welcome_pager);
        pager.setAdapter(new WelcomePagerAdapter(getActivity().getSupportFragmentManager()));

        leftArrow = (FontIconView) view.findViewById(R.id.welcome_leftarrow);
        rightArrow = (FontIconView) view.findViewById(R.id.welcome_rightarrow);
        doneButton = (TextView) view.findViewById(R.id.welcome_start_registration_button);
        skipButton = (TextView) view.findViewById(R.id.welcome_skip_button);
        doneButton.setOnClickListener(this);
        skipButton.setOnClickListener(this);

        indicator = (CircleIndicator) view.findViewById(R.id.welcome_indicator);
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
            presenter.onClick(v.getId(), getActivity());
        }
    }
}