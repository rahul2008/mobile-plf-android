package com.philips.cdp.di.iap.model.container;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.utils.IAPLog;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ModelContainer {
    private static ModelContainer container;

    public static ModelContainer getInstance() {
        //	if (container == null) {
        synchronized (ModelContainer.class) {
            if (container == null) {
                container = new ModelContainer();
            }
        }
        //	}
        return container;
    }

    private CartContainer cartContainer;

    private ModelContainer() {
        synchronized (ModelContainer.class) {
            cartContainer = new CartContainer();
        }
    }

    public CartContainer getCartContainer() {
        return cartContainer;
    }

    public void addCartItems(ShoppingCartData productData) {
        List<ShoppingCartData> cartProductList = cartContainer.getCartProductList();
        if (null != cartProductList) {
            removeFromCartList(productData);
            cartProductList.add(0, productData);
            ModelContainer.getInstance().getCartContainer().setCartProductList(cartProductList);
            IAPLog.i(IAPLog.LOG, "Item Added in Cart=" + cartProductList);
            // cartContainer.updateToCartContainerFile(context);
        }
    }

    private void removeFromCartList(ShoppingCartData pProduct) {
        List<ShoppingCartData> cartList = cartContainer.getCartProductList();
        if (null != cartList) {
            for (int i = 0; i < cartList.size(); i++) {
                ShoppingCartData product = cartList.get(i);
                cartList.remove(cartList.indexOf(product));
                IAPLog.i(IAPLog.LOG, "Item Removed from Cart is =" + product.getCtnNumber());

//                if (pProduct.equals(product)) {
//                    if (pProduct.checkForSeekbarvalues(product)) {
//                        int index = recentList.indexOf(product);
//                        if (index != -1) {
//                            recentList.remove(index);
//                        }
//                    }
//                }
            }
        }
    }
}
