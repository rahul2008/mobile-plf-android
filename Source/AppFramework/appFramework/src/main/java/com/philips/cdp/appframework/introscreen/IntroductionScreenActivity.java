/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.cdp.appframework.introscreen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.philips.cdp.appframework.AppFrameworkBaseActivity;
import com.philips.cdp.appframework.R;
import com.philips.cdp.appframework.homescreen.HamburgerActivity;
import com.philips.cdp.modularui.ActivityMap;
import com.philips.cdp.modularui.UIBaseNavigation;
import com.philips.cdp.modularui.UIConstants;
import com.philips.cdp.modularui.UIStateManager;
import com.philips.cdp.modularui.UIState;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.cdp.uikit.customviews.CircleIndicator;
import com.shamanland.fonticon.FontIconView;

/**
 * <b></b>Introduction screen are the screen that acts as the Welcome screens. It may be used to make the user learn about the functionality of the app</b>
 * <br>
 * <p/>
 * <b>To use the Introduction screen flow, start the mActivity with IntroudctionScreenActivity as the Intent</b><br>
 * <pre>&lt;To make the start , skip ,left and right button visibility in each screen, please use the onPageSelected
 * method in the ViewPager's addOnPageChangeListener.To add a new screen, add another case statement in the onPageSelected method in the Viewpager's
 * addOnPageChangeListener.
 *
 * /&gt;</pre><br>
 *
 * <b>Sample Code:</b>
 * <pre>
 *            @Override
 * public void onPageSelected(int position) {
 * switch (position) {
 * case 0:
 * appframework_leftarrow.setVisibility(FontIconView.GONE);
 * appframework_rightarrow.setVisibility(FontIconView.VISIBLE);
 * startRegistrationScreenButton.setVisibility(TextView.GONE);
 * appframeworkSkipButton.setVisibility(TextView.VISIBLE);
 * break;
 * case 1:
 * appframework_leftarrow.setVisibility(FontIconView.VISIBLE);
 * appframework_rightarrow.setVisibility(FontIconView.VISIBLE);
 * startRegistrationScreenButton.setVisibility(TextView.GONE);
 * appframeworkSkipButton.setVisibility(TextView.VISIBLE);
 * break;
 * case 2:
 * appframework_leftarrow.setVisibility(FontIconView.VISIBLE);
 * appframework_rightarrow.setVisibility(FontIconView.GONE);
 * startRegistrationScreenButton.setVisibility(TextView.VISIBLE);
 * appframeworkSkipButton.setVisibility(TextView.GONE);
 * break;
 * default:
 * }
 * }
 *        </pre>
 * <p/>
 * <p/>
 * <b>To change text in each Welcome Screen/Fragment please use the strings file:</b>
 * <br>
 * <b>To modify a screen/fragment use the Fragments onCreateView method's switch statement, choose the screen to be modified and
 * make the necessary changes. Sample code below:
 * </b>
 * <pre>
 *         switch (page) {
 * case PAGE_ONE:
 * view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(
 * ContextCompat.getDrawable(getActivity(), R.drawable.introduction_start_page_bg));
 * break;
 * case PAGE_TWO:
 * view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(
 * ContextCompat.getDrawable(getActivity(), R.drawable.introduction_center_page_bg));
 * smallText.setText(getResources().getString(R.string.introduction_screen_two_bottom_text));
 * break;
 * case PAGE_THREE:
 * view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(
 * ContextCompat.getDrawable(getActivity(), R.drawable.introduction_end_page_bg));
 * smallText.setText(getResources().getString(R.string.introduction_screen_three_bottom_text));
 * break;
 * default:
 * view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(
 * ContextCompat.getDrawable(getActivity(), R.drawable.introduction_start_page_bg));
 * smallText.setText(getResources().getString(R.string.introduction_screen_one_bottom_text));
 * }
 *        </pre>
 */

public class IntroductionScreenActivity extends AppFrameworkBaseActivity implements UserRegistrationListener, RegistrationTitleBarListener, View.OnClickListener {
    private FontIconView appframework_leftarrow, appframework_rightarrow;
    private TextView startRegistrationScreenButton, appframeworkSkipButton;
    private CircleIndicator mIndicator;
    private UIBaseNavigation mNavigator;
    public static final int userRegistrationClickID = 7001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavigator = UIStateManager.getInstance().getCurrentState().getNavigator();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.app_framework_introduction_activity);
        final ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));

        appframework_leftarrow = (FontIconView) findViewById(R.id.appframework_leftarrow);
        appframework_rightarrow = (FontIconView) findViewById(R.id.appframework_rightarrow);
        startRegistrationScreenButton = (TextView) findViewById(R.id.start_registration_button);
        appframeworkSkipButton = (TextView) findViewById(R.id.appframework_skip_button);
        startRegistrationScreenButton.setOnClickListener(this);
        appframeworkSkipButton.setOnClickListener(this);

        mIndicator = (CircleIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.setFillColor(Color.WHITE);
        mIndicator.setStrokeColor(Color.WHITE);
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
        RegistrationHelper.getInstance().registerUserRegistrationListener(this);
    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        mNavigator = UIStateManager.getInstance().getCurrentState().getNavigator();
        if (null != activity) {
            UIState returnedState = (UIState) mNavigator.onPageLoad(IntroductionScreenActivity.this);

                    if (ActivityMap.activityMap.get(returnedState.getStateID()) == UIConstants.UI_HAMBURGER_SCREEN) {
                        UIStateManager.getInstance().setCurrentState(UIStateManager.getInstance().getFromStateList(UIConstants.UI_HAMBURGER_STATE));
                        startActivity(new Intent(IntroductionScreenActivity.this, HamburgerActivity.class));
                    }
            }
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }

    @Override
    public void onUserLogoutSuccess() {

    }

    @Override
    public void onUserLogoutFailure() {

    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {

    }

    @Override
    protected void onDestroy() {
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this);
        super.onDestroy();
    }

    @Override
    public void updateRegistrationTitle(int i) {

    }

    @Override
    public void updateRegistrationTitleWithBack(int i) {

    }

    @Override
    public void updateRegistrationTitleWithOutBack(int i) {

    }

    @Override
    public void onClick(View v) {

        UIState returnedState  = (UIState) mNavigator.onClick(v.getId(), IntroductionScreenActivity.this);
        if (ActivityMap.activityMap.get(returnedState.getStateID()) == UIConstants.UI_USER_REGISTRATION_SCREEN) {
            UIStateManager.getInstance().setCurrentState(UIStateManager.getInstance().getFromStateList(UIConstants.UI_REGISTRATION_STATE));
            startActivity(new Intent(IntroductionScreenActivity.this, RegistrationActivity.class));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        mNavigator.setState();
    }

    @Override
    protected void onRestart() {

        super.onRestart();
    }
}
