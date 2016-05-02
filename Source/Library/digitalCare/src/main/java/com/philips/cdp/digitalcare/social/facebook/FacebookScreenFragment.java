/*package com.philips.cdp.digitalcare.social.facebook;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cdp.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.social.PostCallback;
import com.philips.cdp.digitalcare.social.ProductImageHelper;
import com.philips.cdp.digitalcare.social.ProductImageResponseCallback;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

*//**
 * @description: FacebookScreenFragment will help to post messages on Philips
 *               facebook wall.
 * @author: ritesh.jha@philips.com
 * @since: Feb 5, 2015
 *//*
public class FacebookScreenFragment extends DigitalCareBaseFragment implements
		OnCheckedChangeListener, ProductImageResponseCallback,
		FBAccountCallback, PostCallback {

	private static final String TAG = FacebookScreenFragment.class
			.getSimpleName();
	private LinearLayout mOptionParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private LinearLayout mFacebookParentPort = null;
	private LinearLayout mFacebookParentLand = null;
	private DigitalCareFontButton mPopSharePort = null;
	private DigitalCareFontButton mPopShareLand = null;
	private DigitalCareFontButton mPopCancelPort = null;
	private DigitalCareFontButton mPopCancelLand = null;
	private FacebookUtility mFacebookUtility = null;
	private ImageView mProductImage = null;
	private ImageView mProductImageClose = null;
	private File mFile = null;
	private TextView mPostFrom = null;
	private TextView mPostTo = null;
	private CheckBox mCheckBox = null;
	private EditText mEditText = null;
	private Bundle mSaveInstanceState = null;
	private View mView = null;
	private ProgressDialog mPostProgress = null;
	private static String mProductInformation = null;
	private final long mPostProgressTrack = 1000 * 3l;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.fragment_facebook_screen, container,
				false);

		if (mFacebookUtility == null) {
			mSaveInstanceState = savedInstanceState;
			mFacebookUtility = new FacebookUtility(getActivity(),
					mSaveInstanceState, mView, this, this);
		}
		mProductInformation = " "
				+ getActivity().getResources().getString(
						R.string.support_productinformation) + " ";
		DigiCareLogger.d(TAG, "onCreateView");
		return mView;
	}

	public void onFaceBookCallback(Activity activity, int requestCode,
			int resultCode, Intent data) {
		DigiCareLogger.d(TAG, "onActivity Result received inside the...");
		if (mFacebookUtility != null) {
			DigiCareLogger
					.d(TAG,
							"onActivity Result received when Facebook Utility is not null");
			mFacebookUtility.onActivityResultFragment(activity, requestCode,
					resultCode, data);
		}
	}

	private void setHeaderText() {
		String mLocalizedHeaderText = getActivity().getResources().getString(
				R.string.social_post_to);
		if (mLocalizedHeaderText
				.equalsIgnoreCase("Send a message to @PhilipsCare"))
			mPostTo.setText("Send a message to Philips");

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

		String mContent = null;
		if (isChecked) {
			DigiCareLogger.d(TAG, "Checked ++");
			mContent = mEditText.getText().toString() + mProductInformation;
			mEditText.setText(mContent);

		} else {
			DigiCareLogger.d(TAG, "Checked --");
			mContent = mEditText.getText().toString();
			if (mContent.contains(mProductInformation)) {
				mContent = mContent.replace(mProductInformation, "");
				mEditText.setText(mContent);
			}
		}
	}

	public static final class drawable {
		public static final int ball_red = Color.RED;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Resources resource = getActivity().getResources();

		mOptionParent = (LinearLayout) getActivity().findViewById(
				R.id.fbPostContainer);
		mParams = (FrameLayout.LayoutParams) mOptionParent.getLayoutParams();
		mFacebookParentPort = (LinearLayout) getActivity().findViewById(
				R.id.facebookParentPort);
		mFacebookParentLand = (LinearLayout) getActivity().findViewById(
				R.id.facebookParentLand);
		mPopCancelPort = (DigitalCareFontButton) mView
				.findViewById(R.id.facebookCancelPort);
		mPopCancelLand = (DigitalCareFontButton) mView
				.findViewById(R.id.facebookCancelLand);
		mPopSharePort = (DigitalCareFontButton) mView
				.findViewById(R.id.facebookSendPort);
		mPopShareLand = (DigitalCareFontButton) mView
				.findViewById(R.id.facebookSendLand);
		mCheckBox = (CheckBox) getActivity()
				.findViewById(R.id.fb_Post_CheckBox);

		int id = Resources.getSystem().getIdentifier("btn_check_holo_light",
				"drawable", "android");
		mCheckBox.setButtonDrawable(id);
		mEditText = (EditText) getActivity().findViewById(R.id.share_text);
		mProductImage = (ImageView) getActivity().findViewById(
				R.id.fb_post_camera);
		mPostTo = (TextView) getActivity().findViewById(
				R.id.fb_Post_ToHeaderText);
		mPostFrom = (TextView) getActivity().findViewById(
				R.id.fb_Post_FromHeaderText);
		mProductImageClose = (ImageView) getActivity().findViewById(
				R.id.fb_Post_camera_close);

		mPopSharePort.setOnClickListener(this);
		mPopShareLand.setOnClickListener(this);
		mPopCancelPort.setOnClickListener(this);
		mPopCancelLand.setOnClickListener(this);
		mPopSharePort.setTransformationMethod(null);
		mPopShareLand.setTransformationMethod(null);
		mPopCancelPort.setTransformationMethod(null);
		mPopCancelLand.setTransformationMethod(null);
		mProductImage.setOnClickListener(this);
		mProductImageClose.setOnClickListener(this);
		mCheckBox.setOnCheckedChangeListener(this);
		enableCheckBoxonOpen();
		setHeaderText();
		Configuration config = resource.getConfiguration();
		setViewParams(config);

		AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACTUS_FACEBOOK);
	}

	*//**
	 * onPause of fragment.
	 *//*
	public void onPause() {
		super.onPause();
		if (mFacebookUtility != null) {
			mFacebookUtility.onPause();
		}
	}

	*//**
	 * onResume of fragment.
	 *//*
	public void onDestroy() {
		super.onDestroy();
		if (mFacebookUtility != null) {
			mFacebookUtility.onDestroy();
		}
	}

	*//**
	 * onSaveInstanceState fragment.
	 * 
	 * @param outState
	 *            Bundle Object
	 *//*
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
		ProductImageHelper mProdImageHelper = ProductImageHelper.getInstance();
		if (mProdImageHelper != null)
			mProdImageHelper.resetDialog();
		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {
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

	@Override
	public void onImageReceived(Bitmap image, String Uri) {
		mFile = new File(Uri);
		mFacebookUtility.setImageToUpload(image);
		mProductImage.setImageBitmap(image);
		mProductImage.setScaleType(ScaleType.FIT_XY);
		mProductImageClose.setVisibility(View.VISIBLE);
		DigiCareLogger.d(TAG, "IMAGE RECEIVED : " + mFile.getAbsolutePath());
	}

	@Override
	public void onImageDettached() {
		mFile = null;
		DigiCareLogger.d(TAG, "Product Image Dettached");
		mFacebookUtility.setImageToUpload(null);
		mProductImage.setImageDrawable(getActivity().getResources()
				.getDrawable(R.drawable.social_photo_default));
		mProductImage.setScaleType(ScaleType.FIT_XY);
		mProductImageClose.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.facebookCancelPort || id == R.id.facebookCancelLand) {
			backstackFragment();
		} else if ((id == R.id.facebookSendPort || id == R.id.facebookSendLand)
				&& mFacebookUtility != null) {
			if (isDescriptionAvailable()) {
				sendAlert();
			} else {

				if (isConnectionAvailable()) {

					mPostProgress = new ProgressDialog(getActivity());
					mPostProgress
							.setMessage(getActivity().getResources().getString(
									R.string.facebook_post_progress_message));
					mPostProgress.setCancelable(true);
					mPostProgress.show();
					mFacebookUtility.performPublishAction(mEditText.getText()
							.toString());
				}
			}
		} else if (id == R.id.fb_post_camera) {
			ProductImageHelper.getInstance(getActivity(), this, v).pickImage();
		} else if (id == R.id.fb_Post_camera_close) {
			onImageDettached();
		}
	}

	private boolean isDescriptionAvailable() {
		String s = mEditText.getText().toString().trim();
		if (s.equalsIgnoreCase(""))
			return true;
		return false;
	}

	private void sendAlert() {
		showAlert(getActivity().getResources().getString(
				R.string.social_post_editor_alert));
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.contact_us);
	}

	@Override
	public void setName(String name) {
		String mName = mPostFrom.getText().toString();
		mName = mName + " @" + name;
		mPostFrom.setText(mName);
		DigiCareLogger.d(TAG, "Callback received");
	}

	@Override
	public void onTaskCompleted() {

		updateUi(new Runnable() {
			@Override
			public void run() {
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

	private void enableCheckBoxonOpen() {
		mCheckBox.setChecked(true);
	}

	private void closeProgress() {
		if (mPostProgress != null && mPostProgress.isShowing())
			mPostProgress.dismiss();
	}

}

interface FBAccountCallback {
	void setName(String name);
}
*/