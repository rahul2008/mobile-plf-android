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
import com.philips.cdp.appframework.homoscreen.HomeActivity;
import com.philips.cdp.appframework.userregistrationscreen.UserRegistrationActivity;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.uikit.customviews.CircleIndicator;
import com.shamanland.fonticon.FontIconView;

/**
 * Created by 310240027 on 5/31/2016.
 */
public class IntroductionScreenActivity extends AppFrameworkBaseActivity implements UserRegistrationListener,RegistrationTitleBarListener,View.OnClickListener {
    private FontIconView appframework_leftarrow, appframework_rightarrow;
    private TextView startRegistrationScreenButton, appframeworkSkipButton;
    private CircleIndicator mIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if(null != activity) {
            startActivity(new Intent(IntroductionScreenActivity.this, HomeActivity.class));
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
        User user = new User(this);

        switch (v.getId()){
            case R.id.start_registration_button :
                if(user.isUserSignIn()){
                    startActivity(new Intent(IntroductionScreenActivity.this,HomeActivity.class));
                }else {
                    startActivity(new Intent(IntroductionScreenActivity.this, UserRegistrationActivity.class));
                }
                break;
            case R.id.appframework_skip_button :
                if(user.isUserSignIn()){
                    startActivity(new Intent(IntroductionScreenActivity.this,HomeActivity.class));
                }else {
                    startActivity(new Intent(IntroductionScreenActivity.this, UserRegistrationActivity.class));
                }
                break;

        }
    }
}
