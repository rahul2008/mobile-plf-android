package com.philips.platform.mya.csw.permission;

import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.mya.csw.mock.RestInterfaceMock;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by 310237243 on 23/01/2018.
 */
public class PermissionViewTest {

    @Test
    public void onResumeCalls() {
        givenInternetIsReachable();
        whenResuming();
        Mockito.verify(permissionPresenter).getConsentStatus();
    }



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


    private void givenInternetIsReachable() {
        restInterfaceMock.isInternetAvailable = true;
    }

    private void whenResuming() {
        permissionView.onResume();
    }

    private RestInterfaceMock restInterfaceMock  = new RestInterfaceMock();
    private PermissionView permissionView;

    @Mock
    private PermissionPresenter permissionPresenter;
}