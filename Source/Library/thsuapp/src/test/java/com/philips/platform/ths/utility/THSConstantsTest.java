package com.philips.platform.ths.utility;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class THSConstantsTest {

    com.philips.platform.ths.utility.THSConstants THSConstants;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
       THSConstants = new THSConstants();
    }

    @Test
    public void getInstance(){
        //assertThat(THSConstants).isNotNull();
        //assertThat(THSConstants).isInstanceOf(THSConstants.class);
    }


}