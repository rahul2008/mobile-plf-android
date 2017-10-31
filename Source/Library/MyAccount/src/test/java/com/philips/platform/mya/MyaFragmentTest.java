package com.philips.platform.mya;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;


import static org.junit.Assert.*;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaFragmentTest {

    @Test
    public void myaFragment_inflatesCorrectLayout() throws Exception {
        MyaFragment myaFragment = new MyaFragment();
        startFragment(myaFragment);
        assertNotNull(myaFragment.getView());
        assertEquals(R.id.csw_frame_layout_view_container, myaFragment.getView().getId());
    }


}