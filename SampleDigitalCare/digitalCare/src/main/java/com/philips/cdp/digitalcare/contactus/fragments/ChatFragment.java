package com.philips.cdp.digitalcare.contactus.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.digitalcare.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;

/**
 * ChatFragment will help to provide options to start Philips chat.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 19 Jan 2015
 */
public class ChatFragment extends DigitalCareBaseFragment {
    private static View mView = null;
    private Button mChatNow = null;
    private Button mChatNowLand = null;
    private Button mChatNoThanks = null;
    private Button mChatNoThanksLand = null;
    private LinearLayout.LayoutParams mChatNowParentBottom = null;
    private LinearLayout.LayoutParams mHelpTextParams = null;
    private LinearLayout mChatNowParentPort = null;
    private LinearLayout mChatNowParentLand = null;
    private TextView mHelpText = null;
    private ImageView mChatNowBG = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;


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

        mHelpText = (TextView) getActivity().findViewById(R.id.chatDesc);

        mChatNowBG = (ImageView) getActivity().findViewById(R.id.chatnow_bg);

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);

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
        mHelpTextParams = (LinearLayout.LayoutParams) mHelpText
                .getLayoutParams();
        setButtonParams();
        Configuration config = getResources().getConfiguration();
        setViewParams(config);

        AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACTUS_LIVECHAT, getPreviousName());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mActionBarMenuIcon != null && mActionBarArrow != null)
            if (mActionBarMenuIcon.getVisibility() == View.VISIBLE)
                enableActionBarLeftArrow();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        setViewParams(config);
    }

    @Override
    public void setViewParams(Configuration config) {
        float density = getResources().getDisplayMetrics().density;

        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mChatNowParentPort.setVisibility(View.VISIBLE);
            mChatNowParentLand.setVisibility(View.GONE);

        } else {
            mChatNowParentLand.setVisibility(View.VISIBLE);
            mChatNowParentPort.setVisibility(View.GONE);
        }
        mHelpText.setLayoutParams(mHelpTextParams);
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

    private void enableActionBarLeftArrow() {
        mActionBarMenuIcon.setVisibility(View.GONE);
        mActionBarArrow.setVisibility(View.VISIBLE);
        mActionBarArrow.bringToFront();
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
