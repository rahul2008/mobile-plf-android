package com.philips.platform.mya.csw.permission;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.mya.csw.R;
import com.philips.platform.mya.csw.mock.DialogViewMock;
import com.philips.platform.mya.csw.mock.RestInterfaceMock;

public class PermissionViewTest {

    @Before
    public void setup() {
        initMocks(this);
        permissionView = new PermissionView() {
            @Override
            protected RestInterface getRestClient() {
                return restInterfaceMock;
            }

            @Override
            protected PermissionPresenter getPermissionPresenter() {
                return permissionPresenter;
            }
        };
    }

    @Test
    public void onResumeCallsPresenterIfInternetReachable() {
        givenInternetIsReachable();
        whenResuming();
        verify(permissionPresenter).getConsentStatus();
    }

    @Test
    @Ignore
    public void onResumeShowsErrorDialogIfInternetUnReachable() {
        givenInternetIsUnReachable();
        whenResuming();
    }

    @Test
    public void onResume_setsResourceIdOnParentFragment() throws Exception {
        whenResuming();
        thenSetResourceIdIsInvokedWith(R.string.csw_privacy_settings);
    }

    private void thenSetResourceIdIsInvokedWith(int i) {
        assertEquals(permissionView.getTitleResourceId(), i);
    }

    private void givenInternetIsReachable() {
        restInterfaceMock.isInternetAvailable = true;
    }

    private void givenInternetIsUnReachable() {
        restInterfaceMock.isInternetAvailable = false;
    }

    private void whenResuming() {
        permissionView.onResume();
    }

    private RestInterfaceMock restInterfaceMock = new RestInterfaceMock();
    private PermissionView permissionView;

    @Mock
    private PermissionPresenter permissionPresenter;

    @Mock
    private DialogViewMock dialogView;
}