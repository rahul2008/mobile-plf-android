/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.listener;

import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;
import com.philips.platform.uappframework.listener.uAppListener;

import java.util.List;

public interface ProdRegUiListener extends uAppListener {

    void onProdRegContinue(List<RegisteredProduct> registeredProducts, UserWithProducts userWithProduct);

    void onProdRegBack(List<RegisteredProduct> registeredProducts, UserWithProducts userWithProduct);
}
