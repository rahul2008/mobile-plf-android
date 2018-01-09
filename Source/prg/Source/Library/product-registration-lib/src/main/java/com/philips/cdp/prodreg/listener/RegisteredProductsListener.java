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
 * @since 1.0.0
 */
public interface RegisteredProductsListener {

    /**
     * It is used to give back registered products with timeStamp to proposition
     * @param registeredProducts -  get list of RegisteredProduct
     * @param timeStamp - get timeStamp information of registered product
     * @since 1.0.0
     */
    void getRegisteredProducts(List<RegisteredProduct> registeredProducts, long timeStamp);
}
