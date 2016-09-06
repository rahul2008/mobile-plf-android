package com.philips.cdp.sampledigitalcare.launcher.uAppComponetLaunch;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.digitalcare.CcDependencies;
import com.philips.cdp.digitalcare.CcInterface;
import com.philips.cdp.digitalcare.CcLaunchInput;
import com.philips.cdp.digitalcare.CcSettings;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.listeners.CcListener;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.productselection.productselectiontype.HardcodedProductList;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.cl.di.dev.pa.R;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.uappframework.listener.ActionBarListener;

/**
 * SampleActivity is the main container class which can contain Digital Care fragments.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 6 Aug 2015
 */


public class MicroAppFragmentActivity extends FragmentActivity implements View.OnClickListener,
        CcListener {
    private static final String TAG = MicroAppFragmentActivity.class.getSimpleName();
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private TextView mActionBarTitle = null;
    private FragmentManager mFragmentManager = null;
    private CcSettings ccSettings = null;
    private CcLaunchInput ccLaunchInput = null;

    private ActionBarListener actionBarListener = new ActionBarListener() {
        @Override
        public void updateActionBar(@IdRes int i, boolean b) {

        }

        @Override
        public void updateActionBar(String title, boolean actionBarLeftArrow) {

            // Toast.makeText(MicroAppFragmentActivity.this, title, Toast.LENGTH_SHORT).show();
            mActionBarTitle.setText(title);
            if (actionBarLeftArrow) {
                enableActionBarLeftArrow();
            } else {
                enableActionBarHome();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        DigiCareLogger.i(TAG, " onCreate ++ ");
        setContentView(R.layout.activity_sample);

        if (MicroAppLauncher.mList != null) {
            String[] ctnList = new String[MicroAppLauncher.mList.size()];
            for (int i = 0; i < MicroAppLauncher.mList.size(); i++)
                ctnList[i] = MicroAppLauncher.mList.get(i);

            ProductModelSelectionType productsSelection = new HardcodedProductList(ctnList);
            productsSelection.setCatalog(Catalog.CARE);
            productsSelection.setSector(Sector.B2C);


            /*FragmentLauncher fragLauncher = new FragmentLauncher(
                    this, R.id.sampleMainContainer, actionBarClickListener);
            // fragLauncher.setAnimation(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
            //Testing purpose (Fragments Launch without Animation)
            fragLauncher.setAnimation(0, 0);

            DigitalCareConfigManager.getInstance().invokeDigitalCare(fragLauncher, productsSelection);*/

            com.philips.platform.uappframework.launcher.FragmentLauncher launcher =
                    new com.philips.platform.uappframework.launcher.FragmentLauncher
                            (this, R.id.sampleMainContainer, actionBarListener);
            launcher.setCustomAnimation(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

           /* CcInterface ccInterface = new CcInterface();
            ccInterface.init(this, null);
            ccInterface.launch(launcher, productsSelection, this);*/


            CcInterface ccInterface = new CcInterface();
            if (ccSettings == null) ccSettings = new CcSettings(this);
            if (ccLaunchInput == null) ccLaunchInput = new CcLaunchInput();
            ccLaunchInput.setProductModelSelectionType(productsSelection);
            ccLaunchInput.setConsumerCareListener(this);
            CcDependencies ccDependencies = new CcDependencies(AppInfraSingleton.getInstance());

            ccInterface.init(ccDependencies, ccSettings);
            ccInterface.launch(launcher, ccLaunchInput);


            try {
                initActionBar();
            } catch (ClassCastException e) {
                DigiCareLogger.e(TAG, "SampleActivity Actionbar: " + e.getMessage());
            }
            enableActionBarHome();

            DigitalCareConfigManager.getInstance();
            mFragmentManager = getSupportFragmentManager();
        }
    }

    @Override
    public boolean onMainMenuItemClicked(String mainMenuItem) {
        return false;
    }

    @Override
    public boolean onProductMenuItemClicked(String productMenu) {
        return false;
    }

    @Override
    public boolean onSocialProviderItemClicked(String socialProviderItem) {
        return false;
    }

    protected void initActionBar() throws ClassCastException {
        mActionBarMenuIcon = (ImageView) findViewById(R.id.sample_home_icon);
        mActionBarArrow = (ImageView) findViewById(R.id.sample_back_to_home_img);
        mActionBarTitle = (TextView) findViewById(R.id.sample_action_bar_title);

        mActionBarMenuIcon.setOnClickListener(this);
        mActionBarArrow.setOnClickListener(this);
    }

    private boolean previousFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            removeFromStack();
        }
        return true;
    }

    private void removeFromStack() {
        mFragmentManager.popBackStack();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        removeCurrentFragment(transaction);
    }

    private void removeCurrentFragment(FragmentTransaction transaction) {


        Fragment currentFrag = mFragmentManager
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
            return previousFragment();
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
            previousFragment();
    }
}
