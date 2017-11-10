package com.philips.platform.csw;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import com.philips.platform.csw.mock.*;
import com.philips.platform.csw.utils.CustomRobolectricRunner;
import com.philips.platform.csw.wrapper.CswFragmentWrapper;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uappframework.listener.ActionBarListener;

import android.os.Bundle;

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
    public void getFragmentCount() throws Exception {
        givenFragmentCountIs(3);
        whenGetFragmetCountIsInvoked();
        thenFragmentCountIs(3);
    }

    @Test
    public void getFragmentCountWithBackStack() throws Exception {
        givenFragmentApplicationAndPropositionNamesAre(APPLICATION_NAME, PROPOSITION_NAME, IS_ADDED_TO_BACKSTACK);
        givenFragmentCountIs(3);
        whenGetFragmetCountIsInvoked();
        thenFragmentCountIs(4);
    }

    @Test
    public void onCreateView_setsApplicationAndPropositionName() throws Exception {
        givenArgumentsAre(APPLICATION_NAME, PROPOSITION_NAME, IS_ADDED_TO_BACKSTACK);
        whenOnCreateViewIsInvoked();
        thenApplicationNameIs(APPLICATION_NAME);
        thenPropositionNameIs(PROPOSITION_NAME);
    }

    @Test
    public void onCreateView_DoesNotSetApplicationAndPropositionName_WhenArgumentsAreNull() throws Exception {
        givenNullArguments();
        whenOnCreateViewIsInvoked();
        thenApplicationNameIs(null);
        thenPropositionNameIs(null);
    }

    @Test
    public void onCreateView_InvokesInflatorWthRightParams() throws Exception {
        givenArgumentsAre(APPLICATION_NAME, PROPOSITION_NAME, IS_ADDED_TO_BACKSTACK);
        whenOnCreateViewIsInvoked();
        thenBuildPermissionViewIsCreatedWith(R.id.csw_frame_layout_view_container, APPLICATION_NAME, PROPOSITION_NAME);
    }

    @Test
    public void onCreateView_BuildsPermissionView() throws Exception {
        givenArgumentsAre(APPLICATION_NAME, PROPOSITION_NAME, IS_ADDED_TO_BACKSTACK);
        whenOnCreateViewIsInvoked();
        thenBuildPermissionViewIsCreatedWith(R.id.csw_frame_layout_view_container, APPLICATION_NAME, PROPOSITION_NAME);
    }

    @Test
    public void onViewStateRestored_ReadsApplicationNameAndPropositionNameFromState() {
        givenBundleStateWithValues(APPLICATION_NAME, PROPOSITION_NAME, IS_ADDED_TO_BACKSTACK);
        whenOnViewStateRestoredIsInvoked();
        thenApplicationNameIs(APPLICATION_NAME);
        thenPropositionNameIs(PROPOSITION_NAME);
        thenIsAddedToBackStackIs(IS_ADDED_TO_BACKSTACK);
    }

    @Test
    public void onViewStateRestored_DoesNotSetAppNameAndPropName_WhenStateIsNull() {
        whenOnViewStateRestoredIsInvoked();
        thenApplicationNameIs(null);
        thenPropositionNameIs(null);
    }

    @Test
    public void onViewStateSave_savesApplicationNameAndPropositionNameToState() {
        givenEmptyBundleState();
        givenFragmentApplicationAndPropositionNamesAre(APPLICATION_NAME, PROPOSITION_NAME, IS_ADDED_TO_BACKSTACK);
        whenOnViewStateSaveIsInvoked();
        thenStateContainsApplicationName(APPLICATION_NAME);
        thenStateContainsPropositionName(PROPOSITION_NAME);
        thenStateContainsIsAddedToBackStack(IS_ADDED_TO_BACKSTACK);
    }

    @Test
    public void onViewStateSave_savesApplicationNameAndPropositionNameToState_WhenStateIsNull() {
        whenOnViewStateSaveIsInvoked();
        Assert.assertNull(state);
    }

    @Test
    public void onViewStateSave_DoesNotSetAppNameAndPropName_WhenStateIsNull() {
        whenOnViewStateRestoredIsInvoked();
        thenApplicationNameIs(null);
        thenPropositionNameIs(null);
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

    private void givenFragmentApplicationAndPropositionNamesAre(String applicationName, String propositionName, boolean addedToBackStack) {
        Bundle bundle = new Bundle();
        bundle.putString(PROPOSITION_NAME_KEY, propositionName);
        bundle.putString(APPLICATION_NAME_KEY, applicationName);
        bundle.putBoolean(BUNDLE_KEY_ADDTOBACKSTACK, addedToBackStack);
        fragment.onViewStateRestored(bundle);
    }

    private void thenUpdateTitleListenerReturnedIs(ActionBarListener expectedActionBarListener) {
        assertEquals(expectedActionBarListener, actualActionBarListener);
    }

    private void whenGetUpdateTitleListener() {
        actualActionBarListener = fragment.getUpdateTitleListener();
    }

    private void givenUpdateTitleListenerIsSet(ActionBarListener actionBarListener) {
        fragment.setOnUpdateTitleListener(actionBarListener);
    }

    private void givenNullArguments() {
        fragment.setArguments(null);
    }

    private void whenOnViewStateRestoredIsInvoked() {
        fragment.onViewStateRestored(state);
    }

    private void whenOnViewStateSaveIsInvoked() {
        fragment.onSaveInstanceState(state);
    }

    private void givenBundleStateWithValues(String applicationName, String propositionName, boolean addedToBackStack) {
        state = new Bundle();
        state.putString(PROPOSITION_NAME_KEY, propositionName);
        state.putString(APPLICATION_NAME_KEY, applicationName);
        state.putBoolean(BUNDLE_KEY_ADDTOBACKSTACK, addedToBackStack);
    }

    private void thenResourceIdReturnedIs(int expectedResourceId) {
        assertEquals(expectedResourceId, actualResourceId);
    }

    private void whenGetResourceIdIsInvoked() {
        actualResourceId = fragment.getResourceID();
    }

    private void givenResourceIdIsSetTo(int resourceId) {
        fragment.setResourceID(resourceId);
    }

    private void givenEmptyBundleState() {
        state = new Bundle();
    }

    private void thenBuildPermissionViewIsCreatedWith(int csw_frame_layout_view_container, String applicationName, String propositionName) {
        assertEquals(csw_frame_layout_view_container, fragmentTransactionMock.replace_containerId);
        assertEquals(applicationName, fragmentTransactionMock.replace_fragment.getArguments().get("appName"));
        assertEquals(propositionName, fragmentTransactionMock.replace_fragment.getArguments().get("propName"));
    }

    private void thenPropositionNameIs(String propositionName) {
        Assert.assertEquals(propositionName, fragment.getPropositionName());
    }

    private void thenIsAddedToBackStackIs(boolean isAddedToBackstack) {
        Assert.assertEquals(isAddedToBackstack, fragment.getIsAddedToBackStack());
    }

    private void thenApplicationNameIs(String applicationName) {
        Assert.assertEquals(applicationName, fragment.getApplicationName());
    }

    private void thenStateContainsApplicationName(String expected) {
        Assert.assertEquals(expected, state.getString(APPLICATION_NAME_KEY));
    }

    private void thenStateContainsPropositionName(String expected) {
        Assert.assertEquals(expected, state.getString(PROPOSITION_NAME_KEY));
    }

    private void thenStateContainsIsAddedToBackStack(boolean expected) {
        Assert.assertEquals(expected, state.getBoolean(BUNDLE_KEY_ADDTOBACKSTACK));
    }

    private void whenOnCreateViewIsInvoked() {
        fragment.onCreateView(mockLayoutInflater, null, null);
    }

    private void givenArgumentsAre(String applicationName, String propositionName, boolean isAddedToBackStack) {
        fragment.setArguments(applicationName, propositionName, isAddedToBackStack);
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

    private void thenFragmentCountIs(int expectedCount) {
        assertEquals(expectedCount, actualFragmentCount);
    }

    private void whenGetFragmetCountIsInvoked() {
        actualFragmentCount = fragment.getFragmentCount();
    }

    private void givenFragmentCountIs(int numberOfFragments) {
        fragmentManagerMock.backStackCount = numberOfFragments;
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
    private static final String APPLICATION_NAME_KEY = "appName";
    private static final String PROPOSITION_NAME_KEY = "propName";
    public static final String BUNDLE_KEY_ADDTOBACKSTACK = "addToBackStack";
    private static final String PROPOSITION_NAME = "PROPOSITION_NAME";
    private static final String APPLICATION_NAME = "APPLICATION_NAME";
    private static final boolean IS_ADDED_TO_BACKSTACK = true;

    LayoutInflatorMock mockLayoutInflater = LayoutInflatorMock.createMock();
}