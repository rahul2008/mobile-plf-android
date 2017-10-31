package com.philips.platform.mya;

import android.view.View;

import com.philips.platform.mya.runner.CustomRobolectricRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import static org.robolectric.shadows.support.v4.Shadows.shadowOf;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;


import static org.junit.Assert.*;


@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaFragmentTest {

    @Before
    public void setup(){
        myaFragment = new MyaFragment();
    }

    @Test
    public void onCreate_inflatesCorrectLayout() throws Exception {
        whenCallingOnCreateView();
        thenFragmentRootIsRedered();
    }

    @Test
    public void onCreate_AccountViewIsInstanciated() throws Exception {
        whenCallingOnCreateView();
        thenAccountViewIsRendered();
    }

    @Test
    public void onCreate_AttributesAre() throws Exception {
        givenArguments("appName1", "propName1");
        whenCallingOnCreateView();
        thenApplicationNameIs("appName1");
        thenPropositionNameIs("propName1");
    }

    private void givenArguments(String applicationName, String propositionName) {
        myaFragment.setArguments(applicationName, propositionName);
    }

    private void whenCallingOnCreateView() {
        startFragment(myaFragment);
    }

    private void thenFragmentRootIsRedered() {
        assertNotNull(myaFragment.getView());
        assertEquals(R.id.csw_frame_layout_view_container, myaFragment.getView().getId());
    }

    private void thenApplicationNameIs(String expectedApplicationName) {
        assertEquals(expectedApplicationName, myaFragment.applicationName);
    }

    private void thenPropositionNameIs(String expectedPropositionName) {
        assertEquals(expectedPropositionName, myaFragment.propositionName);
    }

    private void thenAccountViewIsRendered() {
        assertNotNull(myaFragment.getView().findViewById(R.id.mya_account_permissions));
    }

    private MyaFragment myaFragment;
}