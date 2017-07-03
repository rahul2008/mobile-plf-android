package com.philips.platform.samplemicroapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.uappdemo.UappDemoInterface;
import com.philips.platform.uappdemolibrary.R;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import static com.philips.platform.samplemicroapp.SampleMicroAppInterface.WELCOME_MESSAGE;


public class SampleActivity extends UIDActivity {

    private FragmentManager fragmentManager;
    private TextView mTitleTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIDHelper.init(UappDemoInterface.THEME_CONFIGURATION);
        super.onCreate(savedInstanceState);
        initCustomActionBar();
        setContentView(R.layout.activity_test_ur);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            SampleFragment sampleFragment = new SampleFragment();
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String message = extras.getString(WELCOME_MESSAGE);
                Bundle bundle = new Bundle();
                bundle.putString(WELCOME_MESSAGE, message);
                sampleFragment.setArguments(bundle);
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.parent_layout, sampleFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void initCustomActionBar() {
        ActionBar mActionBar = this.getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);

            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the text view in the ActionBar !
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);
            View mCustomView = LayoutInflater.from(this).inflate(R.layout.custom_action_bar, null); // layout which contains your button.

            mTitleTextView = (TextView) mCustomView.findViewById(R.id.text);

            final FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.UpButton);
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    onBackPressed();
                }
            });

            ImageView arrowImage = (ImageView) mCustomView
                    .findViewById(R.id.arrow);
//        arrowImage.setBackground(getResources().getDrawable(R.drawable.prodreg_left_arrow));

            mActionBar.setCustomView(mCustomView, params);
            setTitle(getString(R.string.app_name));
            mActionBar.setDisplayShowCustomEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        boolean backState = false;
        Fragment currentFrag = fragmentManager
                .findFragmentById(R.id.parent_layout);
        if (currentFrag != null && currentFrag instanceof BackEventListener) {
            backState = ((BackEventListener) currentFrag).handleBackEvent();
        }

        if (!backState) {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState, final PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("test", true);
    }

    @Override
    public void setTitle(final CharSequence title) {
        super.setTitle(title);
        mTitleTextView.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        if (mTitleTextView != null)
            mTitleTextView.setText(titleId);
        else
            super.setTitle(titleId);
    }

}
