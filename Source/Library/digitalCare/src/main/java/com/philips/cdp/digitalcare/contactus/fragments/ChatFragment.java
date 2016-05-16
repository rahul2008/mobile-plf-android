/**
 * ChatFragment will help to provide options to start Philips chat.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 19 Jan 2015
 * Copyright (c) 2016 Philips. All rights reserved.
 *
 */
package com.philips.cdp.digitalcare.contactus.fragments;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;


public class ChatFragment extends DigitalCareBaseFragment {
    private static View mView = null;
    private Button mChatNow = null;
    private Button mChatNowLand = null;
    private Button mChatNoThanks = null;
    private Button mChatNoThanksLand = null;
    private LinearLayout.LayoutParams mChatNowParentBottom = null;
    private LinearLayout.LayoutParams mHelpTextParams = null;
    private FrameLayout.LayoutParams mChatNowBgParams = null;
    private LinearLayout mChatNowParentPort = null;
    private LinearLayout mChatNowParentLand = null;
    private TextView mChatDescText = null;
    private TextView mHelpText = null;
    private ImageView mChatNowBG = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private ScrollView mChatScrollView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        try {
            mView = inflater.inflate(R.layout.fragment_chat, container, false);
        } catch (InflateException e) {
        }

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // mChatNowParent = (LinearLayout) getActivity().findViewById(
        // R.id.chatNowParent);
        mChatNow = (DigitalCareFontButton) getActivity().findViewById(
                R.id.chatNow);
        mChatNowLand = (DigitalCareFontButton) getActivity().findViewById(
                R.id.chatNowLand);

        mChatNoThanks = (DigitalCareFontButton) getActivity().findViewById(
                R.id.chatNoThanks);
        mChatNoThanksLand = (DigitalCareFontButton) getActivity().findViewById(
                R.id.chatNoThanksLand);

        mChatDescText = (TextView) getActivity().findViewById(R.id.chatDesc);
        mHelpText = (TextView) getActivity().findViewById(R.id.helpText);

        mChatNowBG = (ImageView) getActivity().findViewById(R.id.chatnow_bg);

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);

        mChatScrollView = (ScrollView) getActivity().findViewById(R.id.chatScrollView);

        mChatNow.setOnClickListener(this);
        mChatNowLand.setOnClickListener(this);
        mChatNoThanks.setOnClickListener(this);
        mChatNoThanksLand.setOnClickListener(this);

        mChatNow.setTransformationMethod(null);
        mChatNowLand.setTransformationMethod(null);
        mChatNoThanks.setTransformationMethod(null);
        mChatNoThanksLand.setTransformationMethod(null);

        // mImageView = (ImageView) getActivity().findViewById(R.id.imageView);
        mChatNowParentPort = (LinearLayout) getActivity().findViewById(
                R.id.chatNowParentPort);
        mChatNowParentLand = (LinearLayout) getActivity().findViewById(
                R.id.chatNowParentLand);

        mChatNowParentBottom = (LinearLayout.LayoutParams) mChatNowParentPort
                .getLayoutParams();
        mHelpTextParams = (LinearLayout.LayoutParams) mChatDescText
                .getLayoutParams();
        mChatNowBgParams = (FrameLayout.LayoutParams) mChatNowBG
                .getLayoutParams();
        setButtonParams();
        Configuration config = getResources().getConfiguration();
        setViewParams(config);

        AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACTUS_LIVECHAT, getPreviousName());
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        setViewParams(config);
    }

    private boolean isTablet(){
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float yInches= metrics.heightPixels/metrics.ydpi;
        float xInches= metrics.widthPixels/metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches*xInches + yInches*yInches);

        if (diagonalInches>=6.5){
            // 6.5inch device or bigger
            return true;
        }
        return false;
    }

    @Override
    public void setViewParams(Configuration config) {
        //This is required to release the older live_chat_bg background drawable.
        mChatNowBG.setBackgroundColor(Color.BLACK);

        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mChatNowParentPort.setVisibility(View.VISIBLE);
            mChatNowParentLand.setVisibility(View.GONE);
            mHelpText.setPadding((int) getResources().getDimension(R.dimen.activity_margin), 0, (int) getResources().getDimension(R.dimen.chatnowhelptext_padding_right), 0);

            if(isTablet()){
                mChatNowBG.setBackgroundResource(R.drawable.live_chat_bg_tablet_port);
            }
            else{
                mChatNowBgParams.height = (int) getResources().getDimension(R.dimen.chat_bg_height);
                mChatNowBG.setLayoutParams(mChatNowBgParams);
                mChatNowBG.setBackgroundResource(R.drawable.live_chat_bg_phone_port);
            }
        } else {
            mChatNowParentLand.setVisibility(View.VISIBLE);
            mChatNowParentPort.setVisibility(View.GONE);
            mHelpText.setPadding((int) getResources().getDimension(R.dimen.activity_margin), 0, (int) getResources().getDimension(R.dimen.chatnowhelptext_padding_right_land), 0);
//            mChatScrollView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    mChatScrollView.scrollTo(0, 270);
//                }
//            }, 100);
            if(isTablet()){
                mChatNowBG.setBackgroundResource(R.drawable.live_chat_bg_tablet_land);
            }
            else{
                mChatNowBgParams.height = (int) getResources().getDimension(R.dimen.chat_bg_height_land);
                mChatNowBG.setLayoutParams(mChatNowBgParams);
                mChatNowBG.setBackgroundResource(R.drawable.live_chat_bg_phone_land);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.chatNow || id == R.id.chatNowLand) {
            showFragment(new ChatNowFragment());
        } else if (id == R.id.chatNoThanks || id == R.id.chatNoThanksLand) {
            backstackFragment();
        }
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.chat_with_philips);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_CONTACTUS_LIVECHAT;
    }


    private void setButtonParams() {
        float density = getResources().getDisplayMetrics().density;
        LinearLayout.LayoutParams chatbuttonparams = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));
        chatbuttonparams.leftMargin = chatbuttonparams.rightMargin = (int) getActivity().getResources().getDimension(R.dimen.activity_margin);
        chatbuttonparams.topMargin = chatbuttonparams.bottomMargin = (int) getActivity().getResources().getDimension(R.dimen.chat_button_top_margin);
        ;
        chatbuttonparams.weight = 1;
        //chat button params
        mChatNow.setLayoutParams(chatbuttonparams);
        mChatNowLand.setLayoutParams(chatbuttonparams);

        //cancel button params
        RelativeLayout.LayoutParams cancelbuttonparams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));

        ((ViewGroup) mChatNoThanks.getParent()).setLayoutParams(chatbuttonparams);
        ((ViewGroup) mChatNoThanksLand.getParent()).setLayoutParams(chatbuttonparams);

        mChatNoThanks.setLayoutParams(cancelbuttonparams);
        mChatNoThanksLand.setLayoutParams(cancelbuttonparams);
    }
}
