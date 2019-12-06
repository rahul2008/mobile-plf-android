/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.ui.traditional.countrySelection;

import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.philips.cdp.registration.dao.Country;
import com.philips.cdp.registration.listener.SelectedCountryListener;
import com.philips.cdp.registration.ui.traditional.CountrySelectionContract;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

/**
 * Created by philips on 12/3/17.
 */
@RunWith(RobolectricTestRunner.class)
public class CountrySelectionAdapterTest {

    @Mock
    private List<Country> countryListMock;

    @Mock
    private SelectedCountryListener countryListenerMock;

    private com.philips.cdp.registration.ui.traditional.countryselection.CountrySelectionAdapter countrySelectionAdapter;

    @Mock
    private ViewGroup viewGroupMock;

    @Mock
    private RecyclerView.ViewHolder viewHolderMock;

    @Mock
    private
    CountrySelectionContract countrySelectionContractMock;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        countrySelectionAdapter = new com.philips.cdp.registration.ui.traditional.countryselection.CountrySelectionAdapter(countryListMock, countrySelectionContractMock);
    }

    @Test
    public void getItemViewType_shouldReturnZero() throws Exception {

        Assert.assertSame(0,countrySelectionAdapter.getItemViewType(0));
    }

    @Test
    public void getItemViewType_shouldReturnOne() throws Exception {

        Assert.assertSame(1,countrySelectionAdapter.getItemViewType(1));
    }



    @Test(expected = NullPointerException.class)
    public void onCreateViewHolder() throws Exception {

        countrySelectionAdapter.onCreateViewHolder(viewGroupMock,0);
    }

    @Test(expected = NullPointerException.class)
    public void onCreateViewHolder_WhenPositionIsNotZero() throws Exception {

        countrySelectionAdapter.onCreateViewHolder(viewGroupMock,1);
    }


    @Test
    public void onBindViewHolder_WhenPositionIsZero() throws Exception {

        countrySelectionAdapter.onBindViewHolder(viewHolderMock,0);
    }

    @Test
    public void onBindViewHolder_WhenPositionIsNotZero() throws Exception {

        countrySelectionAdapter.onBindViewHolder(viewHolderMock,1);
    }

    @Test
    public void getItemCount() throws Exception {
        Assert.assertSame(1,countrySelectionAdapter.getItemCount());
    }

}