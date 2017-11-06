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
public class CatkLaunchInputTest {
    @Test
    public void CheckContextIsSet() throws Exception {
        CatkInputs catkLaunchInput = new CatkInputs();
        catkLaunchInput.setContext(ShadowApplication.getInstance().getApplicationContext());
        assertNotNull(catkLaunchInput.getContext());
    }

    @Test
    public void CheckContextIsSetNull() throws Exception {
        CatkInputs catkLaunchInput = new CatkInputs();
        catkLaunchInput.setContext(null);
        assertNull(catkLaunchInput.getContext());
    }

    @Test
    public void CheckContextIsSetnotAssiged() throws Exception {
        CatkInputs catkLaunchInput = new CatkInputs();;
        assertNull(catkLaunchInput.getContext());
    }


}