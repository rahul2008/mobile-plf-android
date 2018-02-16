package com.philips.cdp.registration.ui.traditional.countryselection;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.dao.Country;
import com.philips.cdp.registration.listener.SelectedCountryListener;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

/**
 * Created by philips on 12/3/17.
 */
@RunWith(CustomRobolectricRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = 21)
public class CountrySelectionAdapterTest {


    @Mock
    private List<Country> countryListMock;

    @Mock
    private SelectedCountryListener countryListenerMock;

    CountrySelectionAdapter countrySelectionAdapter;

    @Mock
    private ViewGroup viewGroupMock;

    @Mock
    private RecyclerView.ViewHolder viewHolderMock;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        countrySelectionAdapter = new CountrySelectionAdapter(countryListMock, countryListenerMock);
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