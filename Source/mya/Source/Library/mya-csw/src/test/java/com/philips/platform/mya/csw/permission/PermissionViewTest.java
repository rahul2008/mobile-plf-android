package com.philips.platform.mya.csw.permission;

import android.support.v4.app.FragmentActivity;

import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.mya.csw.mock.DialogViewMock;
import com.philips.platform.mya.csw.mock.RestInterfaceMock;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by 310237243 on 23/01/2018.
 */
public class PermissionViewTest {

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
       //verify(dialogView).showDialog((FragmentActivity) any());
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


    private void givenInternetIsUnReachable() {
        restInterfaceMock.isInternetAvailable = false;
    }


    private void whenResuming() {
        permissionView.onResume();
    }

    private RestInterfaceMock restInterfaceMock  = new RestInterfaceMock();
    private PermissionView permissionView;

    @Mock
    private PermissionPresenter permissionPresenter;

    @Mock
    private DialogViewMock dialogView;
}