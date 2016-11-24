/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.philips.cdp.di.iap.TestUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class IAPJsonRequestTest {
    private IAPJsonRequest mIAIapJsonRequest;

    @Before
    public void setUp() throws Exception{
        mIAIapJsonRequest = new IAPJsonRequest(1,null,null,null,null);
    }

    @Test(expected = JSONException.class)
    public void testParseNetworkResponse() throws Exception{
        JSONObject obj = new JSONObject(TestUtils.readFile(IAPJsonRequestTest.class, "json_exception.txt"));
        NetworkResponse networkResponse = new NetworkResponse(obj.toString().getBytes("utf-8"));
        mIAIapJsonRequest.parseNetworkResponse(networkResponse);

    }

    /*@Test
    public void testParseNetworkResponseWithException(){
        NetworkResponse networkResponse = new NetworkResponse(fileToByteArray("C:\\Users\\310228564\\InAppGit\\Source\\Library\\iap\\src\\test\\java\\com\\philips\\cdp\\di\\iap\\session\\wrong_json.txt"));
        assertNotNull(mIAIapJsonRequest.parseNetworkResponse(networkResponse));
    }*/

    @Test(expected = NullPointerException.class)
    public void testHandleMiscErrorWithAuthFailure(){
        TestUtils.getStubbedHybrisDelegate();
        mIAIapJsonRequest.handleMiscErrors(new AuthFailureError());
    }

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