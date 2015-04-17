package com.philips.cl.di.digitalcare.social.facebook;

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
import android.widget.Toast;

import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.digitalcare.social.PostCallback;
import com.philips.cl.di.digitalcare.social.ProductImageHelper;
import com.philips.cl.di.digitalcare.social.ProductImageResponseCallback;
import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.digitalcare.util.Utils;

/**
 * @description: FacebookScreenFragment will help to post messages on Philips
 *               facebook wall.
 * @author: ritesh.jha@philips.com
 * @since: Feb 5, 2015
 */
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
	private Handler mPostHandler = null;
	private static String mProductInformation = null;

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
		mPostHandler = new Handler();
		mProductInformation = " "
				+ getActivity().getResources().getString(
						R.string.support_productinformation) + " ";
		DLog.d(TAG, "onCreateView");
		return mView;
	}

	public void onFaceBookCallback(Activity activity, int requestCode,
			int resultCode, Intent data) {
		DLog.d(TAG, "onActivity Result received inside the...");
		if (mFacebookUtility != null) {
			DLog.d(TAG,
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
			DLog.d(TAG, "Checked ++");
			mContent = mEditText.getText().toString() + mProductInformation;
			mEditText.setText(mContent);

		} else {
			DLog.d(TAG, "Checked --");
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
		Toast.makeText(getActivity(),
				"Image Path : " + mFile.getAbsolutePath(), Toast.LENGTH_SHORT)
				.show();
		mFacebookUtility.setImageToUpload(image);
		mProductImage.setImageBitmap(image);
		mProductImage.setScaleType(ScaleType.FIT_XY);
		mProductImageClose.setVisibility(View.VISIBLE);
	}

	@Override
	public void onImageDettach() {
		mFile = null;
		DLog.d(TAG, "Product Image Dettached");
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

				if (Utils.isNetworkConnected(getActivity())) {

					mPostProgress = new ProgressDialog(getActivity());
					mPostProgress
							.setMessage(getActivity().getResources().getString(
									R.string.facebook_post_progress_message));
					mPostProgress.setCancelable(false);
					mPostProgress.show();
					mPostHandler.postDelayed(mRunnable, 5000l);
					mFacebookUtility.performPublishAction(mEditText.getText()
							.toString());
				}
			}
		} else if (id == R.id.fb_post_camera) {
			ProductImageHelper.getInstance(getActivity(), this, v).pickImage();
		} else if (id == R.id.fb_Post_camera_close) {
			onImageDettach();
		}
	}
	
	Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			if (mPostProgress != null && mPostProgress.isShowing()) {
				DLog.d(TAG, "5 Seconds finished");
				mPostProgress.setCancelable(true);
			}
		}
	};

	private boolean isDescriptionAvailable() {
		String s = mEditText.getText().toString().trim();
		if (s.equalsIgnoreCase(""))
			return true;
		return false;
	}

	private void sendAlert() {
		Toast.makeText(
				getActivity(),
				getActivity().getResources().getString(
						R.string.social_post_editor_alert), Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.opt_contact_us);
	}

	@Override
	public void setName(String name) {
		String mName = mPostFrom.getText().toString();
		mName = mName + " @" + name;
		mPostFrom.setText(mName);
		DLog.d(TAG, "Callback received");
	}

	@Override
	public void onTaskCompleted() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(
						getActivity(),
						getActivity().getResources().getString(
								R.string.social_post_success),
						Toast.LENGTH_SHORT).show();
				closeProgress();
				backstackFragment();
			}
		});
	}

	@Override
	public void onTaskFailed() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(
						getActivity(),
						getActivity().getResources().getString(
								R.string.social_post_failed),
						Toast.LENGTH_SHORT).show();
				closeProgress();
			}
		});
	}

	private void enableCheckBoxonOpen() {
		mCheckBox.setChecked(true);
	}

	private void closeProgress() {
		if (mPostProgress!=null && mPostProgress.isShowing())
			mPostProgress.dismiss();
	}

}

interface FBAccountCallback {
	void setName(String name);
}
