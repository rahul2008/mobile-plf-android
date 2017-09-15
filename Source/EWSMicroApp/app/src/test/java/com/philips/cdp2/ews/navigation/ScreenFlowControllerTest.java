/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.navigation;

import android.annotation.SuppressLint;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.philips.cdp2.ews.view.EWSBaseFragment;
import com.philips.cdp2.ews.view.EWSDevicePowerOnFragment;
import com.philips.cdp2.ews.view.EWSProductSupportFragment;
import com.philips.cdp2.powersleep.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ScreenFlowControllerTest {

    @Mock
    private FragmentManager fragmentManagerMock;

    @Mock
    private FragmentTransaction fragmentTransactionMock;

    @Mock
    private AppCompatActivity activityMock;

    @Mock
    private Toolbar toolbarMock;

    private TestScreenFlowParticipantFragment rootFragment;

    private Fragment fragment;

    private ScreenFlowController screenFlowController;

    @Mock
    private TextView textViewMock;

    @Mock
    private ActionBar actionBarMock;

    @IdRes
    private int contentFrameId = 1;
    private int toolbarId = R.string.ews_title;

    @SuppressLint("CommitTransaction")
    @Before
    public void setUp() throws Exception {
        initMocks(this);

        stubToolbar();
        when(activityMock.getSupportFragmentManager()).thenReturn(fragmentManagerMock);
        when(fragmentManagerMock.beginTransaction()).thenReturn(fragmentTransactionMock);

        rootFragment = new TestScreenFlowParticipantFragment(toolbarId, 1);
        screenFlowController = new ScreenFlowController();

        fragment = new Fragment();
    }

    @Test
    public void shouldInitToolbarWhenStartIsCalled() throws Exception {
        start();

        verify(activityMock).setSupportActionBar(toolbarMock);
        verify(actionBarMock).setDisplayShowHomeEnabled(false);
        verify(actionBarMock).setDisplayShowTitleEnabled(false);
        verify(actionBarMock).setDisplayShowCustomEnabled(true);
    }

    @Test
    public void shouldAddRootFragmentToContentFrameOnFirstStart() {
        start();

        verify(fragmentTransactionMock).replace(contentFrameId, rootFragment, rootFragment.getClass().getName());
        verify(fragmentTransactionMock).addToBackStack(rootFragment.getClass().getName());
        verify(fragmentTransactionMock).commitAllowingStateLoss();
    }

    @Test
    public void shouldNotAddRootFragmentAgainIfStartIsCalledMoreThanOnce() {
        start();
        start();
        verify(fragmentTransactionMock, times(1)).replace(contentFrameId, rootFragment, rootFragment.getClass().getName());
        verify(fragmentTransactionMock, times(1)).addToBackStack(rootFragment.getClass().getName());
        verify(fragmentTransactionMock, times(1)).commitAllowingStateLoss();
    }

    @Test
    public void shouldAddBackStackListenerWhenStarted() {
        start();

        verify(fragmentManagerMock).addOnBackStackChangedListener(any(FragmentManager.OnBackStackChangedListener.class));
    }

    @Test
    public void shouldStartOnlyOnceWhenStartedTwoTimeInARow() {
        start();

        verify(fragmentManagerMock).addOnBackStackChangedListener(any(FragmentManager.OnBackStackChangedListener.class));
    }

    @Test
    public void shouldRemovedBackStackListenerOnlyOnceWhenStoppedTwice() {
        start();

        screenFlowController.stop();
        screenFlowController.stop();

        verify(fragmentManagerMock, times(1)).removeOnBackStackChangedListener(any(FragmentManager.OnBackStackChangedListener.class));
    }

    @Test
    public void shouldRemovedBackStackListenerWhenStopped() {
        start();

        FragmentManager.OnBackStackChangedListener backStackChangedListener = getOnBackStackChangedListener();

        screenFlowController.stop();

        verify(fragmentManagerMock).removeOnBackStackChangedListener(backStackChangedListener);
    }

    @Test
    public void shouldNotAddRootFragmentToViewWhenStartedAndBackStackIsNotEmpty() {
        addFragmentToMockBackStack(rootFragment, 1);

        start();

        verify(fragmentTransactionMock, never()).addToBackStack(rootFragment.getClass().getName());
    }

    @Test
    public void shouldNeverAddASecondRootFragmentToViewWhenShowFragmentIsCalled() {
        List<Fragment> fragments = Collections.singletonList((Fragment) rootFragment);
        when(fragmentManagerMock.getFragments()).thenReturn(fragments);

        start();
        addFragmentToMockBackStack(rootFragment, 1);

        reset(fragmentTransactionMock);

        screenFlowController.showFragment(rootFragment);
        verify(fragmentTransactionMock, never()).commitAllowingStateLoss();
    }

    @Test
    public void shouldFinishActivityWhenBackStackIsEmpty() {
        when(fragmentManagerMock.getBackStackEntryCount()).thenReturn(0);

        start();
        FragmentManager.OnBackStackChangedListener onBackStackChangedListener = getOnBackStackChangedListener();
        onBackStackChangedListener.onBackStackChanged();
        verify(activityMock).finish();
    }

    @Test
    public void shouldNotAddFragmentWhenControllerIsNotYetStarted() {
        TestScreenFlowParticipantFragment fragment = new TestScreenFlowParticipantFragment(toolbarId, 1);
        screenFlowController.showFragment(fragment);
        verifyZeroInteractions(fragmentTransactionMock);
    }

    @Test
    public void shouldAddAdditionalFragmentsToBackStackWhenHierarchyLevelIncreases() {
        TestScreenFlowParticipantFragment fragmentHierarchy1 = new TestScreenFlowParticipantFragment(toolbarId, 1);
        TestScreenFlowParticipantFragment fragmentHierarchy2 = new TestScreenFlowParticipantFragment(toolbarId, 2);
        start();
        screenFlowController.showFragment(fragmentHierarchy1);
        screenFlowController.showFragment(fragmentHierarchy2);

        verify(fragmentTransactionMock).replace(contentFrameId, rootFragment, rootFragment.getClass().getName());
        verify(fragmentTransactionMock).replace(contentFrameId, fragmentHierarchy1, fragmentHierarchy1.getClass().getName());
        verify(fragmentTransactionMock).replace(contentFrameId, fragmentHierarchy2, fragmentHierarchy2.getClass().getName());
    }

    @Test
    public void shouldRemoveLowerHierarchyLevelFragmentsFromBackStackWhenHierarchyLevelDecreases() {

        TestScreenFlowParticipantFragment fragmentHierarchy1_A = new TestScreenFlowParticipantFragment(toolbarId, 1);
        TestAnotherScreenFlowParticipantFragment fragmentHierarchy1_B = new TestAnotherScreenFlowParticipantFragment(1);
        TestScreenFlowParticipantFragment fragmentHierarchy2 = new TestScreenFlowParticipantFragment(toolbarId, 2);

        when(fragmentManagerMock.getBackStackEntryCount()).thenReturn(0);
        start();

        addFragmentToMockBackStack(rootFragment, 1);
        screenFlowController.showFragment(fragmentHierarchy1_A);

        addFragmentToMockBackStack(fragmentHierarchy1_A, 2);
        screenFlowController.showFragment(fragmentHierarchy2);

        addFragmentToMockBackStack(fragmentHierarchy2, 3);
        screenFlowController.showFragment(fragmentHierarchy1_B);

        verify(fragmentManagerMock, times(2)).popBackStack();
    }

    @Test
    public void shouldNotRemoveAndAddSameFragmentWhenIdenticalInstanceOfFragmentIsShownTwice() {
        TestAnotherScreenFlowParticipantFragment fragment = new TestAnotherScreenFlowParticipantFragment(1);

        when(fragmentManagerMock.getBackStackEntryCount()).thenReturn(0);
        start();
        verify(fragmentTransactionMock).replace(contentFrameId, rootFragment, rootFragment.getClass().getName());

        addFragmentToMockBackStack(rootFragment, 1);
        screenFlowController.showFragment(fragment);

        verify(fragmentTransactionMock).replace(contentFrameId, fragment, fragment.getClass().getName());

        addFragmentToMockBackStack(rootFragment, 1);
        addFragmentToMockBackStack(fragment, 2);

        addFragmentToMockBackStack(fragment, 2);
        screenFlowController.showFragment(fragment);

        verify(fragmentTransactionMock).replace(contentFrameId, fragment, fragment.getClass().getName());
        verify(fragmentManagerMock, never()).popBackStack();
    }

    @Test
    public void shouldSetToolbarTitleWhenBackStackIsUpdated() {
        addFragmentToMockBackStack(rootFragment, 1);

        start();

        FragmentManager.OnBackStackChangedListener backStackChangedListener = getOnBackStackChangedListener();
        backStackChangedListener.onBackStackChanged();

        verify(textViewMock).setText(toolbarId);
    }

    @Test
    public void shouldNotUpdateTitleWhenBackStackIsEmpty() throws Exception {
        when(fragmentManagerMock.getBackStackEntryCount()).thenReturn(0);

        start();

        verify(toolbarMock, never()).setTitle(anyInt());
    }

    @Test
    public void shouldReturnFalseWhenThereAreNoFragmentsAndBackIsPressed() {
        start();

        final boolean res = screenFlowController.onBackPressed();

        assertFalse(res);
    }

    @Test
    public void shouldReturnFalseWhenThereAreScreenFlowParticipantsAndBackIsPressed() {
        start();

        when(fragmentManagerMock.getFragments()).thenReturn(Collections.singletonList(fragment));

        final boolean res = screenFlowController.onBackPressed();

        assertFalse(res);
    }

    @Test
    public void shouldReturnFalseWhenScreenFlowParticipantDoesNotHandlesBackPress() {
        assertFalse(handleBackPress(false));
    }

    @Test
    public void shouldReturnTrueWhenScreenFlowParticipantHandlesBackPress() {
        assertTrue(handleBackPress(true));
    }

    @Test
    public void shouldNotCallPopBackStackWhenHasBackStackReturnsFalse() throws Exception {

        start();

        when(fragmentManagerMock.getBackStackEntryCount()).thenReturn(0);
        screenFlowController.popBackStack();

        verify(fragmentManagerMock, never()).popBackStack();
    }

    @Test
    public void shouldCallPopBackStackWhenHasBackStackReturnsTrue() throws Exception {
        start();

        when(fragmentManagerMock.getBackStackEntryCount()).thenReturn(1);
        screenFlowController.popBackStack();

        verify(fragmentManagerMock).popBackStack();
    }

    @Test
    public void shouldSetHomeToolbarIconWhenBackStackIsUpdated() {
        TestAnotherScreenFlowParticipantFragment notFirstFragment = new TestAnotherScreenFlowParticipantFragment(1);

        addFragmentToMockBackStack(notFirstFragment, 2);

        verifyNavigationIcon();
        verify(toolbarMock).setNavigationIcon(ScreenFlowController.NAVIGATION_BACK_ICON);
    }

    @Test
    public void shouldNOTSetHomeToolbarIconForRootFragment() {

        addFragmentToMockBackStack(rootFragment, 1);

        verifyNavigationIcon();
        verify(toolbarMock).setNavigationIcon(null);
    }

    @Test
    public void shouldSetNavigationBackIconWhenRootFragmentIsNotGettingStartedFragment() {
        EWSBaseFragment ewsGettingStartedFragment = new EWSDevicePowerOnFragment();
        addFragmentToMockBackStack(ewsGettingStartedFragment, 1);

        verifyNavigationIcon();
        verify(toolbarMock).setNavigationIcon(ScreenFlowController.NAVIGATION_BACK_ICON);
    }

    private void verifyNavigationIcon() {
        start();

        FragmentManager.OnBackStackChangedListener backStackChangedListener = getOnBackStackChangedListener();
        backStackChangedListener.onBackStackChanged();
    }

    @Test
    public void shouldPopBackStackToNextHierarchyLevelWhenHomePressed() throws Exception {
        TestScreenFlowParticipantFragment fragment = new TestScreenFlowParticipantFragment(toolbarId, 2);

        addFragmentToMockBackStack(rootFragment, 1);
        addFragmentToMockBackStack(fragment, 2);

        start();
        screenFlowController.homeButtonPressed();

        verify(fragmentManagerMock).popBackStack();
    }

    @Test
    public void shouldNotInteractWithFragmentManagerWhenBackStackIsEmpty() throws Exception {
        when(fragmentManagerMock.getBackStackEntryCount()).thenReturn(0);
        start();
        reset(fragmentManagerMock);

        screenFlowController.homeButtonPressed();

        verify(fragmentManagerMock, never()).popBackStack();
        verify(fragmentManagerMock, never()).executePendingTransactions();
    }

    @Test
    public void ShouldFinishActivityWhenFinishIsCalled() throws Exception {
        start();
        addFragmentToMockBackStack(rootFragment, 4);

        screenFlowController.finish();

        verify(activityMock).finish();
    }

    @Test
    public void shouldSetToolbarTitleIdWhenAsked() throws Exception {
        start();
        screenFlowController.setToolbarTitle(R.string.ews_title);

        verify(textViewMock).setText(R.string.ews_title);
    }

    @Test
    public void shouldSetToolbarTitleStringWhenAsked() throws Exception {
        String support = "Support";

        start();
        screenFlowController.setToolbarTitle(support);

        verify(textViewMock).setText(support);
    }

    @Test
    public void shouldPopBackImmediatelyIfTopFragmentIsProductSupportWhenHomeButtonIsPressed() throws Exception {
        int numberOfFragments = EWSProductSupportFragment.PRODUCT_SUPPORT_HIERARCHY_LEVEL + 1;
        TestAnotherScreenFlowParticipantFragment fragment1 = new TestAnotherScreenFlowParticipantFragment(2);

        addFragmentToMockBackStack(rootFragment, 1);
        addFragmentToMockBackStack(fragment1, numberOfFragments);

        start();
        screenFlowController.homeButtonPressed();

        verify(fragmentManagerMock, times(EWSProductSupportFragment.PRODUCT_SUPPORT_HIERARCHY_LEVEL)).popBackStack();
    }

    @Test
    public void shouldPopBackFromStackIfTopFragmentIsNotFromEWSWhenHomeButtonIsPressed() throws Exception {
        addFragmentToMockBackStack(rootFragment, 1);
        addFragmentToMockBackStack(new Fragment(), 2);

        start();
        screenFlowController.homeButtonPressed();

        verify(fragmentManagerMock).popBackStack();
    }

    @Test
    public void shouldPopBackImmediatelyIfTopFragmentIsProductSupportWhenBackButtonIsPressed() throws Exception {
        int numberOfFragments = EWSProductSupportFragment.PRODUCT_SUPPORT_HIERARCHY_LEVEL + 1;

        addFragmentToMockBackStack(rootFragment, 1);
        addFragmentToMockBackStack(new Fragment(), numberOfFragments);

        start();
        screenFlowController.onBackPressed();

        verify(fragmentManagerMock).popBackStackImmediate();
    }

    private void addFragmentToMockBackStack(final Fragment fragment, int numberOfFragments) {
        FragmentManager.BackStackEntry backStackEntryMock = mock(FragmentManager.BackStackEntry.class);
        when(fragmentManagerMock.getBackStackEntryCount()).thenReturn(numberOfFragments);
        when(fragmentManagerMock.getBackStackEntryAt(numberOfFragments - 1)).thenReturn(backStackEntryMock);
        when(backStackEntryMock.getName()).thenReturn(fragment.getClass().getName());
        when(fragmentManagerMock.findFragmentByTag(fragment.getClass().getName())).thenReturn(fragment);
    }

    private void start() {
        screenFlowController.start(activityMock, contentFrameId, rootFragment);
    }

    private boolean handleBackPress(final boolean isBackPressed) {
        start();
        addFragmentToMockBackStack(rootFragment, 1);
        rootFragment.backPressed = isBackPressed;

        return screenFlowController.onBackPressed();
    }

    private void stubToolbar() {
        when(activityMock.findViewById(R.id.ews_toolbar)).thenReturn(toolbarMock);
        when(activityMock.getSupportActionBar()).thenReturn(actionBarMock);
        when(toolbarMock.findViewById(R.id.toolbar_title)).thenReturn(textViewMock);
        when(toolbarMock.getContext()).thenReturn(activityMock);
    }

    private FragmentManager.OnBackStackChangedListener getOnBackStackChangedListener() {
        ArgumentCaptor<FragmentManager.OnBackStackChangedListener> captor = ArgumentCaptor.forClass(FragmentManager.OnBackStackChangedListener.class);
        verify(fragmentManagerMock).addOnBackStackChangedListener(captor.capture());
        return captor.getValue();
    }

    @SuppressLint("ValidFragment")
    private class TestScreenFlowParticipantFragment extends Fragment implements ScreenFlowParticipant {

        public int testToolbarId;
        public int testHierarchyLevel;
        public boolean backPressed;

        public TestScreenFlowParticipantFragment(final int testToolbarId, final int testHierarchyLevel) {
            this.testToolbarId = testToolbarId;
            this.testHierarchyLevel = testHierarchyLevel;
        }

        @Override
        public int getHierarchyLevel() {
            return testHierarchyLevel;
        }

        @Override
        public boolean onBackPressed() {
            return backPressed;
        }

        @Override
        public int getNavigationIconId() {
            return 0;
        }

        @Override
        public int getToolbarTitle() {
            return R.string.ews_title;
        }
    }

    @SuppressLint("ValidFragment")
    private class TestAnotherScreenFlowParticipantFragment extends Fragment implements ScreenFlowParticipant {

        private final int testHierarchyLevel;

        public TestAnotherScreenFlowParticipantFragment(final int testHierarchyLevel) {
            this.testHierarchyLevel = testHierarchyLevel;
        }

        @Override
        public int getHierarchyLevel() {
            return testHierarchyLevel;
        }

        @Override
        public boolean onBackPressed() {
            return false;
        }

        @Override
        public int getNavigationIconId() {
            return ScreenFlowController.NAVIGATION_BACK_ICON;
        }

        @Override
        public int getToolbarTitle() {
            return R.string.ews_08c_title;
        }
    }

    @SuppressLint("ValidFragment")
    private class TestSomeConsumer extends Fragment {

    }
}