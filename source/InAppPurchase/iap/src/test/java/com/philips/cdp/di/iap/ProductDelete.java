package com.philips.cdp.di.iap;

import android.content.Context;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartPresenter;
import com.philips.cdp.di.iap.model.CartModel;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.Store;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ProductDelete{
    @Test
    public void delete(){
            Context context = Mockito.mock(Context.class);
            ShoppingCartPresenter shoppingCartPresenter = new ShoppingCartPresenter(context,null);
            ArrayList<ShoppingCartData> mProductData = shoppingCartPresenter.getProductList();
            int numberOfEntriesBefore = mProductData.size();
            shoppingCartPresenter.deleteProduct(mProductData.get(0));
            int numberOfEntriesAfter = shoppingCartPresenter.getProductList().size();
            assertTrue(numberOfEntriesBefore == numberOfEntriesAfter-1);
    }
}