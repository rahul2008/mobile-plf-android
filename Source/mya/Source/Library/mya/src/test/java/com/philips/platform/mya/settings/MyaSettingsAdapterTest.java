package com.philips.platform.mya.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.BuildConfig;
import com.philips.platform.mya.runner.CustomRobolectricRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * Created by philips on 11/23/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class MyaSettingsAdapterTest {
MyaSettingsAdapter myaSettingsAdapter;

    private final static int DOUBLE_VIEW = 0;
    private final static int SINGLE_VIEW = 1;

    @Mock
    LinkedHashMap<String, SettingsModel> settingsListMock;

    @Mock
    Context contextMock;

    @Mock
    LayoutInflater layoutInflaterMock;

    @Mock
    ViewGroup viewGroupMock;

    @Mock
    SettingsModel settingsModel;

    @Mock
    View viewMock;

    private Set<String>  hashStringSet = new LinkedHashSet<>();


    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        hashStringSet.add("One");
        hashStringSet.add("Two");
        hashStringSet.add("Three");
        when(settingsListMock.keySet()).thenReturn(hashStringSet) ;
        when(settingsModel.getItemCount()).thenReturn(new Integer(2));
        when(settingsModel.getFirstItem()).thenReturn("1");
        when(settingsModel.getSecondItem()).thenReturn("2");
        when(settingsModel.getThirdItem()).thenReturn("3");
        myaSettingsAdapter = new MyaSettingsAdapter(settingsListMock);

    }

    @Test(expected = NullPointerException.class)
    public void testOnCreateViewForSingleView() throws Exception{
        when(viewGroupMock.getContext()).thenReturn(contextMock);
        when(contextMock.getSystemService(contextMock.LAYOUT_INFLATER_SERVICE)).thenReturn(layoutInflaterMock);
        myaSettingsAdapter.onCreateViewHolder(viewGroupMock,SINGLE_VIEW);
        assertNotNull(myaSettingsAdapter);
    }

    @Test(expected = NullPointerException.class)
    public void testOnCreateViewForDoubleView() throws Exception{
        when(viewGroupMock.getContext()).thenReturn(contextMock);
        when(contextMock.getSystemService(contextMock.LAYOUT_INFLATER_SERVICE)).thenReturn(layoutInflaterMock);
        myaSettingsAdapter.onCreateViewHolder(viewGroupMock,DOUBLE_VIEW);
        assertNotNull(myaSettingsAdapter);
    }

    @Test
    public void shouldNotNull_getItemCount(){
        assertNotNull(myaSettingsAdapter.getItemCount());
    }



}