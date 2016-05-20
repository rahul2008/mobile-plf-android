/*
package com.philips.cdp.digitalcare.social.twitter;

import java.io.File;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cdp.digitalcare.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.social.PostCallback;
import com.philips.cdp.digitalcare.social.ProductImageHelper;
import com.philips.cdp.digitalcare.social.ProductImageResponseCallback;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

*/
/**
 * This Screen helps endusers to send the product info/concern along with
 * Product image to the Philips Twitter Support page.
 * 
 * @author naveen@philips.com
 * @since Feb 10, 2015
 *//*

public class TwitterSupportFragment extends DigitalCareBaseFragment implements
		OnCheckedChangeListener, ProductImageResponseCallback, PostCallback,
		TextWatcher {

	private static final String TAG = TwitterSupportFragment.class
			.getSimpleName();
	private String mUsername;
	private View mTwitterView = null;
	private File mFile = null;
	private SharedPreferences mSharedPreferences = null;
	private LinearLayout mFacebookParentPort = null;
	private LinearLayout mFacebookParentLand = null;
	private LinearLayout mContainer = null;
	private DigitalCareFontButton mCancelPort = null;
	private DigitalCareFontButton mSendPort = null;
	private DigitalCareFontButton mCancelLand = null;
	private DigitalCareFontButton mSendLand = null;
	private CheckBox mCheckBox = null;
	private EditText mEditText = null;
	private ImageView mProductImage = null;
	private ImageView mProductCloseButton = null;
	private ImageView mTwitterIcon = null;
	private TextView mCharacterCount = null;
	private ProgressDialog mPostProgress = null;
	private LayoutParams mContainerParams = null;
	private static String mTwitter_to = null;;
	private static String mProductInformation = null;
	private TextView mTweetfrom = null;
	private TextView mTweetTo = null;
	private final int TWITTER_TEXT = 140;
	private final int TWITTER_TEXT_WITH_IMAGE = 117;
	private int mTwitterTextCounter = TWITTER_TEXT;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mTwitterView = inflater.inflate(R.layout.fragment_facebook_screen,
				container, false);
		mSharedPreferences = getPrefrence();
		mUsername = mSharedPreferences.getString(
				TwitterAuthentication.PREF_USER_NAME, "");
		mTwitter_to = getUsername();
		mProductInformation = getProductInformation();
		DigiCareLogger.d(TAG, "Twitter UI Created with Uname value.."
				+ mUsername);
		return mTwitterView;
	}

	protected SharedPreferences getPrefrence() {
		return getActivity().getSharedPreferences(
				TwitterAuthentication.PREF_NAME, 0);
	}

	protected String getUsername() {
		return "@"
				+ getActivity().getResources().getString(R.string.twitter_page)
				+ " ";
	}

	protected String getProductInformation() {
		return getActivity().getResources().getString(
				R.string.support_productinformation)
				+ " "
				+ DigitalCareConfigManager.getInstance()
						.getConsumerProductInfo().getProductTitle()
				+ " "
				+ DigitalCareConfigManager.getInstance()
						.getConsumerProductInfo().getCtn();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Resources mResources = getActivity().getResources();

		mContainer = (LinearLayout) getActivity().findViewById(
				R.id.fbPostContainer);
		mContainerParams = (android.widget.FrameLayout.LayoutParams) mContainer
				.getLayoutParams();

		mFacebookParentPort = (LinearLayout) getActivity().findViewById(
				R.id.facebookParentPort);
		mFacebookParentLand = (LinearLayout) getActivity().findViewById(
				R.id.facebookParentLand);

		mCancelPort = (DigitalCareFontButton) getActivity().findViewById(
				R.id.facebookCancelPort);
		mSendPort = (DigitalCareFontButton) getActivity().findViewById(
				R.id.facebookSendPort);
		mCancelLand = (DigitalCareFontButton) getActivity().findViewById(
				R.id.facebookCancelLand);
		mSendLand = (DigitalCareFontButton) getActivity().findViewById(
				R.id.facebookSendLand);
		mTweetfrom = (TextView) getActivity().findViewById(
				R.id.fb_Post_FromHeaderText);
		mTweetTo = (TextView) getActivity().findViewById(
				R.id.fb_Post_ToHeaderText);
		mCharacterCount = (TextView) getActivity().findViewById(
				R.id.fb_post_textCount);
		mTwitterIcon = (ImageView) getActivity().findViewById(
				R.id.socialLoginIcon);
		mCheckBox = (CheckBox) getActivity()
				.findViewById(R.id.fb_Post_CheckBox);
		int id = Resources.getSystem().getIdentifier("btn_check_holo_light",
				"drawable", "android");
		mCheckBox.setButtonDrawable(id);
		mEditText = (EditText) getActivity().findViewById(R.id.share_text);
		mProductImage = (ImageView) getActivity().findViewById(
				R.id.fb_post_camera);
		mProductCloseButton = (ImageView) getActivity().findViewById(
				R.id.fb_Post_camera_close);

		mCancelPort.setOnClickListener(this);
		mCancelPort.setTransformationMethod(null);
		mSendPort.setOnClickListener(this);
		mSendPort.setTransformationMethod(null);
		mCancelLand.setOnClickListener(this);
		mCancelLand.setTransformationMethod(null);
		mSendLand.setOnClickListener(this);
		mSendLand.setTransformationMethod(null);
		mProductImage.setOnClickListener(this);
		mProductCloseButton.setOnClickListener(this);
		mCheckBox.setOnCheckedChangeListener(this);
		setLimitToEditText(mEditText, TWITTER_TEXT);
		mEditText.addTextChangedListener(this);
		enableCheckBoxonOpen();
		Configuration mConfig = mResources.getConfiguration();
		setViewParams(mConfig);
		mTweetTo.setText(mTweetTo.getText() + " @"
				+ getActivity().getString(R.string.twitter_page));
		AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACTUS_TWITTER,
				getPreviousName());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		DigiCareLogger.d(TAG, "Configuration Changed");
		ProductImageHelper mProdImageHelper = ProductImageHelper.getInstance();
		if (mProdImageHelper != null)
			mProdImageHelper.resetDialog();
		setViewParams(newConfig);
	}

	private void tweetMessage() {
		new TweetPost(getActivity().getBaseContext(), mFile, this, mEditText
				.getText().toString()).execute();
		mPostProgress = new ProgressDialog(getActivity());
		mPostProgress.setMessage(getActivity().getResources().getString(
				R.string.twitter_post_progress_message));
		mPostProgress.setCancelable(false);
		mPostProgress.show();
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String mContent = null;

		if (isChecked) {
			mContent = mEditText.getText().toString() + " "
					+ mProductInformation;
			if (mContent.length() < mTwitterTextCounter) {
				mEditText.setText(mContent);
				mEditText.setSelection(mEditText.getText().toString().length());
			} else {
				showAlert(getActivity().getResources().getString(
						R.string.twitter_post_char_limitation));
				mCheckBox.setChecked(false);
			}
		} else {
			mContent = mEditText.getText().toString();
			if (mContent.contains(mProductInformation)) {
				mContent = mContent.replace(mProductInformation, "").trim();
			}
			mEditText.setText(mContent);
			mEditText.setSelection(mEditText.getText().toString().length());
		}
	}

	@Override
	public void onClick(View v) {

		int id = v.getId();
		if (id == R.id.facebookCancelPort) {
			backstackFragment();
		} else if (id == R.id.facebookSendPort) {
			if (isDescriptionAvailable()) {
				sendNoDescriptionAlert();
			} else {
				if (isConnectionAvailable())
					tweetMessage();
			}
		} else if (id == R.id.facebookCancelLand) {
			backstackFragment();
		} else if (id == R.id.facebookSendLand) {
			if (isDescriptionAvailable()) {
				sendNoDescriptionAlert();
			} else {
				if (isConnectionAvailable())
					tweetMessage();
			}
		} else if (id == R.id.fb_Post_CheckBox) {
		} else if (id == R.id.fb_post_camera) {
			ProductImageHelper.getInstance(getActivity(), this, v).pickImage();
		} else if (id == R.id.fb_Post_camera_close) {
			onImageDettached();
		}
	}

	private boolean isDescriptionAvailable() {
		String s = mEditText.getText().toString();
		s = s.trim();
		s = s.toString() + " ";
		if (s.equalsIgnoreCase(mTwitter_to))
			return true;
		return false;
	}

	private void sendNoDescriptionAlert() {
		showAlert(getActivity().getResources().getString(
				R.string.social_post_editor_alert));
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {

		int mPost_AddressLength = mTwitter_to.length();
		String mTwitterMessageContent = mEditText.getText().toString();

		StringBuilder mTwitterAddressBuilder = new StringBuilder(
				mTwitterMessageContent);
		mTwitterAddressBuilder.replace(0, mPost_AddressLength, mTwitter_to);
		DigiCareLogger.d(TAG, "Twitter String : [" + mTwitterAddressBuilder
				+ "]");

		if (!(s.toString().startsWith(mTwitter_to, 0))) {
			DigiCareLogger.d(TAG,
					"String from the Character S : " + s.toString());
			mEditText.setText(mTwitterAddressBuilder);
		}

		if (s.length() <= mTwitterTextCounter) {
			int mTextCounter = mTwitterTextCounter - s.length();
			mCharacterCount.setText(String.valueOf(mTextCounter));
		}
	}

	@Override
	public void setViewParams(Configuration config) {
		configureValues();
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mFacebookParentPort.setVisibility(View.VISIBLE);
			mFacebookParentLand.setVisibility(View.GONE);
			mContainerParams.leftMargin = mContainerParams.rightMargin = mLeftRightMarginPort;
		} else {
			mFacebookParentLand.setVisibility(View.VISIBLE);
			mFacebookParentPort.setVisibility(View.GONE);
			mContainerParams.leftMargin = mContainerParams.rightMargin = mLeftRightMarginLand;
		}
		mContainer.setLayoutParams(mContainerParams);
	}

	private void configureValues() {
		mTweetfrom.setText("From @" + mUsername);
		mTwitterIcon.setImageResource(R.drawable.consumercare_social_twitter_icon);
	}

	@Override
	public void onImageReceived(Bitmap image, String Uri) {
		DigiCareLogger.v(TAG, "IMage received URI : " + Uri);
		if (Uri != null) {
			mFile = new File(Uri);
			DigiCareLogger
					.d(TAG, "IMAGE RECEIVED : " + mFile.getAbsolutePath());
			mProductImage.setImageBitmap(image);
			mProductImage.setScaleType(ScaleType.FIT_XY);
			mProductCloseButton.setVisibility(View.VISIBLE);
			setLimitToEditText(mEditText, TWITTER_TEXT_WITH_IMAGE);
			mTwitterTextCounter = TWITTER_TEXT_WITH_IMAGE;

			if (getCharacterCount() <= mTwitterTextCounter) {
				int mTextCounter = mTwitterTextCounter - getCharacterCount();
				mCharacterCount.setText(String.valueOf(mTextCounter));
			}
		}
	}

	private int getCharacterCount() {
		return mEditText.getText().toString().length();
	}

	@Override
	public void onImageDettached() {
		mFile = null;
		mProductImage.setImageDrawable(getActivity().getResources()
				.getDrawable(R.drawable.social_photo_default));
		mProductImage.setScaleType(ScaleType.FIT_CENTER);
		mProductCloseButton.setVisibility(View.GONE);
		setLimitToEditText(mEditText, TWITTER_TEXT);
		mTwitterTextCounter = TWITTER_TEXT;
		if (getCharacterCount() <= mTwitterTextCounter) {
			int mTextCounter = mTwitterTextCounter - getCharacterCount();
			mCharacterCount.setText(String.valueOf(mTextCounter));
		}
	}

	@Override
	public void onTaskCompleted() {

		updateUi(new Runnable() {
			@Override
			public void run() {
				String socialType = "Twitter";
				AnalyticsTracker.trackAction(
						AnalyticsConstants.ACTION_SOCIAL_SHARE,
						AnalyticsConstants.ACTION_KEY_SOCIAL_TYPE, socialType);
				showAlert(getActivity().getResources().getString(
						R.string.social_post_success));
				closeProgress();
				backstackFragment();
			}
		});
	}

	@Override
	public void onTaskFailed() {

		updateUi(new Runnable() {
			@Override
			public void run() {
				showAlert(getActivity().getResources().getString(
						R.string.social_post_failed));
				closeProgress();
			}
		});
	}

	private void closeProgress() {
		if (mPostProgress != null && mPostProgress.isShowing()) {
			mPostProgress.dismiss();
			mPostProgress = null;
		}
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.contact_us);
	}

	private void setLimitToEditText(EditText editText, int limit) {

		String mExisting = editText.getText().toString();
		editText.setText(null);
		editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				limit) });
		editText.setText(mExisting);
		if (!(editText.getText().toString().contains(mTwitter_to)))
			editText.setText(mTwitter_to);
	}

	private void enableCheckBoxonOpen() {
		mCheckBox.setChecked(true);
	}

	@Override
	public String setPreviousPageName() {
		return AnalyticsConstants.PAGE_CONTACTUS_TWITTER;
	}
}
*/
