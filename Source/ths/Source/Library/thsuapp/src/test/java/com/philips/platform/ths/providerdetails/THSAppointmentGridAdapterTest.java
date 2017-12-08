/*
package com.philips.platform.ths.providerdetails;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class THSAppointmentGridAdapterTest {

    THSAppointmentGridAdapter thsAppointmentGridAdapter;

    @Mock
    Context context;

    @Mock
    Date date;

    @Mock
    View view;

    @Mock
    ViewGroup viewGroup;

    @Mock
    THSBaseFragment thsBaseFragmentMock;

    @Mock
    THSProviderInfo thsProviderInfo;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        List list = new ArrayList();
        list.add(date);
        thsAppointmentGridAdapter = new THSAppointmentGridAdapter(context,list, thsBaseFragmentMock, thsProviderInfo,null);
    }

    @Test
    public void areAllItemsEnabled() throws Exception {
        boolean areAllItemsEnabled = thsAppointmentGridAdapter.areAllItemsEnabled();
        assertFalse(areAllItemsEnabled);
    }

    @Test
    public void isEnabled() throws Exception {
        boolean isEnabled = thsAppointmentGridAdapter.isEnabled(0);
        assertTrue(isEnabled);
    }

    @Test
    public void getCount() throws Exception {
        int count = thsAppointmentGridAdapter.getCount();
        assert count==1;
    }

    @Test
    public void getCountWithNullArray() throws Exception {
        thsAppointmentGridAdapter = new THSAppointmentGridAdapter(context,null, thsBaseFragmentMock, thsProviderInfo,null);
        int count = thsAppointmentGridAdapter.getCount();
        assert count==0;
    }

    @Test
    public void getItem() throws Exception {
        Date item = thsAppointmentGridAdapter.getItem(0);
        assertNotNull(item);
    }

    @Test
    public void getItemId() throws Exception {
        long itemId = thsAppointmentGridAdapter.getItemId(0);
        assert itemId == 0;
    }

    @Test
    public void getView() throws Exception {
        thsAppointmentGridAdapter.getView(0,view,viewGroup);
    }

    @Test
    public void updateGrid() throws Exception {
        thsAppointmentGridAdapter.updateGrid(new ArrayList<Date>());
    }

    @Test
    public void getFormatedTime() throws Exception {
        thsAppointmentGridAdapter.getFormatedTime(date);
    }

}*/
