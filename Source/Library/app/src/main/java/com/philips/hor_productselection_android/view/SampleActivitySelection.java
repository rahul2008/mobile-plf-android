package com.philips.hor_productselection_android.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.hor_productselection_android.R;
import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.activity.ProductSelectionBaseActivity;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;

/**
 * SampleActivitySelection is the main container class which can contain Digital Care fragments.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 2 Feb 2016
 */
public class SampleActivitySelection extends ProductSelectionBaseActivity implements View.OnClickListener {
    private static final String TAG = SampleActivitySelection.class.getSimpleName();
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private TextView mActionBarTitle = null;
    private FragmentManager fragmentManager = null;
    private AppInfraInterface mAppInfraInterface;

    private ActionbarUpdateListener actionBarClickListener = new ActionbarUpdateListener() {

        @Override
        public void updateActionbar(String titleActionbar, Boolean hamburgerIconAvailable) {
            mActionBarTitle.setText(titleActionbar);
            if (hamburgerIconAvailable) {
                enableActionBarHome();
            } else {
                enableActionBarLeftArrow();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//        setNoActionBarTheme();
        Log.i(TAG, " Multiproduct - SampleActivitySelection onCreate");

        /*
        This module is integrated with Philips Standard UI_Kit. So here Theme is as per UI_Kit only. Vertical apps are free to use
        their apps specific themes.
         */
//        setTheme(getUiKitThemeUtil().getTheme());

        setContentView(R.layout.activity_multiproduct_sample);
       /* DigitalCareConfigManager.getInstance().invokeDigitalCareAsFragment(this, R.id.sampleMainContainer, actionBarClickListener,
                R.anim.slide_in_bottom, R.anim.slide_out_bottom);*/
//        FragmentComponentBuilder componentBuilder = new FragmentComponentBuilder();
//        componentBuilder.setActionbarUpdateListener(actionBarClickListener);
//        componentBuilder.setEnterAnimation(R.anim.slide_in_bottom);
//        componentBuilder.setExitAnimation(R.anim.slide_out_bottom);
//        componentBuilder.setmLayoutResourceID(R.id.sampleMainContainer);
//        componentBuilder.setFragmentActivity(this);

        mAppInfraInterface = new AppInfra.Builder().build(getApplicationContext());

        ProductModelSelectionHelper.getInstance().initialize(this, mAppInfraInterface);
        ProductModelSelectionHelper.getInstance().setLocale("en", "GB");

        //ProductModelSelectionHelper.getInstance().invokeDigitalCareAsFragment(this, R.id.sampleMultiProductContainer, null /*actionBarClickListener*/, R.anim.uikit_popover_fadein, R.anim.uikit_popover_fadeout);
//        ProductModelSelectionHelper.getInstance().setMultiProductSize(mList.size());


//        DigitalCareConfigManager.getInstance().invokeProductSelectionModule(componentBuilder);
//        try {
//            initActionBar();
//        } catch (ClassCastException e) {
//            Log.e(TAG, "SampleActivitySelection Actionbar: " + e.getMessage());
//        }
//        enableActionBarHome();

        fragmentManager = getSupportFragmentManager();
    }

//    protected void initActionBar() throws ClassCastException {
//        mActionBarMenuIcon = (ImageView) findViewById(R.id.sample_home_icon);
//        mActionBarArrow = (ImageView) findViewById(R.id.sample_back_to_home_img);
//        mActionBarTitle = (TextView) findViewById(R.id.sample_action_bar_title);
//
//        mActionBarMenuIcon.setOnClickListener(this);
//        mActionBarArrow.setOnClickListener(this);
//    }

    private boolean backstackFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
//            enableActionBarHome();
            fragmentManager.popBackStack();
            removeCurrentFragment();
        }
        return true;
    }

    private void removeCurrentFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFrag = fragmentManager
                .findFragmentById(R.id.mainContainer);

        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }
        transaction.commit();
    }

    private void enableActionBarLeftArrow() {
        mActionBarMenuIcon.setVisibility(View.GONE);
        mActionBarArrow.setVisibility(View.VISIBLE);
        mActionBarArrow.bringToFront();
    }

    private void enableActionBarHome() {
        mActionBarMenuIcon.setVisibility(View.VISIBLE);
        mActionBarMenuIcon.bringToFront();
        mActionBarArrow.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return backstackFragment();
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        int _id = view.getId();
        if (_id == R.id.sample_home_icon) {
            finish();
        } else if (_id == R.id.sample_back_to_home_img)
            backstackFragment();
    }
}
