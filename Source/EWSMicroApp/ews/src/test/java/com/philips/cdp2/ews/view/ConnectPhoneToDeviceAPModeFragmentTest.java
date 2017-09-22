/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import android.content.pm.PackageManager;

import com.philips.cdp2.ews.databinding.FragmentEwsPressPlayFollowSetupDeviceBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.viewmodel.EWSPressPlayAndFollowSetupViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class ConnectPhoneToDeviceAPModeFragmentTest {

    private ConnectPhoneToDeviceAPModeFragment<EWSPressPlayAndFollowSetupViewModel, FragmentEwsPressPlayFollowSetupDeviceBinding> fragment;

    @Mock
    private FragmentEwsPressPlayFollowSetupDeviceBinding viewModelBinderMock;

    @Mock
    private EWSComponent ewsComponentMock;

    @Mock
    private EWSPressPlayAndFollowSetupViewModel viewModelMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(EWSTagger.class);
        fragment = new EWSPressPlayAndFollowSetupFragment();

        injectMembers();
    }

    private void injectMembers() {
        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                ((EWSPressPlayAndFollowSetupFragment) fragment).viewModel = viewModelMock;
                return null;
            }
        }).when(ewsComponentMock).inject(((EWSPressPlayAndFollowSetupFragment) fragment));
        fragment.inject(ewsComponentMock);
    }

    @Test
    public void ShouldCheckAllRequestedPermissionsHaveBeenGranted() throws Exception {
        final int[] grantedResults = {PackageManager.PERMISSION_GRANTED};
        getPermissionResult(grantedResults);

        verify(viewModelMock).areAllPermissionsGranted(grantedResults);
    }

    private int[] getPermissionResult(final int[] grantedResults) {
        final String[] permissions = {EWSPressPlayAndFollowSetupViewModel.ACCESS_COARSE_LOCATION};

        fragment.onRequestPermissionsResult(ConnectPhoneToDeviceAPModeFragment.LOCATION_PERMISSIONS_REQUEST_CODE,
                permissions, grantedResults);
        return grantedResults;
    }

    @Test
    public void shouldConnectToApplianceHotspotWhenAllPermissionAreGrantedOnResume() throws Exception {
        final int[] grantedResults = {PackageManager.PERMISSION_GRANTED};
        when(viewModelMock.areAllPermissionsGranted(grantedResults)).thenReturn(true);

        getPermissionResult(grantedResults);
        fragment.onResume();

        verify(viewModelMock).connectPhoneToDeviceHotspotWifi();
    }
}