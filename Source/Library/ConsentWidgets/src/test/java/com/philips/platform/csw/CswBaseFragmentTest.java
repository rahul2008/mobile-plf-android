package com.philips.platform.csw;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import com.philips.platform.csw.mock.ActionBarListenerMock;
import com.philips.platform.csw.utils.CustomRobolectricRunner;
import com.philips.platform.csw.wrapper.CswBaseFragmentWrapper;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = com.philips.platform.mya.consentaccesstoolkit.BuildConfig.class, sdk = 25)
public class CswBaseFragmentTest {
    @Test
    public void onResume_setsTitle() throws Exception {

    }

    @Before
    public void setup() {
        actionBarListener = new ActionBarListenerMock();
        baseFragment = new CswBaseFragmentWrapper();
        baseFragment.setActionBarListener(actionBarListener);
    }

    CswBaseFragmentWrapper baseFragment;
    ActionBarListenerMock actionBarListener;
}
