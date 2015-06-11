package com.philips.cl.di.sampledigitalcareapp;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.philips.cl.di.digitalcare.DigitalCareActivity;
import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.MainMenuListener;
import com.philips.cl.di.digitalcare.productdetails.ProductMenuListener;
import com.philips.cl.di.digitalcare.social.SocialProviderListener;

public class LaunchDigitalCare extends Activity implements OnClickListener,
		MainMenuListener, ProductMenuListener, SocialProviderListener {

	private Button mLaunchDigitalCare = null;

	private Button mLaunchProduct = null;
	private Button mLaunchContact = null;
	private Button mLaunchLocate = null;
	private Button mLaunchRate = null;
	private Button mLaunchProductRegister = null;

	private Spinner mLanguage_spinner, mCountry_spinner;

	private static final int DEFAULT_ANIMATION_START = R.anim.slide_in_bottom;
	private static final int DEFAULT_ANIMATION_STOP = R.anim.slide_out_bottom;

	private String mLanguage[], mCountry[], mlanguageCode[], mcountryCode[];
	private ConsumerProductInfoDemo mConsumerProductInfoDemo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_digital_care);

		mLaunchDigitalCare = (Button) findViewById(R.id.launchDigitalCare);
		mLaunchProduct = (Button) findViewById(R.id.launchproduct);
		mLaunchContact = (Button) findViewById(R.id.launch_contact);
		mLaunchLocate = (Button) findViewById(R.id.launch_locate);
		mLaunchLocate.setVisibility(View.GONE);
		mLaunchRate = (Button) findViewById(R.id.launchrate);
		mLaunchProductRegister = (Button) findViewById(R.id.launchproductregister);

		// set listener
		mLaunchDigitalCare.setOnClickListener(this);
		mLaunchProduct.setOnClickListener(this);
		mLaunchContact.setOnClickListener(this);
		mLaunchLocate.setOnClickListener(this);
		mLaunchRate.setOnClickListener(this);
		mLaunchProductRegister.setOnClickListener(this);

		// setting language spinner
		mLanguage_spinner = (Spinner) findViewById(R.id.spinner1);
		mLanguage = getResources().getStringArray(R.array.Language);
		mlanguageCode = getResources().getStringArray(R.array.Language_code);
		ArrayAdapter<String> mLanguage_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mLanguage);
		mLanguage_spinner.setAdapter(mLanguage_adapter);

		mConsumerProductInfoDemo = new ConsumerProductInfoDemo();
		DigitalCareConfigManager.getInstance(this).setConsumerProductInfo(
				mConsumerProductInfoDemo);
		DigitalCareConfigManager.getInstance(this).registerMainMenuListener(
				this);

		DigitalCareConfigManager.getInstance(this).registerProductMenuListener(
				this);

		DigitalCareConfigManager.getInstance(this)
				.registerSocialProviderListener(this);

		mLanguage_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						setLocaleForTesting(mcountryCode[position],
								mlanguageCode[position]);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		// setting country spinner
		mCountry_spinner = (Spinner) findViewById(R.id.spinner2);
		mCountry = getResources().getStringArray(R.array.country);
		mcountryCode = getResources().getStringArray(R.array.country_code);
		ArrayAdapter<String> mCountry_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mCountry);
		mCountry_spinner.setAdapter(mCountry_adapter);
		mCountry_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						setLocaleForTesting(mcountryCode[position],
								mlanguageCode[position]);

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
	}

	@Override
	public boolean onMainMenuItemClicked(String mainMenuItem) {
		if (mainMenuItem.equals(getStringKey(R.string.registration))
				|| mainMenuItem.equals(getStringKey(R.string.view_faq))) {
			Intent intent = new Intent(LaunchDigitalCare.this,
					DummyScreen.class);
			startActivity(intent);
			return true;
		}
		return false;
	}

	private String getStringKey(int resId) {
		return getResources().getResourceEntryName(resId);
	}

	@Override
	public boolean onProductMenuItemClicked(String productMenu) {
		if (productMenu.equals(getResources().getResourceEntryName(
				R.string.product_download_manual))) {
			Intent intent = new Intent(LaunchDigitalCare.this,
					DummyScreen.class);
			startActivity(intent);
			return true;
		}
		return false;
	}

	@Override
	public boolean onSocialProviderItemClicked(String socialProviderItem) {
		return false;
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		default:
			Intent intent = new Intent(this, DigitalCareActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("STARTANIMATIONID", DEFAULT_ANIMATION_START);
			intent.putExtra("ENDANIMATIONID", DEFAULT_ANIMATION_STOP);
			startActivity(intent);
		}
	}

	private void setLocaleForTesting(String country, String language) {
		Locale mLocale = new Locale(language, country);
		DigitalCareConfigManager.setLocale(mLocale);
	}
}
