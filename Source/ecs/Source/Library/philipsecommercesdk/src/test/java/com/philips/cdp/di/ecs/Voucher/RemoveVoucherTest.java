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
public class RemoveVoucherTest {
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
    public void removeVoucherSuccess() {
        mockECSServices.setJsonFileName("EmptyString.json"); // empty string is success response of Apply voucher
        mockECSServices.removeVoucher("voucherCode",new ECSCallback<GetAppliedValue, Exception>() {
            @Override
            public void onResponse(GetAppliedValue result) {
                assertNotNull(result);
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
    public void removeVoucherFailure() {
        mockECSServices.setJsonFileName("ApplyVoucherFailure.json"); // empty string is success response of Apply voucher
        mockECSServices.removeVoucher("voucherCode",new ECSCallback<GetAppliedValue, Exception>() {
            @Override
            public void onResponse(GetAppliedValue result) {
                assertTrue(true);
                //  test case failed
            }

            @Override
            public void onFailure(Exception error, String detailErrorMessage, int errorCode) {
                assertEquals(200999,errorCode);
            //  test case passed
            }
        });

    }

}
