/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;

/**
 * It is used to give call back to proposition when Product registration status .
 */
public interface ProdRegListener {

    /**
     * It is used to give call back to proposition when Product registration succeed .
     * @param registeredProduct - RegisteredProduct registeredProduct
     * @param userWithProduct - UserWithProducts userWithProduct
     *
     * @since 1.0.0
     */
    void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProduct);

    /**
     * It is used to give call back to proposition when Product registration fails .
     * @param registeredProduct - RegisteredProduct registeredProduct
     * @param userWithProduct - UserWithProducts userWithProduct
     *
     * @since 1.0.0
     */
    void onProdRegFailed(RegisteredProduct registeredProduct, UserWithProducts userWithProduct);
}
