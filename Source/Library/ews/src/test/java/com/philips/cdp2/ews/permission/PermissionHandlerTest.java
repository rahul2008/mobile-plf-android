/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.util.StringProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.cdp2.ews.setupsteps.SecondSetupStepsViewModel.ACCESS_COARSE_LOCATION;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ContextCompat.class, Snackbar.class, Fragment.class})
public class PermissionHandlerTest {

    @InjectMocks PermissionHandler permissionHandler;

    @Mock
    private Context contextMock;

    @Mock
    BaseContentConfiguration mockBaseContentConfiguration;

    @Mock
    StringProvider mockStringProvider;


    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(ContextCompat.class);
        PowerMockito.mockStatic(Snackbar.class);
        PowerMockito.mockStatic(Fragment.class);
        when(mockBaseContentConfiguration.getAppName()).thenReturn(123234);
        when(mockStringProvider.getString(R.string.label_location_permission_required, mockBaseContentConfiguration.getAppName())).thenReturn("appname");
    }

    @Test
    public void itShouldCheckForPermissionWhenAsked() throws Exception {
        when(ContextCompat.checkSelfPermission(contextMock,
                ACCESS_COARSE_LOCATION)).thenReturn(0);

        assertTrue(permissionHandler.hasPermission(contextMock,
                ACCESS_COARSE_LOCATION));
    }

    @Test
    public void itShouldReturnTrueIfAllTheRequestedPermissionsAreGranted() throws Exception {
        final int[] grantedPermissions = {PackageManager.PERMISSION_GRANTED};

        assertTrue(permissionHandler.areAllPermissionsGranted(grantedPermissions));
    }

    @Test
    public void itShouldReturnFalseIfAtLeastOneRequestedPermissionIsNotGranted() throws Exception {
        final int[] grantedPermissions = {PackageManager.PERMISSION_DENIED, PackageManager.PERMISSION_GRANTED};

        assertFalse(permissionHandler.areAllPermissionsGranted(grantedPermissions));
    }

    @Test
    public void showShowSnackbarAskingForGrantingTheRequestedPermission() throws Exception {
        final Fragment fragmentMock = PowerMockito.mock(Fragment.class);
        final View viewMock = mock(View.class);

        verify(getSnackbar(fragmentMock, viewMock)).show();
    }

    private Snackbar getSnackbar(final Fragment fragmentMock, final View viewMock) {
        final int requestCode = 10;
        final Resources resMock = mock(Resources.class);
        Snackbar snackBarMock = PowerMockito.mock(Snackbar.class);

        Mockito.when(fragmentMock.getView()).thenReturn(viewMock);
        Mockito.when(fragmentMock.getResources()).thenReturn(resMock);

        when(Snackbar.make(viewMock,
                mockStringProvider.getString(R.string.label_location_permission_required, mockBaseContentConfiguration.getAppName()),
                Snackbar.LENGTH_INDEFINITE)).thenReturn(snackBarMock);

        permissionHandler.requestPermission(fragmentMock, R.string.label_location_permission_required,
                ACCESS_COARSE_LOCATION, requestCode);
        return snackBarMock;
    }

    @Test
    public void itShouldRequestSystemToGrantLocationPermissionWhenClockedOnOKButtonOnSnackbar() throws Exception {
        final ArgumentCaptor<View.OnClickListener> captor = ArgumentCaptor.forClass(View.OnClickListener.class);
        final Fragment fragmentMock = PowerMockito.mock(Fragment.class);
        final View viewMock = mock(View.class);

        Snackbar snackBarMock = getSnackbar(fragmentMock, viewMock);
        when(fragmentMock.isAdded()).thenReturn(true);
        verify(snackBarMock).setAction(anyInt(), captor.capture());
        captor.getValue().onClick(viewMock);

        verify(fragmentMock).requestPermissions(new String[]{ACCESS_COARSE_LOCATION}, 10);
        verify(snackBarMock).dismiss();
    }
}