package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.philips.cdp.di.iap.session.IAPHandlerProductListListener;
import com.philips.cdp.di.iap.session.IAPSettings;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import java.util.ArrayList;
import java.util.List;

public class DemoAppActivity extends Activity implements View.OnClickListener,
        UserRegistrationListener, AdapterView.OnItemSelectedListener {

    private final int DEFAULT_THEME = R.style.Theme_Philips_DarkPink_WhiteBackground;

    //We require this to track for hiding the cart icon in demo app
    private IAPSettings mIAPSettings;
    private IAPHandler mIapHandler;
    private CountryPreferences mCountryPreference;
    private User mUser;
    private Handler handler;

    private LinearLayout mSelectCountryLl;
    private FrameLayout mShoppingCart;
    private TextView mCountText = null;
    private Spinner mSpinner;
    private Button mShopNow;
    private Button mPurchaseHistory;
    private Button mFragmentLaunch;
    private Button mLaunchProductDetail;

    private String mSelectedCountry;
    private int mSelectedCountryIndex;
    private boolean mProductCountRequested;
    private ArrayList<String> mProductList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(DEFAULT_THEME);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo_app_layout);
        showAppVersion();

        Button mRegister = (Button) findViewById(R.id.btn_register);
        mRegister.setOnClickListener(this);
        mFragmentLaunch = (Button) findViewById(R.id.btn_fragment_launch);
        mFragmentLaunch.setOnClickListener(this);
        mFragmentLaunch.setVisibility(View.GONE);

        mShopNow = (Button) findViewById(R.id.btn_shop_now);
        mShopNow.setOnClickListener(this);

        mPurchaseHistory = (Button) findViewById(R.id.btn_purchase_history);
        mPurchaseHistory.setOnClickListener(this);

        mLaunchProductDetail = (Button) findViewById(R.id.btn_launch_product_detail);
        mLaunchProductDetail.setOnClickListener(this);

        mShoppingCart = (FrameLayout) findViewById(R.id.shopping_cart_icon);
        mShoppingCart.setOnClickListener(this);

        mCountText = (TextView) findViewById(R.id.count_txt);

        RegistrationHelper.getInstance().registerUserRegistrationListener(this);

        mIAPSettings = new IAPSettings("US", "en", DEFAULT_THEME);
        mIapHandler = IAPHandler.init(this, mIAPSettings);

        mProductList = new ArrayList<>();
        mProductList.add("HX9042/64");
        mProductList.add("HX9042/64");
        mProductList.add("HX9042/64");

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

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIapHandler.getCompleteProductList(mGetCompleteProductListener);
            }
        }, 1000);
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

        init();
    }

    private void init() {
        mUser = new User(this);
        if (mUser.isUserSignIn()) {
            displayViews();
            if (mSelectedCountryIndex > 0 && !mProductCountRequested) {
                Utility.showProgressDialog(this, getString(R.string.iap_please_wait));
                mIapHandler.getProductCartCount(mProductCountListener);
            }
        }
    }

    @Override
    protected void onDestroy() {
        Utility.dismissProgressDialog();
        super.onDestroy();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.shopping_cart_icon:
                if (isNetworkAvailable(DemoAppActivity.this)) {
                    mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_SHOPPING_CART_VIEW, null, mBuyProductListener);
                } else {
                    Toast.makeText(DemoAppActivity.this, "Network unavailable", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_register:
                IAPLog.d(IAPLog.DEMOAPPACTIVITY, "DemoActivity : Registration");
                RegistrationLaunchHelper.launchDefaultRegistrationActivity(this);
                break;
            case R.id.btn_shop_now:
                if (isNetworkAvailable(DemoAppActivity.this)) {
                    mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_PRODUCT_CATALOG_VIEW, null, null);
                } else {
                    Toast.makeText(DemoAppActivity.this, "Network unavailable", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_purchase_history:
                if (isNetworkAvailable(DemoAppActivity.this)) {
                    mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_PURCHASE_HISTORY_VIEW, null, null);
                } else {
                    Toast.makeText(DemoAppActivity.this, "Network unavailable", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_fragment_launch:
                //  mIapHandler.launchCategorizedCatalog(mProductList);
                Intent intent = new Intent(this, LauncherFragmentActivity.class);
                this.startActivity(intent);
                break;
            case R.id.btn_launch_product_detail:
                if (isNetworkAvailable(DemoAppActivity.this)) {
                    mIapHandler.launchIAP(IAPConstant.IAPLandingViews.IAP_PRODUCT_DETAIL_VIEW, "HX8071/10", null);
                } else {
                    Toast.makeText(DemoAppActivity.this, "Network unavailable", Toast.LENGTH_SHORT).show();
                }
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
    }

    @Override
    public void onUserLogoutFailure() {

    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {

    }

    private IAPHandlerProductListListener mGetCompleteProductListener = new IAPHandlerProductListListener() {

        @Override
        public void onFailure(int errorCode) {
            Utility.dismissProgressDialog();
            IAPLog.d(IAPLog.LOG, "Server error" + errorCode);
        }

        @Override
        public void onSuccess(ArrayList<String> productList) {
            Utility.dismissProgressDialog();
            IAPLog.d(IAPLog.LOG, "Product List =" + productList.toString());
            Toast.makeText(getApplicationContext(), "Product List = " + productList.toString(), Toast.LENGTH_LONG).show();
        }
    };

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mShopNow.setEnabled(true);
                //Don't process Select country
                mSelectedCountryIndex = position;
                mCountryPreference.saveCountryPrefrence(position);

                if (position == 0) {
                    mShoppingCart.setVisibility(View.GONE);
                    mShopNow.setVisibility(View.GONE);
                    mPurchaseHistory.setVisibility(View.GONE);
                    mFragmentLaunch.setVisibility(View.GONE);
                    return;
                }

                mFragmentLaunch.setVisibility(View.VISIBLE);
                mShoppingCart.setVisibility(View.VISIBLE);
                mShopNow.setVisibility(View.VISIBLE);
                mPurchaseHistory.setEnabled(true);

                mSelectedCountry = parent.getItemAtPosition(position).toString();
                if (mSelectedCountry.equals("UK"))
                    mSelectedCountry = "GB";

                setLocale("en", mSelectedCountry);

                if (!mProductCountRequested) {
                    mIAPSettings = new IAPSettings(mSelectedCountry, "en", DEFAULT_THEME);
                    setUseLocalData();
                    mIapHandler = IAPHandler.init(this, mIAPSettings);
                    updateCartIcon();
                    if (!shouldUseLocalData()) {
                        Utility.showProgressDialog(this, getString(R.string.iap_please_wait));
                        mProductCountRequested = true;
                        mIapHandler.getProductCartCount(mProductCountListener);
                        mPurchaseHistory.setVisibility(View.VISIBLE);
                    }else
                        mPurchaseHistory.setVisibility(View.GONE);
                }
    }

    private void setLocale(String languageCode, String countryCode) {
        PILLocaleManager localeManager = new PILLocaleManager(DemoAppActivity.this);
        localeManager.setInputLocale(languageCode, countryCode);
    }

    private void updateCartIcon() {
        if (shouldUseLocalData()) {
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
        mSelectCountryLl.setVisibility(View.VISIBLE);
        mPurchaseHistory.setVisibility(View.VISIBLE);
        mPurchaseHistory.setEnabled(true);
    }

    private void hideViews() {
        mShoppingCart.setVisibility(View.GONE);
        mSelectCountryLl.setVisibility(View.GONE);
        mShopNow.setVisibility(View.GONE);
        mPurchaseHistory.setVisibility(View.GONE);
        mSelectedCountryIndex = 0;
        mCountryPreference.clearCountryPreference();
        mSpinner.setSelection(0);
        mCountText.setVisibility(View.GONE);
        mFragmentLaunch.setVisibility(View.GONE);
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

    public boolean isNetworkAvailable(Context pContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}