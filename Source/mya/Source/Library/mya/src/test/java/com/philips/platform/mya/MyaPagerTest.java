/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.mya;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;

import com.philips.platform.mya.base.MyaBaseFragment;
import com.philips.platform.mya.profile.MyaProfileFragment;
import com.philips.platform.mya.settings.MyaSettingsFragment;
import com.philips.platform.mya.tabs.MyaTabFragment;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class MyaPagerTest {

    private MyaPager myaPager;
    private int tabCount = 2;

    @Mock
    MyaProfileFragment myaProfileFragmentMock;

    @Mock
    MyaSettingsFragment myaSettingsFragmentMock;

    @Mock
    FragmentManager fragmentManagerMock;

    private MyaTabFragment myaTabFragment;
    private Bundle bundle;
    private ActionBarListener actionBarListener;
    private FragmentLauncher fragmentLauncher;

    @Before
    public void setUp() throws Exception{
        initMocks(this);
        actionBarListener = getActionBarListener();
        fragmentLauncher = new FragmentLauncher(null, 123456, actionBarListener);
        bundle = new Bundle();
        bundle.putInt("some_int_key",99);
        myaTabFragment = new MyaTabFragment();
        myaTabFragment.setArguments(bundle);
        myaTabFragment.setActionbarUpdateListener(actionBarListener);
        myaTabFragment.setFragmentLauncher(fragmentLauncher);
        myaPager = new MyaPager(fragmentManagerMock, tabCount, myaTabFragment);
    }

    private ActionBarListener getActionBarListener() {
        return new ActionBarListener() {
            @Override
            public void updateActionBar(int i, boolean b) {

            }

            @Override
            public void updateActionBar(String s, boolean b) {

            }
        };
    }

    @Test
    public void notNull_getCount() throws Exception{
        assertNotNull(myaPager.getCount());
        assertTrue(myaPager.getCount() == 2);
    }

    @Test
    public void equals_ProfileFragment() throws Exception{
        assertNotNull(myaPager.getItem(0));
        assertTrue(myaPager.getItem(0) instanceof  MyaProfileFragment);
    }

    @Test
    public void equals_SettingsFragment() throws Exception{
        assertNotNull(myaPager.getItem(1));
        assertTrue(myaPager.getItem(1) instanceof  MyaSettingsFragment);
    }

    @Test
    public void equals_DefaultNull() throws Exception{
        assertEquals(null, myaPager.getItem(2));
    }

    @Test
    public void testSettingValues() {
        testAssertions((MyaBaseFragment) myaPager.getItem(0));
        testAssertions((MyaBaseFragment) myaPager.getItem(1));
    }

    private void testAssertions(MyaBaseFragment item) {
        assertEquals(item.getFragmentLauncher(), fragmentLauncher);
        assertEquals(item.getActionbarUpdateListener(), actionBarListener);
        assertEquals(item.getArguments(), bundle);
    }
}