/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.adapters;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.response.retailers.StoreEntity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.when;

/**
 * Created by philips on 9/27/17.
 */
@RunWith(RobolectricTestRunner.class)
public class BuyFromRetailersAdapterTest {

    BuyFromRetailersAdapter buyFromRetailersAdapter;

    @Mock
    Context contextMock;

    @Mock
    FragmentManager fragmentManagerMock;

    @Mock
    ArrayList<StoreEntity> storeListMock;

    @Mock
    BuyFromRetailersAdapter.BuyFromRetailersListener buyFromRetailersListenerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        buyFromRetailersAdapter = new BuyFromRetailersAdapter(contextMock, fragmentManagerMock, storeListMock, buyFromRetailersListenerMock,null);
    }

    @Mock
    View viewMock;

    @Mock
    StoreEntity storeEntityMock;

    @Mock
    ViewGroup viewGroupMock;

    @Test(expected = AssertionError.class)
    public void onCreateViewHolder() throws Exception {

        when(viewGroupMock.getContext()).thenReturn(contextMock);
        buyFromRetailersAdapter.onCreateViewHolder(viewGroupMock,0);
    }

    @Test
    public void shouldTagOnSelectRetailer() throws Exception {
        when(storeEntityMock.getName()).thenReturn("philips");
       // buyFromRetailersAdapter.tagOnSelectRetailer(storeEntityMock);
    }

    @Test(expected = NullPointerException.class)
    public void onBindViewHolder() throws Exception {

        when(storeEntityMock.getLogoURL()).thenReturn("url");
        when(storeEntityMock.getName()).thenReturn("philips");
        when(storeEntityMock.getAvailability()).thenReturn("yes");

        when(storeListMock.get(0)).thenReturn(storeEntityMock);


        BuyFromRetailersAdapter.RetailerViewHolder retailerViewHolder = buyFromRetailersAdapter.new RetailerViewHolder(viewMock);
        buyFromRetailersAdapter.onBindViewHolder(retailerViewHolder,0);

    }

    @Test
    public void getItemCount() throws Exception {
        Assert.assertEquals(0,buyFromRetailersAdapter.getItemCount());
    }

}