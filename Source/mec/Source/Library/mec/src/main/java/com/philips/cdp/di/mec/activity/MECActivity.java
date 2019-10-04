package com.philips.cdp.di.mec.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.mec.R;
import com.philips.cdp.di.mec.analytics.MECAnalytics;
import com.philips.cdp.di.mec.analytics.MECAnalyticsConstant;
import com.philips.cdp.di.mec.container.CartModelContainer;
import com.philips.cdp.di.mec.integration.MECLaunchInput;
import com.philips.cdp.di.mec.integration.MECListener;
import com.philips.cdp.di.mec.screens.InAppBaseFragment;
import com.philips.cdp.di.mec.screens.MECProductCatalogFragment;
import com.philips.cdp.di.mec.utils.MECConstant;
import com.philips.cdp.di.mec.utils.NetworkUtility;
import com.philips.cdp.di.mec.utils.Utility;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import java.util.ArrayList;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MECActivity extends UIDActivity implements ActionBarListener, MECListener {
    private final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;
    private TextView mTitleTextView;
    private TextView mCountText;
    private ImageView mBackImage;
    private FrameLayout mCartContainer;
    private String mTitle;
    private ProgressDialog mProgressDialog = null;

    public MECActivity(){
        setLanguagePackNeeded(false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mec_activity);

        if(CartModelContainer.getInstance().getAppInfraInstance() == null){
            finish();
        }
        createActionBar();

        addLandingViews(savedInstanceState);
    }

    private void createActionBar() {
        FrameLayout frameLayout = findViewById(R.id.iap_header_back_button);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        mBackImage = findViewById(R.id.iap_iv_header_back_button);
        Drawable mBackDrawable = VectorDrawableCompat.create(getResources(), R.drawable.mec_back_arrow, getTheme());
        mBackImage.setBackground(mBackDrawable);
        mTitleTextView = findViewById(R.id.iap_actionBar_headerTitle_lebel);
        setTitle(getString(R.string.mec_app_name));

        mCartContainer = findViewById(R.id.cart_container);
        ImageView mCartIcon = findViewById(R.id.cart_icon);
        mCountText = findViewById(R.id.item_count);
        Drawable mCartIconDrawable = VectorDrawableCompat.create(getResources(), R.drawable.mec_shopping_cart,getTheme());
        mCartIcon.setBackground(mCartIconDrawable);
        mCartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showFragment(ShoppingCartFragment.TAG);
            }
        });

    }

    private void addLandingViews(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            int landingScreen = getIntent().getIntExtra(MECConstant.MEC_LANDING_SCREEN, -1);
            ArrayList<String> CTNs = getIntent().getExtras().getStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS);
            ArrayList<String> ignoreRetailerList = getIntent().getExtras().getStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST);
            String voucherCode=getIntent().getExtras().getString(MECConstant.MEC_VOUCHER_FROM_APP);

            Bundle bundle = new Bundle();
            switch (landingScreen) {
                case MECLaunchInput.MECFlows.MEC_PRODUCT_CATALOG_VIEW:
                    bundle.putStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS, CTNs);
                    bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailerList);
                    addFragment(MECProductCatalogFragment.createInstance(bundle,
                            InAppBaseFragment.AnimationType.NONE), MECProductCatalogFragment.TAG);
                    break;
                case MECLaunchInput.MECFlows.MEC_SHOPPING_CART_VIEW:
                    /*bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailerList);
                    addFragment(ShoppingCartFragment.createInstance(bundle,
                            InAppBaseFragment.AnimationType.NONE), ShoppingCartFragment.TAG);*/
                    break;
                case MECLaunchInput.MECFlows.MEC_PURCHASE_HISTORY_VIEW:
                    /*addFragment(PurchaseHistoryFragment.createInstance(bundle,
                            InAppBaseFragment.AnimationType.NONE), PurchaseHistoryFragment.TAG);*/
                    break;
                case MECLaunchInput.MECFlows.MEC_PRODUCT_DETAIL_VIEW:
                    /*if (getIntent().hasExtra(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL)) {
                        bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailerList);
                        bundle.putString(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL,
                                getIntent().getStringExtra(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL));
                        addFragment(ProductDetailFragment.createInstance(bundle,
                                InAppBaseFragment.AnimationType.NONE), ProductDetailFragment.TAG);
                    }*/
                    break;
                case MECLaunchInput.MECFlows.MEC_BUY_DIRECT_VIEW:
                    /*if (getIntent().hasExtra(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL)) {
                        bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailerList);
                        bundle.putString(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL,
                                getIntent().getStringExtra(MECConstant.MEC_PRODUCT_CATALOG_NUMBER_FROM_VERTICAL));
                        addFragment(BuyDirectFragment.createInstance(bundle,
                                InAppBaseFragment.AnimationType.NONE), BuyDirectFragment.TAG);
                    }*/
                    break;
                default:
                    bundle.putStringArrayList(MECConstant.CATEGORISED_PRODUCT_CTNS, CTNs);
                    bundle.putStringArrayList(MECConstant.MEC_IGNORE_RETAILER_LIST, ignoreRetailerList);
                    addFragment(MECProductCatalogFragment.createInstance(bundle,
                            InAppBaseFragment.AnimationType.NONE), MECProductCatalogFragment.TAG);

            }
        } else {
            setTitle(mTitle);
        }
    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(MECConstant.MEC_KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
    }


    @Override
    protected void onDestroy() {
        dismissProgressDialog();
        NetworkUtility.getInstance().dismissErrorDialog();
        MECAnalytics.clearAppTaggingInterface();
        super.onDestroy();
    }

    public void addFragment(InAppBaseFragment newFragment,
                            String newFragmentTag) {
        newFragment.setActionBarListener(this, this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_mainFragmentContainer, newFragment, newFragmentTag);
        transaction.addToBackStack(newFragmentTag);
        transaction.commitAllowingStateLoss();
    }

    public void showFragment(String fragmentTag) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!NetworkUtility.getInstance().isNetworkAvailable(connectivityManager)) {
            NetworkUtility.getInstance().showErrorDialog(this,
                    getSupportFragmentManager(), getString(R.string.mec_ok),
                    getString(R.string.mec_you_are_offline), getString(R.string.mec_no_internet));
        } else {
            /*addFragment(ShoppingCartFragment.createInstance(new Bundle(),
                    InAppBaseFragment.AnimationType.NONE), fragmentTag);*/
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitle = title.toString();
        mTitleTextView.setText(title);
    }

    @Override
    public void onBackPressed() {
        Utility.hideKeypad(this);
        MECAnalytics.trackAction(MECAnalyticsConstant.SEND_DATA,
                MECAnalyticsConstant.SPECIAL_EVENTS, MECAnalyticsConstant.BACK_BUTTON_PRESS);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.fl_mainFragmentContainer);
        boolean backState = false;
        if (currentFrag != null && currentFrag instanceof BackEventListener) {
            backState = ((BackEventListener) currentFrag).handleBackEvent();
        }
        if (!backState) {
            super.onBackPressed();
        }
    }

    @Override
    public void finish() {
        super.finish();
        CartModelContainer.getInstance().resetApplicationFields();
    }

    @Override
    protected void onPause() {
        MECAnalytics.pauseCollectingLifecycleData();
        super.onPause();
    }

    @Override
    protected void onResume() {
        MECAnalytics.collectLifecycleData(this);
        super.onResume();
    }

    @Override
    public void updateActionBar(int resourceId, boolean visibility) {
        mTitleTextView.setText(getString(resourceId));
        if (visibility) {
            mBackImage.setVisibility(View.VISIBLE);
            // For arabic, Hebrew and Perssian the back arrow change from left to right
            if((Locale.getDefault().getLanguage().contentEquals("ar")) || (Locale.getDefault().getLanguage().contentEquals("fa")) || (Locale.getDefault().getLanguage().contentEquals("he"))) {
                mBackImage.setRotation(180);
            }
        } else {
            mBackImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateActionBar(String resourceString, boolean visibility) {
        mTitleTextView.setText(resourceString);
        if (visibility) {
            mBackImage.setVisibility(View.VISIBLE);
            // For arabic, Hebrew and Perssian the back arrow change from left to right
            if((Locale.getDefault().getLanguage().contentEquals("ar")) || (Locale.getDefault().getLanguage().contentEquals("fa")) || (Locale.getDefault().getLanguage().contentEquals("he"))) {
                mBackImage.setRotation(180);
            }
        } else {
            mBackImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void onGetCartCount(int count) {
        if (count > 0) {
            mCountText.setText(String.valueOf(count));
            mCountText.setVisibility(View.VISIBLE);
        } else {
            mCountText.setVisibility(View.GONE);
        }
    }

    @Override
    public void onUpdateCartCount() {
        //Todo Nothing
    }

    @Override
    public void updateCartIconVisibility(boolean shouldShow) {
        if (shouldShow) {
            mCartContainer.setVisibility(View.VISIBLE);
        } else {
            mCartContainer.setVisibility(View.GONE);
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
    public void onSuccess(boolean bool) {
        dismissProgressDialog();
    }

    @Override
    public void onFailure(int errorCode) {
        showToast(errorCode);
        dismissProgressDialog();
    }

    private void showToast(int errorCode) {
        String errorText = null;
        if (MECConstant.MEC_ERROR_NO_CONNECTION == errorCode) {
            errorText = "No connection";
        } else if (MECConstant.MEC_ERROR_CONNECTION_TIME_OUT == errorCode) {
            errorText = "Connection time out";
        } else if (MECConstant.MEC_ERROR_AUTHENTICATION_FAILURE == errorCode) {
            errorText = "Authentication failure";
        } else if (MECConstant.MEC_ERROR_INSUFFICIENT_STOCK_ERROR == errorCode) {
            errorText = "Product out of stock";
        } else if (MECConstant.MEC_ERROR_INVALID_CTN == errorCode) {
            errorText = "Invalid ctn";
        }
        if (errorText != null) {
            Toast toast = Toast.makeText(this, errorText, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage(getString(R.string.mec_please_wait) + "...");
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
}

