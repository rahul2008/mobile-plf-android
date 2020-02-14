package com.philips.hor_productselection_android.view;

import android.os.Bundle;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.activity.ProductSelectionBaseActivity;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.hor_productselection_android.R;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.UIDHelper;

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
    private Toolbar toolbar;

    private ActionBarListener actionBarClickListener = new ActionBarListener() {
        @Override
        public void updateActionBar(@StringRes int i, boolean b) {

        }

        @Override
        public void updateActionBar(String titleActionbar, boolean hamburgerIconAvailable) {

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
        ProductSelectionLogger.i(TAG, " Multiproduct - SampleActivitySelection onCreate");
        setContentView(R.layout.activity_multiproduct_sample);
        mAppInfraInterface = new AppInfra.Builder().build(getApplicationContext());

        ProductModelSelectionHelper.getInstance().initialize(this, mAppInfraInterface);
        UIDHelper.setupToolbar(this);
        //getTheme().applyStyle(com.philips.cdp.uikit.R.style.Theme_Philips_BrightOrange_Gradient, true);
        toolbar = (Toolbar) findViewById(R.id.uid_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_icon);

        fragmentManager = getSupportFragmentManager();
    }

    private boolean backstackFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
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