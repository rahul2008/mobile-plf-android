/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.toolbox.ImageLoader;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 9/27/17.
 */
@RunWith(RobolectricTestRunner.class)
public class AddressSelectionAdapterTest {

    AddressSelectionAdapter addressSelectionAdapter;

    @Mock
    List<Addresses> addressessListMock;

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    @Mock
    Context contextMock;

    @Mock
    private com.philips.platform.uid.view.widget.RadioButton radioButtonMock;

    @Mock
    ImageLoader imageLoaderMock;

    Context mContext;
    @Mock
    IAPUser mIAPMockedUser;
    private StoreListener mStore;
    MockIAPSetting mockIAPSetting;
    MockIAPDependencies mockIAPDependencies;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mIAPMockedUser.getJanRainEmail()).thenReturn(NetworkURLConstants.JANRAIN_EMAIL);
        when(mIAPMockedUser.getJanRainID()).thenReturn(NetworkURLConstants.JANRAIN_ID);
        mockIAPSetting = new MockIAPSetting(mContext);
        mockIAPDependencies = new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class));
        mockIAPSetting.setUseLocalData(false);
        mStore = new MockStore(mContext, mIAPMockedUser).getStore(mockIAPSetting,mockIAPDependencies);
        String mJanRainEmail =  mStore.getJanRainEmail();
        addressSelectionAdapter = new AddressSelectionAdapter(addressessListMock,mJanRainEmail);
    }

    @Mock
    ViewGroup viewGroupMock;
    @Mock
    View viewMock;

    @Mock
    LayoutInflater layoutInflaterMock;

    @Test(expected = IllegalArgumentException.class)
    public void onCreateViewHolder() throws Exception {

        when(viewGroupMock.getContext()).thenReturn(contextMock);
        when(contextMock.getSystemService(contextMock.LAYOUT_INFLATER_SERVICE)).thenReturn(layoutInflaterMock);
        //when(layoutInflaterMock.inflate(R.layout.ecs_address_selection_item, viewGroupMock, false)).thenReturn(viewMock);
        addressSelectionAdapter.onCreateViewHolder(viewGroupMock, TYPE_ITEM);

    }

    @Test(expected = IllegalArgumentException.class)
    public void onCreateViewHolderForTypeFooter() throws Exception {

        when(viewGroupMock.getContext()).thenReturn(contextMock);
        when(contextMock.getSystemService(contextMock.LAYOUT_INFLATER_SERVICE)).thenReturn(layoutInflaterMock);
        //when(layoutInflaterMock.inflate(R.layout.ecs_address_selection_item, viewGroupMock, false)).thenReturn(viewMock);
        addressSelectionAdapter.onCreateViewHolder(viewGroupMock, TYPE_FOOTER);

    }

    @Test
    public void getItemCount() throws Exception {
        assertEquals(1, addressSelectionAdapter.getItemCount());
    }

    @Test(expected = NullPointerException.class)
    public void onBindViewHolderTYPE_ITEM() throws Exception {

        when(addressessListMock.get(1)).thenReturn(addressMock);
        AddressSelectionAdapter.AddressSelectionHolder addressSelectionHolder = addressSelectionAdapter.new AddressSelectionHolder(viewMock);
        addressSelectionAdapter.onBindViewHolder(addressSelectionHolder, TYPE_ITEM);
    }


    @Mock
    Addresses addressMock;

    @Test
    public void onBindViewHolderTYPE_Null() throws Exception {
        addressSelectionAdapter.onBindViewHolder(null, TYPE_FOOTER);
    }

    @Test
    public void getSelectedPosition() throws Exception {
        addressSelectionAdapter.getSelectedPosition();
    }

    @Test
    public void getOptionsClickPosition() throws Exception {
        addressSelectionAdapter.getOptionsClickPosition();
    }

    @Test
    public void setAddresses() throws Exception {
        addressSelectionAdapter.setAddresses(addressessListMock);
    }

    @Test
    public void getItemViewType() throws Exception {
        addressSelectionAdapter.getItemViewType(0);
    }

    @Test
    public void shouldBindAddNewAddress() throws Exception {
        AddressSelectionAdapter.AddressSelectionFooter addressSelectionFooter = addressSelectionAdapter.new AddressSelectionFooter(viewMock);
        addressSelectionAdapter.bindAddNewAddress(viewMock);
    }

    @Mock
    Button buttonMock;

    @Test
    public void shouldBindDeliverToThisAddress() throws Exception {
        addressSelectionAdapter.bindDeliverToThisAddress(buttonMock);
    }

    @Test
    public void shouldUpdatePaymentButtonsVisibility() throws Exception {
        addressSelectionAdapter.updatePaymentButtonsVisibility(viewGroupMock, buttonMock, 0);
    }

    @Test
    public void shoiuldSetToggleStatus() throws Exception {
        addressSelectionAdapter.setToggleStatus(radioButtonMock, 0);
    }


}