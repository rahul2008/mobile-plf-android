package com.philips.platform.mya;

import android.view.ViewGroup;

import com.philips.platform.mya.mock.FragmentActivityMock;
import com.philips.platform.mya.mock.FragmentManagerMock;
import com.philips.platform.mya.mock.FragmentTransactionMock;
import com.philips.platform.mya.mock.LayoutInflatorMock;
import com.philips.platform.mya.runner.CustomRobolectricRunner;
import com.philips.platform.mya.wrapper.MyaFragmentWrapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaFragmentTest {

    @Before
    public void setup() {
        myaFragment = new MyaFragmentWrapper();
        myaFragment.fragmentActivity = mockFragmentActivity;
    }

    @Test
    public void onCreate_InvokesInflatorWithRightParameters() throws Exception {
        whenCallingOnCreateView();
        thenInflatorIsCalledWith(R.layout.mya_fragment_my_account_root, null, false);
    }

    @Test
    public void onCreate_inflatesCorrectLayout() throws Exception {
        whenCallingOnCreateView();
        thenAccountViewIsInflatedWith(R.id.mya_frame_layout_view_container);

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
        myaFragment.onCreateView(mockLayoutInflater, null, null);
    }


    private void thenApplicationNameIs(String expectedApplicationName) {
        assertEquals(expectedApplicationName, myaFragment.applicationName);
    }

    private void thenPropositionNameIs(String expectedPropositionName) {
        assertEquals(expectedPropositionName, myaFragment.propositionName);
    }

    private void thenInflatorIsCalledWith(int layout, ViewGroup group, boolean attachToRoot) {
        assertEquals(layout, mockLayoutInflater.usedResource);
        assertEquals(group, mockLayoutInflater.usedViewGroup);
        assertEquals(attachToRoot, mockLayoutInflater.usedAttachToRoot);
    }

    private void thenAccountViewIsInflatedWith(int layout) {
        assertEquals(layout, fragmentTransaction.replace_containerId);
        assertNotNull(fragmentTransaction.replace_fragment);
        assertTrue(fragmentTransaction.commitAllowingStateLossWasCalled);
        assertTrue(fragmentManager.beginTransactionCalled);
    }

    private MyaFragmentWrapper myaFragment;
    private LayoutInflatorMock mockLayoutInflater = LayoutInflatorMock.createMock();
    private FragmentTransactionMock fragmentTransaction = new FragmentTransactionMock();
    private FragmentManagerMock fragmentManager = new FragmentManagerMock(fragmentTransaction);
    private FragmentActivityMock mockFragmentActivity = new FragmentActivityMock(fragmentManager);
}