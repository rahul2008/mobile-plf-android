package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPFlowInput;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

import java.util.ArrayList;


public class DemoAppActivity extends UiKitActivity implements View.OnClickListener, IAPListener,
        UserRegistrationUIEventListener, UserRegistrationListener {

    private final int DEFAULT_THEME = R.style.Theme_DLS_GroupBlue_VeryLight;
    private DemoApplication mApplicationContext;

    private LinearLayout mAddCTNLl;

    private FrameLayout mShoppingCart;
    private com.philips.platform.uid.view.widget.EditText mEtCTN;

    private com.philips.platform.uid.view.widget.Button mRegister;
    private com.philips.platform.uid.view.widget.Button mShopNow;
    private com.philips.platform.uid.view.widget.Button mShopNowCategorized;
    private com.philips.platform.uid.view.widget.Button mBuyDirect;
    private com.philips.platform.uid.view.widget.Button mPurchaseHistory;
    private com.philips.platform.uid.view.widget.Button mLaunchFragment;
    private com.philips.platform.uid.view.widget.Button mLaunchProductDetail;
    private com.philips.platform.uid.view.widget.Button mAddCtn;

    private ArrayList<String> mCategorizedProductList;

    private ProgressDialog mProgressDialog = null;
    private TextView mTitleTextView;
    private TextView mCountText;

    private IAPInterface mIapInterface;
    private IAPLaunchInput mIapLaunchInput;
    private IAPSettings mIAPSettings;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);

        IAPLog.enableLogging(true);
        mApplicationContext = (DemoApplication) getApplicationContext();

        //addActionBar();

        setContentView(R.layout.demo_app_layout);
        actionBar();
        showAppVersion();
        mEtCTN = (com.philips.platform.uid.view.widget.EditText) findViewById(R.id.et_add_ctn);
        mAddCTNLl = (LinearLayout) findViewById(R.id.ll_ctn);

        mRegister = (com.philips.platform.uid.view.widget.Button) findViewById(R.id.btn_register);
        mRegister.setOnClickListener(this);

        mLaunchFragment = (com.philips.platform.uid.view.widget.Button) findViewById(R.id.btn_fragment_launch);
        mLaunchFragment.setOnClickListener(this);
        mLaunchFragment.setVisibility(View.GONE);

        mBuyDirect = (com.philips.platform.uid.view.widget.Button) findViewById(R.id.btn_buy_direct);
        mBuyDirect.setOnClickListener(this);

        mShopNow = (com.philips.platform.uid.view.widget.Button) findViewById(R.id.btn_shop_now);
        mShopNow.setOnClickListener(this);

        mPurchaseHistory = (com.philips.platform.uid.view.widget.Button) findViewById(R.id.btn_purchase_history);
        mPurchaseHistory.setOnClickListener(this);

        mLaunchProductDetail = (com.philips.platform.uid.view.widget.Button) findViewById(R.id.btn_launch_product_detail);
        mLaunchProductDetail.setOnClickListener(this);

        mShoppingCart = (FrameLayout) findViewById(R.id.shopping_cart_icon);
        mShoppingCart.setOnClickListener(this);

        mShopNowCategorized = (com.philips.platform.uid.view.widget.Button) findViewById(R.id.btn_categorized_shop_now);
        mShopNowCategorized.setOnClickListener(this);

        mAddCtn = (com.philips.platform.uid.view.widget.Button) findViewById(R.id.btn_add_ctn);
        mAddCtn.setOnClickListener(this);

        mCategorizedProductList = new ArrayList<>();

        mApplicationContext.getAppInfra().getTagging().setPreviousPage("demoapp:");
        mUser = new User(this);
        mUser.registerUserRegistrationListener(this);
        //Integration interface

        mIAPSettings = new IAPSettings(this);
        mIAPSettings.setUseLocalData(true);

        IAPDependencies mIapDependencies = new IAPDependencies(new AppInfra.Builder().build(this));
        mIapInterface = new IAPInterface();
        mIapInterface.init(mIapDependencies, mIAPSettings);
    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.VERY_LIGHT, NavigationColor.VERY_DARK));
        getTheme().applyStyle(themeIndex, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIapLaunchInput = new IAPLaunchInput();
        mIapLaunchInput.setIapListener(this);
        init();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void actionBar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.demoScreen_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.iap_header_back_button);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        ImageView mBackImage = (ImageView) findViewById(R.id.iap_iv_header_back_button);
        Drawable mBackDrawable = VectorDrawable.create(getApplicationContext(), R.drawable.back_arrow);
        mBackImage.setBackground(mBackDrawable);
        mTitleTextView = (TextView) findViewById(R.id.iap_header_title);
        setTitle(getString(R.string.iap_app_name));

//        FrameLayout mCartContainer = (FrameLayout) findViewById(R.id.cart_container);
//        ImageView mCartIcon = (ImageView) findViewById(R.id.cart_icon);
//        Drawable mCartIconDrawable = VectorDrawable.create(getApplicationContext(), R.drawable.iap_shopping_cart);
        // mCartIcon.setBackground(mCartIconDrawable);
//        mCartIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showFragment(ShoppingCartFragment.TAG);
//            }
//        });

        mCountText = (TextView) findViewById(R.id.item_count);
    }

//    private void addActionBar() {
//        ActionBar mActionBar = getSupportActionBar();
//        if (mActionBar == null) return;
//        mActionBar.setDisplayShowHomeEnabled(false);
//        mActionBar.setDisplayShowTitleEnabled(false);
//        mActionBar.setDisplayShowCustomEnabled(true);
//        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
//                ActionBar.LayoutParams.MATCH_PARENT,
//                ActionBar.LayoutParams.WRAP_CONTENT,
//                Gravity.CENTER);
//
//        View mCustomView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.action_bar, null); // layout which contains your button.
//
//        FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.iap_header_back_button);
//        frameLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//                onBackPressed();
//            }
//        });
//        ImageView arrowImage = (ImageView) mCustomView.findViewById(R.id.iap_iv_header_back_button);
//        //noinspection deprecation
//        arrowImage.setBackground(getResources().getDrawable(R.drawable.back_arrow));
//
//        mTitleTextView = (TextView) mCustomView.findViewById(R.id.iap_header_title);
//        setTitle(getString(R.string.demo_app_name));
//
//        mCountText = (TextView) mCustomView.findViewById(R.id.item_count);
//
//        mActionBar.setCustomView(mCustomView, params);
//        Toolbar parent = (Toolbar) mCustomView.getParent();
//        parent.setContentInsetsAbsolute(0, 0);
//    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitleTextView.setText(title);
    }

    private void init() {

        mCategorizedProductList.clear();
        if (mUser.isUserSignIn()) {
            displayViews();
            showProgressDialog();
            try {
                if (!mIAPSettings.isUseLocalData())
                    mIapInterface.getProductCartCount(this);
                else
                    dismissProgressDialog();
            } catch (RuntimeException exception) {
                dismissProgressDialog();
            }
        } else {
            hideViews();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        mUser.unRegisterUserRegistrationListener(this);
        super.onDestroy();
    }

    private void launchIAP(int pLandingViews, IAPFlowInput pIapFlowInput) {
        if (isNetworkAvailable(mApplicationContext)) {
            mIapLaunchInput.setIAPFlow(pLandingViews, pIapFlowInput);
            try {
                showProgressDialog();
                mIapInterface.launch(new ActivityLauncher
                                (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, DEFAULT_THEME),
                        mIapLaunchInput);
            } catch (RuntimeException exception) {
                dismissProgressDialog();
                Toast.makeText(DemoAppActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(DemoAppActivity.this, "Network unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(final View view) {
        if (view == mShoppingCart) {
            launchIAP(IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW, null);
        } else if (view == mShopNow) {
            launchIAP(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, null);
        } else if (view == mPurchaseHistory) {
            launchIAP(IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW, null);
        } else if (view == mLaunchProductDetail) {
            IAPFlowInput iapFlowInput =
                    new IAPFlowInput(mEtCTN.getText().toString().toUpperCase().replaceAll("\\s+", ""));
            launchIAP(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, iapFlowInput);
            mEtCTN.setText("");
            hideKeypad(this);
        } else if (view == mShopNowCategorized) {
            if (mCategorizedProductList.size() > 0) {
                IAPFlowInput input = new IAPFlowInput(mCategorizedProductList);
                launchIAP(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, input);
            } else {
                Toast.makeText(DemoAppActivity.this, "Please add CTN", Toast.LENGTH_SHORT).show();
            }
        } else if (view == mBuyDirect) {
            IAPFlowInput iapFlowInput =
                    new IAPFlowInput(mEtCTN.getText().toString().toUpperCase().replaceAll("\\s+", ""));
            launchIAP(IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW, iapFlowInput);
            mEtCTN.setText("");
            hideKeypad(this);
        } else if (view == mRegister) {
            mApplicationContext.getAppInfra().getTagging().setPreviousPage("demoapp:home");
            //RegistrationHelper.getInstance().getAppTaggingInterface().setPreviousPage("demoapp:home");
            URLaunchInput urLaunchInput = new URLaunchInput();
            urLaunchInput.setRegistrationFunction(RegistrationFunction.SignIn);
            urLaunchInput.setUserRegistrationUIEventListener(this);
            urLaunchInput.setEndPointScreen(RegistrationLaunchMode.DEFAULT);
            urLaunchInput.setAccountSettings(false);
            URInterface urInterface = new URInterface();
            urInterface.launch(new ActivityLauncher(ActivityLauncher.
                    ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0), urLaunchInput);
        } else if (view == mLaunchFragment) {
            Intent intent = new Intent(this, LauncherFragmentActivity.class);
            this.startActivity(intent);
        } else if (view == mAddCtn) {
            String str = mEtCTN.getText().toString().toUpperCase().replaceAll("\\s+", "");
            if (!mCategorizedProductList.contains(str)) {
                mCategorizedProductList.add(str);
            }
            mEtCTN.setText("");
        }
    }

    private void updateCartIcon() {
        if (mIAPSettings.isUseLocalData()) {
            mShoppingCart.setVisibility(View.INVISIBLE);
        }
    }

    private void displayViews() {
        mAddCTNLl.setVisibility(View.VISIBLE);
        mShopNowCategorized.setVisibility(View.VISIBLE);
        mShopNow.setVisibility(View.VISIBLE);
        mShopNow.setEnabled(true);
        mLaunchProductDetail.setVisibility(View.VISIBLE);
        mLaunchProductDetail.setEnabled(true);

        if (!mIAPSettings.isUseLocalData()) {
            showProgressDialog();
            try {
                mIapInterface.getProductCartCount(this);
            } catch (RuntimeException e) {
                Toast.makeText(DemoAppActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                dismissProgressDialog();
            }

            updateCartIcon();
            mPurchaseHistory.setVisibility(View.VISIBLE);
            mPurchaseHistory.setEnabled(true);
            mBuyDirect.setVisibility(View.VISIBLE);
            mLaunchFragment.setVisibility(View.VISIBLE);
            mShoppingCart.setVisibility(View.VISIBLE);
        } else {
            mPurchaseHistory.setVisibility(View.GONE);
            mBuyDirect.setVisibility(View.GONE);
            mLaunchFragment.setVisibility(View.GONE);
            mShoppingCart.setVisibility(View.GONE);
        }
    }

    private void hideViews() {
        mCountText.setVisibility(View.GONE);
        mLaunchFragment.setVisibility(View.GONE);
        mShoppingCart.setVisibility(View.INVISIBLE);
        mAddCTNLl.setVisibility(View.GONE);
        mShopNow.setVisibility(View.GONE);
        mBuyDirect.setVisibility(View.GONE);
        mLaunchProductDetail.setVisibility(View.GONE);
        mPurchaseHistory.setVisibility(View.GONE);
        mShopNowCategorized.setVisibility(View.GONE);
    }

    private void showAppVersion() {
        String code = null;
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

    public void hideKeypad(Context pContext) {
        InputMethodManager inputMethodManager = (InputMethodManager)
                pContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (null != ((Activity) pContext).getCurrentFocus()) {
            inputMethodManager.hideSoftInputFromWindow(((Activity) pContext).getCurrentFocus().getWindowToken(),
                    0);
        }
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(getString(R.string.iap_please_wait) + "...");
        }
        if ((!mProgressDialog.isShowing()) && !isFinishing()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.show();
                }
            });

        }
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing() && !isFinishing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private void showToast(int errorCode) {
        String errorText = "Server error";
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

    //In-App listener functions
    @Override
    public void onGetCartCount(int count) {
        if (count > 0) {
            mCountText.setText(String.valueOf(count));
            mCountText.setVisibility(View.VISIBLE);
        } else if (count == 0) {
            mCountText.setVisibility(View.GONE);
        } else if (count == -1) {
            //Plan B
            mShoppingCart.setVisibility(View.GONE);
        }

        dismissProgressDialog();
        mIapInterface.getCompleteProductList(this);
    }

    @Override
    public void onUpdateCartCount() {
        mIapInterface.getProductCartCount(this);
    }

    @Override
    public void updateCartIconVisibility(boolean shouldShow) {
        if (shouldShow) {
            mShoppingCart.setVisibility(View.VISIBLE);
            mCountText.setVisibility(View.VISIBLE);
        } else {
            mShoppingCart.setVisibility(View.INVISIBLE);
            mCountText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onGetCompleteProductList(ArrayList<String> productList) {
        dismissProgressDialog();
    }

    @Override
    public void onSuccess() {
        dismissProgressDialog();
    }

    @Override
    public void onFailure(int errorCode) {
        dismissProgressDialog();
        showToast(errorCode);
    }

    //User Registration interface functions
    @Override
    public void onUserRegistrationComplete(Activity activity) {
        activity.finish();
        displayViews();

        IAPDependencies mIapDependencies = new IAPDependencies(new AppInfra.Builder().build(this));
        mIapInterface = new IAPInterface();
        mIapInterface.init(mIapDependencies, mIAPSettings);
        mIapLaunchInput = new IAPLaunchInput();
        mIapLaunchInput.setIapListener(this);
        init();
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
        mIapLaunchInput.setIapListener(null);
    }

    @Override
    public void onUserLogoutFailure() {

    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {

    }
}