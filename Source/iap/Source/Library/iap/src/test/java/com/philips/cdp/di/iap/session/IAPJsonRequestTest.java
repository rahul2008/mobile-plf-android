/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Handler;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.TestUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class IAPJsonRequestTest {
    private IAPJsonRequest mIAIapJsonRequest;

    @Mock
    VolleyError volleyErrorMock;

    @Mock
    Handler handlerMock;

    @Mock
    RequestQueue requestQueueMock;

    MockNetworkController networkControllerMock;

    @Mock
    Context contextMock;

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);
        networkControllerMock = (MockNetworkController) TestUtils.getStubbedHybrisDelegate().getNetworkController(contextMock);
        mIAIapJsonRequest = new IAPJsonRequest(1,null,null,null,null);
    }

    @Test
    public void testParseNetworkResponse() throws Exception{
        JSONObject obj = new JSONObject(TestUtils.readFile(IAPJsonRequestTest.class, "json_exception.txt"));
        NetworkResponse networkResponse = new NetworkResponse(obj.toString().getBytes("utf-8"));
        mIAIapJsonRequest.parseNetworkResponse(networkResponse);

    }

    @Test
    public void testDeliverError(){
        networkControllerMock.mRequestQueue = requestQueueMock;
        mIAIapJsonRequest.mHandler = handlerMock;
        final HybrisDelegate instance = HybrisDelegate.getInstance();
        instance.controller = networkControllerMock;
        mIAIapJsonRequest.deliverError(volleyErrorMock);
        verify(handlerMock).post(any(Runnable.class));
    }

    @Test
    public void testDeliverErrorRequiresRedirection(){

        HashMap map = new HashMap();
        map.put("Location","hh");
        NetworkResponse networkResponse = new NetworkResponse(301,null,map,false,12L);
        VolleyError volleyError = new VolleyError(networkResponse);

        networkControllerMock.mRequestQueue = requestQueueMock;
        mIAIapJsonRequest.mHandler = handlerMock;
        final HybrisDelegate instance = HybrisDelegate.getInstance();
        instance.controller = networkControllerMock;
        mIAIapJsonRequest.deliverError(volleyError);
        verify(requestQueueMock).add(any(IAPJsonRequest.class));
    }

    /*@Test
    public void testParseNetworkResponseWithException(){
        NetworkResponse networkResponse = new NetworkResponse(fileToByteArray("C:\\Users\\310228564\\InAppGit\\Source\\Library\\iap\\src\\test\\java\\com\\philips\\cdp\\di\\iap\\session\\wrong_json.txt"));
        assertNotNull(mIAIapJsonRequest.parseNetworkResponse(networkResponse));
    }*/

   /* @Test(expected = NullPointerException.class)
    public void testHandleMiscErrorWithAuthFailure(){
        TestUtils.getStubbedHybrisDelegate();
        mIAIapJsonRequest.handleMiscErrors(new AuthFailureError());
    } SDK 27 error*/

    /*private byte[] fileToByteArray(String path) {
        File file = new File(path);
        byte[] byteArray = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024 * 8];
            int bytesRead;
            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }
            byteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }*/
}