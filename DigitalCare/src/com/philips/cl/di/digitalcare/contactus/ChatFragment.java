package com.philips.cl.di.digitalcare.contactus;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontButton;

/**
 *	ChatFragment will help to provide options to start Philips chat.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 19 Jan 2015
 */
public class ChatFragment extends DigitalCareBaseFragment {
	private DigitalCareFontButton mChatNow = null;
	private DigitalCareFontButton mChatNowLand = null;

	private DigitalCareFontButton mChatNoThanks = null;
	private DigitalCareFontButton mChatNoThanksLand = null;

	// private ImageView mImageView = null;
	private LinearLayout.LayoutParams mChatNowParentBottom = null;

	private LinearLayout mChatNowParentPort = null;
	private LinearLayout mChatNowParentLand = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat, container, false);
		return view;
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
		Configuration config = getResources().getConfiguration();
		setViewParams(config);
		
		AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACTUS_LIVECHAT);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mChatNowParentPort.setVisibility(View.VISIBLE);
			mChatNowParentLand.setVisibility(View.GONE);
			mChatNowParentBottom.leftMargin = mChatNowParentBottom.rightMargin = mLeftRightMarginPort;
		} else {
			mChatNowParentLand.setVisibility(View.VISIBLE);
			mChatNowParentPort.setVisibility(View.GONE);
			mChatNowParentBottom.leftMargin = mChatNowParentBottom.rightMargin = mLeftRightMarginLand;
		}
		mChatNowParentPort.setLayoutParams(mChatNowParentBottom);
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
}
