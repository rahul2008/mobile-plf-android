package com.philips.cdp.di.iap.hybris;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.di.iap.session.IAPNetworkError;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

/**
 * Created by indrajitkumar on 27/09/16.
 */
public class HybrisHandlerTest {
    private HybrisHandler hybrisHandler;
    private IAPListener ipIapListener;
    @Mock
    Context contextMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        hybrisHandler = new HybrisHandler(contextMock);
        ipIapListener = new IAPListener() {
            @Override
            public void onGetCartCount(int count) {

            }

            @Override
            public void onUpdateCartCount() {

            }

            @Override
            public void updateCartIconVisibility(boolean shouldShow) {

            }

            @Override
            public void onGetCompleteProductList(ArrayList<String> productList) {

            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int errorCode) {

            }
        };
    }


    @Test(expected = NullPointerException.class)
    public void getCompleteProductList() throws Exception {
        hybrisHandler.getCompleteProductList(ipIapListener);
    }

    @Test(expected = NullPointerException.class)
    public void getProductCount() throws Exception {
        hybrisHandler.getProductCount(ipIapListener);
    }

    @Test
    public void getIAPErrorCodeForUnknownError() throws Exception {
        hybrisHandler.getIAPErrorCode(new Message());
    }

    @Test(expected = RuntimeException.class)
    public void getIAPErrorCode() throws Exception {
        Message msg = new Message();
        msg.obj = new IAPNetworkError(null, 0, null);
        hybrisHandler.getIAPErrorCode(msg);
    }
}