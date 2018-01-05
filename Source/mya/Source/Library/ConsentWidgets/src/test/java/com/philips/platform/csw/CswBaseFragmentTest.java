package com.philips.platform.csw;

import com.philips.platform.csw.mock.ActionBarListenerMock;
import com.philips.platform.csw.mock.CswFragmentMock;
import com.philips.platform.csw.utils.CustomRobolectricRunner;
import com.philips.platform.csw.wrapper.CswBaseFragmentWrapper;
import com.philips.platform.mya.consentwidgets.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class CswBaseFragmentTest {

    @Before
    public void setup() {
        actionBarListener = new ActionBarListenerMock();
        baseFragment = new CswBaseFragmentWrapper();
        baseFragment.cswFragment = cswFragment;
        baseFragment.setActionBarListener(actionBarListener);
    }

    @Test
    public void onResume_updateActionBar() throws Exception {
        givenTitleResourceIdIs(123);
        givenFragmentCountIs(1);
        whenOnResumeIsInvoked();
        thenActionBarIsUpdatedWith(123, false);
        givenFragmentCountIs(2);
        whenOnResumeIsInvoked();
    }

    @Test
    public void onResume_setsResourceIdOnParentFragment() throws Exception {
        givenTitleResourceIdIs(123);
        whenOnResumeIsInvoked();
        thenSetResourceIdIsInvokedWith(123);
    }

    private void thenSetResourceIdIsInvokedWith(int i) {
        assertEquals(cswFragment.getResourceID(), i);
    }

    @Test
    public void onResume_doesNotSetTitleIfParentFragmentIsNull() throws Exception {
        givenANullParentFragment();
        whenOnResumeIsInvoked();
        thenTitleIsNotSet();
    }

    private void thenTitleIsNotSet() {
        assertEquals(null, baseFragment.cswFragment);
    }

    private void thenActionBarIsUpdatedWith(int expectedActionBarInt, boolean expectedActionBarBoolean) {
        assertEquals(expectedActionBarInt, actionBarListener.updatedActionBarInt);
    }

    private void givenFragmentCountIs(int i) {
        cswFragment.fragmentCount = i;
    }

    private void givenTitleResourceIdIs(int i) {
        baseFragment.resourceTitleId = i;
    }

    private void givenANullParentFragment() {
        baseFragment.cswFragment = null;
    }

    private void whenOnResumeIsInvoked() {
        baseFragment.onResume();
    }

    CswBaseFragmentWrapper baseFragment;
    ActionBarListenerMock actionBarListener;
    CswFragmentMock cswFragment = new CswFragmentMock();
}
