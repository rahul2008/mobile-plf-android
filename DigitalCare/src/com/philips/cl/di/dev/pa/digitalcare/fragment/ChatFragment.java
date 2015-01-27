package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
//	private LinearLayout mChatNowParent = null;
	private FontButton mChatNow = null;
	private FontButton mChatNowLand = null;
//	private ImageView mImageView = null;
	private LinearLayout.LayoutParams mChatNowParentBottom = null;

	private LinearLayout mChatNowParentPort = null;
	private LinearLayout mChatNowParentLand = null;

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
		// mChatNowParent = (LinearLayout) getActivity().findViewById(
		// R.id.chatNowParent);
		mChatNow = (FontButton) getActivity().findViewById(R.id.chatNow);
		mChatNow.setOnClickListener(actionBarClickListener);

		mChatNowLand = (FontButton) getActivity()
				.findViewById(R.id.chatNowLand);
		mChatNowLand.setOnClickListener(actionBarClickListener);

//		mImageView = (ImageView) getActivity().findViewById(R.id.imageView);
		mChatNowParentPort = (LinearLayout) getActivity().findViewById(
				R.id.chatNowParentPort);
		mChatNowParentLand = (LinearLayout) getActivity().findViewById(
				R.id.chatNowParentLand);

		mChatNowParentBottom = (LinearLayout.LayoutParams) mChatNowParentPort
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
			if (id == R.id.chatNow || id == R.id.chatNowLand) {
				showFragment(new ChatNowFragment());
			}
		};
	};

	private void setViewParams(Configuration config) {
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
}
