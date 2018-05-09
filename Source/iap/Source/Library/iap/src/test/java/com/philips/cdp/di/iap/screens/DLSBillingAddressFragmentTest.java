package com.philips.cdp.di.iap.screens;

import android.content.Context;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.HashMap;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class DLSBillingAddressFragmentTest {
    private Context mContext;
    private DLSBillingAddressFragment dlsBillingAddressFragment;

    @Before
    public void setUp() {
        initMocks(this);
        mContext = RuntimeEnvironment.application;
        dlsBillingAddressFragment = new DLSBillingAddressFragment();

    }

    @Test(expected = NullPointerException.class)
    public void shouldDisplayAddressSelectionFragment() {

        SupportFragmentTestUtil.startFragment(dlsBillingAddressFragment);
    }

    @Mock
    HashMap<String, String> addressFiledMapMock;
    @Test(expected = NullPointerException.class)
    public void shouldUpdateFileds() throws Exception {
        dlsBillingAddressFragment.updateFields(addressFiledMapMock);
    }
}