package com.philips.cdp.di.iapdemo;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.Fragments.BaseAnimationSupportFragment;
import com.philips.cdp.di.iap.Fragments.ShoppingCartFragment;
import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPFlowInput;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.ArrayList;

import static com.philips.cdp.di.iap.utils.Utility.dismissProgressDialog;

/**
 * Created by 310164421 on 6/8/2016.
 */
public class LauncherFragmentActivity extends UiKitActivity implements ActionBarListener, IAPListener {
    ArrayList<String> mProductCTNs;
    private TextView mTitleTextView;
    private TextView mCountText;
    private final int DEFAULT_THEME = R.style.Theme_Philips_BrightBlue;
    private ImageView mBackImage;
    private FrameLayout mCartContainer;
    IAPInterface mIapInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(DEFAULT_THEME);
        super.onCreate(savedInstanceState);

        addActionBar();
        setContentView(R.layout.fragment_launcher_layout);
        mProductCTNs = new ArrayList<>();
        mProductCTNs.add("HX8331/11");
        setLocale("en", "US");
        LaunchIAPFragment();
    }

    private void LaunchIAPFragment() {
        mIapInterface = new IAPInterface();
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPSettings iapSettings = new IAPSettings(this);
        DemoApplication application = (DemoApplication)getApplicationContext();
        IAPDependencies iapDependencies = new IAPDependencies(application.getAppInfra());
        iapSettings.setUseLocalData(false);
        IAPFlowInput iapFlowInput = new IAPFlowInput(mProductCTNs);
        mIapInterface.init(iapDependencies, iapSettings);
        iapLaunchInput.setIapListener(this);
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, iapFlowInput);
        try {
            mIapInterface.launch(new FragmentLauncher(this, R.id.vertical_Container, this), iapLaunchInput);
        } catch (RuntimeException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.vertical_Container);
        if (fragmentManager.getBackStackEntryCount() == 1) finish();
        boolean backState = false;
        if (currentFrag != null && currentFrag instanceof BackEventListener) {
            backState = ((BackEventListener) currentFrag).handleBackEvent();
        }
        if (!backState) {
            super.onBackPressed();
        }
    }


    private void addActionBar() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        IAPLog.d(IAPLog.BASE_FRAGMENT_ACTIVITY, "DemoAppActivity == onCreate");
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        View mCustomView = LayoutInflater.from(getApplicationContext()).inflate(com.philips.cdp.di.iap.R.layout.iap_action_bar, null); // layout which contains your button.
        FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.iap_header_back_button);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });
        mBackImage = (ImageView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.iap_iv_header_back_button);
        Drawable mBackDrawable = VectorDrawable.create(getApplicationContext(), com.philips.cdp.di.iap.R.drawable.iap_back_arrow);
        mBackImage.setBackground(mBackDrawable);
        mTitleTextView = (TextView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.iap_header_title);
        setTitle(getString(com.philips.cdp.di.iap.R.string.app_name));
        mCartContainer = (FrameLayout) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.cart_container);
        ImageView mCartIcon = (ImageView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.cart_icon);
        Drawable mCartIconDrawable = VectorDrawable.create(getApplicationContext(), com.philips.cdp.di.iap.R.drawable.iap_shopping_cart);
        mCartIcon.setBackground(mCartIconDrawable);
        mCartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(ShoppingCartFragment.createInstance(new Bundle(),
                        BaseAnimationSupportFragment.AnimationType.NONE), ShoppingCartFragment.TAG);
            }
        });
        mCountText = (TextView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.item_count);
        mActionBar.setCustomView(mCustomView, params);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }

    public void addFragment(BaseAnimationSupportFragment newFragment,
                            String newFragmentTag) {
        newFragment.setActionBarListener(this, this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.vertical_Container, newFragment, newFragmentTag);
        transaction.addToBackStack(newFragmentTag);
        transaction.commitAllowingStateLoss();

        IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
                + newFragmentTag + ")");
    }


    private void setLocale(String languageCode, String countryCode) {
        PILLocaleManager localeManager = new PILLocaleManager(LauncherFragmentActivity.this);
        localeManager.setInputLocale(languageCode, countryCode);
    }

    @Override
    public void updateActionBar(int resourceId, boolean visibility) {
        if (visibility) {
            mTitleTextView.setText(getString(resourceId));
            mBackImage.setVisibility(View.VISIBLE);
        } else {
            mTitleTextView.setText(getString(resourceId));
            mBackImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void updateActionBar(String resourceString, boolean visibility) {
        if (visibility) {
            mTitleTextView.setText(resourceString);
            mBackImage.setVisibility(View.VISIBLE);
        } else {
            mTitleTextView.setText(resourceString);
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
        mIapInterface.getProductCartCount(this);
    }

    @Override
    public void updateCartIconVisibility(boolean shouldShow) {
        if (shouldShow) {
            mCartContainer.setVisibility(View.VISIBLE);
        } else {
            mCartContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onGetCompleteProductList(ArrayList<String> productList) {

    }

    @Override
    public void onSuccess() {
    }

    @Override
    public void onFailure(int errorCode) {

    }
}
