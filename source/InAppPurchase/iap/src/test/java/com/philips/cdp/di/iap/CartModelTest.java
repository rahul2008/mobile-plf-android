package com.philips.cdp.di.iap;

import android.content.Context;

import com.philips.cdp.di.iap.model.CartModel;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.Store;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CartModelTest {

    @Test
    public void matchTestUrlWithHarcodedCreateCartUrl() {
        Context context = Mockito.mock(Context.class);
        Store store = new Store(context, "hostport", "webroot", "userID", "janrainID");
        CartModel cartModel = new CartModel(store, null);
        assertEquals(cartModel.getTestUrl(RequestCode.CREATE_CART), NetworkConstants.createCartUrl);

    }

    @Test
    public void matchTestUrlWithHarcodedGetCartUrl() {
        Context context = Mockito.mock(Context.class);
        Store store = new Store(context, "hostport", "webroot", "userID", "janrainID");
        CartModel cartModel = new CartModel(store, null);
        assertEquals(cartModel.getTestUrl(RequestCode.GET_CART), NetworkConstants.getCurrentCartUrl);

    }

    @Test
    public void matchTestUrlWithHarcodedAddToCartUrl() {
        Context context = Mockito.mock(Context.class);
        Store store = new Store(context, "hostport", "webroot", "userID", "janrainID");
        CartModel cartModel = new CartModel(store, null);
        assertEquals(cartModel.getTestUrl(RequestCode.ADD_TO_CART), NetworkConstants.addToCartUrl);

    }

    @Test
    public void matchTestUrlWithHarcodedDeleteProductUrl() {
        Context context = Mockito.mock(Context.class);
        Store store = new Store(context, "hostport", "webroot", "userID", "janrainID");
        CartModel cartModel = new CartModel(store, null);
        assertEquals(cartModel.getTestUrl(RequestCode.DELETE_PRODUCT), NetworkConstants.deleteProductUrl);

    }

    @Test
    public void matchTestUrlWithHarcodedUpdateQuantityUrl() {
        Context context = Mockito.mock(Context.class);
        Store store = new Store(context, "hostport", "webroot", "userID", "janrainID");
        CartModel cartModel = new CartModel(store, null);
        assertEquals(cartModel.getTestUrl(RequestCode.UPDATE_PRODUCT_COUNT), NetworkConstants.updateQuantityUrl);

    }
}