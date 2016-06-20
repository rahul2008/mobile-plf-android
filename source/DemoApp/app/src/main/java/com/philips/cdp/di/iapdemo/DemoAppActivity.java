package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.session.IAPHandlerListener;
import com.philips.cdp.di.iap.session.IAPSettings;
import com.philips.cdp.di.iap.store.HybrisStore;
import com.philips.cdp.di.iap.store.StoreConfiguration;
import com.philips.cdp.di.iap.store.VerticalAppConfig;
import com.philips.cdp.di.iap.store.WebStoreConfig;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DemoAppActivity extends Activity implements View.OnClickListener,
        UserRegistrationListener, AdapterView.OnItemSelectedListener {

    private final int DEFAULT_THEME = R.style.Theme_Philips_DarkPink_WhiteBackground;

    private IAPHandler mIapHandler;
    private User mUser;
    private LinearLayout mSelectCountryLl;
    private LinearLayout mSelectEnvironment;
    private TextView mCountText = null;
    private FrameLayout mShoppingCart;
    private Spinner mSpinner;
    private Spinner mSpinnerEnv;
    private Button mShopNow;
    private Button mPurchaseHistory;

    private CountryPreferences mCountryPreference;
    private EnvironmentPreferences mEnvironmentPreference;
    private int mSelectedCountryIndex;
    private int mSelectedEnvironmentIndex;
    private boolean mProductCountRequested;

    //We require this to track for hiding the cart icon in demo app
    IAPSettings mIAPSettings;
    private Button mFragmentLaunch;

    private ArrayList<String> mProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(DEFAULT_THEME);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo_app_layout);
        showAppVersion();

        Button mRegister = (Button) findViewById(R.id.btn_register);
        mRegister.setOnClickListener(this);

        mSelectEnvironment = (LinearLayout) findViewById(R.id.select_environment);
        mFragmentLaunch = (Button) findViewById(R.id.btn_fragment_launch);
        mFragmentLaunch.setOnClickListener(this);
        mFragmentLaunch.setVisibility(View.GONE);

        mShopNow = (Button) findViewById(R.id.btn_shop_now);
        mShopNow.setOnClickListener(this);

        mPurchaseHistory = (Button) findViewById(R.id.btn_purchase_history);
        mPurchaseHistory.setOnClickListener(this);

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

        mSpinnerEnv = (Spinner) findViewById(R.id.spinner_env);
        mSpinnerEnv.setOnItemSelectedListener(this);

        List<String> countries = new ArrayList<>();
        countries.add("Select Country");
        countries.add("US");
        countries.add("UK");

        List<String> environments = new ArrayList<>();
        environments.add("Select Environment");
        environments.add("tst.pl.shop.philips.com");
        environments.add("acc.occ.shop.philips.com");
        //environments.add("www.occ.shop.philips.com");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countries);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);

        ArrayAdapter<String> dataAdapterEnv = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, environments);
        dataAdapterEnv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerEnv.setAdapter(dataAdapterEnv);

        mCountryPreference = new CountryPreferences(this);
        mSpinner.setSelection(mCountryPreference.getSelectedCountryIndex());

        mEnvironmentPreference = new EnvironmentPreferences(this);
        mSpinnerEnv.setSelection(mEnvironmentPreference.getSelectedEnvironmentIndex());
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
                mIapHandler.launchCategorizedCatalog(mProductList);
                Intent intent = new Intent(this, LauncherFragmentActivity.class);
                this.startActivity(intent);
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

        switch (parent.getId()) {
            case R.id.spinner:
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
                mPurchaseHistory.setVisibility(View.VISIBLE);
                mPurchaseHistory.setEnabled(true);
                if (!RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment().equalsIgnoreCase("Production"))
                    mSelectEnvironment.setVisibility(View.VISIBLE);

                String selectedCountry = parent.getItemAtPosition(position).toString();
                if (selectedCountry.equals("UK"))
                    selectedCountry = "GB";

                setLocale("en", selectedCountry);

                if (!mProductCountRequested) {
                    mIAPSettings = new IAPSettings(selectedCountry, "en", DEFAULT_THEME);
//            setUseLocalData();
                    mIapHandler = IAPHandler.init(this, mIAPSettings);
                    updateCartIcon();
                    if (!shouldUseLocalData()) {
                        Utility.showProgressDialog(this, getString(R.string.iap_please_wait));
                        mProductCountRequested = true;
                        mIapHandler.getProductCartCount(mProductCountListener);
                    }
                }
                break;
            case R.id.spinner_env:
                mSelectedEnvironmentIndex = position;
                if (position == 0)
                    return;
                boolean isSelectionChanged = (mEnvironmentPreference.getSelectedEnvironmentIndex() != position) ? true : false;
                mEnvironmentPreference.saveEnvironmentPrefrence(position);
                String selectedEnvironment = parent.getItemAtPosition(position).toString();
                setHostPort(selectedEnvironment);
                // Use the Below Code in case Production Environment also has to be given
                /*if (isSelectionChanged) {
                    mUser.logout(new LogoutHandler() {
                        @Override
                        public void onLogoutSuccess() {
                            onUserLogoutSuccess();
                        }

                        @Override
                        public void onLogoutFailure(final int i, final String s) {
                            onUserLogoutFailure();
                        }
                    });
                    quit();
                }*/

                /*
                    Put the below lines in case production Not required
                 */
                if (isSelectionChanged) {
                    updateCartIcon();
                    if (!shouldUseLocalData()) {
                        if (!Utility.isProgressDialogShowing())
                            Utility.showProgressDialog(this, getString(R.string.iap_please_wait));
                        mProductCountRequested = true;
                        mIapHandler.getProductCartCount(mProductCountListener);
                    }
                }
                break;
        }
    }

    private void setHostPort(String env) {
        StoreSpec spec = HybrisDelegate.getInstance().getStore();
        if (spec instanceof HybrisStore) {
            Class<?> hybrisStore = spec.getClass();
            try {
                Field storeConfiguration = hybrisStore.getDeclaredField("mStoreConfig");
                storeConfiguration.setAccessible(true);

                //Set VerticalConfig
                storeConfiguration.set(spec, getVerticalConfig(env, (HybrisStore) spec));
                spec.setNewUser(DemoAppActivity.this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private StoreConfiguration getVerticalConfig(String env, HybrisStore spec) throws Exception {
        StoreConfiguration storeConfig = new StoreConfiguration(this, spec);
        Field verticalAppConfig = storeConfig.getClass().getDeclaredField("mVerticalAppConfig");
        verticalAppConfig.setAccessible(true);

        VerticalAppConfig newVerticalConfig = new VerticalAppConfig(this);
        Field hostPort = newVerticalConfig.getClass().getDeclaredField("mHostPort");
        hostPort.setAccessible(true);
        hostPort.set(newVerticalConfig, env);

        verticalAppConfig.set(storeConfig, newVerticalConfig);

        Field webStoreConfig = storeConfig.getClass().getDeclaredField("mWebStoreConfig");
        webStoreConfig.setAccessible(true);
        WebStoreConfig newWebStoreConfig = new WebStoreConfig(DemoAppActivity.this, storeConfig);
        Field siteID = newWebStoreConfig.getClass().getDeclaredField("mSiteID");
        siteID.setAccessible(true);

        siteID.set(newWebStoreConfig, "US_Tuscany");

        webStoreConfig.set(storeConfig, newWebStoreConfig);
        return storeConfig;
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
        mSelectEnvironment.setVisibility(View.GONE);
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

    //Use this in case Production Environment has to be Used
    /*public void quit() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
    }*/
}