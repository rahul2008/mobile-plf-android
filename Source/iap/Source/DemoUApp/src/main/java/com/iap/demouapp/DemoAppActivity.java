
package com.iap.demouapp;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
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
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;

import java.util.ArrayList;

import static com.philips.cdp.di.iap.utils.Utility.hideKeypad;


public class DemoAppActivity extends AppCompatActivity implements View.OnClickListener, IAPListener,
        UserRegistrationUIEventListener, UserRegistrationListener {

    private final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;
    // private DemoApplication mApplicationContext;

    private LinearLayout mAddCTNLl;

    private FrameLayout mShoppingCart;
    private EditText mEtCTN;

    private Button mRegister;
    private Button mShopNow;
    private Button mShopNowCategorized;
    private Button mBuyDirect;
    private Button mPurchaseHistory;
    private Button mLaunchProductDetail;
    private Button mAddCtn;
    private Button mShopNowCategorizedWithRetailer;
    private ArrayList<String> mCategorizedProductList;

    private TextView mTitleTextView;
    private TextView mCountText;

    private IAPInterface mIapInterface;
    private IAPLaunchInput mIapLaunchInput;
    private IAPSettings mIAPSettings;
    private User mUser;
    ImageView mCartIcon;
    Boolean isCartVisible;

    private ArrayList<String> ignorelistedRetailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);


        ignorelistedRetailer = new ArrayList<>();
        IAPLog.enableLogging(true);
        setContentView(R.layout.demo_app_layout);

        showAppVersion();
        mEtCTN = findViewById(R.id.et_add_ctn);
        mAddCTNLl = findViewById(R.id.ll_ctn);

        mRegister = findViewById(R.id.btn_register);
        mRegister.setOnClickListener(this);

        mBuyDirect = findViewById(R.id.btn_buy_direct);
        mBuyDirect.setOnClickListener(this);

        mShopNow = findViewById(R.id.btn_shop_now);
        mShopNow.setOnClickListener(this);

        mPurchaseHistory = findViewById(R.id.btn_purchase_history);
        mPurchaseHistory.setOnClickListener(this);

        mLaunchProductDetail = findViewById(R.id.btn_launch_product_detail);
        mLaunchProductDetail.setOnClickListener(this);

        mShoppingCart = findViewById(R.id.shopping_cart_icon);
        mShoppingCart.setOnClickListener(this);

        mShopNowCategorized = findViewById(R.id.btn_categorized_shop_now);
        mShopNowCategorized.setOnClickListener(this);

        mAddCtn = findViewById(R.id.btn_add_ctn);
        mAddCtn.setOnClickListener(this);

        mShopNowCategorizedWithRetailer = findViewById(R.id.btn_categorized_shop_now_with_ignore_retailer);
        mShopNowCategorizedWithRetailer.setOnClickListener(this);

        mCartIcon = findViewById(R.id.cart_iv);
        mCountText = findViewById(R.id.item_count);

        mCategorizedProductList = new ArrayList<>();
        showScreenSizeInDp();
        // mApplicationContext.getAppInfra().getTagging().setPreviousPage("demoapp:");
        mUser = new User(this);
        mUser.registerUserRegistrationListener(this);
        //Integration interface
        mIapInterface = new IAPInterface();
        mIAPSettings = new IAPSettings(this);
        // enableViews();
        actionBar();
        initIAP();

    }

    private void initIAP() {
        ignorelistedRetailer.add("Frys.com");
        ignorelistedRetailer.add("Amazon - US");
        ignorelistedRetailer.add("BestBuy.com");
        IAPDependencies mIapDependencies = new IAPDependencies(new AppInfra.Builder().build(this));
        mIapInterface.init(mIapDependencies, mIAPSettings);
        mIapLaunchInput = new IAPLaunchInput();
        mIapLaunchInput.setIapListener(this);
        //ignorelistedRetailer.add("John Lewis ");
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mUser != null && mUser.isUserSignIn()) {
            mRegister.setText(this.getString(R.string.log_out));
            displayUIOnCartVisible();
        } else {
            mRegister.setVisibility(View.VISIBLE);
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
        }

        mIapLaunchInput = new IAPLaunchInput();
        mIapLaunchInput.setIapListener(this);
    }

    private void displayUIOnCartVisible() {
        mIapInterface.isCartVisible(this);
      //  mIapInterface.getCompleteProductList(this);
//        onResumeRetailer();

    }

    private void onResumeRetailer(){
        mAddCTNLl.setVisibility(View.VISIBLE);
        mShopNowCategorizedWithRetailer.setVisibility(View.VISIBLE);
        mShopNowCategorizedWithRetailer.setText(String.format(getString(R.string.categorized_shop_now_ignore_retailer), ignorelistedRetailer.get(0)));
        mShopNowCategorized.setVisibility(View.VISIBLE);
        mLaunchProductDetail.setVisibility(View.VISIBLE);
        mLaunchProductDetail.setEnabled(true);
        mCartIcon.setVisibility(View.GONE);
        mCountText.setVisibility(View.GONE);
        mShopNow.setVisibility(View.GONE);
        mPurchaseHistory.setVisibility(View.GONE);
        mShoppingCart.setVisibility(View.GONE);
    }
    private void displayFlowViews(boolean b) {

        mAddCTNLl.setVisibility(View.VISIBLE);
        mShopNowCategorizedWithRetailer.setVisibility(View.VISIBLE);
        mShopNowCategorizedWithRetailer.setText(String.format(getString(R.string.categorized_shop_now_ignore_retailer), ignorelistedRetailer.get(0)));
        mShopNowCategorized.setVisibility(View.VISIBLE);
        mLaunchProductDetail.setVisibility(View.VISIBLE);
        mLaunchProductDetail.setEnabled(true);
        if (b) {
            mCartIcon.setVisibility(View.VISIBLE);
            mCountText.setVisibility(View.VISIBLE);
            try {
                mIapInterface.getCompleteProductList(this);
            } catch (RuntimeException e) {

            }
            mShopNow.setVisibility(View.VISIBLE);
            mShopNow.setEnabled(true);
            mPurchaseHistory.setVisibility(View.VISIBLE);
            mPurchaseHistory.setEnabled(true);
            mShoppingCart.setVisibility(View.VISIBLE);
            mIapInterface.getProductCartCount(this);
        } else {
            mCartIcon.setVisibility(View.GONE);
            mCountText.setVisibility(View.GONE);
            mShopNow.setVisibility(View.GONE);
            mPurchaseHistory.setVisibility(View.GONE);
            mShoppingCart.setVisibility(View.GONE);
        }

    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));

    }

    /*
    * CA6702/00
    CA6700/47
    DL8791/00
    DIS362/03
    DL8781/37
    DL8760/37
    HD8967/47
    HD8645/47
    * */
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mUser.isUserSignIn()) {
//            displayUIOnCartVisible();
//        }
//    }
//
//    private void enableViews() {
//        if (!mUser.isUserSignIn()) {
//            hideViews();
//            return;
//        }
//        displayViews();
//        // if (!mIAPSettings.isUseLocalData()) {
//
//        // }
//        showScreenSizeInDp();
//    }

    @Override
    protected void onStop() {
        super.onStop();
        mCategorizedProductList.clear();
    }

    private void actionBar() {
        FrameLayout frameLayout = findViewById(R.id.iap_header_back_button);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        ImageView mBackImage = findViewById(R.id.iap_iv_header_back_button);
        Drawable mBackDrawable = VectorDrawableCompat.create(getResources(), R.drawable.back_arrow, getTheme());
        mBackImage.setBackground(mBackDrawable);
        mTitleTextView = findViewById(R.id.iap_header_title);
        setTitle(getString(R.string.iap_app_name));

//         mCartIcon = (ImageView) findViewById(R.id.cart_iv);

        //if (!mIAPSettings.isUseLocalData()) {
//        mCartIcon.setVisibility(View.VISIBLE);
//        mCountText.setVisibility(View.VISIBLE);
        Drawable mCartIconDrawable = VectorDrawableCompat.create(getResources(), R.drawable.iap_shopping_cart, getTheme());
        mCartIcon.setBackground(mCartIconDrawable);
        mShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // showFragment(ShoppingCartFragment.TAG);
                launchIAP(IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW, null, null);
            }
        });


//        } else {
//            mCartIcon.setVisibility(View.GONE);
//            mCountText.setVisibility(View.GONE);
//        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitleTextView.setText(title);
    }

    @Override
    protected void onDestroy() {
//        dismissProgressDialog();
        mUser.unRegisterUserRegistrationListener(this);
        super.onDestroy();
    }

    private void launchIAP(int pLandingViews, IAPFlowInput pIapFlowInput, ArrayList<String> pIgnoreRetailerList) {
        if (pIgnoreRetailerList == null)
            mIapLaunchInput.setIAPFlow(pLandingViews, pIapFlowInput);
        else
            mIapLaunchInput.setIAPFlow(pLandingViews, pIapFlowInput, pIgnoreRetailerList);

        try {
            mIapInterface.launch(new ActivityLauncher
                            (ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, DEFAULT_THEME),
                    mIapLaunchInput);

        } catch (RuntimeException exception) {
            Toast.makeText(DemoAppActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(final View view) {
        if (view == mShoppingCart) {
            launchIAP(IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW, null, null);
        } else if (view == mShopNow) {
            launchIAP(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, null, null);
        } else if (view == mPurchaseHistory) {
            launchIAP(IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW, null, null);
        } else if (view == mLaunchProductDetail) {
            IAPFlowInput iapFlowInput =
                    new IAPFlowInput(mEtCTN.getText().toString().toUpperCase().replaceAll("\\s+", ""));
            launchIAP(IAPLaunchInput.IAPFlows.IAP_PRODUCT_DETAIL_VIEW, iapFlowInput, null);
            mEtCTN.setText("");
            hideKeypad(this);
        } else if (view == mShopNowCategorized) {
            if (mCategorizedProductList.size() > 0) {
                IAPFlowInput input = new IAPFlowInput(mCategorizedProductList);
                launchIAP(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, input, null);
            } else {
                Toast.makeText(DemoAppActivity.this, "Please add CTN", Toast.LENGTH_SHORT).show();
            }
        } else if (view == mShopNowCategorizedWithRetailer) {
            if (mCategorizedProductList.size() > 0) {
                IAPFlowInput input = new IAPFlowInput(mCategorizedProductList);
                Toast.makeText(this, "Given retailer list will ignore in" + ignorelistedRetailer.get(0) + "Retailer list Screen.", Toast.LENGTH_SHORT).show();
                launchIAP(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, input, ignorelistedRetailer);
            } else {
                Toast.makeText(DemoAppActivity.this, "Please add CTN", Toast.LENGTH_SHORT).show();
            }
        } else if (view == mBuyDirect) {
            IAPFlowInput iapFlowInput =
                    new IAPFlowInput(mEtCTN.getText().toString().toUpperCase().replaceAll("\\s+", ""));
            launchIAP(IAPLaunchInput.IAPFlows.IAP_BUY_DIRECT_VIEW, iapFlowInput, null);
            mEtCTN.setText("");
            hideKeypad(this);
        } else if (view == mRegister) {
            // mApplicationContext.getAppInfra().getTagging().setPreviousPage("demoapp:home");
            //RegistrationHelper.getInstance().getAppTaggingInterface().setPreviousPage("demoapp:home");
            if (mRegister.getText().toString().equalsIgnoreCase(this.getString(R.string.log_out))) {
                if (mUser.isUserSignIn()) {
                    mUser.logout(new LogoutHandler() {
                        @Override
                        public void onLogoutSuccess() {

                            finish();
                        }

                        @Override
                        public void onLogoutFailure(int i, String s) {

                            Toast.makeText(DemoAppActivity.this, "Logout went wrong", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(DemoAppActivity.this, "User is not logged in", Toast.LENGTH_SHORT).show();
                }
            } else {

                gotoLogInScreen();
            }

        } else if (view == mAddCtn) {
            String str = mEtCTN.getText().toString().toUpperCase().replaceAll("\\s+", "");
            if (!mCategorizedProductList.contains(str)) {
                mCategorizedProductList.add(str);
            }
            mEtCTN.setText("");
            hideKeypad(this);
        }
    }

    private void gotoLogInScreen() {
        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setRegistrationFunction(RegistrationFunction.SignIn);
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.setEndPointScreen(RegistrationLaunchMode.DEFAULT);
        URInterface urInterface = new URInterface();
        urInterface.launch(new ActivityLauncher(ActivityLauncher.
                ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0), urLaunchInput);
    }

//    private void updateCartIcon() {
////        if (mIAPSettings.isUseLocalData()) {
////            mShoppingCart.setVisibility(View.GONE);
////        } else {
//        mShoppingCart.setVisibility(View.VISIBLE);
////        }
//    }

    private void displayViews() {
        mAddCTNLl.setVisibility(View.VISIBLE);
        mShopNowCategorized.setVisibility(View.VISIBLE);
        mShopNowCategorizedWithRetailer.setVisibility(View.VISIBLE);
        mShopNowCategorizedWithRetailer.setText(String.format(getString(R.string.categorized_shop_now_ignore_retailer), ignorelistedRetailer.get(0)));
        mShopNow.setVisibility(View.VISIBLE);
        mShopNow.setEnabled(true);
        mLaunchProductDetail.setVisibility(View.VISIBLE);
        mLaunchProductDetail.setEnabled(true);
//        mPurchaseHistory.setVisibility(View.VISIBLE);
//        mPurchaseHistory.setEnabled(true);


    }

    private void hideViews() {
        mCountText.setVisibility(View.GONE);
        //mLaunchFragment.setVisibility(View.GONE);
        mShoppingCart.setVisibility(View.GONE);
        mAddCTNLl.setVisibility(View.GONE);
        mShopNow.setVisibility(View.GONE);
        mBuyDirect.setVisibility(View.GONE);
        mLaunchProductDetail.setVisibility(View.GONE);
        mPurchaseHistory.setVisibility(View.GONE);
        mShopNowCategorized.setVisibility(View.GONE);
        mShopNowCategorizedWithRetailer.setVisibility(View.GONE);
    }

    private void showAppVersion() {
        String code = null;
        try {
            code = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            IAPLog.e(IAPLog.LOG, e.getMessage());
        }
        TextView versionView = findViewById(R.id.appversion);
        versionView.setText(String.valueOf(code));
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
            mCountText.setText(count + "");
            mCountText.setVisibility(View.VISIBLE);
        } else if (count == 0) {
            mCountText.setVisibility(View.GONE);
        } else if (count == -1) {
            //Plan B
            mShoppingCart.setVisibility(View.GONE);
        }

        try {
            mIapInterface.getCompleteProductList(this);
        } catch (Exception e) {
            IAPLog.e(IAPLog.LOG, e.getMessage());
        }

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
            mShoppingCart.setVisibility(View.GONE);
            mCountText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetCompleteProductList(ArrayList<String> productList) {
        Toast.makeText(this, "Fetched product list done", Toast.LENGTH_SHORT).show();
//        mEtCTN.setText(productList.get(1));
//        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add(productList.get(1));
//        IAPFlowInput input = new IAPFlowInput(arrayList);
//        launchIAP(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, input);
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onSuccess(boolean bool) {
        displayFlowViews(bool);
    }

    @Override
    public void onFailure(int errorCode) {
        showToast(errorCode);
    }


    //User Registration interface functions
    @Override
    public void onUserRegistrationComplete(Activity activity) {
        activity.finish();
        mRegister.setText(this.getString(R.string.log_out));
        // displayUIOnCartVisible();
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

    void showScreenSizeInDp() {

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        Toast.makeText(this, "Screen width in dp is :" + dpWidth, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
