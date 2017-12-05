package com.philips.platform.mya.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.BuildConfig;
import com.philips.platform.mya.runner.CustomRobolectricRunner;
import com.philips.platform.mya.settings.SettingsModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.util.TreeMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 11/23/17.
 */

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaProfileAdaptorTest {

MyaProfileAdaptor myaProfileAdaptor;

    @Mock
    TreeMap<String,String> profileListMock;

    @Mock
    Context contextMock;

    @Mock
    View.OnClickListener onClickListener;

    @Mock
    LayoutInflater layoutInflaterMock;

    @Mock
    ViewGroup viewGroupMock;

    @Mock
    View viewMock;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        myaProfileAdaptor = new MyaProfileAdaptor(profileListMock);
        myaProfileAdaptor.setOnClickListener(onClickListener);
    }

    @Test
    public void shouldNotNull_getItemCount(){
        assertNotNull(myaProfileAdaptor.getItemCount());
    }
    @Test(expected = NullPointerException.class)
    public void testOnCreateView() throws Exception{
        when(viewGroupMock.getContext()).thenReturn(contextMock);
        when(contextMock.getSystemService(contextMock.LAYOUT_INFLATER_SERVICE)).thenReturn(layoutInflaterMock);
        myaProfileAdaptor.onCreateViewHolder(viewGroupMock,0);
        assertNotNull(myaProfileAdaptor);
    }


}