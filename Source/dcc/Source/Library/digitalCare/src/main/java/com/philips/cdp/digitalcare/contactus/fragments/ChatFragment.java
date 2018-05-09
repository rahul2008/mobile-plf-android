/**
 * ChatFragment will help to provide options to start Philips chat.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 19 Jan 2015
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.contactus.fragments;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;

@SuppressWarnings("serial")
public class ChatFragment extends DigitalCareBaseFragment {

    private Button mChatNow = null;
    private Button mChatNowLand = null;
    private FrameLayout.LayoutParams mChatNowBgParams = null;
    private LinearLayout mChatNowParentPort = null;
    private LinearLayout mChatNowParentLand = null;
    private ImageView mChatNowBG = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View view = inflater.inflate(R.layout.consumercare_fragment_chat, container, false);
         initView(view);
         return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Configuration config = getResources().getConfiguration();
        setViewParams(config);

        DigitalCareConfigManager.getInstance().getTaggingInterface().trackPageWithInfo
                (AnalyticsConstants.PAGE_CONTACTUS_LIVECHAT, getPreviousName(), getPreviousName());
    }

    private void initView(View view) {
        mChatNow = (Button) view.findViewById(R.id.chatNow);
        mChatNowLand = (Button) view.findViewById(
                R.id.chatNowLand);
        mChatNowBG = (ImageView) view.findViewById(R.id.chatnow_bg);

        mActionBarMenuIcon = (ImageView) view.findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) view.findViewById(R.id.back_to_home_img);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);

        mChatNow.setOnClickListener(this);
        mChatNowLand.setOnClickListener(this);
        mChatNow.setTransformationMethod(null);
        mChatNowLand.setTransformationMethod(null);
        mChatNowParentPort = (LinearLayout) view.findViewById(
                R.id.chatNowParentPort);
        mChatNowParentLand = (LinearLayout) view.findViewById(
                R.id.chatNowParentLand);
        mChatNowBgParams = (FrameLayout.LayoutParams) mChatNowBG
                .getLayoutParams();
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

    private boolean isTablet() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

        return diagonalInches >= 6.5;
    }

    @Override
    public void setViewParams(Configuration config) {
        mChatNowBG.setBackgroundColor(Color.BLACK);

        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mChatNowParentPort.setVisibility(View.VISIBLE);
            mChatNowParentLand.setVisibility(View.GONE);
            if (isTablet()) {
                mChatNowBG.setBackgroundResource(R.drawable.consumercare_live_chat_bg_tablet_port);
            } else {
                mChatNowBgParams.height = (int) getResources().getDimension(R.dimen.chat_bg_height);
                mChatNowBG.setLayoutParams(mChatNowBgParams);
                mChatNowBG.setBackgroundResource(R.drawable.chat_to_us);
            }
        } else {
            mChatNowParentLand.setVisibility(View.VISIBLE);
            mChatNowParentPort.setVisibility(View.GONE);
            if (isTablet()) {
                mChatNowBG.setBackgroundResource(R.drawable.consumercare_live_chat_bg_tablet_land);
            } else {
                mChatNowBgParams.height = (int) getResources().getDimension(R.dimen.chat_bg_height_land);
                mChatNowBG.setLayoutParams(mChatNowBgParams);
                mChatNowBG.setBackgroundResource(R.drawable.chat_to_us);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.chatNow || id == R.id.chatNowLand) {
            showFragment(new ChatNowFragment());
        }
    }

    @Override
    public String getActionbarTitle() {
        String title = getResources().getString(R.string.live_chat);
        return title;
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_CONTACTUS_LIVECHAT;
    }

}
