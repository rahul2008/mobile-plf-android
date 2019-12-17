package com.mec.demouapp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.philips.cdp.di.mec.integration.MECBazaarVoiceInput;
import com.philips.cdp.di.mec.integration.MECDependencies;
import com.philips.cdp.di.mec.integration.MECFlowInput;
import com.philips.cdp.di.mec.integration.MECInterface;
import com.philips.cdp.di.mec.integration.MECLaunchInput;
import com.philips.cdp.di.mec.integration.MECListener;
import com.philips.cdp.di.mec.integration.MECSettings;
import com.philips.cdp.di.mec.screens.reviews.BazaarVoiceEnvironment;
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

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class BaseDemoFragment extends Fragment implements View.OnClickListener, MECListener,
        UserRegistrationUIEventListener, MECBannerEnabler,ActionBarListener, CompoundButton.OnCheckedChangeListener {

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
    private RelativeLayout DemoContentBody;
    private FrameLayout fragmentContainer;
    private ImageView mBackImage;

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
    private boolean enableMock = false;
    EditText mEtMaxCartCount;
    private ToggleButton toggleHybris;
    private boolean isHybrisEnable = true;
    private ToggleButton toggleBanner;
    private boolean isBannerEnabled = false;
    private ToggleButton toggleListener;
    private boolean isToggleListener = false;
    private RadioGroup rgVoucher,rgLauncher;
    private CheckBox bvCheckBox;
    private MECBazaarVoiceInput mecBazaarVoiceInput;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        urInterface = new URInterface();
        urInterface.init(new MecDemoUAppDependencies(new AppInfra.Builder().build(getContext())), new MecDemoAppSettings(getContext()));

        ignorelistedRetailer = new ArrayList<>();
        rootView = inflater.inflate(R.layout.base_demo_fragment, container, false);
        mEtCTN = rootView.findViewById(R.id.et_add_ctn);
        mEtVoucherCode = rootView.findViewById(R.id.et_add_voucher);
        mAddCTNLl = rootView.findViewById(R.id.ll_ctn);
        bvCheckBox = rootView.findViewById(R.id.bv_checkbox);
        bvCheckBox.setOnCheckedChangeListener(this);

        DemoContentBody  = rootView.findViewById(R.id.demo_content_layout);
        fragmentContainer  = rootView.findViewById(R.id.mec_fragment_container);


        mEtPropositionId = rootView.findViewById(R.id.et_add_proposition_id);
        mBtnSetPropositionId = rootView.findViewById(R.id.btn_set_proposition_id);


        AppInfraInterface appInfra = new AppInfra.Builder().build(getContext());
        AppConfigurationInterface configInterface = appInfra.getConfigInterface();
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();

        String propertyForKey = (String) configInterface.getPropertyForKey("propositionid", "MEC", configError);
        mEtPropositionId.setText(propertyForKey);


        mecBazaarVoiceInput = new MECBazaarVoiceInput();
        mBtnSetPropositionId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                configInterface.setPropertyForKey("propositionid", "MEC", mEtPropositionId.getText().toString(), configError);

                Toast.makeText(getActivity(), "Proposition id is set", Toast.LENGTH_SHORT).show();
                getActivity().finishAffinity();
                System.exit(0);
            }
        });

        toggleMock = rootView.findViewById(R.id.toggleMock);

        toggleMock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                enableMock = isChecked;
                initializeMECComponant();
            }
        });

        toggleBanner = rootView.findViewById(R.id.toggleBanner);

        toggleBanner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                isBannerEnabled = isChecked;
                initializeMECComponant();
            }
        });

        toggleHybris = rootView.findViewById(R.id.toggleHybris);
        toggleHybris.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isHybrisEnable = isChecked;
                initializeMECComponant();
            }
        });

        rgVoucher = rootView.findViewById(R.id.rg_voucher);
        rgLauncher = rootView.findViewById(R.id.rg_launcher);



        rgLauncher.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_activity) {

                }
                if (checkedId == R.id.rb_fragment) {

                }
            }
        });


        toggleListener = rootView.findViewById(R.id.toggleListener);

        toggleListener.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isToggleListener = isChecked;
                initializeMECComponant();
            }
        });

        Button btnSetMaxCount = rootView.findViewById(R.id.btn_set_max_Count);

        btnSetMaxCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeMECComponant();
            }
        });

        mEtMaxCartCount = rootView.findViewById(R.id.et_max_cart_count);

        mRegister = rootView.findViewById(R.id.btn_register);
        mRegister.setOnClickListener(this);

        mBuyDirect = rootView.findViewById(R.id.btn_buy_direct);
        mBuyDirect.setOnClickListener(this);

        mShopNow = rootView.findViewById(R.id.btn_shop_now);
        mShopNow.setOnClickListener(this);

        mPurchaseHistory = rootView.findViewById(R.id.btn_purchase_history);
        mPurchaseHistory.setOnClickListener(this);

        mLaunchProductDetail = rootView.findViewById(R.id.btn_launch_product_detail);
        mLaunchProductDetail.setOnClickListener(this);

        mShoppingCart = rootView.findViewById(R.id.mec_demo_app_shopping_cart_icon);

        mShopNowCategorized = rootView.findViewById(R.id.btn_categorized_shop_now);
        mShopNowCategorized.setOnClickListener(this);


        mLL_voucher = rootView.findViewById(R.id.ll_voucher);
        mLL_propositionId = rootView.findViewById(R.id.ll_enter_proposition_id);

        mAddCtn = rootView.findViewById(R.id.btn_add_ctn);
        mAddCtn.setOnClickListener(this);

        mBtn_add_voucher = rootView.findViewById(R.id.btn_add_voucher);
        mBtn_add_voucher.setOnClickListener(this);

        mShopNowCategorizedWithRetailer = rootView.findViewById(R.id.btn_categorized_shop_now_with_ignore_retailer);
        mShopNowCategorizedWithRetailer.setOnClickListener(this);

        mCartIcon = rootView.findViewById(R.id.mec_demo_app_cart_iv);
        mCountText = rootView.findViewById(R.id.mec_demo_app_item_count);

        mCategorizedProductList = new ArrayList<>();

        mUserDataInterface = urInterface.getUserDataInterface();


        mMecInterface = new MECInterface();
        mMecSettings = new MECSettings(getActivity());
        actionBar();
        initializeMECComponant();
        initializeBazaarVoice();


        return rootView;

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

        UappDependencies uappDependencies = new UappDependencies(new AppInfra.Builder().build(getActivity()));
        UappSettings uappSettings = new UappSettings(getContext());

        urInterface.init(uappDependencies, uappSettings);

        MECDependencies mIapDependencies = new MECDependencies(new AppInfra.Builder().build(getActivity()), urInterface.getUserDataInterface());

        try {
            mMecInterface.init(mIapDependencies, mMecSettings);
        } catch (RuntimeException ex) {
        }

        mMecLaunchInput = new MECLaunchInput();

        if (!TextUtils.isEmpty(mEtMaxCartCount.getText().toString().trim())) {
        }
        mMecLaunchInput.setMecListener(this);


        mMecLaunchInput.mecBannerEnabler = this::getBannerView;
        mMecLaunchInput.setHybrisEnabled(this.isHybrisEnable);
        mMecLaunchInput.mecBazaarVoiceInput = mecBazaarVoiceInput;


    }

    @Override
    public void onStart() {
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


    @Override
    public void onStop() {
        super.onStop();
        mCategorizedProductList.clear();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void actionBar() {
        FrameLayout frameLayout = rootView.findViewById(R.id.mec_demo_app_header_back_button_framelayout);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                getActivity().onBackPressed();
            }
        });

        mBackImage = rootView.findViewById(R.id.mec_demo_app_iv_header_back_button);
        Drawable mBackDrawable = VectorDrawableCompat.create(getResources(), R.drawable.mec_demo_app_back_arrow, getActivity().getTheme());
        mBackImage.setBackground(mBackDrawable);
        mTitleTextView = rootView.findViewById(R.id.mec_demo_app_header_title);
        mTitleTextView.setText(R.string.mec_app_name);
        //getActivity().setTitle(getString(R.string.mec_app_name));
        mShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClickable())
                    launchMEC(MECLaunchInput.MECFlows.Companion.getMEC_SHOPPING_CART_VIEW(), null, null);
            }
        });
    }



    private void launchMEC(int pLandingViews, MECFlowInput pMecFlowInput, ArrayList<String> pIgnoreRetailerList) {

        mMecLaunchInput.mecBazaarVoiceInput = mecBazaarVoiceInput;

        if (pIgnoreRetailerList == null)
            mMecLaunchInput.setMECFlow(pLandingViews, pMecFlowInput, voucherCode);
        else
            mMecLaunchInput.setMECFlow(pLandingViews, pMecFlowInput, voucherCode, pIgnoreRetailerList);

        try {
            DemoContentBody.setVisibility(View.VISIBLE);
            fragmentContainer.setVisibility(View.GONE);
            int themeResourceID = new ThemeHelper(getActivity()).getThemeResourceId();
            mMecInterface.launch(new ActivityLauncher
                            (getActivity(), ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, null, themeResourceID, null),
                    mMecLaunchInput);

        } catch (RuntimeException exception) {
            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void launchMECasFragment(int pLandingViews, MECFlowInput pMecFlowInput, ArrayList<String> pIgnoreRetailerList) {

        mMecLaunchInput.mecBazaarVoiceInput = mecBazaarVoiceInput;
        if (pIgnoreRetailerList == null)
            mMecLaunchInput.setMECFlow(pLandingViews, pMecFlowInput, voucherCode);
        else
            mMecLaunchInput.setMECFlow(pLandingViews, pMecFlowInput, voucherCode, pIgnoreRetailerList);

        try {

            DemoContentBody.setVisibility(View.GONE);
            fragmentContainer.setVisibility(View.VISIBLE);
            mMecInterface.launch(new FragmentLauncher(getActivity(), R.id.mec_fragment_container, this),
                    mMecLaunchInput);

        } catch (RuntimeException exception) {
            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(final View view) {
        if (!isClickable()) return;

        if (view == mShoppingCart) {

        } else if (view == mShopNow) {
            if(getActivity() instanceof LaunchAsActivity) {
                launchMEC(MECLaunchInput.MECFlows.Companion.getMEC_PRODUCT_CATALOG_VIEW(), null, null);
            } else if (getActivity() instanceof LaunchAsFragment) {
                launchMECasFragment(MECLaunchInput.MECFlows.Companion.getMEC_PRODUCT_CATALOG_VIEW(), null, null);
            }
        } else if (view == mPurchaseHistory) {

        } else if (view == mLaunchProductDetail) {
            if (null != mCategorizedProductList && mCategorizedProductList.size() > 0) {
                MECFlowInput input = new MECFlowInput(mCategorizedProductList.get(0));
                if(getActivity() instanceof LaunchAsActivity) {
                    launchMEC(MECLaunchInput.MECFlows.Companion.getMEC_PRODUCT_DETAIL_VIEW(), input, null);
                } else if (getActivity() instanceof LaunchAsFragment) {
                    launchMECasFragment(MECLaunchInput.MECFlows.Companion.getMEC_PRODUCT_DETAIL_VIEW(), input, null);
                }
            } else {
                Toast.makeText(getActivity(), "Please add CTN", Toast.LENGTH_SHORT).show();
            }
        } else if (view == mShopNowCategorized) {
            if (mCategorizedProductList.size() > 0) {
                MECFlowInput input = new MECFlowInput(mCategorizedProductList);
                if(getActivity() instanceof LaunchAsActivity) {
                    launchMEC(MECLaunchInput.MECFlows.Companion.getMEC_PRODUCT_CATALOG_VIEW(), input, null);
                } else if (getActivity() instanceof LaunchAsFragment) {
                    launchMECasFragment(MECLaunchInput.MECFlows.Companion.getMEC_PRODUCT_CATALOG_VIEW(), input, null);
                }
            } else {
                Toast.makeText(getActivity(), "Please add CTN", Toast.LENGTH_SHORT).show();
            }
        } else if (view == mShopNowCategorizedWithRetailer) {

        } else if (view == mBuyDirect) {

        } else if (view == mRegister) {
            if (mRegister.getText().toString().equalsIgnoreCase(this.getString(R.string.log_out))) {
                if (mUserDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN) {
                    mUserDataInterface.logoutSession(new LogoutSessionListener() {
                        @Override
                        public void logoutSessionSuccess() {
                            getActivity().finish();
                        }

                        @Override
                        public void logoutSessionFailed(Error error) {
                            Toast.makeText(getActivity(), "Logout went wrong", Toast.LENGTH_SHORT).show();
                        }

                    });
                } else {
                    Toast.makeText(getActivity(), "User is not logged in", Toast.LENGTH_SHORT).show();
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


        ActivityLauncher activityLauncher = new ActivityLauncher(getActivity(), ActivityLauncher.
                ActivityOrientation.SCREEN_ORIENTATION_SENSOR, null, 0, null);
        urInterface.launch(activityLauncher, urLaunchInput);
    }

    private void showAppVersion() {
        String code = null;
        try {
            code = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            //IAPLog.e(IAPLog.LOG, e.getMessage());
        }
        TextView versionView = rootView.findViewById(R.id.appversion);
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
            Toast toast = Toast.makeText(getActivity(), errorText, Toast.LENGTH_SHORT);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(getActivity(), "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(getActivity(), "portrait", Toast.LENGTH_SHORT).show();
        }
    }


    public void showProgressDialog() {
        mProgressDialog = new ProgressDialog(UIDHelper.getPopupThemedContext(getActivity()));
        mProgressDialog.getWindow().setGravity(Gravity.CENTER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Please wait" + "...");

        if ((!mProgressDialog.isShowing()) && !(getActivity()).isFinishing()) {
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
            InputStream is = getActivity().getAssets().open(fileName);
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
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.banner_view, null);
            return v;
        }
        return null;

    }

    boolean isUserLoggedIn(){
        return  mUserDataInterface != null && mUserDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN ;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.isPressed()) {
            showDialog();
        }
        initializeMECComponant();
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog OptionDialog = builder.create();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        setBazaarVoiceEnvironmentToProd();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        bvCheckBox.setChecked(! bvCheckBox.isChecked());
                        OptionDialog.dismiss();
                        break;
                }
            }
        };

        builder.setMessage("Are you sure to change Bazaar voice environment ?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void setBazaarVoiceEnvironmentToProd(){
        String env= (bvCheckBox.isChecked()) ? BazaarVoiceEnvironment.PRODUCTION.toString(): BazaarVoiceEnvironment.STAGING.toString();
        SharedPreferences preferences = getActivity().getSharedPreferences("bvEnv", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("BVEnvironment", env);
        editor.commit();
        getActivity().finishAffinity();
        System.exit(0);
    }

    private void initializeBazaarVoice(){
        SharedPreferences shared = getActivity().getSharedPreferences("bvEnv", MODE_PRIVATE);
        String name = (shared.getString("BVEnvironment", BazaarVoiceEnvironment.PRODUCTION.toString()));
        if(name.equalsIgnoreCase(BazaarVoiceEnvironment.PRODUCTION.toString())) {
            bvCheckBox.setChecked(true);
            mecBazaarVoiceInput = new MECBazaarVoiceInput() {

                @NotNull
                @Override
                public BazaarVoiceEnvironment getBazaarVoiceEnvironment() {

                    return BazaarVoiceEnvironment.PRODUCTION;

                }

                @NotNull
                @Override
                public String getBazaarVoiceClientID() {

                    return "philipsglobal";

                }

                @NotNull
                @Override
                public String getBazaarVoiceConversationAPIKey() {

                    return "caAyWvBUz6K3xq4SXedraFDzuFoVK71xMplaDk1oO5P4E";

                }
            };
        }else{
            bvCheckBox.setChecked(false);
            mecBazaarVoiceInput = new MECBazaarVoiceInput() {

                @NotNull
                @Override
                public BazaarVoiceEnvironment getBazaarVoiceEnvironment() {

                    return BazaarVoiceEnvironment.STAGING;

                }

                @NotNull
                @Override
                public String getBazaarVoiceClientID() {

                    return "philipsglobal";

                }

                @NotNull
                @Override
                public String getBazaarVoiceConversationAPIKey() {

                    return "ca23LB5V0eOKLe0cX6kPTz6LpAEJ7SGnZHe21XiWJcshc";
                }
            };
        }

    }


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


