package com.philips.platform.pim;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.fragment.PIMFragment;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.pim.utilities.PIMConstants;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

public class PIMActivity extends UIDActivity implements ActionBarListener {
    private final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;
    private ImageView mBackImage;
    private final String TAG = PIMActivity.class.getSimpleName();
    private LoggingInterface mLoggingInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pim_activity);
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG,TAG,"onCreate called");

        createActionBar();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_mainFragmentContainer, new PIMFragment(), PIMFragment.class.getSimpleName()).addToBackStack(null).commit();
    }


    private void createActionBar() {
        FrameLayout frameLayout = findViewById(R.id.pim_header_back_button);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG,TAG,"Header back button clicked");
                onBackPressed();
            }
        });

        mBackImage = findViewById(R.id.pim_iv_header_back_button);
        Drawable mBackDrawable = VectorDrawableCompat.create(getResources(), R.drawable.back_arrow, getTheme());
        mBackImage.setBackground(mBackDrawable);
        setTitle(getString(R.string.action_bar_title_texrt));
    }

    @Override
    public void updateActionBar(int resId, boolean enableBackKey) {
        if (enableBackKey) {
            mBackImage.setVisibility(View.VISIBLE);
            mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG,TAG,"Back key visibility set to VISIBLE");
        } else {
            mBackImage.setVisibility(View.GONE);
            mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG,TAG,"Back key visibility set to GONE");
        }
    }

    @Override
    public void updateActionBar(String resString, boolean enableBackKey) {
        if (enableBackKey) {
            mBackImage.setVisibility(View.VISIBLE);
            mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG,TAG,"Back key visibility set to VISIBLE");
        } else {
            mBackImage.setVisibility(View.GONE);
            mLoggingInterface.log(LoggingInterface.LogLevel.DEBUG,TAG,"Back key visibility set to GONE");
        }
    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(PIMConstants.PIM_KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
    }
}
