package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.utils.IAPConstant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ShoppingCartFragmentTest {
    private Context mContext;
    ShoppingCartFragment shoppingCartFragment;

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

    /*   @Test
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithBUTTON_STATE_CHANGEDString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.BUTTON_STATE_CHANGED.toString());
    }*/

/*    @Test
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithPRODUCT_DETAIL_FRAGMENTString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.PRODUCT_DETAIL_FRAGMENT);
    }*/

/*    @Test
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_LAUNCH_PRODUCT_CATALOGString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG);
    }*/

  /*  @Test
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_DELETE_PRODUCTString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_DELETE_PRODUCT);
    }*/

    @Test
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_EDIT_DELIVERY_MODEString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_EDIT_DELIVERY_MODE);
    }

 /*   @Test
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_UPDATE_PRODUCT_COUNTString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_UPDATE_PRODUCT_COUNT);
    }*/

   /* @Test
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_DELETE_PRODUCT_CONFIRMString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_DELETE_PRODUCT_CONFIRM);
    }*/

    /*  @Override
    public void onEventReceived(final String event) {
        dismissProgressDialog();
        if (event.equalsIgnoreCase(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED)) {
            addFragment(EmptyCartFragment.createInstance(new Bundle(), AnimationType.NONE), EmptyCartFragment.TAG);
        } else if (event.equalsIgnoreCase(String.valueOf(IAPConstant.BUTTON_STATE_CHANGED))) {
            mCheckoutBtn.setEnabled(!Boolean.valueOf(event));
        } else if (event.equalsIgnoreCase(String.valueOf(IAPConstant.PRODUCT_DETAIL_FRAGMENT))) {
            startProductDetailFragment();
        } else if (event.equalsIgnoreCase(String.valueOf(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG))) {
            showProductCatalogFragment(ShoppingCartFragment.TAG);
        } else if (event.equalsIgnoreCase(IAPConstant.IAP_DELETE_PRODUCT)) {
            if (!isProgressDialogShowing()) {
                showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
                mShoppingCartAPI.deleteProduct(mData.get(mAdapter.getSelectedItemPosition()));
            }
        } else if (event.equalsIgnoreCase(IAPConstant.IAP_UPDATE_PRODUCT_COUNT)) {
            if (!isProgressDialogShowing()) {
                showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
                mShoppingCartAPI.updateProductQuantity(mData.get(mAdapter.getSelectedItemPosition()),
                        mAdapter.getNewCount(), mAdapter.getQuantityStatusInfo());
            }
        } else if (event.equalsIgnoreCase(IAPConstant.IAP_EDIT_DELIVERY_MODE)) {

            addFragment(DeliveryMethodFragment.createInstance(new Bundle(), AnimationType.NONE),
                    AddressSelectionFragment.TAG);
        }else if(event.equalsIgnoreCase(IAPConstant.IAP_DELETE_PRODUCT_CONFIRM)) {
            Utility.showActionDialog(mContext,getString(R.string.iap_remove_product),getString(R.string.iap_cancel)
                    ,null,getString(R.string.iap_product_remove_description),getFragmentManager(),this);
          }
        }*/

}