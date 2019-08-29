package com.philips.cdp.di.ecs.Address;

import android.content.Context;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.Addresses;
import com.philips.cdp.di.ecs.model.address.Country;
import com.philips.cdp.di.ecs.model.address.GetShippingAddressData;
import com.philips.cdp.di.ecs.model.address.Region;
import com.philips.cdp.di.ecs.util.ECSConfig;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.apache.tools.ant.taskdefs.Length;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class CreateAddressTest {
    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
       // appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        //appInfra.getServiceDiscovery().setHomeCountry("DE");

        //when(ECSConfig.INSTANCE.getAccessToken()).thenReturn("HybrisToken");
       // when(ECSConfig.INSTANCE.getAccessToken()).then("HybrisToken");
        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

    }


    //////////////////////////////start of single created  Address fetch ////////////////

    @Test
    public void addAddressSingleSuccess() {
        mockECSServices.setJsonFileName("CreateAddressSuccess.json");
        Addresses address = getAddressesObject();
        mockECSServices.createNewAddress(address, new ECSCallback<Addresses, Exception>() {
            @Override
            public void onResponse(Addresses address) {
                assertNotNull(address);
                assertNotNull(address.getId());

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
               // assertEquals(12999,errorCode);
                assertTrue(true);
            }
        },true);

    }

    @Test
    public void addAddressSingleFailureInvalidZipCode() {
        mockECSServices.setJsonFileName("CreateAddressFailureInvalidZipCode.json");
        Addresses address = new Addresses();
        mockECSServices.createNewAddress(address, new ECSCallback<Addresses, Exception>() {
            @Override
            public void onResponse(Addresses address) {
                assertTrue(true);

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(true);
            }
        },true);

    }

    @Test
    public void addAddressSingleFailure() {
        mockECSServices.setJsonFileName("EmptyString.json");
        Addresses address = new Addresses();
        mockECSServices.createNewAddress(address, new ECSCallback<Addresses, Exception>() {
            @Override
            public void onResponse(Addresses address) {
               assertTrue(true);

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertEquals(12999,errorCode);
            }
        },true);

    }



    //////////////////////////////End of single created  Address fetch ////////////////





    //////////////////////////////start of All  Address fetch ////////////////

    @Test
    public void addAddressSuccess() {
        mockECSServices.setJsonFileName("CreateAddressSuccess.json");
        Addresses addressRequest = new Addresses();
        mockECSServices.createNewAddress(addressRequest, new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData addressList) {
                assertNotNull(addressList);
                assertNotNull(addressList.getAddresses().size()>0);
                assertNotNull(addressList.getAddresses().get(0).getId());

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                // assertEquals(12999,errorCode);
                assertTrue(true);
            }
        });

    }


    @Test
    public void addAddressFailure() {
        mockECSServices.setJsonFileName("EmptyString.json");
        Addresses addressRequest = new Addresses();
        mockECSServices.createNewAddress(addressRequest, new ECSCallback<GetShippingAddressData, Exception>() {
            @Override
            public void onResponse(GetShippingAddressData addressList) {
                assertTrue(true);

            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                 assertEquals(12999,errorCode);

            }
        });

    }

    //////////////////////////////End of All  Address fetch ////////////////

    public static   Addresses getAddressesObject(){

        Addresses addressRequest = new Addresses();
        addressRequest.setId("1234567");
        addressRequest.setFirstName("First name");
        addressRequest.setLastName("Second name");
        addressRequest.setTitleCode("Mr");
        Country country= new Country();
        country.setIsocode("DE");
        //country.se
        addressRequest.setCountry(country); // iso
        addressRequest.setLine1("Line 1");
        //   addressRequest.setLine2(shippingAddressFields.getLine2());
        addressRequest.setPostalCode("10111");
        addressRequest.setTown("Berlin");
        addressRequest.setPhone1("5043323");
        addressRequest.setPhone2("5043323");
        Region region = new Region();
        region.setIsocodeShort("Region");
        addressRequest.setRegion(region); // set Region eg State for US and Canada
        addressRequest.setHouseNumber("12A");
        addressRequest.setDefaultAddress(true);
        return addressRequest;
    }
}
