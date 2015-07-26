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
import com.philips.cdp.digitalcare.MainMenuListener;
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
    private Button mLaunchProduct = null;
    private Button mLaunchContact = null;
    private Button mLaunchLocate = null;
    private Button mLaunchRate = null;
    private Button mLaunchProductRegister = null;
    private Spinner mLanguage_spinner, mCountry_spinner;
    private String mLanguage[], mCountry[], mlanguageCode[], mcountryCode[];
    private SampleConsumerProductInfo mConsumerProductInfo = null;

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

        switch (view.getId()) {
            default:
                /*
                Setting AppID is very much required from App side, in order to TAG the page. Here in below code
                we are putting dummy value. Please provide proper APP_ID from you App.
                 */
                DigitalCareConfigManager.getInstance().setAppIdForTagging("101");
                setDigitalCareLocale(mlanguageCode[mLanguage_spinner.getSelectedItemPosition()], mcountryCode[mCountry_spinner.getSelectedItemPosition()]);
                DigitalCareConfigManager.getInstance().invokeDigitalCareAsActivity(R.anim.slide_in_bottom, R.anim.slide_out_bottom
                       /* "slide_in_bottom", "slide_out_bottom"*/);
        }
    }

//    private void registerHockeyApp() {
//        /** Should be commented for debug builds */
//
//        CrashManager.register(this, HOCKEY_APP_ID, new CrashManagerListener() {
//
//            public boolean shouldAutoUploadCrashes() {
//                return true;
//            }
//        });
//    }

    private void setDigitalCareLocale(String language, String country) {

        DigitalCareConfigManager.getInstance().setLocale(language, country);
    }

}

