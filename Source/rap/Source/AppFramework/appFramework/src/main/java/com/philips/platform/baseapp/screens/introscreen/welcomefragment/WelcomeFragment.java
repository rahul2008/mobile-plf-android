/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.introscreen.welcomefragment;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.baseapp.base.AbstractOnboardingBaseFragment;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppFrameworkTagging;
import com.philips.platform.baseapp.screens.introscreen.pager.WelcomePagerAdapter;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.baseapp.screens.utility.SharedPreferenceUtility;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.DotNavigationIndicator;
import com.philips.platform.uid.view.widget.Label;
import com.shamanland.fonticon.FontIconView;

/**
 * <b></b>Introduction screen are the screen that acts as the Welcome screens. It may be used to make the user learn about the functionality of the app</b>
 * <br>
 * <p/>
 * <b>To use the Introduction screen flow, start the mActivity with IntroudctionScreenActivity as the Intent</b><br>
 * <pre>&lt;To make the start , skip ,left and right button visibility in each screen, please use the onPageSelected
 */
public class WelcomeFragment extends AbstractOnboardingBaseFragment implements View.OnClickListener,
        WelcomeFragmentView, BackEventListener, View.OnLongClickListener {

    public static String TAG = WelcomeFragment.class.getSimpleName();

    private ImageView rightArrow;
    private Label doneButton;
    private Label skipButton;
    private DotNavigationIndicator indicator;
    private AbstractUIBasePresenter presenter;
    private ViewPager pager;
    private Button environmentSelection;
    private WelcomePagerAdapter welcomePagerAdapter ;
    public void onBackPressed() {
        RALog.d(TAG, " On Back Pressed");
        if (pager.getCurrentItem() == 0) {
            AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) getFragmentActivity().getApplication();
            try {
                BaseFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
                targetFlowManager.getBackState(targetFlowManager.getCurrentState());
                targetFlowManager.clearStates();
                getActivity().finishAffinity();
            } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                    e) {
                RALog.d(TAG, e.getMessage());
                Toast.makeText(getFragmentActivity(), getFragmentActivity().getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
            }
        } else {
            pager.arrowScroll(View.FOCUS_LEFT);
        }
    }

    protected AbstractUIBasePresenter getWelcomePresenter() {
        return new WelcomeFragmentPresenter(this);
    }


    private void hideSystemUi() {
        int visibility =
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) visibility |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        getActivity().getWindow().getDecorView().setSystemUiVisibility(visibility);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        hideSystemUi();
        presenter = getWelcomePresenter();
        startLogging();
        View view = inflater.inflate(R.layout.af_welcome_fragment, container, false);
        welcomePagerAdapter = new WelcomePagerAdapter(getActivity().getSupportFragmentManager());
        pager = (ViewPager) view.findViewById(R.id.welcome_pager);
        pager.setAdapter(welcomePagerAdapter);
        rightArrow = (ImageView) view.findViewById(R.id.welcome_rightarrow);
        doneButton = (Label) view.findViewById(R.id.welcome_start_registration_button);
        skipButton = (Label) view.findViewById(R.id.welcome_skip_button);
        environmentSelection = (Button) view.findViewById(R.id.environment_selection);
        doneButton.setOnClickListener(this);
        skipButton.setOnClickListener(this);
        environmentSelection.setOnLongClickListener(this);

        indicator = (DotNavigationIndicator) view.findViewById(R.id.welcome_indicator);
        indicator.setViewPager(pager);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == (pager.getAdapter().getCount() -1)) {
                    rightArrow.setVisibility(FontIconView.INVISIBLE);
                    skipButton.setVisibility(TextView.GONE);
                    doneButton.setVisibility(TextView.VISIBLE);
                } else {
                    rightArrow.setVisibility(FontIconView.VISIBLE);
                    skipButton.setVisibility(TextView.VISIBLE);
                    doneButton.setVisibility(TextView.GONE);
                }

                setEnviromentSelectionVisibility(position);
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

        startAppTagging();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Uri uri = getActivity().getIntent().getData();
        if(uri!=null&&uri.toString().contains("telehealth.com")){
            if (presenter != null) {
                SharedPreferenceUtility sharedPreferenceUtility =  new SharedPreferenceUtility(getFragmentActivity().getApplicationContext());
                sharedPreferenceUtility.writePreferenceBoolean(Constants.THS_DEEP_LINK_FLOW,true);
                presenter.onEvent(R.id.welcome_skip_button);
            }
        }
    }

    private void setEnviromentSelectionVisibility(int position) {
        environmentSelection.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
    }

    protected void startAppTagging() {
        AppFrameworkTagging.getInstance().trackPage(TAG);
    }

    protected void startLogging() {
        RALog.d(TAG, " start Logging ");
        ((AppFrameworkApplication) getFragmentActivity().getApplicationContext()).getLoggingInterface().log(LoggingInterface.LogLevel.INFO, TAG,
                " IntroductionScreen Activity Created ");
    }

    @Override
    public void onClick(View v) {
        if (presenter != null) {
            presenter.onEvent(v.getId());
            // Fix for Bug 63728:Reference app crashed after the app has launched and we tap on skip button
            v.setOnClickListener(null);
        }
    }

    @Override
    public boolean handleBackEvent() {
        onBackPressed();
        return true;
    }

    @Override
    public void clearAdapter() {
        pager.setAdapter(null);
    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.environment_selection:
                presenter.onEvent(view.getId());
                return true;
        }
        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentItem=pager.getCurrentItem();
        welcomePagerAdapter=new WelcomePagerAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(welcomePagerAdapter);
        pager.setCurrentItem(currentItem);
    }
}