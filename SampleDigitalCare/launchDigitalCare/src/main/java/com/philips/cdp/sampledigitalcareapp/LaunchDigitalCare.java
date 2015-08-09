package com.philips.cdp.sampledigitalcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.listeners.MainMenuListener;
import com.philips.cdp.digitalcare.productdetails.ProductMenuListener;
import com.philips.cdp.digitalcare.social.SocialProviderListener;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.sampledigitalcareapp.R;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

public class LaunchDigitalCare extends FragmentActivity implements OnClickListener,
        MainMenuListener, ProductMenuListener, SocialProviderListener {

    public static final String HOCKEY_APP_ID = "9d6c50153b0c5394faa920d9dda951c7";
    private Button mLaunchDigitalCare = null;
    private Button mLaunchAsFragment = null;

    private static boolean mActivityButtonSelected = true;
    private static boolean mFragmentButtonSelected = true;

    private Spinner mLanguage_spinner, mCountry_spinner;
    private String mLanguage[], mCountry[], mlanguageCode[], mcountryCode[];
    private SampleConsumerProductInfo mConsumerProductInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_digital_care);

        mLaunchDigitalCare = (Button) findViewById(R.id.launchDigitalCare);
        mLaunchAsFragment = (Button) findViewById(R.id.launchAsFragment);

        // set listener
        mLaunchDigitalCare.setOnClickListener(this);
        mLaunchAsFragment.setOnClickListener(this);

        // setting language spinner
        mLanguage_spinner = (Spinner) findViewById(R.id.spinner1);
        mLanguage = getResources().getStringArray(R.array.Language);
        mlanguageCode = getResources().getStringArray(R.array.Language_code);
        ArrayAdapter<String> mLanguage_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mLanguage);
        mLanguage_spinner.setAdapter(mLanguage_adapter);

        // setting country spinner
        mCountry_spinner = (Spinner) findViewById(R.id.spinner2);
        mCountry = getResources().getStringArray(R.array.country);
        mcountryCode = getResources().getStringArray(R.array.country_code);
        ArrayAdapter<String> mCountry_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mCountry);
        mCountry_spinner.setAdapter(mCountry_adapter);

        // Digital care initialization
        initializeDigitalCareLibrary();

//        registerHockeyApp();
    }

    @Override
    protected void onDestroy(){
        DigitalCareConfigManager.getInstance().unregisterMainMenuListener(this);
        DigitalCareConfigManager.getInstance().unregisterProductMenuListener(this);
        DigitalCareConfigManager.getInstance().unregisterSocialProviderListener(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mActivityButtonSelected){
            mLaunchDigitalCare.setVisibility(View.VISIBLE);
        }
        else{
            mLaunchDigitalCare.setVisibility(View.INVISIBLE);
        }

        if(mFragmentButtonSelected){
            mLaunchAsFragment.setVisibility(View.VISIBLE);
        }
        else{
            mLaunchAsFragment.setVisibility(View.INVISIBLE);
        }
    }

    private void initializeDigitalCareLibrary() {
        // Initializing DigitalCare Component.
        DigitalCareConfigManager.getInstance().initializeDigitalCareLibrary(
                this);

        // Set ConsumerProductInfo
        mConsumerProductInfo = new SampleConsumerProductInfo();
        DigitalCareConfigManager.getInstance().setConsumerProductInfo(
                mConsumerProductInfo);

        // Set DigitalCareLibrary Listeners
        DigitalCareConfigManager.getInstance().registerMainMenuListener(this);
        DigitalCareConfigManager.getInstance()
                .registerProductMenuListener(this);
        DigitalCareConfigManager.getInstance().registerSocialProviderListener(
                this);


        // Passing default Locale to DigitalCare Library, app should pass the locale which is used
        // by application and also set locale to digitalcare library dynamically when ever app
        // locale changes
        setDigitalCareLocale(mlanguageCode[mLanguage_spinner.getSelectedItemPosition()], mcountryCode[mCountry_spinner.getSelectedItemPosition()]);

        //For Debugging purpose, enable this only in debug build
        DigiCareLogger.enableLogging();
    }

    @Override
    public boolean onMainMenuItemClicked(String mainMenuItem) {
        if (mainMenuItem.equals(getStringKey(R.string.sign_into_my_philips))) {
            Intent intent = new Intent(LaunchDigitalCare.this,
                    DummyScreen.class);
            startActivity(intent);
            return true;
        }
        if(mainMenuItem.equals(getStringKey(R.string.view_product_details)))
        {
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
    /*
      Setting AppID is very much required from App side, in order to TAG the page. Here in below code
      we are putting dummy value. Please provide proper APP_ID from you App.
      Also if tagging is not enabled , consumer care is not tagging any events*/

        DigitalCareConfigManager.getInstance().enableTagging(true);
        DigitalCareConfigManager.getInstance().setAppIdForTagging("101");
        DigitalCareConfigManager.getInstance().setCurrentPageNameForTagging("SampleApp");
        setDigitalCareLocale(mlanguageCode[mLanguage_spinner.getSelectedItemPosition()], mcountryCode[mCountry_spinner.getSelectedItemPosition()]);

        switch (view.getId()) {
            case R.id.launchDigitalCare:

                mActivityButtonSelected = true;
                mFragmentButtonSelected = false;

                mLaunchAsFragment.setVisibility(View.INVISIBLE);

                DigitalCareConfigManager.getInstance().invokeDigitalCareAsActivity(R.anim.slide_in_bottom,
                        R.anim.slide_out_bottom,
                        DigitalCareConfigManager.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED
                       );
                break;
            case R.id.launchAsFragment:

                mActivityButtonSelected = false;
                mFragmentButtonSelected = true;

                mLaunchDigitalCare.setVisibility(View.INVISIBLE);

                startActivity(new Intent(this, SampleActivity.class));
                break;

        }
    }

    private void registerHockeyApp() {
        /** Should be commented for debug builds */

        CrashManager.register(this, HOCKEY_APP_ID, new CrashManagerListener() {

            public boolean shouldAutoUploadCrashes() {
                return true;
            }
        });
    }

    private void setDigitalCareLocale(String language, String country) {

        DigitalCareConfigManager.getInstance().setLocale(language, country);
    }

}

