package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
public interface ProdRegListener {

    void onProdRegSuccess(RegisteredProduct registeredProduct, UserWithProducts userWithProduct);

    void onProdRegFailed(RegisteredProduct registeredProduct, UserWithProducts userWithProduct);
}
