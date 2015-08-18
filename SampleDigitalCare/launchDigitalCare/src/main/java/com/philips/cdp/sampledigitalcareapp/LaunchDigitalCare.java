package com.philips.cdp.sampledigitalcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.listeners.MainMenuListener;
import com.philips.cdp.digitalcare.productdetails.ProductMenuListener;
import com.philips.cdp.digitalcare.social.SocialProviderListener;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import java.util.ArrayList;
import java.util.Arrays;

public class LaunchDigitalCare extends FragmentActivity implements OnClickListener,
        MainMenuListener, ProductMenuListener, SocialProviderListener {

    public static final String HOCKEY_APP_ID = "9d6c50153b0c5394faa920d9dda951c7";
    private static boolean mActivityButtonSelected = true;
    private static boolean mFragmentButtonSelected = true;
    private Button mLaunchDigitalCare = null;
    private Button mLaunchAsFragment = null;

    private AutoCompleteTextView mCategory_AutoText = null;
    private AutoCompleteTextView mSubCategory_AutoText = null;
    private AutoCompleteTextView mCtn_AutoText = null;
    private AutoCompleteTextView mProductTitle_AutoText = null;
    private AutoCompleteTextView mProductReview_AutoText = null;

    private Spinner mLanguage_spinner, mCountry_spinner;
    private String mLanguage[], mCountry[], mlanguageCode[], mcountryCode[], mCategory[], msubCategory[], mctn[], mproductTitle[], mproductReview[];
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


        //setting autocompeletion for category
        mCategory_AutoText = (AutoCompleteTextView) findViewById(R.id.category);
        ArrayList<String> categoryList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.category)));
        AutoCompleteAdapter category_adapter = new AutoCompleteAdapter(this,
                R.layout.list_items, R.id.autotext, categoryList);
        mCategory_AutoText.setAdapter(category_adapter);

        //setting autocompeletion for subcategory
        mSubCategory_AutoText = (AutoCompleteTextView) findViewById(R.id.subcategory);
        ArrayList<String> subcategoryList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.subcategory)));
        AutoCompleteAdapter subcategory_adapter = new AutoCompleteAdapter(this,
                R.layout.list_items, R.id.autotext, subcategoryList);
        mSubCategory_AutoText.setAdapter(subcategory_adapter);

        //setting autocompeletion for ctn
        mCtn_AutoText = (AutoCompleteTextView) findViewById(R.id.ctn);
        ArrayList<String> ctnList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.ctn)));
        AutoCompleteAdapter ctn_adapter = new AutoCompleteAdapter(this,
                R.layout.list_items, R.id.autotext, ctnList);
        mCtn_AutoText.setAdapter(ctn_adapter);

        //setting autocompeletion for product_title
        mProductTitle_AutoText = (AutoCompleteTextView) findViewById(R.id.product_title);
        ArrayList<String> productTitleList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.product_title)));
        AutoCompleteAdapter product_title_adapter = new AutoCompleteAdapter(this,
                R.layout.list_items, R.id.autotext, productTitleList);
        mProductTitle_AutoText.setAdapter(product_title_adapter);

        //setting autocompeletion for product_review
        mProductReview_AutoText = (AutoCompleteTextView) findViewById(R.id.product_review_url);
        ArrayList<String> productReviewList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.product_review)));
        AutoCompleteAdapter product_review_adapter = new AutoCompleteAdapter(this,
                R.layout.list_items, R.id.autotext, productReviewList);
        mProductReview_AutoText.setAdapter(product_review_adapter);

        setDefaultInfoValue();

        // Digital care initialization
        initializeDigitalCareLibrary();

//        registerHockeyApp();
    }

    @Override
    protected void onDestroy() {
        DigitalCareConfigManager.getInstance().unregisterMainMenuListener(this);
        DigitalCareConfigManager.getInstance().unregisterProductMenuListener(this);
        DigitalCareConfigManager.getInstance().unregisterSocialProviderListener(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mActivityButtonSelected) {
            mLaunchDigitalCare.setVisibility(View.VISIBLE);
        } else {
            mLaunchDigitalCare.setVisibility(View.INVISIBLE);
        }

        if (mFragmentButtonSelected) {
            mLaunchAsFragment.setVisibility(View.VISIBLE);
        } else {
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
        if (mainMenuItem.equals(getStringKey(R.string.view_product_details))) {
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
        //  setDigitalCareLocale(mlanguageCode[mLanguage_spinner.getSelectedItemPosition()], mcountryCode[mCountry_spinner.getSelectedItemPosition()]);

        switch (view.getId()) {
            case R.id.launchDigitalCare:

                mActivityButtonSelected = true;
                mFragmentButtonSelected = false;

                mLaunchAsFragment.setVisibility(View.INVISIBLE);

                if (setConsumerProductInfo()) {
                    DigitalCareConfigManager.getInstance().invokeDigitalCareAsActivity(R.anim.slide_in_bottom,
                            R.anim.slide_out_bottom,
                            DigitalCareConfigManager.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED
                    );
                } else {
                    Toast.makeText(getApplicationContext(), "Please Set Consumer Product Info", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.launchAsFragment:

                mActivityButtonSelected = false;
                mFragmentButtonSelected = true;

                mLaunchDigitalCare.setVisibility(View.INVISIBLE);

                if (setConsumerProductInfo()) {
                    startActivity(new Intent(this, SampleActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Please Set Consumer Product Info", Toast.LENGTH_SHORT).show();
                }
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


    private boolean setConsumerProductInfo() {

//        Toast.makeText(getApplicationContext(), mCategory_AutoText.getText().toString().trim() +
//                "\n" + mSubCategory_AutoText.getText().toString().trim() +
//                "\n" + mCtn_AutoText.getText().toString().trim() +
//                "\n" + mProductTitle_AutoText.getText().toString().trim() +
//                "\n" + mProductReview_AutoText.getText().toString().trim()
//                , Toast.LENGTH_SHORT).show();

        if (!mCategory_AutoText.getText().toString().isEmpty() && !mSubCategory_AutoText.getText().toString().isEmpty()
                && !mCtn_AutoText.getText().toString().isEmpty() || !mProductTitle_AutoText.getText().toString().isEmpty() ||
                !mProductReview_AutoText.getText().toString().isEmpty()) {
            SampleConsumerProductInfo.setCategory(mCategory_AutoText.getText().toString());
            SampleConsumerProductInfo.setSubCategory(mSubCategory_AutoText.getText().toString());
            SampleConsumerProductInfo.setCtn(mCtn_AutoText.getText().toString());
            SampleConsumerProductInfo.setProductTitle(mProductTitle_AutoText.getText().toString());
            SampleConsumerProductInfo.setProductReviewUrl(mProductReview_AutoText.getText().toString());
            return true;
        } else {
            return false;
        }
    }


    private void setDefaultInfoValue(){
        mCategory_AutoText.setText("MENS_SHAVING_CA");
        mSubCategory_AutoText.setText("HAIR_STYLERS_SU");
        mCtn_AutoText.setText("HD8967_01");
        mProductTitle_AutoText.setText("Saeco GranBaristo Avanti");
        mProductReview_AutoText.setText("/c-p/BT9280_33/beardtrimmer-series-9000-waterproof-beard-trimmer-with-worlds-first-laser-guide");


    }

}