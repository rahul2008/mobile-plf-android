package com.philips.platform.mya.launcher;

import android.content.Context;

import com.philips.platform.mya.interfaces.MyaListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;

/**
 * Created by philips on 11/22/17.
 */
public class MyaLaunchInputTest {

    MyaLaunchInput myaLaunchInput;
    @Mock
    private MyaListener myaListener;

    @Mock
    private Context mContext;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        myaLaunchInput = new MyaLaunchInput(mContext, myaListener);
        myaLaunchInput.addToBackStack(true);
    }

    @Test
    public void shouldNotNull_Context(){
        assertNotNull(myaLaunchInput.getContext());
    }

    @Test
    public void shouldNotNull_Listener(){
        assertNotNull(myaLaunchInput.getMyaListener());
    }

    @Test
    public void shouldNotNull_addToBackStack(){
        assertNotNull(myaLaunchInput.isAddtoBackStack());
    }
}