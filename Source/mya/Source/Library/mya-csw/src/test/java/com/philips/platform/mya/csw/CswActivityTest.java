package com.philips.platform.mya.csw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.platform.mya.csw.permission.PermissionFragment;
import com.philips.platform.mya.csw.utils.CswTestApplication;
import com.philips.platform.mya.csw.utils.ShadowXIConTextView;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = CswTestApplication.class, shadows = {ShadowXIConTextView.class})
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*", "com.android.*", "com.sun.org.apache.xerces.internal.jaxp.*" })
@PrepareForTest({ThemeUtils.class, FontLoader.class})
public class CswActivityTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private FontLoader fontLoaderMock;
    @Mock
    private CswInterface cswInterfaceMock;
    @Mock
    private View viewMock;

    //
    private Intent testIntent;
    private ActivityController<CswActivity> activityController;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        // Init statics
        mockStatic(ThemeUtils.class);
        when(ThemeUtils.getThemeResourceID((Context) any())).thenReturn(R.style.AccentAqua_UltraLight);
        mockStatic(FontLoader.class);
        when(FontLoader.getInstance()).thenReturn(fontLoaderMock);

        testIntent = new Intent();
        testIntent.putExtra(CswConstants.DLS_THEME, R.style.AccentAqua);

        activityController = Robolectric.buildActivity(CswActivity.class, testIntent);
        activityController.get().cswInterface = cswInterfaceMock;
    }

    @Test
    public void givenActivitySetup_whenSettingUp_thenShouldNotCrash() {
        CswActivity startedActivity = activityController.create().start().resume().visible().get();

        assertNotNull(startedActivity);
    }

    @Test
    public void givenActivitySetup_whenOnBackPressed_thenShouldX() {
        CswActivity startedActivity = activityController.create().start().resume().visible().get();

        startedActivity.onBackPressed();

        // TODO Assert or verify
    }

    @Test
    public void givenActivitySetup_whenOnClick_andCorrectId_thenShouldX() {
        CswActivity startedActivity = activityController.create().start().resume().visible().get();
        when(viewMock.getId()).thenReturn(R.id.csw_textview_back);

        startedActivity.onClick(viewMock);

        // TODO Assert or verify
    }

    @Test
    public void givenActivitySetup_whenUpdateActionBar_andShowBackTrue_thenShouldBeVisible() {
        CswActivity startedActivity = activityController.create().start().resume().visible().get();

        startedActivity.updateActionBar(R.string.csw_offline_title, true);

        TextView titleView = startedActivity.findViewById(R.id.csw_textview_header_title);
        int visibility = titleView.getVisibility();
        assertEquals(View.VISIBLE, visibility);
    }

//    @Test
//    public void givenActivitySetup_whenUpdateActionBar_andShowBackFalse_thenShouldBeInvisible() {
//        CswActivity startedActivity = activityController.create().start().resume().visible().get();
//
//        startedActivity.updateActionBar(R.string.csw_offline_title, false);
//
//        TextView titleView = startedActivity.findViewById(R.id.csw_textview_header_title);
//        int visibility = titleView.getVisibility();
//        assertEquals(View.INVISIBLE, visibility);
//    }

    @Test
    public void givenActivitySetup_whenUpdateActionBarString_andShowBackTrue_thenShouldBeVisible() {
        CswActivity startedActivity = activityController.create().start().resume().visible().get();

        startedActivity.updateActionBar("This is a test title", true);

        TextView titleView = startedActivity.findViewById(R.id.csw_textview_header_title);
        int visibility = titleView.getVisibility();
        assertEquals(View.VISIBLE, visibility);
    }

//    @Test
//    public void givenActivitySetup_whenUpdateActionBarString_andShowBackFalse_thenShouldBeInvisible() {
//        CswActivity startedActivity = activityController.create().start().resume().visible().get();
//
//        startedActivity.updateActionBar("This is a test title", false);
//
//        TextView titleView = startedActivity.findViewById(R.id.csw_textview_header_title);
//        int visibility = titleView.getVisibility();
//        assertEquals(View.INVISIBLE, visibility);
//    }
}