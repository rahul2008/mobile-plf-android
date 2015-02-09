package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.dev.pa.digitalcare.util.FacebookUtility;

/*
 *	FacebookScreenFragment will help to post messages on Philips facebook wall.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 5 Feb 2015
 */
public class FacebookScreenFragment extends BaseFragment {

	private LinearLayout mOptionParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private LinearLayout mFacebookParentPort = null;
	private LinearLayout mFacebookParentLand = null;
	private DigitalCareFontButton popSharePort = null;
	private DigitalCareFontButton popShareLand = null;
	private DigitalCareFontButton popCancelPort = null;
	private DigitalCareFontButton popCancelLand = null;
	private static FacebookUtility mFacebookUtility = null;
	private Bundle mSaveInstanceState = null;
	private View view = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_facebook_screen, container,
				false);

		if (mFacebookUtility == null) {
			mSaveInstanceState = savedInstanceState;
			mFacebookUtility = new FacebookUtility(getActivity(),
					mSaveInstanceState, view);
		}
		return view;
	}

	public void onActivityResultFragment(Activity activity, int requestCode,
			int resultCode, Intent data) {
		if (mFacebookUtility != null) {
			mFacebookUtility.onActivityResultFragment(activity, requestCode,
					resultCode, data);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Resources resource = getActivity().getResources();

		mOptionParent = (LinearLayout) getActivity().findViewById(
				R.id.fbPostContainer);
		mParams = (android.widget.FrameLayout.LayoutParams) mOptionParent
				.getLayoutParams();
		mFacebookParentPort = (LinearLayout) getActivity().findViewById(
				R.id.facebookParentPort);
		mFacebookParentLand = (LinearLayout) getActivity().findViewById(
				R.id.facebookParentLand);
		popCancelPort = (DigitalCareFontButton) view
				.findViewById(R.id.facebookCancelPort);
		popCancelLand = (DigitalCareFontButton) view
				.findViewById(R.id.facebookCancelLand);
		popSharePort = (DigitalCareFontButton) view
				.findViewById(R.id.facebookSendPort);
		popShareLand = (DigitalCareFontButton) view
				.findViewById(R.id.facebookSendLand);

		popSharePort.setOnClickListener(clickListner);
		popShareLand.setOnClickListener(clickListner);
		popCancelPort.setOnClickListener(clickListner);
		popCancelLand.setOnClickListener(clickListner);

		Configuration config = resource.getConfiguration();
		setViewParams(config);
	}

	/**
	 * onPause of fragment.
	 */
	public void onPause() {
		super.onPause();
		if (mFacebookUtility != null) {
			mFacebookUtility.onPause();
		}
	}

	/**
	 * onResume of fragment.
	 */
	public void onDestroy() {
		super.onDestroy();
		if (mFacebookUtility != null) {
			mFacebookUtility.onDestroy();
		}
	}

	/**
	 * onSaveInstanceState fragment.
	 * 
	 * @param outState
	 *            Bundle Object
	 */
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mFacebookUtility != null) {
			mFacebookUtility.onSaveInstanceState(outState);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mFacebookUtility != null) {
			mFacebookUtility.onResume();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);

		setViewParams(config);
	}

	private void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mFacebookParentPort.setVisibility(View.VISIBLE);
			mFacebookParentLand.setVisibility(View.GONE);
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mFacebookParentLand.setVisibility(View.VISIBLE);
			mFacebookParentPort.setVisibility(View.GONE);
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mOptionParent.setLayoutParams(mParams);
	}

	private View.OnClickListener clickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if ((id == R.id.facebookCancelPort)
					|| id == (R.id.facebookCancelLand)) {
				backstackFragment();
			} else if (((id == R.id.facebookSendPort) || (id == R.id.facebookSendLand))
					&& mFacebookUtility != null) {
				mFacebookUtility.performPublishAction();
				// backstackFragment();
			}
		}
	};
}
