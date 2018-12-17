/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.baseapp.screens.utility;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.uid.view.widget.Label;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowDialog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, application = TestAppFrameworkApplication.class)
public class OverlayDialogFragmentTest {

    private TestActivity testActivity;
    private OverlayDialogFragment overlayDialogFragment;
    private ActivityController<TestActivity> activityController;

    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(TestActivity.class);
        testActivity = activityController.create().start().get();
    }

    @Test
    public void testDialogLaunchWithInvaildResId() {
        overlayDialogFragment = OverlayDialogFragment.newInstance(testActivity.getString(R.string.RA_DLS_Help_Philips_Shop), -1);
        overlayDialogFragment.show(testActivity.getFragmentManager(), null);
        Dialog dialog = ShadowDialog.getLatestDialog();
        Drawable drawable = ((ImageView) dialog.findViewById(R.id.imageHelp)).getBackground();
        assertNull(drawable);
    }

    @Test
    public void testDialogLaunchWithVaildData() {
        overlayDialogFragment = OverlayDialogFragment.newInstance(testActivity.getString(R.string.RA_DLS_Help_Philips_Shop), R.mipmap.philips_shop_overlay);
        overlayDialogFragment.show(testActivity.getFragmentManager(), null);
        Dialog dialog = ShadowDialog.getLatestDialog();
        Drawable drawable = ((ImageView) dialog.findViewById(R.id.imageHelp)).getBackground();
        Label description = (Label) dialog.findViewById(R.id.textView_overlay_subText);
        assertEquals(testActivity.getString(R.string.RA_DLS_Help_Philips_Shop), description.getText().toString());
        assertNotNull(drawable);
    }

    @Test
    public void testDialogDismissOnClick() {
        overlayDialogFragment = OverlayDialogFragment.newInstance(testActivity.getString(R.string.RA_DLS_Help_Philips_Shop), R.mipmap.philips_shop_overlay);
        overlayDialogFragment.show(testActivity.getFragmentManager(), null);
        Dialog dialog = ShadowDialog.getLatestDialog();
        RelativeLayout parentLayout = (RelativeLayout) dialog.findViewById(R.id.dialogBackground);
        parentLayout.performClick();
        assertFalse(dialog.isShowing());
    }

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        overlayDialogFragment = null;
        testActivity = null;
        activityController = null;
    }

}
