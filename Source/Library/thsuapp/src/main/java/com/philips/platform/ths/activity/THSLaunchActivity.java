package com.philips.platform.ths.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


import com.philips.platform.ths.R;
import com.philips.platform.ths.welcome.THSWelcomeFragment;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

import static com.philips.platform.ths.uappclasses.THSMicroAppInterface.WELCOME_MESSAGE;


public class THSLaunchActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private TextView mTitleTextView;
    private int containerId;

    private static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";

    private final int DEFAULT_THEME = R.style.Theme_DLS_GroupBlue_UltraLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
//        initCustomActionBar();
        setContentView(R.layout.ths_rename_activity_test_ur);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            THSWelcomeFragment pthWelcomeFragment = new THSWelcomeFragment
                    ();
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String message = extras.getString(WELCOME_MESSAGE);
                Bundle bundle = new Bundle();
                bundle.putString(WELCOME_MESSAGE, message);
                pthWelcomeFragment.setArguments(bundle);
            }
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.parent_layout, pthWelcomeFragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    /*private void initCustomActionBar() {
        ActionBar mActionBar = this.getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the text view in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.ths_custom_action_bar, null); // layout which contains your button.

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
    }*/

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


    private int getContainerId() {
        return R.id.parent_layout;
    }

     public void initTheme() {
        int themeIndex = getIntent().getIntExtra(KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT));
    }

}
