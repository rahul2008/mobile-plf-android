/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.address;

import android.content.Context;

import com.google.gson.Gson;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.adapters.AddressSelectionAdapter;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class AddressSelectionAdapterTest {
    private AddressSelectionAdapter mAdapter;
    @Mock
    Context mContext;

    @Before
    public void setUP() {
        String addr = TestUtils.readFile(AddressSelectionAdapterTest.class, "one_addresses.txt");
        GetShippingAddressData data = new Gson().fromJson(addr, GetShippingAddressData.class);
        mAdapter = new AddressSelectionAdapter(mContext, data.getAddresses()) {
            @Override
            void initOptionsDrawable() {
                //
            }
        };
    }

    @Test
    public void testAdapterItemSizeIsOne() {
        assertEquals(1, mAdapter.getItemCount());
    }

    @Test
    public void testDefaultSelectedItemIsAtZeroIndex() {
        assertEquals(0, mAdapter.getSelectedPosition());
    }

    @Test
    public void testDefaultOptionsItemIsUnset() {
        assertEquals(-1, mAdapter.getOptionsClickPosition());
    }

    @Test
    public void testItemCountSameAfterSettingSameData() {
        String addr = TestUtils.readFile(AddressSelectionAdapterTest.class, "one_addresses.txt");
        GetShippingAddressData data = new Gson().fromJson(addr, GetShippingAddressData.class);
        mAdapter.setAddresses(data.getAddresses());
        assertEquals(1, mAdapter.getItemCount());
    }

//    @Test
//    public void testCreateAddress() {
//        String addr = TestUtils.readFile(AddressSelectionAdapterTest.class, "one_addresses.txt");
//        GetShippingAddressData data = new Gson().fromJson(addr, GetShippingAddressData.class);
//        Addresses address = data.getAddresses().get(0);
//        assertEquals("test\ntest\ntest\n12-345\nPoland",
//                mAdapter.createAddress(address));
//    }
}
