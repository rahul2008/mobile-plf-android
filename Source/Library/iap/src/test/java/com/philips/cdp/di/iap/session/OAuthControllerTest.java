/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.os.Message;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.RefreshOAuthRequest;
import com.philips.cdp.di.iap.utils.IAPLog;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class OAuthControllerTest {
    OAuthController mOAuthController;

    @Mock
    RefreshOAuthRequest mRefreshOAuthRequest;

    @Mock
    AbstractModel model;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        TestUtils.getStubbedHybrisDelegate();
        mOAuthController = new OAuthController();
    }

    @Test
    public void testGetAccessToken() throws Exception {
        mOAuthController.getAccessToken();
    }

    @Test
    public void testResetAccessToken() throws Exception {
        mOAuthController.resetAccessToken();
    }

    @Test(expected = NullPointerException.class)
    public void testRefreshToken() throws Exception {
        mOAuthController.refreshToken(null);
    }

    @Test(expected = RuntimeException.class)
    public void requestSyncRefreshToken() throws Exception {
        mOAuthController.requestSyncRefreshToken(mRefreshOAuthRequest, new RequestListener() {
            @Override
            public void onSuccess(Message msg) {

            }

            @Override
            public void onError(Message msg) {

            }
        });
    }

    @Test(expected = NullPointerException.class)
    public void requestSyncOAuthToken() throws Exception {
        mOAuthController.requestSyncOAuthToken(model);
    }

    @Test
    public void createOAuthRequest() throws Exception {
        mOAuthController.createOAuthRequest(model);
    }

    @Test
    public void notifySuccessListener() throws Exception {
        mOAuthController.notifySuccessListener(null, model);
    }

    @Test
    public void notifyErrorListener() throws Exception {
        mOAuthController.notifyErrorListener(null, model);
    }

    @Test
    public void isInvalidGrantErrorSuccessResponse() throws Exception {
        JSONObject obj = new JSONObject(TestUtils.readFile(OAuthControllerTest.class, "server_error.txt"));
        NetworkResponse networkResponse = new NetworkResponse(obj.toString().getBytes("utf-8"));
        assertTrue(mOAuthController.isInvalidGrantError(new VolleyError(networkResponse)));
    }

    @Test
    public void isInvalidGrantErrorExceptionResponse() throws Exception {
        NetworkResponse networkResponse = new NetworkResponse(fileToByteArray("C:\\Users\\310228564\\InAppGit\\Source\\Library\\iap\\src\\test\\java\\com\\philips\\cdp\\di\\iap\\session\\json_syntax_exception.txt"));
        mOAuthController.isInvalidGrantError(new VolleyError(networkResponse));
    }

    @Test
    public void isInvalidGrantErrorNotSuccessResponse() throws Exception {
        assertFalse(mOAuthController.isInvalidGrantError(new VolleyError()));
    }

    private byte[] fileToByteArray(String path) {
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
            IAPLog.e(IAPLog.LOG, e.getMessage());
        }
        return byteArray;
    }
}