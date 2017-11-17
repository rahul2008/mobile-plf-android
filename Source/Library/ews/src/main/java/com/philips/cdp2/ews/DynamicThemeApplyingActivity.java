package com.philips.cdp2.ews;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DynamicThemeApplyingActivity extends UIDActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO those values will by dynamic with configurability story
        setTheme(R.style.Theme_DLS_GroupBlue_UltraLight);
        UIDHelper.init(new ThemeConfiguration(this, ColorRange.GROUP_BLUE, NavigationColor.BRIGHT,
                ContentColor.ULTRA_LIGHT));
        UIDHelper.injectCalligraphyFonts();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
