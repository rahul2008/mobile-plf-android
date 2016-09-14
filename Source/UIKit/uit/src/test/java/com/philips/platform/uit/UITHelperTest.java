/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package com.philips.platform.uit;

import com.philips.platform.uit.thememanager.UITHelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest(UITHelper.class)
public class UITHelperTest {
    @Test
    public void ShouldInitColligraphyWhenRequested() throws Exception {
        PowerMockito.mockStatic(CalligraphyConfig.class);

        UITHelper.injectCalligraphyFonts();

        PowerMockito.verifyStatic();
        CalligraphyConfig.initDefault(Mockito.any(CalligraphyConfig.class));
    }
}