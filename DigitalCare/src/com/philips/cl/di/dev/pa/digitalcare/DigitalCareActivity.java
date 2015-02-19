package com.philips.cl.di.dev.pa.digitalcare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.philips.cl.di.dev.pa.digitalcare.fragment.FacebookScreenFragment;
import com.philips.cl.di.dev.pa.digitalcare.fragment.SupportHomeFragment;
import com.philips.cl.di.dev.pa.digitalcare.social.TwitterConnect;
import com.philips.cl.di.dev.pa.digitalcare.util.ALog;
import com.philips.cl.di.dev.pa.digitalcare.util.DigiCareContants;
import com.philips.cl.di.dev.pa.digitalcare.util.ProductImage;

/*
 *	DigitalCareActivity  is the main container class for Digital Care fragments. 
 *  Also responsible for fetching Product images, Facebook authentication & also 
 *  Twitter authentication .
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 5 Dec 2015
 */
public class DigitalCareActivity extends DigitalCareBaseActivity {
	private static final String TAG = "DigitalCareActivity";
	private ProductImage mImage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DigitalCareApplication.getDigitalCareInstance(this);
		setContentView(R.layout.activity_digi_care);
		try {
			initActionBar();
		} catch (ClassCastException e) {
			ALog.e(TAG, "Actionbar: " + e.getMessage());
		}
		showFragment(new SupportHomeFragment());
		enableActionBarHome();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		ALog.i(TAG, "DigitalCareActivity onActivityResult");
		FacebookScreenFragment fbFrag = new FacebookScreenFragment();
		fbFrag.onActivityResultFragment(this, requestCode, resultCode, data);
		
		if (resultCode == Activity.RESULT_OK
				&& requestCode == TwitterConnect.WEBVIEW_REQUEST_CODE) {
			
			
			TwitterConnect mTwitter = TwitterConnect.getInstance();
			mTwitter.onActivityResult(data);
		}
		
		if (requestCode == DigiCareContants.IMAGE_PICK
				&& resultCode == Activity.RESULT_OK) {
             if (mImage == null)
		 	     mImage = ProductImage.getInstance();
             
			mImage.onActivityResult(data, requestCode);
		}

		if (requestCode == DigiCareContants.IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
			if(mImage == null)
				mImage = ProductImage.getInstance();
			mImage.onActivityResult(data, requestCode);
		}
	}
}
