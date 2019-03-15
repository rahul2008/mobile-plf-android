package com.philips.platform.pim;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.philips.platform.pim.utilities.PIMConstants;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

public class PimActivity extends UIDActivity implements ActionBarListener {
    private final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;
    private ImageView mBackImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.udi_activity);
        createActionBar();
    }


    private void createActionBar() {
        FrameLayout frameLayout = findViewById(R.id.udi_header_back_button);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onBackPressed();
            }
        });

        mBackImage = findViewById(R.id.udi_iv_header_back_button);
        Drawable mBackDrawable = VectorDrawableCompat.create(getResources(), R.drawable.udi_back_arrow, getTheme());
        mBackImage.setBackground(mBackDrawable);
        setTitle(getString(R.string.udi_app_name));

    }

    @Override
    public void updateActionBar(int resId, boolean enableBackKey) {
        if (enableBackKey) {
            mBackImage.setVisibility(View.VISIBLE);
        } else {
            mBackImage.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateActionBar(String resString, boolean enableBackKey) {
        if (enableBackKey) {
            mBackImage.setVisibility(View.VISIBLE);
        } else {
            mBackImage.setVisibility(View.GONE);
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
