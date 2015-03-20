package com.philips.cl.di.digitalcare.social.facebook;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.customview.DigitalCareFontButton;
import com.philips.cl.di.digitalcare.social.ProductImageHelper;
import com.philips.cl.di.digitalcare.social.ProductImageResponseCallback;
import com.philips.cl.di.digitalcare.util.DLog;

/**
 * @description: FacebookScreenFragment will help to post messages on Philips
 *               facebook wall.
 * @author: ritesh.jha@philips.com
 * @since: Feb 5, 2015
 */
public class FacebookScreenFragment extends DigitalCareBaseFragment implements
		OnCheckedChangeListener, ProductImageResponseCallback,
		FBAccountCallback {

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
	private static FacebookUtility mFacebookUtility = null;
	private ImageView mProductImage = null;
	private ImageView mProductImageClose = null;
	private File mFile = null;
	private TextView mPostFrom = null;
	private CheckBox mCheckBox = null;
	private EditText mEditText = null;
	private Bundle mSaveInstanceState = null;
	private View mView = null;
	private final String PRODCUT_INFO = "The Prduct Description from CDLS";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mView = inflater.inflate(R.layout.fragment_facebook_screen, container,
				false);

		if (mFacebookUtility == null) {
			mSaveInstanceState = savedInstanceState;
			mFacebookUtility = new FacebookUtility(getActivity(),
					mSaveInstanceState, mView, this);
		}
		return mView;
	}

	public void onActivityResultFragment(Activity activity, int requestCode,
			int resultCode, Intent data) {
		if (mFacebookUtility != null) {
			mFacebookUtility.onActivityResultFragment(activity, requestCode,
					resultCode, data);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			DLog.d(TAG, "Checked True");
			DLog.d(TAG, getEditorText().toString());
			mEditText.setText(getEditorText());

		} else {
			DLog.d(TAG, "Checked False");
			DLog.d(TAG, "" + getEditorText().toString());
			mEditText.setText(getEditorText());
		}
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
		mEditText = (EditText) getActivity().findViewById(R.id.share_text);
		mProductImage = (ImageView) getActivity().findViewById(
				R.id.fb_post_camera);
		mPostFrom = (TextView) getActivity().findViewById(
				R.id.fb_Post_FromHeaderText);
		mProductImageClose = (ImageView) getActivity().findViewById(
				R.id.fb_Post_camera_close);

		mPopSharePort.setOnClickListener(this);
		mPopShareLand.setOnClickListener(this);
		mPopCancelPort.setOnClickListener(this);
		mPopCancelLand.setOnClickListener(this);
		mProductImage.setOnClickListener(this);
		mProductImageClose.setOnClickListener(this);
		mCheckBox.setOnCheckedChangeListener(this);
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
		Log.d(TAG, "Product Image Dettached");
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
			mFacebookUtility.performPublishAction(getEditorText());
			// backstackFragment();
		} else if (id == R.id.fb_post_camera) {
			ProductImageHelper.getInstance(getActivity(), this).pickImage();
		} else if (id == R.id.fb_Post_camera_close) {
			onImageDettach();
		}
	}

	public String getEditorText() {
		String mContent = null, mEditorContent = mEditText.getText().toString();

		if (mCheckBox.isChecked()) {
			mContent = PRODCUT_INFO + " " + mEditorContent;
		} else {
			if (mEditorContent.contains(PRODCUT_INFO))
				mContent = mEditorContent.replace(PRODCUT_INFO, "").trim();
			else
				mContent = mEditorContent;

		}

		DLog.d(TAG, "Text in the Content Description" + mContent);
		return mContent;
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.post);
	}

	@Override
	public void setName(String name) {
		String mName = mPostFrom.getText().toString();
		mName = mName + " @" + name;
		mPostFrom.setText(mName);
		DLog.d(TAG, "Callback received");
	}

}

interface FBAccountCallback {
	void setName(String name);
}
