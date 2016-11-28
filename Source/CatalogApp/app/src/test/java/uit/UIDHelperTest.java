/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */

package uit;

import com.philips.platform.uid.thememanager.UIDHelper;

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
@PrepareForTest(UIDHelper.class)
public class UIDHelperTest {
    @Test
    public void ShouldInitColligraphyWhenRequested() throws Exception {
        PowerMockito.mockStatic(CalligraphyConfig.class);

        UIDHelper.injectCalligraphyFonts();

        PowerMockito.verifyStatic();
        CalligraphyConfig.initDefault(Mockito.any(CalligraphyConfig.class));
    }
}