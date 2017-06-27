package com.philips.amwelluapp.utility;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class PTHConstantsTest {

    PTHConstants pthConstants;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
       pthConstants = new PTHConstants();
    }

    @Test
    public void getInstance(){
        //assertThat(pthConstants).isNotNull();
        //assertThat(pthConstants).isInstanceOf(PTHConstants.class);
    }


}