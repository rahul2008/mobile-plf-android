package com.philips.cdp.di.iapdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.session.IAPHandler;
import com.philips.cdp.di.iap.session.IAPHandlerListener;
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

public class DemoAppActivity extends Activity implements View.OnClickListener,
         UserRegistrationListener, IAPHandlerListener{

    private final int DEFAULT_THEME = R.style.Theme_Philips_DarkPurple_WhiteBackground;

    private IAPHandler mIapHandler;
    private TextView mCountText = null;
    private ArrayList<ShoppingCartData> mProductArrayList = new ArrayList<>();
    private FrameLayout mShoppingCart;
    private ListView mProductListView;
    private String[] mCatalogNumbers = {"HX8331/11", "HX8071/10", "HX9042/64"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(DEFAULT_THEME);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo_app_layout);

        Button register = (Button) findViewById(R.id.btn_register);
        register.setOnClickListener(this);

        populateProduct();

        mShoppingCart = (FrameLayout) findViewById(R.id.shoppingCart);
        mShoppingCart.setOnClickListener(this);

        mProductListView = (ListView) findViewById(R.id.product_list);
        ProductListAdapter mProductListAdapter = new ProductListAdapter(this, mProductArrayList);
        mProductListView.setAdapter(mProductListAdapter);

        mCountText = (TextView) findViewById(R.id.count_txt);

        RegistrationHelper.getInstance().registerUserRegistrationListener(this);
        mIapHandler = new IAPHandler();
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
            mShoppingCart.setVisibility(View.VISIBLE);
            mProductListView.setVisibility(View.VISIBLE);
//            mIapHandler.initApp(this);
                    Utility.showProgressDialog(this, getString(R.string.loading_cart));
                    mIapHandler.getProductCartCount(this, mProductCountListener);
            }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this);
    }

    private void populateProduct() {
        for (String mCatalogNumber : mCatalogNumbers) {
            ShoppingCartData product = new ShoppingCartData();
            product.setCtnNumber(mCatalogNumber);
            mProductArrayList.add(product);
        }
    }

    void addToCart(String ctnNumber) {
        if (!(Utility.isProgressDialogShowing())) {
                Utility.showProgressDialog(this, getString(R.string.adding_to_cart));
                mIapHandler.addProductToCart(this,ctnNumber, mAddToCartListener);
                IAPLog.d(IAPLog.DEMOAPPACTIVITY, "addProductToCart");
        }
    }

    void buyProduct(final String ctnNumber) {
        Utility.showProgressDialog(this, getString(R.string.please_wait));
        mIapHandler.buyProduct(this, ctnNumber, mBuyProductListener, DEFAULT_THEME);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.shoppingCart:
                mIapHandler.launchIAP(this,DEFAULT_THEME);
                break;
            case R.id.btn_register:
                IAPLog.d(IAPLog.DEMOAPPACTIVITY, "DemoActivity : Registration");
                RegistrationLaunchHelper.launchDefaultRegistrationActivity(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        mShoppingCart.setVisibility(View.VISIBLE);
        mProductListView.setVisibility(View.VISIBLE);
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

    }

    @Override
    public void onUserLogoutFailure() {

    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {

    }

    private IAPHandlerListener mAddToCartListener = new IAPHandlerListener() {
        @Override
        public void onSuccess(final int count) {
            //// TODO: 24-03-2016 Adding out of stock condition
            mIapHandler.getProductCartCount(DemoAppActivity.this, mProductCountListener);
        }

        @Override
        public void onFailure(final int errorCode) {
            Utility.dismissProgressDialog();
            showToast(errorCode);
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
        }

        @Override
        public void onFailure(final int errorCode) {
            Utility.dismissProgressDialog();
            showToast(errorCode);
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
        String errorText = "Unkown error";
        if(IAPConstant.IAP_ERROR_NO_CONNECTION == errorCode) {
            errorText = "No connection";
        } else if(IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT == errorCode) {
            errorText = "Connection time out";
        } else if(IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE == errorCode) {
            errorText = "Authentication failure";
        } else if(IAPConstant.IAP_ERROR_INSUFFICIENT_STOCK_ERROR == errorCode) {
            errorText = "Product out of stock";
        }

        Toast toast = Toast.makeText(this,errorText,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    @Override
    public void onSuccess(int count) {

    }

    @Override
    public void onFailure(int errorCode) {

    }
}