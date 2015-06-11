package com.philips.cl.di.digitalcare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
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
	private static final String TAG = DigitalCareActivity.class.getSimpleName();
	private ProductImageHelper mImage = null;
	private static final int DEFAULT_ANIMATION_START = R.anim.slide_in_bottom;
	private static final int DEFAULT_ANIMATION_STOP = R.anim.slide_out_bottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_digi_care);
		try {
			initActionBar();
		} catch (ClassCastException e) {
			DLog.e(TAG, "Actionbar: " + e.getMessage());
		}
		Bundle bundleExtras = getIntent().getExtras();
		int startAnimId = (Integer) bundleExtras.get("STARTANIMATIONID");
		int endAnimId = (Integer) bundleExtras.get("ENDANIMATIONID");
		overridePendingTransition(startAnimId,
				endAnimId);
		showFragment(new SupportHomeFragment());
		enableActionBarHome();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		DLog.i(TAG, "DigitalCareActivity onActivityResult");

		if (requestCode == DigitalCareContants.FACEBOOK_REQUESTC0DE) {

			startFaceBookSDK(requestCode, resultCode, data);

		}
		if (resultCode == Activity.RESULT_CANCELED
				&& requestCode == TwitterAuthentication.WEBVIEW_REQUEST_CODE) {
			stopTwitterSDK();
		}
		if (resultCode == Activity.RESULT_OK
				&& requestCode == TwitterAuthentication.WEBVIEW_REQUEST_CODE) {
			startTwitterSDK(data);
		} else if (resultCode == Activity.RESULT_OK) {
			AnalyticsTracker.trackAction(
					AnalyticsConstants.ACTION_KEY_RECEIPT_PHOTO,
					AnalyticsConstants.ACTION_KEY_PHOTO,
					AnalyticsConstants.ACTION_VALUE_PHOTO_VALUE);

			if (requestCode == DigitalCareContants.IMAGE_PICK) {
				startImageParse(requestCode, data);

			} else if (requestCode == DigitalCareContants.IMAGE_CAPTURE) {
				startImageParse(requestCode, data);
			}
		}
	}

	protected void startTwitterSDK(Intent data) {
		TwitterAuthentication mTwitter = TwitterAuthentication.getInstance();
		mTwitter.onActivityResult(data);
	}

	protected void stopTwitterSDK() {
		TwitterAuthentication mTwitter = TwitterAuthentication.getInstance();
		mTwitter.onFailedToAuthenticate();
	}

	protected void startFaceBookSDK(int requestCode, int resultCode, Intent data) {
		FacebookScreenFragment fbFrag = new FacebookScreenFragment();
		fbFrag.onFaceBookCallback(this, requestCode, resultCode, data);

		FacebookHelper mFacebookHelper = FacebookHelper.getInstance();
		mFacebookHelper.onFaceBookCallback(this, requestCode, resultCode, data);
		AnalyticsTracker.trackAction(
				AnalyticsConstants.ACTION_KEY_RECEIPT_PHOTO,
				AnalyticsConstants.ACTION_KEY_PHOTO,
				AnalyticsConstants.ACTION_VALUE_PHOTO_VALUE);
	}

	protected void startImageParse(int requestCode, Intent data) {

		if (mImage == null)
			mImage = ProductImageHelper.getInstance();

		mImage.processProductImage(data, requestCode);

	}
}
