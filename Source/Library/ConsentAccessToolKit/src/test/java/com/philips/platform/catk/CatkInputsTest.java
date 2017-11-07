package com.philips.platform.catk;

import com.philips.platform.mya.consentaccesstoolkit.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class CatkInputsTest {
    @Test
    public void checkContextIsSet() throws Exception {
        CatkInputs catkLaunchInput = new CatkInputs();
        catkLaunchInput.setContext(ShadowApplication.getInstance().getApplicationContext());
        assertNotNull(catkLaunchInput.getContext());
    }

    @Test
    public void checkContextIsSetNull() throws Exception {
        CatkInputs catkLaunchInput = new CatkInputs();
        catkLaunchInput.setContext(null);
        assertNull(catkLaunchInput.getContext());
    }

    @Test
    public void checkContextIsSetnotAssiged() throws Exception {
        CatkInputs catkLaunchInput = new CatkInputs();;
        assertNull(catkLaunchInput.getContext());
    }


}