/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.faqs;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.ths.R;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExpandableListAdapterTest {

    ExpandableListAdapter mExpandableListAdapter;

    @Mock
    THSFaqFragment thsFaqFragmentMock;

    HashMap map;

    @Mock
    FaqBeanPojo faqBeanPojoMock1;

    @Mock
    FaqBeanPojo faqBeanPojoMock2;

    @Mock
    View viewMock;

    @Mock
    ViewGroup viewGroupMock;

    @Mock
    Label textViewMock;

    @Mock
    Label textViewShowHideMock;

    @Mock
    Context contextMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        map = new HashMap();
        List list1 = new ArrayList();
        list1.add(faqBeanPojoMock1);
        list1.add(faqBeanPojoMock2);
        map.put("Spoorti",list1);
        when(faqBeanPojoMock1.getAnswer()).thenReturn("hello");
        when(faqBeanPojoMock1.getQuestion()).thenReturn("how are you");
        when(thsFaqFragmentMock.getContext()).thenReturn(contextMock);
        when(contextMock.getString(anyInt())).thenReturn("Text");
        mExpandableListAdapter = new ExpandableListAdapter(thsFaqFragmentMock,map);
    }

    @Test
    public void getChild() throws Exception {
        final Object child = mExpandableListAdapter.getChild(0, 0);
        assertNotNull(child);
        assertThat(child).isInstanceOf(FaqBeanPojo.class);
    }

    @Test
    public void getChildId() throws Exception {
        final long childId = mExpandableListAdapter.getChildId(0, 0);
        assert childId == 0;
    }


    @Test
    public void getChildView() throws Exception {
        when(viewMock.findViewById(R.id.lblListItem)).thenReturn(textViewMock);
        mExpandableListAdapter.getChildView(0,0,true,viewMock,viewGroupMock);
        verify(textViewMock).setText(anyString());
    }

    @Test
    public void getChildrenCount() throws Exception {
        final int childrenCount = mExpandableListAdapter.getChildrenCount(0);
        assert childrenCount == 2;
    }

    @Test
    public void getGroup() throws Exception {
        final Object group = mExpandableListAdapter.getGroup(0);
        assertNotNull(group);
        assertThat(group).isInstanceOf(String.class);
    }

    @Test
    public void getGroupCount() throws Exception {
        final int groupCount = mExpandableListAdapter.getGroupCount();
        assert groupCount == 1;
    }

    @Test
    public void getGroupId() throws Exception {
        final long groupId = mExpandableListAdapter.getGroupId(0);
        assert groupId == 0;
    }

    @Test
    public void getGroupView() throws Exception {
        when(viewMock.findViewById(R.id.lblListHeader)).thenReturn(textViewMock);
        when(viewMock.findViewById(R.id.lblshowAll)).thenReturn(textViewShowHideMock);
        final View groupView = mExpandableListAdapter.getGroupView(0, true, viewMock, viewGroupMock);
        assertNotNull(groupView);
        assertThat(groupView).isInstanceOf(View.class);
    }

    @Test
    public void hasStableIds() throws Exception {
        final boolean b = mExpandableListAdapter.hasStableIds();
        assert b == false;
    }

    @Test
    public void isChildSelectable() throws Exception {
        final boolean childSelectable = mExpandableListAdapter.isChildSelectable(0, 0);
        assert  childSelectable == true;
    }

}