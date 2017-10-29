/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.faqs;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.ths.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ExpandableListAdapterTest {

    ExpandableListAdapter mExpandableListAdapter;

    @Mock
    THSFaqFragment thsFaqFragmentMock;

    HashMap map;

    @Mock
    FaqBean faqBeanMock1;

    @Mock
    FaqBean faqBeanMock2;

    @Mock
    View viewMock;

    @Mock
    ViewGroup viewGroupMock;

    @Mock
    TextView textViewMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        map = new HashMap();
        List list1 = new ArrayList();
        list1.add(faqBeanMock1);
        list1.add(faqBeanMock2);
        map.put("Spoorti",list1);
        when(faqBeanMock1.getAnswer()).thenReturn("hello");
        when(faqBeanMock1.getQuestion()).thenReturn("how are you");
        mExpandableListAdapter = new ExpandableListAdapter(thsFaqFragmentMock,map);
    }

    @Test
    public void getChild() throws Exception {
        final Object child = mExpandableListAdapter.getChild(0, 0);
        assertNotNull(child);
        assertThat(child).isInstanceOf(FaqBean.class);
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