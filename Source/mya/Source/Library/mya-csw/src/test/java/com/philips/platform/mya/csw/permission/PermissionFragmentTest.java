package com.philips.platform.mya.csw.permission;

import com.philips.platform.mya.csw.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class PermissionFragmentTest {

    @Before
    public void setup() {
        initMocks(this);

        permissionFragment = new PermissionFragment();
        permissionFragment.setPresenter(permissionPresenter);

        SupportFragmentTestUtil.startFragment(permissionFragment);
    }

    @Test
    public void onResume_setsResourceIdOnParentFragment() {
        whenResuming();
        thenSetResourceIdIsInvokedWith(R.string.csw_privacy_settings);
    }

    @Test
    public void onResume_fetchesConsentDefinitions() {
        whenResuming();
        thenConsentDefinitionsAreFetched();
    }

    private void whenResuming() {
        permissionFragment.onResume();
    }

    private void thenSetResourceIdIsInvokedWith(int expectedResourceId) {
        assertEquals(expectedResourceId, permissionFragment.getTitleResourceId());
    }

    private void thenConsentDefinitionsAreFetched() {
        verify(permissionPresenter).fetchConsentStates(null);
    }

    private PermissionFragment permissionFragment;

    @Mock
    private PermissionPresenter permissionPresenter;
}