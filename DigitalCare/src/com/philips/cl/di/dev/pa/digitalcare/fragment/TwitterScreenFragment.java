package com.philips.cl.di.dev.pa.digitalcare.fragment;

import java.io.File;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.dev.pa.digitalcare.R;
import com.philips.cl.di.dev.pa.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.dev.pa.digitalcare.twitter.TwitterConnect;
import com.philips.cl.di.dev.pa.digitalcare.util.ALog;
import com.philips.cl.di.dev.pa.digitalcare.util.Callback;
import com.philips.cl.di.dev.pa.digitalcare.util.FragmentUtility;
import com.philips.cl.di.dev.pa.digitalcare.util.ProductImage;

public class TwitterScreenFragment extends BaseFragment implements
		FragmentUtility, OnCheckedChangeListener, Callback {

	private static final String TAG = TwitterScreenFragment.class
			.getSimpleName();
	private String mUsername;
	private View mTwitterView = null;
	private Callback mCallback = this;
	private File mFile = null;
	private SharedPreferences mSharedPreferences = null;

	private LinearLayout mContainer = null;
	private LinearLayout  mTwitterPort = null;
	private LinearLayout mTwitterLand = null;
	private DigitalCareFontButton mCancelPort = null;
	private DigitalCareFontButton mCancelLand = null;
	private DigitalCareFontButton mSendPort = null;
	private DigitalCareFontButton mSendLand = null;
	private CheckBox mCheckBox = null;
	private EditText mProdInformation = null;
	private ImageView mProductImage = null;
	private ImageView mProductCloseButton = null;

	private LayoutParams mContainerParams = null;

	// Configurable parameters
	private TextView mTweetfrom = null;
	private ImageView mTwitterIcon = null;
	private final String DESCRIPTION = "@PhilipsCare can you help me with my Airfryer HD9220/20 I think it is broken. Nulllaaaaaaaa";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mTwitterView = inflater.inflate(R.layout.fragment_facebook_screen,
				container, false);
		mSharedPreferences = getActivity().getSharedPreferences(
				TwitterConnect.PREF_NAME, 0);
		mUsername = mSharedPreferences.getString(TwitterConnect.PREF_USER_NAME,
				"");
		ALog.d(TAG, "Twitter UI Created with Uname value.." + mUsername);
		return mTwitterView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Resources mResources = getActivity().getResources();

		mContainer = (LinearLayout) getActivity().findViewById(
				R.id.fbPostContainer);
		mContainerParams = (android.widget.FrameLayout.LayoutParams) mContainer
				.getLayoutParams();

		mTwitterPort = (LinearLayout) getActivity().findViewById(
				R.id.facebookParentPort);
		mTwitterLand = (LinearLayout) getActivity().findViewById(
				R.id.facebookParentLand);

		mCancelPort = (DigitalCareFontButton) getActivity().findViewById(
				R.id.facebookCancelPort);
		mCancelLand = (DigitalCareFontButton) getActivity().findViewById(
				R.id.facebookCancelLand);

		mSendPort = (DigitalCareFontButton) getActivity().findViewById(
				R.id.facebookSendPort);

		mSendLand = (DigitalCareFontButton) getActivity().findViewById(
				R.id.facebookSendLand);

		mTweetfrom = (TextView) getActivity().findViewById(
				R.id.fb_Post_FromHeaderText);

		mTwitterIcon = (ImageView) getActivity().findViewById(
				R.id.socialLoginIcon);

		mCheckBox = (CheckBox) getActivity()
				.findViewById(R.id.fb_Post_CheckBox);

		mProdInformation = (EditText) getActivity().findViewById(
				R.id.share_text);

		mProductImage = (ImageView) getActivity().findViewById(
				R.id.fb_post_camera);
		mProductCloseButton = (ImageView) getActivity().findViewById(
				R.id.fb_Post_camera_close);

		mCancelPort.setOnClickListener(this);
		mCancelLand.setOnClickListener(this);
		mSendPort.setOnClickListener(this);
		mSendLand.setOnClickListener(this);
		mCheckBox.setOnCheckedChangeListener(this);
		mProductImage.setOnClickListener(this);
		mProductCloseButton.setOnClickListener(this);

		Configuration mConfig = mResources.getConfiguration();
		setViewParams(mConfig);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		ALog.d(TAG, "Configuration Changed");
		setViewParams(newConfig);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.facebookCancelPort:
			break;
		case R.id.facebookCancelLand:

			break;

		case R.id.facebookSendLand:
			break;
		case R.id.facebookSendPort:

			break;

		case R.id.fb_Post_CheckBox:

			break;
		case R.id.fb_post_camera:
			ProductImage.getInstance(getActivity(), mCallback).pickImage();
			break;
		case R.id.fb_Post_camera_close:
			onImageDettach();
			break;

		default:
			break;
		}
	}

	@Override
	public void setViewParams(Configuration config) {
		configureValues();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			ALog.d(TAG, "PORTRAIT Orientation");
			mTwitterPort.setVisibility(View.VISIBLE);
			mTwitterLand.setVisibility(View.GONE);
			mContainerParams.leftMargin = mContainerParams.rightMargin = mLeftRightMarginPort;
		} else {
			ALog.d(TAG, "Horizontal Orientaton");
			mTwitterLand.setVisibility(View.VISIBLE);
			mTwitterPort.setVisibility(View.GONE);
			mContainerParams.leftMargin = mContainerParams.rightMargin = mLeftRightMarginLand;
		}

		mContainer.setLayoutParams(mContainerParams);

	}

	private void configureValues() {
		mTweetfrom.setText("From @" + mUsername);
		mTwitterIcon.setImageResource(R.drawable.social_twitter_icon);
		mCheckBox.setChecked(true);
		mProdInformation.setHint("");
		mProdInformation.setText(DESCRIPTION);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		if (isChecked) {
			mProdInformation.setText(DESCRIPTION);
		} else {
			mProdInformation.setText("");
		}
	}

	@Override
	public void onImageReceived(Bitmap image, String Uri) {
		mFile = new File(Uri);
		Toast.makeText(getActivity(), "Image Path : "+mFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
		mProductImage.setImageBitmap(image);
		mProductImage.setScaleType(ScaleType.FIT_XY);
		mProductCloseButton.setVisibility(View.VISIBLE);

	}

	@Override
	public void onImageDettach() {
		mFile = null;
		Log.d(TAG, "Product Image Dettached");
		mProductImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.social_photo_default));
		mProductImage.setScaleType(ScaleType.FIT_XY);
		mProductCloseButton.setVisibility(View.GONE);

	}

}
