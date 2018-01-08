package com.philips.platform.mya.csw;

import android.os.Bundle;

import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.csw.mock.ActionBarListenerMock;
import com.philips.platform.mya.csw.mock.FragmentManagerMock;
import com.philips.platform.mya.csw.mock.FragmentTransactionMock;
import com.philips.platform.mya.csw.mock.LayoutInflatorMock;
import com.philips.platform.mya.csw.utils.CustomRobolectricRunner;
import com.philips.platform.mya.csw.wrapper.CswFragmentWrapper;
import com.philips.platform.mya.csw.BuildConfig;
import com.philips.platform.mya.csw.R;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
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
    public void getFragmentCount() throws Exception {
        givenBundleStateWithValues(CONSENT_DEFINITIONS, IS_NOT_ADDED_TO_BACKSTACK);
        whenOnViewStateRestoredIsInvoked();
        givenFragmentCountIs(3);
        whenGetFragmetCountIsInvoked();
        thenFragmentCountIs(3);
    }

    @Test
    public void getFragmentCountWithBackStack() throws Exception {
        givenBundleStateWithValues(CONSENT_DEFINITIONS, IS_ADDED_TO_BACKSTACK);
        whenOnViewStateRestoredIsInvoked();
        givenFragmentCountIs(3);
        whenGetFragmetCountIsInvoked();
        thenFragmentCountIs(4);
    }

    @Test
    public void onCreateView_setsApplicationAndPropositionName() throws Exception {
        givenArgumentsAre(CONSENT_DEFINITIONS, IS_ADDED_TO_BACKSTACK);
        whenOnCreateViewIsInvoked();
    }

    @Test
    public void onCreateView_InvokesInflatorWthRightParams() throws Exception {
        givenArgumentsAre(CONSENT_DEFINITIONS, IS_ADDED_TO_BACKSTACK);
        whenOnCreateViewIsInvoked();
        thenBuildPermissionViewIsCreatedWith(R.id.csw_frame_layout_view_container);
    }

    @Test
    public void onCreateView_BuildsPermissionView() throws Exception {
        givenArgumentsAre(CONSENT_DEFINITIONS, IS_ADDED_TO_BACKSTACK);
        whenOnCreateViewIsInvoked();
        thenBuildPermissionViewIsCreatedWith(R.id.csw_frame_layout_view_container);
    }

    @Test
    public void onViewStateRestored_ReadsApplicationNameAndPropositionNameFromState() {
        givenBundleStateWithValues(CONSENT_DEFINITIONS, IS_ADDED_TO_BACKSTACK);
        whenOnViewStateRestoredIsInvoked();
        thenIsAddedToBackStackIs(IS_ADDED_TO_BACKSTACK);
    }

    @Test
    public void onViewStateSave_savesApplicationNameAndPropositionNameToState() {
        givenBundleStateWithValues(CONSENT_DEFINITIONS, IS_ADDED_TO_BACKSTACK);
        whenOnViewStateRestoredIsInvoked();
        whenOnViewStateSaveIsInvoked();
        thenStateContainsIsAddedToBackStack(IS_ADDED_TO_BACKSTACK);
    }

    @Test
    public void onViewStateSave_savesApplicationNameAndPropositionNameToState_WhenStateIsNull() {
        whenOnViewStateSaveIsInvoked();
        Assert.assertNull(state);
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

    @Test
    public void setsAndGetsResourceId() {
        givenResourceIdIsSetTo(123);
        whenGetResourceIdIsInvoked();
        thenResourceIdReturnedIs(123);
    }

    @Test
    public void setsAndGetsUpdateTitleListener() {
        givenUpdateTitleListenerIsSet(actionBarListenerMock);
        whenGetUpdateTitleListener();
        thenUpdateTitleListenerReturnedIs(actionBarListenerMock);
    }

    private void givenUpdateTitleListenerIsSet(ActionBarListener actionBarListener) {
        fragment.setOnUpdateTitleListener(actionBarListener);
    }

    private void givenArgumentsAre(List<ConsentDefinition> definitions, boolean isAddedToBackStack) {
        fragment.setArguments(new Bundle());
        fragment.getArguments().putBoolean(CswFragment.BUNDLE_KEY_ADDTOBACKSTACK, isAddedToBackStack);
    }

    private void givenBundleStateWithValues(List<ConsentDefinition> consentDefinitions, boolean addedToBackStack) {
        state = new Bundle();
        state.putBoolean(BUNDLE_KEY_ADDTOBACKSTACK, addedToBackStack);
    }

    private void givenEmptyBundleState() {
        state = new Bundle();
    }

    private void givenBackStackDepthIs(int depth) {
        fragmentManagerMock.backStackCount = depth;
    }

    private void givenFragmentCountIs(int numberOfFragments) {
        fragmentManagerMock.backStackCount = numberOfFragments;
    }

    private void givenResourceIdIsSetTo(int resourceId) {
        fragment.setResourceID(resourceId);
    }

    private void whenGetFragmetCountIsInvoked() {
        actualFragmentCount = fragment.getFragmentCount();
    }

    private void whenOnViewStateRestoredIsInvoked() {
        fragment.onViewStateRestored(state);
    }

    private void whenOnBackPressedIsCalled() {
        onBackPressed_return = fragment.onBackPressed();
    }

    private void whenOnViewStateSaveIsInvoked() {
        fragment.onSaveInstanceState(state);
    }

    private void whenHandleBackEventIsCalled() {
        handleBackEvent_return = fragment.handleBackEvent();
    }

    private void whenGetUpdateTitleListener() {
        actualActionBarListener = fragment.getUpdateTitleListener();
    }

    private void whenOnCreateViewIsInvoked() {
        fragment.onCreateView(mockLayoutInflater, null, null);
    }

    private void whenGetResourceIdIsInvoked() {
        actualResourceId = fragment.getResourceID();
    }

    private void thenBuildPermissionViewIsCreatedWith(int csw_frame_layout_view_container) {
        assertEquals(csw_frame_layout_view_container, fragmentTransactionMock.replace_containerId);
    }

    private void thenIsAddedToBackStackIs(boolean isAddedToBackstack) {
        Assert.assertEquals(isAddedToBackstack, fragment.getIsAddedToBackStack());
    }

    private void thenResourceIdReturnedIs(int expectedResourceId) {
        assertEquals(expectedResourceId, actualResourceId);
    }

    private void thenStateContainsIsAddedToBackStack(boolean expected) {
        Assert.assertEquals(expected, state.getBoolean(BUNDLE_KEY_ADDTOBACKSTACK));
    }

    private void thenUpdateTitleListenerReturnedIs(ActionBarListener expectedActionBarListener) {
        assertEquals(expectedActionBarListener, actualActionBarListener);
    }

    private void thenPopBackStackWasCalled() {
        assertTrue(fragmentManagerMock.popBackStack_wasCalled);
    }

    private void thenOnBackPressedReturns(boolean expectedReturn) {
        assertEquals(expectedReturn, onBackPressed_return);
    }

    private void thenFragmentCountIs(int expectedCount) {
        assertEquals(expectedCount, actualFragmentCount);
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
    private Bundle state;
    private int actualFragmentCount;
    private int actualResourceId;
    private ActionBarListener actualActionBarListener;
    private ActionBarListenerMock actionBarListenerMock = new ActionBarListenerMock();
    public static final String BUNDLE_KEY_ADDTOBACKSTACK = "addToBackStack";
    private static final List<ConsentDefinition> CONSENT_DEFINITIONS = new ArrayList<>();
    private static final boolean IS_ADDED_TO_BACKSTACK = true;
    private static final boolean IS_NOT_ADDED_TO_BACKSTACK = false;

    LayoutInflatorMock mockLayoutInflater = LayoutInflatorMock.createMock();
}