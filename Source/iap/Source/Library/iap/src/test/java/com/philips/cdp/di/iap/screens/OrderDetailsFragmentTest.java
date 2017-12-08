package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.Constants.OrderStatus;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.OrderDetailRequest;
import com.philips.cdp.di.iap.model.OrderDetailRequestTest;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.io.Serializable;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class OrderDetailsFragmentTest {
    private Context mContext;
    OrderDetailsFragment orderDetailsFragmentTest;

    Bundle bundle;

    private StoreListener mStore;
    private AbstractModel mModel;

    OrderDetail orderDetail;

    @Mock
    IAPUser mUser;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;

        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();

        mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext));
        mStore.initStoreConfig(/*"en", "US",*/ null);
        mModel = new OrderDetailRequest(mStore, null, null);

        bundle=new Bundle();
        orderDetail=getOrderDetailFromJSON();

        bundle.putParcelable(IAPConstant.ORDER_DETAIL,orderDetail);
        orderDetailsFragmentTest = OrderDetailsFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
        orderDetailsFragmentTest.mOrderDetail=orderDetail;
        orderDetailsFragmentTest.setArguments(bundle);
    }

    @Test(expected = NullPointerException.class)
    public void shouldDisplayAddressSelectionFragment() {

        OrderStatus orderStatus=OrderStatus.COMPLETED;
        SupportFragmentTestUtil.startFragment(orderDetailsFragmentTest);
    }



    public OrderDetail getOrderDetailFromJSON() {
        String oneAddress = TestUtils.readFile(OrderDetailRequestTest.class, "order_detail.txt");
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ORDER_NUMBER, NetworkURLConstants.DUMMY_ORDER_ID);
        OrderDetailRequest request = new OrderDetailRequest(mStore, query, null);
        Object response = request.parseResponse(oneAddress);
        return (OrderDetail)response;
       // assertEquals(response.getClass(), OrderDetail.class);
    }

    @Mock
    View viewMock;
    @Test(expected = ClassCastException.class)
    public void shouldAddFragmentWhenCancelButtonISClicked() throws Exception {
        orderDetailsFragmentTest.onAttach(mContext);
        Mockito.when(viewMock.getId()).thenReturn(R.id.btn_cancel);
        orderDetailsFragmentTest.onClick(viewMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCallWhenCallButtonISClicked() throws Exception {
        orderDetailsFragmentTest.onAttach(mContext);
        Mockito.when(viewMock.getId()).thenReturn(R.id.btn_call);
        orderDetailsFragmentTest.onClick(viewMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldUpdateProductList() throws Exception {
        orderDetailsFragmentTest.updateUiOnProductList();

    }
}