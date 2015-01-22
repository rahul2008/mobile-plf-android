package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.customview.FontButton;
import com.philips.cl.di.dev.pa.digitalcare.util.DigiCareContants;

/*
 *	ChatFragment will help to provide options to start Philips chat.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 19 Jan 2014
 */
public class ChatFragment extends BaseFragment {
	private LinearLayout mChatNowParent = null;
	private FrameLayout.LayoutParams mChatNowParentParams = null;
	private FontButton mChatNow = null;
	private ImageView mImageView = null;
	private FrameLayout.LayoutParams mChatNowImageParentParams = null;
	private LinearLayout.LayoutParams mChatNowParentBottom = null;

	private long mWidthLandDefault = 0;
	private long mWidthPortDefault = 0;
	private RelativeLayout mChatNowPort = null;
	private RelativeLayout mChatNowLand = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_chat, container, false);
		mAppObserver.setValue(
				getActivity().getResources().getString(
						R.string.chat_with_philips),
				DigiCareContants.OPTION_NOTHING);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mChatNowParent = (LinearLayout) getActivity().findViewById(
				R.id.chatNowParent);
		mChatNow = (FontButton) getActivity().findViewById(R.id.chatNow);
		mChatNow.setOnClickListener(actionBarClickListener);
		mChatNowParentParams = (FrameLayout.LayoutParams) mChatNowParent
				.getLayoutParams();
		mImageView = (ImageView) getActivity().findViewById(R.id.imageView);
		mChatNowImageParentParams = (FrameLayout.LayoutParams) mImageView
				.getLayoutParams();
		mChatNowPort = (RelativeLayout) getActivity().findViewById(
				R.id.chatNowPort);
		mChatNowLand = (RelativeLayout) getActivity().findViewById(
				R.id.chatNowLand);

		mChatNowParentBottom = (LinearLayout.LayoutParams) mChatNowPort
				.getLayoutParams();

		Configuration config = getResources().getConfiguration();
		setViewParams(config);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		setViewParams(config);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		/*
		 * Updating previous screen(contact us) from here only because at
		 * contact_us screen not receiving any call back.
		 */
		mAppObserver
				.setValue(
						getActivity().getResources().getString(
								R.string.opt_contact_us),
						DigiCareContants.OPTION_NOTHING);
	}

	private OnClickListener actionBarClickListener = new OnClickListener() {

		public void onClick(View view) {
			int id = view.getId();
			switch (id) {
			case R.id.chatNow:
				showFragment(new ChatNowFragment());
				break;
			}
		};
	};

	private void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			// mChatNowPort.setVisibility(View.VISIBLE);
			// mChatNowLand.setVisibility(View.GONE);
			mChatNowParentBottom.leftMargin = mChatNowParentBottom.rightMargin = mLeftRightMarginPort;
		} else {
			mChatNowParentBottom.leftMargin = mChatNowParentBottom.rightMargin = mLeftRightMarginLand;
		}
		mChatNowPort.setLayoutParams(mChatNowParentBottom);
	}
}
