package com.philips.multiproduct.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.philips.multiproduct.MultiProductConfigManager;
import com.philips.multiproduct.R;
import com.philips.multiproduct.utils.MLogger;


public abstract class MultiProductBaseActivity extends FragmentActivity {
    private static String TAG = MultiProductBaseActivity.class.getSimpleName();


    private FragmentManager fragmentManager = null;
    private MultiProductConfigManager mMultiProductConfigManager = null;

    private ImageView mActionBarArrow = null;
    private View.OnClickListener actionBarClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            int _id = view.getId();
            if (_id == R.id.back_to_home_img)
                backstackFragment();
        }
    };

    protected void initActionBar() throws ClassCastException {
        mActionBarArrow = (ImageView) findViewById(R.id.back_to_home_img);
        mActionBarArrow.setVisibility(View.VISIBLE);
        mActionBarArrow.bringToFront();
//        mActionBarTitle = (DigitalCareFontTextView) findViewById(R.id.action_bar_title);

        mActionBarArrow.setOnClickListener(actionBarClickListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        MLogger.i(TAG, "onCreate");
        MultiProductConfigManager.getInstance();
        fragmentManager = getSupportFragmentManager();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        MLogger.i(TAG, TAG + " : onConfigurationChanged ");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
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
    protected void onDestroy() {

        super.onDestroy();
        if (mMultiProductConfigManager != null) {
            mMultiProductConfigManager = null;
        }
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


    protected void showFragment(Fragment fragment) {
        try {
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            fragmentTransaction.replace(R.id.mainContainer, fragment, "tagname");
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            MLogger.e(TAG, e.getMessage());
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getWindow() != null && getWindow().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                    .getWindowToken(), 0);
        }
    }

}
