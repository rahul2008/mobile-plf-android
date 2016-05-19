package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.session.IAPHandlerListener;
import com.philips.cdp.di.iap.session.IAPSettings;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import java.util.ArrayList;
import java.util.List;

public class DemoAppActivity extends Activity implements View.OnClickListener,
        UserRegistrationListener, IAPHandlerListener, AdapterView.OnItemSelectedListener {

    private final int DEFAULT_THEME = R.style.Theme_Philips_DarkBlue_WhiteBackground;

    private IAPHandler mIapHandler;
    private TextView mCountText = null;
    private FrameLayout mShoppingCart;
    Button mShopNow;

    private LinearLayout mSelectCountryLl;

    private CountryPreferences mCountryPreference;
    private int mSelectedCountryIndex;
    private boolean mProductCountRequested;
    private Spinner mSpinner;


    //We require this to track for hiding the cart icon in demo app
    IAPSettings mIAPSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(DEFAULT_THEME);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo_app_layout);
        showAppVersion();

        Button register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(this);

        mShopNow = (Button) findViewById(R.id.btn_shop_now);
        mShopNow.setOnClickListener(this);

        mShoppingCart = (FrameLayout) findViewById(R.id.shopping_cart_icon);
        mShoppingCart.setOnClickListener(this);

        mCountText = (TextView) findViewById(R.id.count_txt);

        RegistrationHelper.getInstance().registerUserRegistrationListener(this);

        mIAPSettings = new IAPSettings("US", "en", DEFAULT_THEME);
        mIapHandler = IAPHandler.init(this, mIAPSettings);

        mSelectCountryLl = (LinearLayout) findViewById(R.id.select_country);
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinner.setOnItemSelectedListener(this);

        List<String> countries = new ArrayList<>();
        countries.add("Select Country");
        countries.add("US");
        countries.add("UK");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);

        mCountryPreference = new CountryPreferences(this);
        mSpinner.setSelection(mCountryPreference.getSelectedCountryIndex());
    }

    @Override
    protected void onResume() {
        super.onResume();

        /** Should be commented for debug builds */
        final String HOCKEY_APP_ID = "dc402a11ae984bd18f99c07d9b4fe6a4";
        CrashManager.register(this, HOCKEY_APP_ID, new CrashManagerListener() {

            public boolean shouldAutoUploadCrashes() {
                return !IAPLog.isLoggingEnabled();
            }
        });

        User user = new User(this);
        if (user.isUserSignIn()) {
            displayViews();
            if (mSelectedCountryIndex >0 && !mProductCountRequested) {
                Utility.showProgressDialog(this, getString(R.string.loading_cart));
                mIapHandler.getProductCartCount(mProductCountListener);
            }
        }
    }

    @Override
    protected void onDestroy() {
        Utility.dismissProgressDialog();
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this);
        super.onDestroy();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.shopping_cart_icon:
                mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_SHOPPING_CART_VIEW,null, mBuyProductListener);
                break;
            case R.id.btn_register:
                IAPLog.d(IAPLog.DEMOAPPACTIVITY, "DemoActivity : Registration");
                RegistrationLaunchHelper.launchDefaultRegistrationActivity(this);
                break;
            case R.id.btn_shop_now:
                mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_PRODUCT_CATALOG_VIEW,null, null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        displayViews();
        activity.finish();
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }

    @Override
    public void onUserLogoutSuccess() {
        hideViews();
        mSelectedCountryIndex = 0;
        mCountryPreference.clearCountryPreference();
        mSpinner.setSelection(0);
        mShoppingCart.setVisibility(View.GONE);
        mCountText.setVisibility(View.GONE);
        mShopNow.setVisibility(View.GONE);
    }

    @Override
    public void onUserLogoutFailure() {

    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {

    }

    private IAPHandlerListener mProductCountListener = new IAPHandlerListener() {
        @Override
        public void onSuccess(final int count) {
            if (count > 0) {
                mCountText.setText(String.valueOf(count));
                mCountText.setVisibility(View.VISIBLE);
            } else {
                mCountText.setVisibility(View.GONE);
            }
            Utility.dismissProgressDialog();

            mProductCountRequested = false;
        }

        @Override
        public void onFailure(final int errorCode) {
            Utility.dismissProgressDialog();
            showToast(errorCode);
            mProductCountRequested = false;
        }
    };

    private IAPHandlerListener mBuyProductListener = new IAPHandlerListener() {
        @Override
        public void onSuccess(final int count) {
        Utility.dismissProgressDialog();
    }

    @Override
    public void onFailure(final int errorCode) {
        Utility.dismissProgressDialog();
        showToast(errorCode);
    }
};

    private void showToast(int errorCode) {
        String errorText = "Unknown error";
        if (IAPConstant.IAP_ERROR_NO_CONNECTION == errorCode) {
            errorText = "No connection";
        } else if (IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT == errorCode) {
            errorText = "Connection time out";
        } else if (IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE == errorCode) {
            errorText = "Authentication failure";
        } else if (IAPConstant.IAP_ERROR_INSUFFICIENT_STOCK_ERROR == errorCode) {
            errorText = "Product out of stock";
        }

        Toast toast = Toast.makeText(this, errorText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onSuccess(int count) {

    }

    @Override
    public void onFailure(int errorCode) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mShopNow.setEnabled(true);
        //Don't process Select country
        mSelectedCountryIndex = position;
        mCountryPreference.saveCountryPrefrence(position);

        if (position == 0) {
            mShoppingCart.setVisibility(View.GONE);
            mShopNow.setVisibility(View.GONE);
            return;
        }

        mShoppingCart.setVisibility(View.VISIBLE);
        mShopNow.setVisibility(View.VISIBLE);

        String selectedCountry = parent.getItemAtPosition(position).toString();
        if (selectedCountry.equals("UK"))
            selectedCountry = "GB";
        if (!mProductCountRequested) {
            Utility.showProgressDialog(this, getString(R.string.loading_cart));
            mIAPSettings = new IAPSettings(selectedCountry, "en", DEFAULT_THEME);;
            setUseLocalData();
            mIapHandler = IAPHandler.init(this, mIAPSettings);
            updateCartIcon();
            mProductCountRequested = true;
            mIapHandler.getProductCartCount(mProductCountListener);
        }
    }

    private void updateCartIcon() {
        if(shouldUseLocalData()) {
            mShoppingCart.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setUseLocalData() {
        mIAPSettings.setUseLocalData(true);
    }

    public boolean shouldUseLocalData() {
        return mIAPSettings.isUseLocalData();
    }

    private void displayViews() {
//        mShoppingCart.setVisibility(View.VISIBLE);
        mSelectCountryLl.setVisibility(View.VISIBLE);
//        mShopNow.setVisibility(View.VISIBLE);
    }

    private void hideViews() {
        mShoppingCart.setVisibility(View.GONE);
        mSelectCountryLl.setVisibility(View.GONE);
        mShopNow.setVisibility(View.GONE);
    }

    private void showAppVersion() {
        String code = "";
        try {
            code = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        TextView versionView = (TextView) findViewById(R.id.appversion);
        versionView.setText(String.valueOf(code));

    }
}