package com.philips.platform.csw;

import android.os.Bundle;


import com.philips.platform.csw.mock.FragmentManagerMock;
import com.philips.platform.csw.mock.FragmentTransactionMock;
import com.philips.platform.csw.mock.LayoutInflatorMock;
import com.philips.platform.csw.utils.CustomRobolectricRunner;
import com.philips.platform.csw.wrapper.CswFragmentWrapper;
import com.philips.platform.mya.consentwidgets.R;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = com.philips.platform.mya.consentaccesstoolkit.BuildConfig.class, sdk = 25)
public class CswFragmentTest {

    @Before
    public void setup() {
        fragmentTransactionMock = new FragmentTransactionMock();
        fragmentManagerMock = new FragmentManagerMock(fragmentTransactionMock);
        fragment = new CswFragmentWrapper();
        fragment.fragmentManagerMock = fragmentManagerMock;
        fragment.setChildFragmentManager(fragmentManagerMock);
    }

    @Test
    public void onCreateView_setsApplicationAndPropositionName() throws Exception {
        givenArgumentsAre(APPLICATION_NAME,PROPOSITION_NAME);
        whenOnCreateViewIsInvoked();
        thenApplicationNameIs(APPLICATION_NAME);
        thenPropositionNameIs(PROPOSITION_NAME);
    }

    @Test
    public void onCreateView_InvokesInflatorWthRightParams() throws Exception {
        givenArgumentsAre(APPLICATION_NAME,PROPOSITION_NAME);
        whenOnCreateViewIsInvoked();
        thenPermissionViewIsInflatedWith(R.id.csw_frame_layout_view_container,APPLICATION_NAME, PROPOSITION_NAME);
    }

    @Test
    public void onCreateView_InflatesPermissionView() throws Exception {
        givenArgumentsAre(APPLICATION_NAME,PROPOSITION_NAME);
        whenOnCreateViewIsInvoked();
        thenInflatorInflateIsCalledWith(R.layout.csw_fragment_consent_widget_root, null, false);
    }

    @Test
    public void onBackPressed_handlesEmptyBackStack() throws Exception {
        givenBackStackDepthIs(0);
        whenOnBackPressedIsCalled();
        thenOnBackPressedReturns(true);
    }

    @Test
    public void onBackPressed_handlesNonEmptyBackStack() throws Exception {
        givenBackStackDepthIs(1);
        whenOnBackPressedIsCalled();
        thenOnBackPressedReturns(false);
        thenPopBackStackWasCalled();
    }

    @Test
    public void handleBackEvent_returnsNotHandledWhenBackStackCountIsZero() throws Exception {
        givenBackStackDepthIs(0);
        whenHandleBackEventIsCalled();
        thenEventIsNotHandled();
    }

    @Test
    public void handleBackEvent_returnsHandledWhenBackStackCountIsNonZero() throws Exception {
        givenBackStackDepthIs(1);
        whenHandleBackEventIsCalled();
        thenEventIsHandled();
    }

    private void thenPermissionViewIsInflatedWith(int csw_frame_layout_view_container, String applicationName, String propositionName) {
        assertEquals(csw_frame_layout_view_container, fragmentTransactionMock.replace_containerId);
        assertEquals(applicationName, fragmentTransactionMock.replace_fragment.getArguments().get("appName"));
        assertEquals(propositionName, fragmentTransactionMock.replace_fragment.getArguments().get("propName"));
    }

    private void thenInflatorInflateIsCalledWith(int csw_fragment_consent_widget_root, Object resource, boolean attachToRoot) {
        assertEquals(csw_fragment_consent_widget_root, mockLayoutInflater.usedResource);
        assertEquals(resource, mockLayoutInflater.usedViewGroup);
        assertEquals(attachToRoot, mockLayoutInflater.usedAttachToRoot);
    }

    private void thenPropositionNameIs(String propositionName) {
        Assert.assertEquals(propositionName, fragment.getPropositionName());
    }

    private void thenApplicationNameIs(String applicationName) {
        Assert.assertEquals(applicationName, fragment.getApplicationName());
    }

    private void whenOnCreateViewIsInvoked() {
        fragment.onCreateView(mockLayoutInflater, null, null);
    }

    private void givenArgumentsAre(String applicationName, String propositionName) {
        Bundle mockBundle = new Bundle();
        mockBundle.putString("appName",applicationName);
        mockBundle.putString("propName",propositionName);
        fragment.setArguments(mockBundle);
    }

    private void givenBackStackDepthIs(int depth) {
        fragmentManagerMock.backStackCount = depth;
    }

    private void whenOnBackPressedIsCalled() {
        onBackPressed_return = fragment.onBackPressed();
    }

    private void whenHandleBackEventIsCalled() {
        handleBackEvent_return = fragment.handleBackEvent();
    }

    private void thenPopBackStackWasCalled() {
        assertTrue(fragmentManagerMock.popBackStack_wasCalled);
    }

    private void thenOnBackPressedReturns(boolean expectedReturn) {
        assertEquals(expectedReturn, onBackPressed_return);
    }

    private void thenEventIsNotHandled() {
        assertFalse(handleBackEvent_return);
    }

    private void thenEventIsHandled() {
        assertTrue(handleBackEvent_return);
    }

    private FragmentManagerMock fragmentManagerMock;
    private boolean onBackPressed_return;
    private boolean handleBackEvent_return;
    private FragmentTransactionMock fragmentTransactionMock;
    private CswFragmentWrapper fragment;
    private static final String PROPOSITION_NAME = "PROPOSITION_NAME";
    private static final String APPLICATION_NAME = "APPLICATION_NAME";

    LayoutInflatorMock mockLayoutInflater = LayoutInflatorMock.createMock();
}