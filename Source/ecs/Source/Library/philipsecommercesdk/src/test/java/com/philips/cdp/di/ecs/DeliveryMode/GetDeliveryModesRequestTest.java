package com.philips.cdp.di.ecs.DeliveryMode;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.philips.cdp.di.ecs.ECSServices;
import com.philips.cdp.di.ecs.MockECSServices;
import com.philips.cdp.di.ecs.StaticBlock;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.address.GetDeliveryModes;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.rest.RestInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class GetDeliveryModesRequestTest {

    private Context mContext;


    MockECSServices mockECSServices;
    ECSServices ecsServices;


    private AppInfra appInfra;


    @Mock
    RestInterface mockRestInterface;

    MockDeliveryModesRequest mockDeliveryModesRequest;

    @Before
    public void setUp() throws Exception {

        mContext = getInstrumentation().getContext();
        appInfra = new AppInfra.Builder().setRestInterface(mockRestInterface).build(mContext);
        appInfra.getServiceDiscovery().setHomeCountry("DE");

        mockECSServices = new MockECSServices("", appInfra);
        ecsServices = new ECSServices("",appInfra);

        StaticBlock.initialize();

        mockDeliveryModesRequest = new MockDeliveryModesRequest(new ECSCallback<GetDeliveryModes, Exception>() {
            @Override
            public void onResponse(GetDeliveryModes result) {

            }

            @Override
            public void onFailure(Exception error, int errorCode) {

            }
        },"deliverymodes.json");
    }

    @Test
    public void getDeliveryModesSuccess() {
        mockECSServices.setJsonFileName("deliverymodes.json");
        mockECSServices.getDeliveryModes(new ECSCallback<GetDeliveryModes, Exception>() {
            @Override
            public void onResponse(GetDeliveryModes result) {
                assertNotNull(result);
                assertNotNull(result.getDeliveryModes());
                assertNotEquals(0,result.getDeliveryModes().size());
                //  test case passed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(false);
                //  test case failed
            }
        });
    }

    @Test
    public void getDeliveryModesFailureEmpty() {
        mockECSServices.setJsonFileName("EmptyJson.json");
        mockECSServices.getDeliveryModes(new ECSCallback<GetDeliveryModes, Exception>() {
            @Override
            public void onResponse(GetDeliveryModes result) {
                assertNull(result.getDeliveryModes());
                //  test case failed
            }

            @Override
            public void onFailure(Exception error, int errorCode) {
                assertTrue(false);
                //  test case passed
            }
        });
    }

    @Test
    public void isValidURL() {
        String excepted = StaticBlock.getBaseURL()+"pilcommercewebservices"+"/v2/"+StaticBlock.getSiteID()+"/users/current/carts/current/deliverymodes?fields=FULL&lang="+StaticBlock.getLocale();
        Assert.assertEquals(excepted,mockDeliveryModesRequest.getURL());
    }

    @Test
    public void getMethodTest(){
        Assert.assertEquals(Request.Method.GET,mockDeliveryModesRequest.getMethod());
    }


    @Test
    public void onErrorResponseTest(){

       /* MyClass myClass = new MyClass();
        myClass.out = Mockito.spy(new PrintStream(...));

        // mock a call with an expected input
        doNothing().when(myClass.out).println("expected command");

        myClass.updateGreen();

        // test that there was a call
        Mockito.verify(myClass.out, Mockito.times(1)).println("expected command");*/

       /* public void onErrorResponse(VolleyError error) {
            ECSError ecsError = ECSNetworkError.getErrorLocalizedErrorMessageForAddress(error);
            ecsCallback.onFailure(ecsError.getException(), ecsError.getErrorcode());
        }*/
        AuthFailureError authFailureError = new AuthFailureError();
        mockDeliveryModesRequest.onErrorResponse(authFailureError);


    }
}