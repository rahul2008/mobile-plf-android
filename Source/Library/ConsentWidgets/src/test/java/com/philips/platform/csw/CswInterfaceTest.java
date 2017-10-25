package com.philips.platform.csw;

import com.philips.platform.csw.mock.FragmentActivityMock;
import com.philips.platform.csw.mock.FragmentLauncherMock;
import com.philips.platform.csw.mock.FragmentManagerMock;
import com.philips.platform.csw.mock.FragmentTransactionMock;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class CswInterfaceTest {

    @Before
    public void setup() {
        fragmentTransactionMock = new FragmentTransactionMock();
        fragmentManagerMock = new FragmentManagerMock(fragmentTransactionMock);
        fragmentActivityMock = new FragmentActivityMock(fragmentManagerMock);
        cswInterface = new CswInterface();

    }

    @Test
    public void launchReplacesWithCswFragmentOnParentContainer() {
        givenParentContainerId(A_SPECIFIC_CONTAINER_ID);
        whenCallingLaunch(fragmentLauncherMock, new CswLaunchInput());
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, CswFragment.class, CSWFRAGMENT);
        thenAddToBackStackWasCalled(CSWFRAGMENT);
        thenCommitAllowingStateLossWasCalled();
    }

    public void givenParentContainerId(int parentContainerId) {
        fragmentLauncherMock = new FragmentLauncherMock(fragmentActivityMock, parentContainerId, null);
    }

    private void whenCallingLaunch(FragmentLauncherMock fragmentLauncherMock, CswLaunchInput cswLaunchInput) {
        cswInterface.launch(fragmentLauncherMock, cswLaunchInput);
    }

    private void thenReplaceWasCalledWith(int expectedParentContainerId, Class<?> expectedFragmentClass, String expectedTag) {
        assertEquals(expectedParentContainerId, fragmentTransactionMock.replace_containerId);
        assertTrue(fragmentTransactionMock.replace_fragment.getClass().isAssignableFrom(expectedFragmentClass));
        assertEquals(expectedTag, fragmentTransactionMock.replace_tag);
    }

    private void thenAddToBackStackWasCalled(String expectedBackStackId) {
        assertEquals(expectedBackStackId, fragmentTransactionMock.addToBackStack_backStackId);
    }

    private void thenCommitAllowingStateLossWasCalled() {
        assertTrue(fragmentTransactionMock.commitAllowingStateLossWasCalled);
    }

    private FragmentLauncherMock fragmentLauncherMock;
    private FragmentActivityMock fragmentActivityMock;
    private FragmentManagerMock fragmentManagerMock;
    private FragmentTransactionMock fragmentTransactionMock;

    private static final String CSWFRAGMENT = "CSWFRAGMENT";

    private CswInterface cswInterface;

    private static final int A_SPECIFIC_CONTAINER_ID = 938462837;
}