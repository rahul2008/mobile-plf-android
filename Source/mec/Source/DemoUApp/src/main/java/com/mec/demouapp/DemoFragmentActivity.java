package com.mec.demouapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.philips.cdp.di.mec.integration.MECBannerEnabler;
import com.philips.cdp.di.mec.integration.MECDependencies;
import com.philips.cdp.di.mec.integration.MECFlowInput;
import com.philips.cdp.di.mec.integration.MECInterface;
import com.philips.cdp.di.mec.integration.MECLaunchInput;
import com.philips.cdp.di.mec.integration.MECListener;
import com.philips.cdp.di.mec.integration.MECSettings;
import com.philips.cdp.di.mec.utils.MECConstant;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.RegistrationContentConfiguration;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutSessionListener;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class DemoFragmentActivity extends AppCompatActivity implements View.OnClickListener, MECListener,
        UserRegistrationUIEventListener, MECBannerEnabler, ActionBarListener {

    private final String TAG = DemoActivity.class.getSimpleName();
    private final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;
    private LinearLayout mAddCTNLl, mLL_voucher;
    private FrameLayout mShoppingCart;
    private EditText mEtCTN, mEtVoucherCode, mEtPropositionId;

    private Button mRegister;
    private Button mShopNow;
    private Button mShopNowCategorized;
    private Button mBuyDirect;
    private Button mPurchaseHistory;
    private Button mLaunchProductDetail;
    private Button mAddCtn, mBtn_add_voucher, mBtnSetPropositionId;
    private Button mShopNowCategorizedWithRetailer;
    private ProgressDialog mProgressDialog = null;
    private ArrayList<String> mCategorizedProductList;
    private TextView mTitleTextView;
    private TextView mCountText;

    private MECInterface mMecInterface;
    private MECLaunchInput mMecLaunchInput;
    private MECSettings mMecSettings;

    private UserDataInterface mUserDataInterface;
    ImageView mCartIcon;
    Boolean isCartVisible;
    String voucherCode;

    private ArrayList<String> ignorelistedRetailer;
    private View mLL_propositionId;
    URInterface urInterface;
    private long mLastClickTime = 0;
    private ToggleButton toggleMock;

    EditText mEtMaxCartCount;
    private ToggleButton toggleHybris;
    private boolean isHybrisEnable = true;
    private ToggleButton toggleBanner;
    private boolean isBannerEnabled = false;
    private ToggleButton toggleListener;
    private boolean isToggleListener = false;
    private RadioGroup rgVoucher,rgLauncher;

    private RelativeLayout DemoContentBody;
    private FrameLayout fragmentContainer;
    private ImageView mBackImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);

        urInterface = new URInterface();
        urInterface.init(new MecDemoUAppDependencies(new AppInfra.Builder().build(getApplicationContext())), new MecDemoAppSettings(getApplicationContext()));

        ignorelistedRetailer = new ArrayList<>();
        setContentView(R.layout.fragment_activity_demo);

        showAppVersion();
        mEtCTN = findViewById(R.id.et_add_ctn);
        mEtVoucherCode = findViewById(R.id.et_add_voucher);
        mAddCTNLl = findViewById(R.id.ll_ctn);


        mEtPropositionId = findViewById(R.id.et_add_proposition_id);
        mBtnSetPropositionId = findViewById(R.id.btn_set_proposition_id);


        //////////////////
        DemoContentBody  = findViewById(R.id.demo_content_layout);
        fragmentContainer  = findViewById(R.id.mec_fragment_container);

        /////////////////


        AppInfraInterface appInfra = new AppInfra.Builder().build(getApplicationContext());
        AppConfigurationInterface configInterface = appInfra.getConfigInterface();
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();

        String propertyForKey = (String) configInterface.getPropertyForKey("propositionid", "MEC", configError);
        mEtPropositionId.setText(propertyForKey);

        mBtnSetPropositionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                configInterface.setPropertyForKey("propositionid", "MEC", mEtPropositionId.getText().toString(), configError);

                Toast.makeText(DemoFragmentActivity.this, "Proposition id is set", Toast.LENGTH_SHORT).show();
                finishAffinity();
                System.exit(0);
            }
        });

        toggleMock = findViewById(R.id.toggleMock);



        toggleBanner = findViewById(R.id.toggleBanner);

        toggleBanner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                isBannerEnabled = isChecked;
                initializeMECComponant();
            }
        });

        toggleHybris = findViewById(R.id.toggleHybris);
        toggleHybris.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isHybrisEnable = isChecked;
                initializeMECComponant();
            }
        });

        rgVoucher = findViewById(R.id.rg_voucher);
        rgLauncher = findViewById(R.id.rg_launcher);



        rgLauncher.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_activity) {

                }
                if (checkedId == R.id.rb_fragment) {

                }
            }
        });


        toggleListener = findViewById(R.id.toggleListener);

        toggleListener.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isToggleListener = isChecked;
                initializeMECComponant();
            }
        });

        Button btnSetMaxCount = findViewById(R.id.btn_set_max_Count);

        btnSetMaxCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeMECComponant();
            }
        });

        mEtMaxCartCount = findViewById(R.id.et_max_cart_count);

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

        mShoppingCart = findViewById(R.id.mec_demo_app_shopping_cart_icon);


        mShopNowCategorized = findViewById(R.id.btn_categorized_shop_now);
        mShopNowCategorized.setOnClickListener(this);


        mLL_voucher = findViewById(R.id.ll_voucher);
        mLL_propositionId = findViewById(R.id.ll_enter_proposition_id);

        mAddCtn = findViewById(R.id.btn_add_ctn);
        mAddCtn.setOnClickListener(this);

        mBtn_add_voucher = findViewById(R.id.btn_add_voucher);
        mBtn_add_voucher.setOnClickListener(this);

        mShopNowCategorizedWithRetailer = findViewById(R.id.btn_categorized_shop_now_with_ignore_retailer);
        mShopNowCategorizedWithRetailer.setOnClickListener(this);

        mCartIcon = findViewById(R.id.mec_demo_app_cart_iv);
        mCountText = findViewById(R.id.mec_demo_app_item_count);

        mCategorizedProductList = new ArrayList<>();



        //  ["HD9745/90","HD9630/90","HD9240/90","HD9621/90","HD9651/90","HD9650/90R1","HD9652/90","HD9910/20","HD9654/90",
        //  "HD9216/80","HD9630/20","HD9220/20","HD9621/80","HD9750/90","HD9750/20","HD9762/90","HD9216/80R1","HD9621/70","HD9741/10"]

       /* mCategorizedProductList.add("HD9745/90000");
        mCategorizedProductList.add("HD9630/90");
        mCategorizedProductList.add("HD9240/90");
        mCategorizedProductList.add("HD9621/90");
        mCategorizedProductList.add("HD9651/90");
        mCategorizedProductList.add("HD9650/90");
        mCategorizedProductList.add("HD9910/20");
        mCategorizedProductList.add("HD9654/90");
        mCategorizedProductList.add("HD9216/80");
        mCategorizedProductList.add("HD9630/20");
        mCategorizedProductList.add("HD9220/20");
        mCategorizedProductList.add("HD9621/80");
        mCategorizedProductList.add("HD9750/20");
        mCategorizedProductList.add("HD9216/80R1");
        mCategorizedProductList.add("HD9621/70");
        mCategorizedProductList.add("HD9741/10");*/

        mUserDataInterface = urInterface.getUserDataInterface();


        mMecInterface = new MECInterface();
        mMecSettings = new MECSettings(this);

        Toolbar toolbar = findViewById(R.id.demoScreen_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar();
        initializeMECComponant();
    }

    private void initializeMECComponant() {
        toggleHybris.setVisibility(View.VISIBLE);
        initMEC();

        if (mUserDataInterface != null && mUserDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN) {
            mRegister.setText(this.getString(R.string.log_out));
        } else {
            mRegister.setVisibility(View.VISIBLE);
            // Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void initMEC() {
        ignorelistedRetailer.add("Frys.com");
        ignorelistedRetailer.add("Amazon - US");
        ignorelistedRetailer.add("BestBuy.com");

        UappDependencies uappDependencies = new UappDependencies(new AppInfra.Builder().build(this));
        UappSettings uappSettings = new UappSettings(getApplicationContext());

        urInterface.init(uappDependencies, uappSettings);

        MECDependencies mIapDependencies = new MECDependencies(new AppInfra.Builder().build(this), urInterface.getUserDataInterface());

        try {
            mMecInterface.init(mIapDependencies, mMecSettings);
        } catch (RuntimeException ex) {
        }
        mMecLaunchInput = new MECLaunchInput();

        if (!TextUtils.isEmpty(mEtMaxCartCount.getText().toString().trim())) {
        }
        mMecLaunchInput.setMecListener(this);
        if (isToggleListener) {
        } else {
        }
        //displayUIOnCartVisible();
    }

    /*private void displayUIOnCartVisible() {
        if(isUserLoggedIn()) {
            showProgressDialog();
            mMecInterface.isCartVisible(this);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        //This is added to clear pagination data from app memory . This should be taken in tech debt .
        //CartModelContainer.getInstance().clearProductList();
        MECUtility.getInstance().resetPegination();


        if(isUserLoggedIn()) {
            try {
               // mIapInterface.getProductCartCount(this);
            }catch (Exception e){

            }
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();
        mShoppingCart.setOnClickListener(this);
    }

    private void onResumeRetailer() {
        mAddCTNLl.setVisibility(View.VISIBLE);
        mLL_voucher.setVisibility(View.VISIBLE);
        mLL_propositionId.setVisibility(View.VISIBLE);
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
        mLL_voucher.setVisibility(View.VISIBLE);
        mLL_propositionId.setVisibility(View.VISIBLE);
        //mShopNowCategorizedWithRetailer.setVisibility(View.VISIBLE);
        //mShopNowCategorizedWithRetailer.setText(String.format(getString(R.string.categorized_shop_now_ignore_retailer), ignorelistedRetailer.get(0)));
        mShopNowCategorized.setVisibility(View.VISIBLE);
        mLaunchProductDetail.setVisibility(View.VISIBLE);
        mLaunchProductDetail.setEnabled(true);


        mCartIcon.setVisibility(View.VISIBLE);
        mCountText.setVisibility(View.VISIBLE);
        mShopNow.setVisibility(View.VISIBLE);
        mShopNow.setEnabled(true);
        mPurchaseHistory.setVisibility(View.VISIBLE);
        mPurchaseHistory.setEnabled(true);
        mShoppingCart.setVisibility(View.VISIBLE);

        dismissProgressDialog();
        // mIapInterface.getProductCartCount(this);

        if (b) {
            mCartIcon.setVisibility(View.VISIBLE);
            mCountText.setVisibility(View.VISIBLE);
            mShoppingCart.setVisibility(View.VISIBLE);
            //mMecInterface.getProductCartCount(this);
        } else {
            mCartIcon.setVisibility(View.GONE);
            mCountText.setVisibility(View.GONE);
            mShoppingCart.setVisibility(View.GONE);
            dismissProgressDialog();
        }

    }

    private void initTheme() {
        int themeResourceID = new ThemeHelper(this).getThemeResourceId();
        int themeIndex = themeResourceID;
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));

    }

    @Override
    protected void onStop() {
        super.onStop();
        mCategorizedProductList.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void actionBar() {
        FrameLayout frameLayout = findViewById(R.id.mec_demo_app_header_back_button_framelayout);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });


        mBackImage = findViewById(R.id.mec_demo_app_iv_header_back_button);
        Drawable mBackDrawable = VectorDrawableCompat.create(getResources(), R.drawable.mec_demo_app_back_arrow, getTheme());
        mBackImage.setBackground(mBackDrawable);
        mBackImage.setVisibility(View.GONE);
        mTitleTextView = findViewById(R.id.mec_demo_app_header_title);
        setTitle(getString(R.string.mec_app_name));

        Drawable mShoppingCartDrawable = VectorDrawableCompat.create(getResources(), R.drawable.mec_demo_app_shopping_cart_xml, getTheme());
        mShoppingCart.setBackground(mShoppingCartDrawable);
        mShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickable())
                    launchMECasFragment(MECLaunchInput.MECFlows.Companion.getMEC_SHOPPING_CART_VIEW(), null, null);
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitleTextView.setText(title);
    }

/*
    private void launchMEC(int pLandingViews, MECFlowInput pMecFlowInput, ArrayList<String> pIgnoreRetailerList) {

        if (pIgnoreRetailerList == null)
            mMecLaunchInput.setMECFlow(pLandingViews, pMecFlowInput, voucherCode);
        else
            mMecLaunchInput.setMECFlow(pLandingViews, pMecFlowInput, voucherCode, pIgnoreRetailerList);

        try {
            int themeResourceID = new ThemeHelper(this).getThemeResourceId();
            mMecInterface.launch(new ActivityLauncher
                            (this, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, null, themeResourceID, null),
                    mMecLaunchInput);

        } catch (RuntimeException exception) {
            Toast.makeText(DemoFragmentActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
*/

    private void launchMECasFragment(int pLandingViews, MECFlowInput pMecFlowInput, ArrayList<String> pIgnoreRetailerList) {

        if (pIgnoreRetailerList == null)
            mMecLaunchInput.setMECFlow(pLandingViews, pMecFlowInput, voucherCode);
        else
            mMecLaunchInput.setMECFlow(pLandingViews, pMecFlowInput, voucherCode, pIgnoreRetailerList);

        try {

            DemoContentBody.setVisibility(View.GONE);
            fragmentContainer.setVisibility(View.VISIBLE);
            mMecInterface.launch(new FragmentLauncher(this, R.id.mec_fragment_container, this),
                    mMecLaunchInput);

        } catch (RuntimeException exception) {
            Toast.makeText(DemoFragmentActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(final View view) {
        if (!isClickable()) return;

        if (view == mShoppingCart) {

        } else if (view == mShopNow) {
            launchMECasFragment(MECLaunchInput.MECFlows.Companion.getMEC_PRODUCT_CATALOG_VIEW(), null, null);
        } else if (view == mPurchaseHistory) {

        } else if (view == mLaunchProductDetail) {


        } else if (view == mShopNowCategorized) {
            if (mCategorizedProductList.size() > 0) {
                MECFlowInput input = new MECFlowInput(mCategorizedProductList);
                launchMECasFragment(MECLaunchInput.MECFlows.Companion.getMEC_PRODUCT_CATALOG_VIEW(), input, null);
            } else {
                Toast.makeText(DemoFragmentActivity.this, "Please add CTN", Toast.LENGTH_SHORT).show();
            }
        } else if (view == mShopNowCategorizedWithRetailer) {

        } else if (view == mBuyDirect) {

        } else if (view == mRegister) {
            if (mRegister.getText().toString().equalsIgnoreCase(this.getString(R.string.log_out))) {
                if (mUserDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN) {
                    mUserDataInterface.logoutSession(new LogoutSessionListener() {
                        @Override
                        public void logoutSessionSuccess() {
                            finish();
                        }

                        @Override
                        public void logoutSessionFailed(Error error) {
                            Toast.makeText(DemoFragmentActivity.this, "Logout went wrong", Toast.LENGTH_SHORT).show();
                        }

                    });
                } else {
                    Toast.makeText(DemoFragmentActivity.this, "User is not logged in", Toast.LENGTH_SHORT).show();
                }
            } else
            {

                gotoLogInScreen();
            }

        } else if (view == mAddCtn) {
            String str = mEtCTN.getText().toString().toUpperCase().replaceAll("\\s+", "");
            if (!mCategorizedProductList.contains(str)) {
                mCategorizedProductList.add(str);
            }
            mEtCTN.setText("");
            //hideKeypad(this);
        } else if (view == mBtn_add_voucher) {
            if (mEtVoucherCode.getText().toString().length() > 0) {
                voucherCode = mEtVoucherCode.getText().toString();
            }
            mEtVoucherCode.setText("");
        }
    }

    private void gotoLogInScreen() {

        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.enableAddtoBackStack(true);
        RegistrationContentConfiguration contentConfiguration = new RegistrationContentConfiguration();
        contentConfiguration.enableLastName(true);
        contentConfiguration.enableContinueWithouAccount(true);
        RegistrationConfiguration.getInstance().setPrioritisedFunction(RegistrationFunction.Registration);
        urLaunchInput.setRegistrationContentConfiguration(contentConfiguration);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);


        ActivityLauncher activityLauncher = new ActivityLauncher(this, ActivityLauncher.
                ActivityOrientation.SCREEN_ORIENTATION_SENSOR, null, 0, null);
        urInterface.launch(activityLauncher, urLaunchInput);


    }

    private void displayViews() {
        mAddCTNLl.setVisibility(View.VISIBLE);
        mLL_voucher.setVisibility(View.VISIBLE);
        mLL_propositionId.setVisibility(View.VISIBLE);
        mShopNowCategorized.setVisibility(View.VISIBLE);
        mShopNowCategorizedWithRetailer.setVisibility(View.VISIBLE);
        mShopNowCategorizedWithRetailer.setText(String.format(getString(R.string.categorized_shop_now_ignore_retailer), ignorelistedRetailer.get(0)));
        mShopNow.setVisibility(View.VISIBLE);
        mShopNow.setEnabled(true);
        mLaunchProductDetail.setVisibility(View.VISIBLE);
        mLaunchProductDetail.setEnabled(true);
    }

    private void hideViews() {
        mCountText.setVisibility(View.GONE);
        mShoppingCart.setVisibility(View.GONE);
        mAddCTNLl.setVisibility(View.GONE);
        mLL_voucher.setVisibility(View.GONE);
        // mLL_propositionId.setVisibility(View.GONE);
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
            //IAPLog.e(IAPLog.LOG, e.getMessage());
        }
        TextView versionView = findViewById(R.id.appversion);
        versionView.setText(String.valueOf(code));
    }

    private void showToast(int errorCode) {
        String errorText = null;
        if (MECConstant.INSTANCE.getMEC_ERROR_NO_CONNECTION() == errorCode) {
            errorText = "No connection";
        } else if (MECConstant.INSTANCE.getMEC_ERROR_CONNECTION_TIME_OUT() == errorCode) {
            errorText = "Connection time out";
        } else if (MECConstant.INSTANCE.getMEC_ERROR_AUTHENTICATION_FAILURE() == errorCode) {
            errorText = "Authentication failure";
        } else if (MECConstant.INSTANCE.getMEC_ERROR_INSUFFICIENT_STOCK_ERROR() == errorCode) {
            errorText = "Product out of stock";
        } else if (MECConstant.INSTANCE.getMEC_ERROR_INVALID_CTN() == errorCode) {
            errorText = "Invalid ctn";
        }
        if (errorText != null) {
            Toast toast = Toast.makeText(this, errorText, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
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
            mMecInterface.getCompleteProductList(this);
        } catch (Exception e) {
        }

    }

    @Override
    public void onUpdateCartCount() {
        mMecInterface.getProductCartCount(this);
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
        mShoppingCart.setOnClickListener(this);
        dismissProgressDialog();
    }

    @Override
    public void onSuccess() {
        dismissProgressDialog();
    }

    @Override
    public void onSuccess(boolean bool) {
        displayFlowViews(bool);
    }

    @Override
    public void onFailure(int errorCode) {
        showToast(errorCode);
        dismissProgressDialog();
    }



    //User Registration interface functions
    @Override
    public void onUserRegistrationComplete(Activity activity) {
        activity.finish();
        initializeMECComponant();
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {
    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {
    }

    @Override
    public void onPersonalConsentClick(Activity activity) {

    }

    void showScreenSizeInDp() {

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
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

    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(UIDHelper.getPopupThemedContext(this));
        mProgressDialog.getWindow().setGravity(Gravity.CENTER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait" + "...");

        if ((!mProgressDialog.isShowing()) && !(DemoFragmentActivity.this).isFinishing()) {
            mProgressDialog.show();
            mProgressDialog.setContentView(R.layout.progressbar_dls);
        }
    }


    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    boolean isClickable() {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 1500) {
            return false;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        return true;
    }



    public JSONObject getResponseJson(String fileName) {

        String jsonString = loadJSONFromAsset(fileName);

        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            return null;
        } catch (Exception e) {
            return null;
        }
        return json;
    }


    @Override
    public View getBannerView() {
        if (isBannerEnabled) {
            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.banner_view, null);
            return v;
        }
        return null;
    }

    boolean isUserLoggedIn(){
        return  mUserDataInterface != null && mUserDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN ;
    }


    /////////// Action Bar Call backs /////////////////

    /**
     * For setting the title of action bar and to set back key Enabled/Disabled
     *
     * @param resId         The resource Id of the String to be displayed
     * @param enableBackKey To set back key enabled or disabled
     * @since 1.0.0
     */
    @Override
    public void updateActionBar(int resId, boolean enableBackKey) {
        updateActionBar(getString(resId),enableBackKey);
    }

    /**
     * For setting the title of action bar and to set back key Enabled/Disabled
     *
     * @param resString     The String to be displayed
     * @param enableBackKey To set back key enabled or disabled
     * @since 1.0.0
     */
    @Override
    public void updateActionBar(String resString, boolean enableBackKey) {
        mTitleTextView.setText(resString);
        if (enableBackKey) {
            mBackImage.setVisibility(View.VISIBLE);
            // For arabic, Hebrew and Perssian the back arrow change from left to right
            if((Locale.getDefault().getLanguage().contentEquals("ar")) || (Locale.getDefault().getLanguage().contentEquals("fa")) || (Locale.getDefault().getLanguage().contentEquals("he"))) {
                mBackImage.setRotation(180);
            }
        } else {
            mBackImage.setVisibility(View.GONE);
        }
    }





}

