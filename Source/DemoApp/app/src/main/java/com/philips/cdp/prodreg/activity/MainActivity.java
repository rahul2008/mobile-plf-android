package com.philips.cdp.prodreg.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.prodreg.R;
import com.philips.cdp.prodreg.fragment.LaunchFragment;
import com.philips.cdp.uikit.UiKitActivity;

public class MainActivity extends UiKitActivity {

    private FragmentManager fragmentManager;
    private TextView mTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomActionBar();
        setContentView(R.layout.activity_test_ur);

        fragmentManager = getSupportFragmentManager();
        LaunchFragment launchFragment = new LaunchFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.parent_layout, launchFragment,
                "Demo_Launch_fragment");
        fragmentTransaction.addToBackStack(launchFragment.getTag());
        fragmentTransaction.commit();
    }

    private void initCustomActionBar() {
        ActionBar mActionBar = this.getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null); // layout which contains your button.

        mTitleTextView = (TextView) mCustomView.findViewById(R.id.text);

        FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.UpButton);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                backStackFragment();
            }
        });

        ImageView arrowImage = (ImageView) mCustomView
                .findViewById(R.id.arrow);
        arrowImage.setBackground(getResources().getDrawable(R.drawable.prodreg_actionbar_back_arrow_white));

        mActionBar.setCustomView(mCustomView, params);
        setTitle(getString(R.string.app_name));
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    private boolean backStackFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
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
                .findFragmentById(R.id.parent_layout);

        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return backStackFragment();
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setTitle(final CharSequence title) {
        super.setTitle(title);
        mTitleTextView.setText(title);
    }


}
