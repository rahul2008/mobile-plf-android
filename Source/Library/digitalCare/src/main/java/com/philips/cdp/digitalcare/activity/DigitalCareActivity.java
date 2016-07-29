/**
 * DigitalCareActivity is the main container class for Digital Care fragments.
 * Also responsible for fetching Product images, Facebook authentication & also
 * Twitter authentication.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 5 Dec 2014
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.activity;

import android.os.Bundle;
import android.provider.Settings;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.homefragment.SupportHomeFragment;
import com.philips.cdp.digitalcare.social.ProductImageHelper;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;

/*import com.philips.cdp.digitalcare.social.twitter.TwitterAuthentication;*/

/*import com.philips.cdp.digitalcare.social.facebook.FacebookHelper;
 import com.philips.cdp.digitalcare.social.facebook.FacebookScreenFragment;*/


/**
 * The Acitivity Class used while the component used as Activity Invoking.
 */
public class DigitalCareActivity extends DigitalCareBaseActivity {
    private static final String TAG = DigitalCareActivity.class.getSimpleName();
    private static int mEnterAnimation = -1;
    private static int mExitAnimation = -1;

    private ProductImageHelper mImage = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int alwaysFinishActivity = 0;

        if (savedInstanceState != null)
            alwaysFinishActivity = getAnInt(savedInstanceState, "ALWAYS_FINISH_ACTIVITIES");

        setContentView(R.layout.consumercare_activity_digi_care);
        try {
            initActionBar();
        } catch (ClassCastException e) {
            DigiCareLogger.e(TAG, "Actionbar: " + e.getMessage());
        }

        if (alwaysFinishActivity == 0) {
            animateThisScreen();
            showFragment(new SupportHomeFragment());
            enableActionBarHome();
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
       /* DigiCareLogger.i(DigiCareLogger.FRAGMENT, "--> BaseActivity protected onSaveInstanceState");*/
        int alwaysFinishActivity = Settings.System.getInt(getContentResolver(), Settings.System.ALWAYS_FINISH_ACTIVITIES, 0);
        bundle.putInt("ALWAYS_FINISH_ACTIVITIES", alwaysFinishActivity);
    }

    public void animateThisScreen() {
        Bundle bundleExtras = getIntent().getExtras();

        String startAnim = null;
        String endAnim = null;

        int startAnimation = getAnInt(bundleExtras, DigitalCareConstants.START_ANIMATION_ID);
        int endAnimation = getAnInt(bundleExtras, DigitalCareConstants.STOP_ANIMATION_ID);
        int orientation = getAnInt(bundleExtras, DigitalCareConstants.SCREEN_ORIENTATION);

        if (startAnimation == 0 && endAnimation == 0) {
            return;
        }

        startAnim = getResources().getResourceName(startAnimation);
        endAnim = getResources().getResourceName(endAnimation);

        String packageName = getPackageName();
        mEnterAnimation = getApplicationContext().getResources().getIdentifier(startAnim,
                "anim", packageName);
        mExitAnimation = getApplicationContext().getResources().getIdentifier(endAnim, "anim",
                packageName);
        setRequestedOrientation(orientation);
        overridePendingTransition(mEnterAnimation, mExitAnimation);
    }

    private int getAnInt(Bundle bundleExtras, String startAnimationId) {
        return bundleExtras.getInt(startAnimationId);
    }

	/*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		DigiCareLogger.i(TAG, "DigitalCareActivity onActivityResult");

		*//*
         * if (requestCode == DigitalCareContants.FACEBOOK_REQUESTC0DE) {
		 * DigiCareLogger.e(TAG, "Facebook Authentication..");
		 * startFaceBookSDK(requestCode, resultCode, data);
		 * 
		 * }
		 *//*
        if (resultCode == FragmentActivity.RESULT_CANCELED
				&& requestCode == TwitterAuthentication.WEBVIEW_REQUEST_CODE) {
			DigiCareLogger.e(TAG, "Twitter failed to authenticate");
			stopTwitterSDK();
		}
		if (resultCode == FragmentActivity.RESULT_OK
				&& requestCode == TwitterAuthentication.WEBVIEW_REQUEST_CODE) {
			DigiCareLogger.i(TAG, "Twitter Authenticated successfully");
			startTwitterSDK(data);
		} else if (resultCode == FragmentActivity.RESULT_OK) {
			AnalyticsTracker.trackAction(
					AnalyticsConstants.ACTION_RECEIPT_PHOTO,
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
	}*/

	/*
     * protected void startFaceBookSDK(int requestCode, int resultCode, Intent
	 * data) { FacebookScreenFragment fbFrag = new FacebookScreenFragment();
	 * fbFrag.onFaceBookCallback(this, requestCode, resultCode, data);
	 * 
	 * FacebookHelper mFacebookHelper = FacebookHelper.getInstance();
	 * mFacebookHelper.onFaceBookCallback(this, requestCode, resultCode, data);
	 * AnalyticsTracker.trackAction(
	 * AnalyticsConstants.ACTION_KEY_RECEIPT_PHOTO,
	 * AnalyticsConstants.ACTION_KEY_PHOTO,
	 * AnalyticsConstants.ACTION_VALUE_PHOTO_VALUE); }
	 */

	/*protected void startImageParse(int requestCode, Intent data) {

		if (mImage == null)
			mImage = ProductImageHelper.getInstance();

		mImage.processProductImage(data, requestCode);

	}*/
}
