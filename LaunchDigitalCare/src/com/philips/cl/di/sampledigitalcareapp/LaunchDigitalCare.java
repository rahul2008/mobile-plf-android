package com.philips.cl.di.sampledigitalcareapp;

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
import android.widget.Toast;

import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.MainMenuListener;
import com.philips.cl.di.digitalcare.productdetails.ProductMenuButtonClickListener;
import com.philips.cl.di.digitalcare.social.SocialProviderListener;
import com.philips.cl.di.digitalcare.util.DigitalCareContants;

public class LaunchDigitalCare extends Activity implements OnClickListener {

	private Button mLaunchDigitalCare = null;

	private Button mLaunchProduct = null;
	private Button mLaunchContact = null;
	private Button mLaunchLocate = null;
	private Button mLaunchRate = null;
	private Button mLaunchProductRegister = null;

	private Spinner mLanguage_spinner, mCountry_spinner;

	private String mLanguage[], mCountry[], mlanguageCode[], mcountryCode[];
	private ConsumerProductInfoDemo mConsumerProductInfoDemo = null;
	private MainMenuButtonClickListner mButtonClickListner = null;
	private ProductMenuClickListener mProductButtonClickListner = null;
	private SocialProviderButtonClickListner mSocialProviderButtonClickListner = null;

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

		/*
		 * DigitalCare Home Screen custom button listeners.
		 */
		mButtonClickListner = new MainMenuButtonClickListner();
		DigitalCareConfigManager.getInstance(this).registerMainMenuListener(
				mButtonClickListner);

		/*
		 * DigitalCare "View Product Details" Screen custom button listeners.
		 */
		mProductButtonClickListner = new ProductMenuClickListener();
		DigitalCareConfigManager.getInstance(this).registerProductMenuListener(
				mProductButtonClickListner);

		/*
		 * DigitalCare Social Provider Button listeners.
		 */
		mSocialProviderButtonClickListner = new SocialProviderButtonClickListner();
		DigitalCareConfigManager.getInstance(this)
				.registerSocialProviderListener(
						mSocialProviderButtonClickListner);

		mLanguage_spinner
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {

						DigitalCareConfigManager
								.setLanguage(mlanguageCode[position]);

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

						DigitalCareConfigManager
								.setCountry(mcountryCode[position]);

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
	}

	private class MainMenuButtonClickListner implements MainMenuListener {

		@Override
		public boolean onMainMenuItemClickListener(String buttonTitle) {
			if (buttonTitle
					.equalsIgnoreCase(getStringKey(R.string.registration))
					|| buttonTitle
							.equalsIgnoreCase(getStringKey(R.string.view_faq))) {
				Intent intent = new Intent(LaunchDigitalCare.this,
						DummyScreen.class);
				startActivity(intent);
				return true;
			}
			return false;
		}
	}

	private String getStringKey(int resId) {
		return getResources().getResourceEntryName(resId);
	}

	private class ProductMenuClickListener implements
			ProductMenuButtonClickListener {

		@Override
		public boolean onProductMenuItemClickListener(String buttonTitle) {
			if (buttonTitle.equalsIgnoreCase(getResources()
					.getResourceEntryName(R.string.product_download_manual))) {
				Intent intent = new Intent(LaunchDigitalCare.this,
						DummyScreen.class);
				startActivity(intent);
				return true;
			}
			return false;
		}
	}

	private class SocialProviderButtonClickListner implements
			SocialProviderListener {

		@Override
		public boolean onSocialProviderItemClickListener(String buttonTitle) {
			// if (buttonTitle.equalsIgnoreCase(getResources()
			// .getResourceEntryName(R.string.twitter))) {
			// Intent intent = new Intent(LaunchDigitalCare.this,
			// DummyScreen.class);
			// startActivity(intent);
			// return true;
			// }
			return false;
		}
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.launchproduct:
			DigitalCareConfigManager.setLaunchingScreen(LaunchDigitalCare.this,
					DigitalCareContants.OPTION_PRODUCS_DETAILS);
			Toast.makeText(getApplicationContext(), "product detail clicked",
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.launch_contact:
			DigitalCareConfigManager.setLaunchingScreen(LaunchDigitalCare.this,
					DigitalCareContants.OPTION_CONTACT_US);
			Toast.makeText(getApplicationContext(), "contact clicked",
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.launch_locate:
			DigitalCareConfigManager.setLaunchingScreen(LaunchDigitalCare.this,
					DigitalCareContants.OPTION_FIND_PHILIPS_NEARBY);
			Toast.makeText(getApplicationContext(), "find philips clicked",
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.launchrate:
			DigitalCareConfigManager.setLaunchingScreen(LaunchDigitalCare.this,
					DigitalCareContants.OPTION_WHAT_ARE_YOU_THINKING);
			Toast.makeText(getApplicationContext(), "tell us clicked",
					Toast.LENGTH_SHORT).show();
			break;

		case R.id.launchproductregister:
			DigitalCareConfigManager.setLaunchingScreen(LaunchDigitalCare.this,
					DigitalCareContants.OPTION_REGISTER_PRODUCT);
			Toast.makeText(getApplicationContext(),
					"product register  clicked", Toast.LENGTH_SHORT).show();
			break;

		default:
			DigitalCareConfigManager.setLaunchingScreen(LaunchDigitalCare.this,
					DigitalCareContants.OPTION_SUPPORT_SCREEN);
			break;
		}
	}
}
