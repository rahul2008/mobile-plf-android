/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
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
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.util.ArrayList;
import java.util.Arrays;

public class InAppPurchaseFragmentState extends UIState implements IAPListener,ActionBarListener{

    Context mContext;
    private FragmentActivity fragmentActivity;
    private int containerID;
    private ArrayList<String> mCtnList = null;
    private TextView mTitleTextView;
    private ImageView mBackImage;
    private ImageView mCartIcon;
    private TextView mCountText;
    private ActionBarListener actionBarListener;

    public InAppPurchaseFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        /*InAppPurchasesFragment iap = new InAppPurchasesFragment();
        ((AppFrameworkBaseActivity)context).showFragment( iap, InAppPurchasesFragment.TAG);*/
        mContext = context;
        if(context instanceof HomeActivity) {
            fragmentActivity = (HomeActivity) context;
            containerID = R.id.frame_container;
            actionBarListener = (HomeActivity)context;
        }
        if (mCtnList == null) {
            mCtnList = new ArrayList<String>(Arrays.asList(fragmentActivity.getResources().getStringArray(R.array.productselection_ctnlist)));
        }
        addActionBar();
        launchIAP();
    }

    private void addActionBar() {
        ActionBar mActionBar = ((HomeActivity)mContext).getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        IAPLog.d(IAPLog.BASE_FRAGMENT_ACTIVITY, "DemoAppActivity == onCreate");
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        View mCustomView = LayoutInflater.from(mContext).inflate(com.philips.cdp.di.iap.R.layout.iap_action_bar, null); // layout which contains your button.
        FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.iap_header_back_button);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                fragmentActivity.onBackPressed();
            }
        });
        mBackImage = (ImageView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.iap_iv_header_back_button);
        Drawable mBackDrawable = VectorDrawable.create(mContext, com.philips.cdp.di.iap.R.drawable.iap_back_arrow);
        mBackImage.setBackground(mBackDrawable);
        mTitleTextView = (TextView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.iap_header_title);
        fragmentActivity.setTitle(fragmentActivity.getResources().getString(com.philips.cdp.di.iap.R.string.app_name));
        mCartIcon = (ImageView) mCustomView.findViewById(com.philips.cdp.di.iap.R.id.cart_icon);
        Drawable mCartIconDrawable = VectorDrawable.create(mContext, com.philips.cdp.di.iap.R.drawable.iap_shopping_cart);
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

    private void launchIAP() {
        IAPInterface iapInterface = new IAPInterface();
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPSettings iapSettings = new IAPSettings(fragmentActivity);
        IAPDependencies iapDependencies = new IAPDependencies(AppInfraSingleton.getInstance());
        iapSettings.setUseLocalData(false);
        iapInterface.init(iapDependencies, iapSettings);
        IAPFlowInput iapFlowInput = new IAPFlowInput(mCtnList);
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW, iapFlowInput);
        FragmentLauncher fragLauncher = new FragmentLauncher(fragmentActivity, containerID,this);
        try {

            iapInterface.launch(fragLauncher, iapLaunchInput);

        } catch (RuntimeException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        /*try{
            ((HomeActivity)mContext).showProgressDialog();
            iapInterface.getProductCartCount(this);
        }catch (RuntimeException e){

        }*/
    }

    public void addFragment(BaseAnimationSupportFragment newFragment,
                            String newFragmentTag) {
        newFragment.setActionBarListener(actionBarListener);
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, newFragment, newFragmentTag);
        transaction.addToBackStack(newFragmentTag);
        transaction.commitAllowingStateLoss();

        IAPLog.d(IAPLog.LOG, "Add fragment " + newFragment.getClass().getSimpleName() + "   ("
                + newFragmentTag + ")");
    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBackTillHomeFragment();
    }

    @Override
    public void onGetCartCount(int i) {
        Toast.makeText(mContext,""+i,Toast.LENGTH_SHORT).show();
        mCountText.setText(""+i);
        ((HomeActivity)mContext).dismissProgressDialog();
    }

    @Override
    public void onGetCompleteProductList(ArrayList<String> arrayList) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(int i) {

    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }
}
