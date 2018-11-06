/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.mya.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.mya.R;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.TreeMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class MyaProfileAdaptorTest {

    @Mock
    private Context contextMock;

    @Mock
    private View.OnClickListener onClickListener;

    @Mock
    private LayoutInflater layoutInflaterMock;

    @Mock
    private ViewGroup viewGroupMock;

    private MyaProfileAdaptor myaProfileAdaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        TreeMap<String, String> profileList = getProfileList();
        myaProfileAdaptor = new MyaProfileAdaptor(profileList);
        myaProfileAdaptor.setOnClickListener(onClickListener);
    }

    @Test
    public void shouldNotNull_getItemCount() {
        assertTrue(myaProfileAdaptor.getItemCount() > 0);
    }

    @Test
    public void testOnCreateView() throws Exception {
        View view = mock(View.class);
        Label label = mock(Label.class);
        when(view.findViewById(R.id.item_title)).thenReturn(label);
        when(viewGroupMock.getContext()).thenReturn(contextMock);
        when(contextMock.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).thenReturn(layoutInflaterMock);
        when(layoutInflaterMock.inflate(R.layout.mya_single_item_layout, viewGroupMock, false)).thenReturn(view);
        MyaProfileAdaptor.ProfileViewHolder profileViewHolder = myaProfileAdaptor.onCreateViewHolder(viewGroupMock, 0);
        assertNotNull(myaProfileAdaptor);
        myaProfileAdaptor.onBindViewHolder(profileViewHolder, 0);
        verify(label).setText("some_data");
        myaProfileAdaptor.onBindViewHolder(profileViewHolder, 2);
        verify(label).setText("some_key2");
    }


    private TreeMap<String, String> getProfileList() {
        TreeMap<String, String> profileList = new TreeMap<>();
        profileList.put("some_key", "some_data");
        profileList.put("some_key1", "some_data1");
        profileList.put("some_key2", null);
        return profileList;
    }

    @Test
    public void ShouldReturnProperCount() {
        assertTrue(myaProfileAdaptor.getItemCount() == 3);
        assertFalse(myaProfileAdaptor.getItemCount() == 2);
    }
}