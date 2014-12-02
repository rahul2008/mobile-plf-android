package com.philips.cl.di.dev.pa.digitalcare;

import java.util.Locale;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.philips.cl.di.dev.pa.digitalcare.customview.FontTextView;

public class DigitalCareActivity extends BaseActivity {
	private static final int OPTION_CONTACT_US = 1;
	private static final int OPTION_PRODUCS_DETAILS = 2;
	private static final int OPTION_FAQ = 3;
	private static final int OPTION_FIND_PHILIPS_NEARBY = 4;
	private static final int OPTION_WHAT_ARE_YOU_THINKING = 5;
	private static final int OPTION_REGISTER_PRODUCT = 6;

	private LinearLayout mParentLayout = null;
	private RelativeLayout mContactUs = null;
	private RelativeLayout mProductDetails = null;
	private RelativeLayout mFaq = null;
	private RelativeLayout mFindPhilips = null;
	private RelativeLayout mWhatYouThink = null;
	private RelativeLayout mRegisterProduct = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mParentLayout = (LinearLayout) findViewById(R.id.optionParent);

		mContactUs = (RelativeLayout) findViewById(R.id.optionContactUs);
		mProductDetails = (RelativeLayout) findViewById(R.id.optionProdDetails);
		mFaq = (RelativeLayout) findViewById(R.id.optionFaq);
		mFindPhilips = (RelativeLayout) findViewById(R.id.optionFindPhilips);
		mWhatYouThink = (RelativeLayout) findViewById(R.id.optionThinking);
		mRegisterProduct = (RelativeLayout) findViewById(R.id.optionRegProd);
	}

	@Override
	protected void onResume() {
		super.onResume();
		int[] keys = getResources().getIntArray(R.array.options_available);

		Log.i("testing", "key length: " + keys.length);
		for (int btnOption : keys) {
			enableOptionButtons(btnOption);
			Log.i("testing", "key : " + btnOption);
		}
	}

	private void enableOptionButtons(int option) {
		switch (option) {
		case OPTION_CONTACT_US:
			mContactUs.setVisibility(View.VISIBLE);
			break;
		case OPTION_PRODUCS_DETAILS:
			mProductDetails.setVisibility(View.VISIBLE);
			break;
		case OPTION_FAQ:
			mFaq.setVisibility(View.VISIBLE);
			break;
		case OPTION_FIND_PHILIPS_NEARBY:
			mFindPhilips.setVisibility(View.VISIBLE);
			break;
		case OPTION_WHAT_ARE_YOU_THINKING:
			mWhatYouThink.setVisibility(View.VISIBLE);
			break;
		case OPTION_REGISTER_PRODUCT:
			mRegisterProduct.setVisibility(View.VISIBLE);
			break;
		}
	}
}
