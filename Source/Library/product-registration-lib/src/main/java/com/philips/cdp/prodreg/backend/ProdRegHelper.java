/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.prodreg.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.prodreg.handler.ProdRegListener;

/**
 * <b> Helper class used to process product registration backend calls</b>
 */
public class ProdRegHelper {

    private String locale;

    /**
     * <b> API to register product</b>
     *
     * @param context  - Context of an activity
     * @param product  - product
     * @param listener - Callback listener
     */
    public void registerProduct(final Context context, final Product product, final ProdRegListener listener) {
        UserProduct userProduct = getUserProduct(product);
        product.setLocale(locale);
        userProduct.setLocale(this.locale);
        userProduct.registerProduct(context, product, listener);
    }

    @NonNull
    UserProduct getUserProduct(final Product product) {
        return new UserProduct(product.getSector(), product.getCatalog());
    }

    public void setLocale(final String language, final String countryCode) {
        this.locale = language + "_" + countryCode;
    }
}
