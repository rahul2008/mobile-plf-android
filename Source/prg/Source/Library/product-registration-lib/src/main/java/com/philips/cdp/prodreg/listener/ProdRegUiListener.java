/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;

import java.util.List;

/**
 * It is used to give call back to proposition when continue or back button is called and also when Product registration fails .
 */
public interface ProdRegUiListener {

    /**
     * API used to give call back to proposition when continue button is clicked
     * @param registeredProducts - List<RegisteredProduct> registeredProducts
     * @param userWithProduct - UserWithProducts userWithProduct
     *                        @since 1.0.0
     */
    void onProdRegContinue(List<RegisteredProduct> registeredProducts, UserWithProducts userWithProduct);

    /**
     * API used to give call back to proposition when back button is clicked
     * @param registeredProducts - List<RegisteredProduct> registeredProducts
     * @param userWithProduct - UserWithProducts userWithProduct
     *                        @since 1.0.0
     */
    void onProdRegBack(List<RegisteredProduct> registeredProducts, UserWithProducts userWithProduct);

    /**
     * API used to give call back to proposition when Product registration fails .
     * @param prodRegError - ProdRegError prodRegError
     *                     @since 1.0.0
     */
    void onProdRegFailed(ProdRegError prodRegError);
}
