package com.philips.cdp.di.iap.model;

import android.content.Context;

import com.android.volley.Request;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class SetDeliveryAddressModeRequestTest {
    private Context mContext;

    @Mock
    private IAPUser mUser;
    @Mock
    private AbstractModel mModel;

    @Before
    public void setUP() {
        mContext = getInstrumentation().getContext();
        StoreListener mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext),new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class)));
        mStore.initStoreConfig(/*"en", "US",*/ null);

        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.DELIVERY_MODE_ID, "standard-net");
        mModel = new SetDeliveryAddressModeRequest(mStore, params, null);
    }

    @Test
    public void testRequestMethodIsPUT() {
        assertEquals(Request.Method.PUT, mModel.getMethod());
    }

    @Test
    public void testBodyParamsIsNull() {
        SetDeliveryAddressModeRequest setDeliveryAddressModeRequest =
                Mockito.mock(SetDeliveryAddressModeRequest.class);
        assertNotNull(setDeliveryAddressModeRequest.requestBody());
    }

    @Test
    public void testStoreIsNotNull() {
        assertNotNull(mModel.getStore());
    }


    @Test
    public void testSetDeliveryModeAddressResponse() {
        assertEquals(IAPConstant.IAP_SUCCESS, mModel.parseResponse(null));
    }
}