
package com.ecs.demotestuapp.integration;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ecs.demotestuapp.R;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.integration.ECSOAuthProvider;
import com.philips.cdp.di.ecs.util.ECSConfiguration;
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
import com.philips.platform.pif.DataInterface.USR.UserDetailConstants;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pif.DataInterface.USR.listeners.RefreshSessionListener;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;



public class EcsDemoTestAppActivity extends AppCompatActivity implements View.OnClickListener,
        UserRegistrationUIEventListener {

    private final String TAG = EcsDemoTestAppActivity.class.getSimpleName();
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

    private UserDataInterface mUserDataInterface;
    ImageView mCartIcon;
    Boolean isCartVisible;
    String voucherCode;

    private ArrayList<String> ignorelistedRetailer;
    private View mLL_propositionId;
    URInterface urInterface;
    private long mLastClickTime = 0;
    private ToggleButton toggleMock;
    private boolean enableMock = false;
    EditText mEtMaxCartCount;
    private ToggleButton toggleHybris;
    private boolean isHybrisEnable = true;
    private ToggleButton toggleBanner;
    private boolean isBannerEnabled = false;
    private ToggleButton toggleListener;
    private boolean isToggleListener = false;
    private RadioGroup rgVoucher;

    private ECSServices ecsServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        urInterface = new URInterface();
        urInterface.init(new EcsDemoTestUAppDependencies(new AppInfra.Builder().build(getApplicationContext())), new EcsDemoTestAppSettings(getApplicationContext()));

        ignorelistedRetailer = new ArrayList<>();
        setContentView(R.layout.demo_app_layout);

        showAppVersion();
        mEtCTN = findViewById(R.id.et_add_ctn);
        mEtVoucherCode = findViewById(R.id.et_add_voucher);
        mAddCTNLl = findViewById(R.id.ll_ctn);


        mEtPropositionId = findViewById(R.id.et_add_proposition_id);
        mBtnSetPropositionId = findViewById(R.id.btn_set_proposition_id);


        AppInfraInterface appInfra = new AppInfra.Builder().build(getApplicationContext());
        AppConfigurationInterface configInterface = appInfra.getConfigInterface();
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();

        String propertyForKey = (String) configInterface.getPropertyForKey("propositionid", "IAP", configError);
        mEtPropositionId.setText(propertyForKey);



        mBtnSetPropositionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                configInterface.setPropertyForKey("propositionid", "IAP", mEtPropositionId.getText().toString(), configError);

                Toast.makeText(EcsDemoTestAppActivity.this, "Proposition id is set", Toast.LENGTH_SHORT).show();
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
                initializeIAPComponant();
            }
        });

        toggleHybris = findViewById(R.id.toggleHybris);
        toggleHybris.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isHybrisEnable = isChecked;
                initializeIAPComponant();
            }
        });

        rgVoucher = findViewById(R.id.rg_voucher);




        toggleListener = findViewById(R.id.toggleListener);

        toggleListener.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isToggleListener = isChecked;
                initializeIAPComponant();
            }
        });

        Button btnSetMaxCount = findViewById(R.id.btn_set_max_Count);

        btnSetMaxCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeIAPComponant();
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



        mCategorizedProductList = new ArrayList<>();



      //  ["HD9745/90","HD9630/90","HD9240/90","HD9621/90","HD9651/90","HD9650/90R1","HD9652/90","HD9910/20","HD9654/90",
        //  "HD9216/80","HD9630/20","HD9220/20","HD9621/80","HD9750/90","HD9750/20","HD9762/90","HD9216/80R1","HD9621/70","HD9741/10"]

        mCategorizedProductList.add("HD9745/90000");
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
        mCategorizedProductList.add("HD9741/10");

        mUserDataInterface = urInterface.getUserDataInterface();


        actionBar();
        initializeIAPComponant();


    }


    void refreshOauth(){


        mUserDataInterface.refreshSession(new RefreshSessionListener() {
            @Override
            public void refreshSessionSuccess() {


                //re OAuth after refreshSession for janrain

                ECSOAuthProvider oAuthInput = new ECSOAuthProvider() {
                    @Override
                    public String getOAuthID() {
                        return ECSConfiguration.INSTANCE.getAuthResponse().getRefreshToken();
                    }

                };



                // ReOAuth ends  =====================

            }

            @Override
            public void refreshSessionFailed(Error error) {

            }

            @Override
            public void forcedLogout() {

            }
        });


    }



    public String getMyJanRainID() {


        ArrayList<String> detailsKey = new ArrayList<>();
        detailsKey.add(UserDetailConstants.ACCESS_TOKEN);
        try {
            HashMap<String,Object> userDetailsMap = mUserDataInterface.getUserDetails(detailsKey);
            return userDetailsMap.get(UserDetailConstants.ACCESS_TOKEN).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initAouth() {

        if(isUserLoggedIn()) {


        }

    }

    private void initializeIAPComponant() {
        toggleHybris.setVisibility(View.VISIBLE);
        initIAP();

        if (mUserDataInterface != null && mUserDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN) {
            mRegister.setText("Log out");
        } else {
            mRegister.setVisibility(View.VISIBLE);
           // Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
        }

        ECSServices ecsServices = new ECSServices(mEtPropositionId.getText().toString().trim(), new AppInfra.Builder().build(getApplicationContext()));



    }

    private void initIAP() {
        ignorelistedRetailer.add("Frys.com");
        ignorelistedRetailer.add("Amazon - US");
        ignorelistedRetailer.add("BestBuy.com");

        UappDependencies uappDependencies = new UappDependencies(new AppInfra.Builder().build(this));
        UappSettings uappSettings = new UappSettings(getApplicationContext());

        urInterface.init(uappDependencies, uappSettings);

    }


    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mShoppingCart.setOnClickListener(this);
    }


    private void displayFlowViews(boolean b) {

        mAddCTNLl.setVisibility(View.VISIBLE);
        mLL_voucher.setVisibility(View.VISIBLE);
        mLL_propositionId.setVisibility(View.VISIBLE);
        mShopNowCategorizedWithRetailer.setVisibility(View.GONE);
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


        if (b) {
            mCartIcon.setVisibility(View.VISIBLE);
            mCountText.setVisibility(View.VISIBLE);
            mShoppingCart.setVisibility(View.VISIBLE);
        } else {
            mCartIcon.setVisibility(View.GONE);
            mCountText.setVisibility(View.GONE);
            mShoppingCart.setVisibility(View.GONE);
        }

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
        setTitle("ECS Demo Test App");
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitleTextView.setText(title);
    }



    @Override
    public void onClick(final View view) {
        if (!isClickable()) return;

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


    private void showAppVersion() {
        String code = null;
        try {
            code = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        TextView versionView = findViewById(R.id.appversion);
        versionView.setText(String.valueOf(code));
    }

    private void showToast(int errorCode) {
        String errorText = null;

    }




    //User Registration interface functions
    @Override
    public void onUserRegistrationComplete(Activity activity) {
        activity.finish();
        mRegister.setText("Log out");
        initializeIAPComponant();
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {
    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {
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


    boolean isUserLoggedIn(){
        return  mUserDataInterface != null && mUserDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN ;
    }
}
