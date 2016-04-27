package com.philips.cdp.di.iap.address;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.CreateAddressRequest;
import com.philips.cdp.di.iap.model.DeleteAddressRequest;
import com.philips.cdp.di.iap.model.GetAddressRequest;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.model.SetDeliveryAddressModeRequest;
import com.philips.cdp.di.iap.model.SetDeliveryAddressRequest;
import com.philips.cdp.di.iap.model.UpdateAddressRequest;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.Country;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.response.addresses.Region;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.NetworkController;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.Store;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import javax.net.ssl.SSLSocketFactory;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(MockitoJUnitRunner.class)
public class AddressControllerGetAdressesTest {

    @Mock
    private NetworkController mNetworkController;
    @Mock
    private HybrisDelegate mHybrisDelegate;
    @Mock
    private AddressController mController;
    @Mock
    private Context mContext;
    @Captor private ArgumentCaptor<GetAddressRequest> mModelistener;
    @Captor private ArgumentCaptor<DeleteAddressRequest> mDeleteListener;
    @Captor private ArgumentCaptor<UpdateAddressRequest> mUpdateAddressListener;
    @Captor private ArgumentCaptor<CreateAddressRequest> mCreateAddressListener;
    @Captor private ArgumentCaptor<SetDeliveryAddressRequest> mSetDeliveryAddressListener;
    @Captor private ArgumentCaptor<SetDeliveryAddressModeRequest> mSetDeliveryModeListener;
    @Mock
    private SSLSocketFactory mSocketFactory;
    @Mock
    private Message mResultMessage;
    @Mock
    private Store mStore;
    @Mock
    private GetAddressRequest mAddressRequest;
    @Mock
    private UpdateAddressRequest mUpdateAddressRequest;
    @Mock
    private DeleteAddressRequest mDeleteAddressRequest;
    @Mock
    private CreateAddressRequest mCreateAddressRequest;

    @Before
    public void setUP() {
        when(mHybrisDelegate.getNetworkController(mContext)).thenReturn(mNetworkController);
        doCallRealMethod().when(mAddressRequest).parseResponse(any(Message.class));
    }

    @Test
    public void verifyHybrisRequestSentForGetAddresses() {
        mController = new AddressController(mContext, null);

        setStoreAndDelegate();
        mController.getShippingAddresses();
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                any(AbstractModel.class));
    }

    @Test
    public void verifyHybrisRequestSentForUpdateAddresses() {
        mController = new AddressController(mContext, null);
        setStoreAndDelegate();
        mController.updateAddress(getQueryString());
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                any(AbstractModel.class));
    }

    private HashMap getQueryString() {
        HashMap<String, String> addressHashMap = new HashMap<>();
        addressHashMap.put(ModelConstants.FIRST_NAME, "Spoorti");
        addressHashMap.put(ModelConstants.LAST_NAME, "Hallur");
        addressHashMap.put(ModelConstants.TITLE_CODE, "mr");
        addressHashMap.put(ModelConstants.COUNTRY_ISOCODE, "US");
        addressHashMap.put(ModelConstants.LINE_1, "NRI Layout");
        addressHashMap.put(ModelConstants.POSTAL_CODE, "590043");
        addressHashMap.put(ModelConstants.TOWN, "Bangalore");
        addressHashMap.put(ModelConstants.ADDRESS_ID, "8799470125079");
        addressHashMap.put(ModelConstants.LINE_2, "");
        addressHashMap.put(ModelConstants.DEFAULT_ADDRESS, "Bangalore");
        addressHashMap.put(ModelConstants.PHONE_NUMBER, "2453696");
        return addressHashMap;
    }

    @Test
    public void verifyHybrisRequestSentForCreateAddress() {
        mController = new AddressController(mContext, null);
        setStoreAndDelegate();
        AddressFields mockAddressFields = Mockito.mock(AddressFields.class);
        when(mockAddressFields.getFirstName()).thenReturn("XYZ");
        when(mockAddressFields.getLastName()).thenReturn("WXY");
        when(mockAddressFields.getTitleCode()).thenReturn("Mr");
        when(mockAddressFields.getCountryIsocode()).thenReturn("ISO");
        when(mockAddressFields.getLine1()).thenReturn("XYZ");
        when(mockAddressFields.getLine2()).thenReturn("XYZ");
        when(mockAddressFields.getPostalCode()).thenReturn("XYZ");
        when(mockAddressFields.getTown()).thenReturn("XYZ");
        when(mockAddressFields.getPhoneNumber()).thenReturn("XYZ");
        mController.createAddress(mockAddressFields);
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                any(AbstractModel.class));
    }

    @Test
    public void verifyDeleteAddresses() {
        mController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetAddress(final Message msg) {
                assertEquals(RequestCode.DELETE_ADDRESS, msg.what);
            }
        });

        setStoreAndDelegate();
        mController.deleteAddress("8799470125079");
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mDeleteListener.capture());

        mResultMessage.what = RequestCode.DELETE_ADDRESS;
        mDeleteListener.getValue().onSuccess(mResultMessage);
    }

    @Test
    public void verifyUpdateAddresses() {
        mController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onGetAddress(final Message msg) {
                assertEquals(RequestCode.UPDATE_ADDRESS, msg.what);
            }
        });

        setStoreAndDelegate();
        mController.updateAddress(new HashMap<String, String>());
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mUpdateAddressListener.capture());

        mResultMessage.what = RequestCode.UPDATE_ADDRESS;
        mUpdateAddressListener.getValue().onSuccess(mResultMessage);
    }

    @Test
    public void verifyCreateAddresses() {
        mController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onCreateAddress(final Message msg) {
                assertEquals(RequestCode.CREATE_ADDRESS, msg.what);
            }
        });

        setStoreAndDelegate();

        AddressFields addr = new AddressFields();
        addr.setTitleCode("Mr");
        mController.createAddress(addr);
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mCreateAddressListener.capture());

        mResultMessage.what = RequestCode.CREATE_ADDRESS;
        mCreateAddressListener.getValue().onSuccess(mResultMessage);
    }

    @Test
    public void verifySetDeliveryAddresses() {
        mController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onSetDeliveryAddress(final Message msg) {
                assertEquals(RequestCode.SET_DELIVERY_ADDRESS, msg.what);
            }
        });

        setStoreAndDelegate();

        mController.setDeliveryAddress("");
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mSetDeliveryAddressListener.capture());

        mResultMessage.what = RequestCode.SET_DELIVERY_ADDRESS;
        mSetDeliveryAddressListener.getValue().onSuccess(mResultMessage);
    }

    @Test
    public void verifySetDeliveryMode() {
        mController = new AddressController(mContext, new MockAddressListener() {
            @Override
            public void onSetDeliveryModes(final Message msg) {
                assertEquals(RequestCode.SET_DELIVERY_MODE, msg.what);
            }
        });

        setStoreAndDelegate();

        mController.setDeliveryMode();
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mSetDeliveryModeListener.capture());

        mResultMessage.what = RequestCode.SET_DELIVERY_MODE;
        mSetDeliveryModeListener.getValue().onSuccess(mResultMessage);
    }

    @Test
    public void verifySetDefaultAddress() {
        mController = new AddressController(mContext, null);
        Addresses addr = mock(Addresses.class);
        when(addr.getCountry()).thenReturn(new Country());
        when(addr.getRegion()).thenReturn(new Region());
        setStoreAndDelegate();
        mController.setDefaultAddress(addr);
    }

    public void setStoreAndDelegate() {
        mController.setHybrisDelegate(mHybrisDelegate);
        mController.setStore(new MockStore(mContext, mock(IAPUser.class)).getStore());
    }

    @Test
    public void verifyAddressISNotEmptyForGetAddresses() {
        mController = new AddressController(mContext, null);
        setStoreAndDelegate();

        //Send hybris request
        mController.getShippingAddresses();

        //Verfiy Hybris call and capture the call back
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mModelistener.capture());

        //Prepare the result with dummy json saved in same path
        mResultMessage.what = RequestCode.GET_ADDRESS;
        mResultMessage.obj = mAddressRequest.parseResponse(
                TestUtils.readFile(AddressControllerGetAdressesTest.class, "one_addresses.txt"));

        //Send the result
        mModelistener.getValue().onSuccess(mResultMessage);
    }

    @Test
    public void verifyAddressDeatilsGetAddresses() {
        mController = new AddressController(mContext, new MockAddressListener() {

            @Override
            public void onGetAddress(Message msg) {
                assertNotNull(msg);
                GetShippingAddressData result = (GetShippingAddressData) msg.obj;
                Addresses addresses = result.getAddresses().get(0);
                assertEquals("Harmeet", addresses.getFirstName());
                assertEquals("Singh", addresses.getLastName());
                assertEquals("test", addresses.getLine1());
                assertEquals("test", addresses.getLine2());
                assertEquals("12-345", addresses.getPostalCode());
                assertEquals("test", addresses.getTown());
                assertEquals("PL", addresses.getCountry().getIsocode());
                assertEquals("8796158590999", addresses.getId());
            }
        });
        setStoreAndDelegate();

        //Send hybris request
        mController.getShippingAddresses();

        //Verfiy Hybris call and capture the call back
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mModelistener.capture());

        //Prepare the result with dummy json saved in same path
        mResultMessage.what = RequestCode.GET_ADDRESS;
        mResultMessage.obj = mAddressRequest.parseResponse(
                TestUtils.readFile(AddressControllerGetAdressesTest.class, "one_addresses.txt"));

        //Send the result
        mModelistener.getValue().onSuccess(mResultMessage);
    }

    @Test
    public void verifyFetchAddressCallBackIsInvloked() {
        mController = new AddressController(mContext, new MockAddressListener() {

            @Override
            public void onGetAddress(Message msg) {
                assertNotNull(msg);
            }
        });
        setStoreAndDelegate();

        //Send hybris request
        mController.getShippingAddresses();

        //Verfiy Hybris call and capture the call back
        verify(mHybrisDelegate, times(1)).sendRequest(any(Integer.TYPE), any(AbstractModel.class),
                mModelistener.capture());

        //Prepare the result with dummy json saved in same path
        mResultMessage.what = RequestCode.GET_ADDRESS;

        //Send the result
        mModelistener.getValue().onError(mResultMessage);
    }
}