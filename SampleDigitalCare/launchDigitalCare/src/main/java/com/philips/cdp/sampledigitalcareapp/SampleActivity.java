package com.philips.cdp.sampledigitalcareapp;

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
import com.philips.cdp.digitalcare.listeners.ActionbarUpdateListener;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

/**
 * SampleActivity is the main container class which can contain Digital Care fragments.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 6 Aug 2015
 */
public class SampleActivity extends FragmentActivity implements View.OnClickListener{
    private static final String TAG = SampleActivity.class.getSimpleName();
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private TextView mActionBarTitle = null;
    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        DigiCareLogger.i(TAG, " SampleActivity onCreate");
        setContentView(R.layout.activity_sample);
        DigitalCareConfigManager.getInstance().invokeDigitalCareAsFragment(this, R.id.sampleMainContainer, actionBarClickListener,
                "abc_fade_in", "abc_fade_out");
        try {
            initActionBar();
        } catch (ClassCastException e) {
            DigiCareLogger.e(TAG, "SampleActivity Actionbar: " + e.getMessage());
        }
        enableActionBarHome();

        DigitalCareConfigManager.getInstance();
        fragmentManager = getSupportFragmentManager();
    }

    protected void initActionBar() throws ClassCastException {
        mActionBarMenuIcon = (ImageView) findViewById(R.id.sample_home_icon);
        mActionBarArrow = (ImageView) findViewById(R.id.sample_back_to_home_img);
        mActionBarTitle = (TextView) findViewById(R.id.sample_action_bar_title);

        mActionBarMenuIcon.setOnClickListener(this);
        mActionBarArrow.setOnClickListener(this);
    }

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

    private ActionbarUpdateListener actionBarClickListener = new ActionbarUpdateListener() {

        @Override
        public void updateActionbar(String titleActionbar, Boolean hamburgerIconAvailable) {
            DigiCareLogger.i("testing","updateActionbar : " + hamburgerIconAvailable);
            mActionBarTitle.setText(titleActionbar);
            if(hamburgerIconAvailable){
                enableActionBarHome();
            }
            else{
                enableActionBarLeftArrow();
            }
        }
    };

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
