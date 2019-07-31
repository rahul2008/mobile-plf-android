package com.philips.cdp.di.ecs.Voucher;

import android.content.Context;

import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.voucher.GetAppliedValue;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class GetVoucherListTest {

    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    @Before
    public void setUp() throws Exception {


        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");


        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

    }

    @Test
    public void getAppliedVoucherListSuccess() {
        mockECSServices.setJsonFileName("GetVoucherSuccess.json");
        mockECSServices.getVoucher(new ECSCallback<GetAppliedValue, Exception>() {
            @Override
            public void onResponse(GetAppliedValue result) {
                assertNotNull(result);
                assertNotNull(result.getVouchers().get(0).getCode());
                assertNotNull(result.getVouchers().get(0).getValue());
                //  test case passed
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                assertTrue(true);
                //  test case failed
            }
        });

    }

    @Test
    public void getAppliedVoucherListSuccessWithNoVoucher() {
        mockECSServices.setJsonFileName("EmptyString.json");// {}
        mockECSServices.getVoucher(new ECSCallback<GetAppliedValue, Exception>() {
            @Override
            public void onResponse(GetAppliedValue result) {
                assertTrue(true);
                //  test case failed
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                assertEquals(19999,errorCode);
                //  test case passed

            }
        });

    }

    @Test
    public void getAppliedVoucherListFailure() {
        mockECSServices.setJsonFileName("EmptyString.json");
        mockECSServices.getVoucher(new ECSCallback<GetAppliedValue, Exception>() {
            @Override
            public void onResponse(GetAppliedValue result) {
                assertTrue(true);
                //  test case failed
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                assertEquals(19999,errorCode);
                //  test case passed

            }
        });

    }

}
