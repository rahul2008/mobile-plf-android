
/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;

import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfig;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EWSBaseActivity extends DynamicThemeApplyingActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Todo check the theme flow then uncomment it
        super.onCreate(savedInstanceState);
    }

    //Todo need to change and get from EWSDependencies class

}