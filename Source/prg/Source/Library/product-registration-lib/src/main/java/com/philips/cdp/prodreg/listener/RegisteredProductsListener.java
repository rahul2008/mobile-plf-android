/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.register.RegisteredProduct;

import java.util.List;

/**
 * It is used to give back registered products with timeStamp to proposition
 */
public interface RegisteredProductsListener {

    /**
     * It is used to give back registered products with timeStamp to proposition
     * @param registeredProducts - List<RegisteredProduct> registeredProducts
     * @param timeStamp - long timeStamp
     *                  @since 1.0.0
     */
    void getRegisteredProducts(List<RegisteredProduct> registeredProducts, long timeStamp);
}
