package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.adapters.ShoppingCartAdapter;
import com.philips.cdp.di.iap.cart.ShoppingCartData;
import com.philips.cdp.di.iap.response.carts.DeliveryModeEntity;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;
import com.philips.cdp.di.iap.response.carts.ProductEntity;
import com.philips.cdp.di.iap.response.carts.StockEntity;
import com.philips.cdp.di.iap.response.orders.DeliveryMode;
import com.philips.cdp.di.iap.utils.IAPConstant;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ShoppingCartFragmentTest {
    private Context mContext;
    ShoppingCartFragment shoppingCartFragment;

    @Mock
    ShoppingCartAdapter.OutOfStockListener outOfStockListenerMock;

    @Mock
    EntriesEntity mockEntriesEntity;

    @Before
    public void setUp() {
        initMocks(this);

       // mActivity = Robolectric.buildActivity(DemoTestActivity.class).create().get();
        shoppingCartFragment = ShoppingCartFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);

        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
    }


    @Test(expected = NullPointerException.class)
    public void shouldDisplayAddressSelectionFragment() {
        startFragment(shoppingCartFragment);
        assertNotNull( shoppingCartFragment );


    }

    @Test
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithEMPTY_CART_FRAGMENT_REPLACEDString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
    }

    @Test
    public void stopFragment_whenOnStopIsCalled() throws Exception {
        shoppingCartFragment.onPause();
        shoppingCartFragment.onStop();
        shoppingCartFragment.onDestroyView();
    }

    @Test(expected = NullPointerException.class)
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithBUTTON_STATE_CHANGEDString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.BUTTON_STATE_CHANGED.toString());
    }

   @Test(expected = NullPointerException.class)
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithPRODUCT_DETAIL_FRAGMENTString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.PRODUCT_DETAIL_FRAGMENT);
    }

    @Test(expected = NullPointerException.class)
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_LAUNCH_PRODUCT_CATALOGString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG);
    }

    @Test(expected = NullPointerException.class)
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_DELETE_PRODUCTString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_DELETE_PRODUCT);
    }

    @Test
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_EDIT_DELIVERY_MODEString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_EDIT_DELIVERY_MODE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_UPDATE_PRODUCT_COUNTString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_UPDATE_PRODUCT_COUNT);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_DELETE_PRODUCT_CONFIRMString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_DELETE_PRODUCT_CONFIRM);
    }

    @Test
    public void shouldStart_ProductDetailFragment() throws Exception {


        StockEntity stockEntity=new StockEntity();
        stockEntity.setStockLevel(3);
        ProductEntity productEntity=new ProductEntity();
        productEntity.setStock(stockEntity);

        EntriesEntity entriesEntity=new EntriesEntity();

        entriesEntity.setProduct(productEntity);

        ShoppingCartData shoppingCartData=new ShoppingCartData(entriesEntity,new DeliveryModeEntity());
        ArrayList<ShoppingCartData> shoppingCartDataArrayList=new ArrayList<>();
        shoppingCartDataArrayList.add(shoppingCartData);


        ShoppingCartAdapter mAdapter = new ShoppingCartAdapter(mContext, shoppingCartDataArrayList, outOfStockListenerMock );

        shoppingCartData.setStockLevel(3);
        mAdapter.setTheProductDataForDisplayingInProductDetailPage(shoppingCartData);

        mAdapter.getItemCount();
        mAdapter.getItemViewType(0);
        mAdapter.getItemViewType(1);
        mAdapter.getQuantityStatusInfo();
        mAdapter.getNewCount();
        mAdapter.getSelectedItemPosition();
//        mAdapter.onCreateViewHolder(any(ViewGroup.class),0);
       // mAdapter.onBindViewHolder(,0);

        shoppingCartFragment.startProductDetailFragment(mAdapter);

    }


    //ShoppingCartAdapter.
}