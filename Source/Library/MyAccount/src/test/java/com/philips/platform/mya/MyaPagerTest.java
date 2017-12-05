package com.philips.platform.mya;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.philips.platform.mya.profile.MyaProfileFragment;
import com.philips.platform.mya.runner.CustomRobolectricRunner;
import com.philips.platform.mya.settings.MyaSettingsFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 11/24/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaPagerTest {

    MyaPager myaPager;
    private int tabCount;

    @Mock
    MyaProfileFragment myaProfileFragmentMock;

    @Mock
    MyaSettingsFragment myaSettingsFragmentMock;

    @Mock
    FragmentManager fragmentManagerMock;

    @Before
    public void setUp() throws Exception{
        initMocks(this);
        myaPager = new MyaPager(fragmentManagerMock,tabCount,new Bundle());
    }

    @Test
    public void notNull_getCount() throws Exception{
        assertNotNull(myaPager.getCount());
    }

    @Test
    public void equals_ProfileFragment() throws Exception{
        assertNotNull(myaPager.getItem(0));
    }

    @Test
    public void equals_SettingsFragment() throws Exception{
        assertNotNull(myaPager.getItem(1));
    }

    @Test
    public void equals_DefaultNull() throws Exception{
        assertEquals(null,myaPager.getItem(2));
    }
}