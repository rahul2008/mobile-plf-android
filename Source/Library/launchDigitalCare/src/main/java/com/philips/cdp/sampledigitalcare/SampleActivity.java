package com.philips.cdp.sampledigitalcare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.productselection.launchertype.FragmentLauncher;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.productselection.productselectiontype.HardcodedProductList;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.cl.di.dev.pa.R;

/**
 * SampleActivity is the main container class which can contain Digital Care fragments.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 6 Aug 2015
 */


public class SampleActivity extends FragmentActivity implements View.OnClickListener {
    private static final String TAG = SampleActivity.class.getSimpleName();
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private TextView mActionBarTitle = null;
    private FragmentManager mFragmentManager = null;
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
        DigiCareLogger.i(TAG, " SampleActivity onCreate");
        setContentView(R.layout.activity_sample);

        if (LaunchDigitalCare.mList != null) {
            String[] ctnList = new String[LaunchDigitalCare.mList.size()];
            for (int i = 0; i < LaunchDigitalCare.mList.size(); i++)
                ctnList[i] = LaunchDigitalCare.mList.get(i);

            ProductModelSelectionType productsSelection = new HardcodedProductList(ctnList);
            productsSelection.setCatalog(Catalog.CARE);
            productsSelection.setSector(Sector.B2C);


            FragmentLauncher fragLauncher = new FragmentLauncher(
                    this, R.id.sampleMainContainer, actionBarClickListener);
            // fragLauncher.setAnimation(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
            //Testing purpose (Fragments Launch without Animation)
            fragLauncher.setAnimation(0, 0);

            DigitalCareConfigManager.getInstance().invokeDigitalCare(fragLauncher, productsSelection);

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
