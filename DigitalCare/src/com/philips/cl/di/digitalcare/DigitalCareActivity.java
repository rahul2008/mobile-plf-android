package com.philips.cl.di.digitalcare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.philips.cl.di.digitalcare.social.ProductImageHelper;
import com.philips.cl.di.digitalcare.social.facebook.FacebookHelper;
import com.philips.cl.di.digitalcare.social.facebook.FacebookScreenFragment;
import com.philips.cl.di.digitalcare.social.twitter.TwitterAuthentication;
import com.philips.cl.di.digitalcare.util.DLog;
import com.philips.cl.di.digitalcare.util.DigitalCareContants;

/**
 * DigitalCareActivity is the main container class for Digital Care fragments.
 * Also responsible for fetching Product images, Facebook authentication & also
 * Twitter authentication.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 5 Dec 2014
 */
public class DigitalCareActivity extends DigitalCareBaseActivity {
	private static final String TAG = "DigitalCareActivity";
	private ProductImageHelper mImage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_digi_care);
		try {
			initActionBar();
		} catch (ClassCastException e) {
			DLog.e(TAG, "Actionbar: " + e.getMessage());
		}
		showFragment(new SupportHomeFragment());
		enableActionBarHome();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		DLog.i(TAG, "DigitalCareActivity onActivityResult");
		DLog.i(TAG, "DigitalCareActivity Request : " + requestCode);
		DLog.i(TAG, "DigitalCareActivity response :" + resultCode);
		DLog.i(TAG, "DigitalCareActivity data : " + data);

		if (requestCode == 64206) {
			FacebookScreenFragment fbFrag = new FacebookScreenFragment();
			fbFrag.onFaceBookCallback(this, requestCode, resultCode, data);

			FacebookHelper mFacebookHelper = FacebookHelper.getInstance();
			mFacebookHelper.onFaceBookCallback(this, requestCode, resultCode, data);

		}
		if (resultCode == Activity.RESULT_CANCELED
				&& requestCode == TwitterAuthentication.WEBVIEW_REQUEST_CODE) {
			TwitterAuthentication mTwitter = TwitterAuthentication
					.getInstance();
			mTwitter.onFailedToAuthenticate();
		}
		if (resultCode == Activity.RESULT_OK
				&& requestCode == TwitterAuthentication.WEBVIEW_REQUEST_CODE) {
			TwitterAuthentication mTwitter = TwitterAuthentication
					.getInstance();
			mTwitter.onActivityResult(data);
		}

		if (requestCode == DigitalCareContants.IMAGE_PICK
				&& resultCode == Activity.RESULT_OK) {
			if (mImage == null)
				mImage = ProductImageHelper.getInstance();

			mImage.processProductImage(data, requestCode);
		}

		if (requestCode == DigitalCareContants.IMAGE_CAPTURE
				&& resultCode == Activity.RESULT_OK) {
			if (mImage == null)
				mImage = ProductImageHelper.getInstance();
			mImage.processProductImage(data, requestCode);
		}
	}
}
