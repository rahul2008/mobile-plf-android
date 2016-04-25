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
    private Context context;
    private ProdRegListener listener;

    public void init(Context context) {
        this.context = context;
    }

    /**
     * <b> API to register product</b>
     *
     * @param product  - product
     */
    public void registerProduct(final Product product) {
        UserProduct userProduct = getUserProduct(context);
        product.setLocale(locale);
        userProduct.setLocale(this.locale);
        userProduct.registerProduct(product, listener);
    }

    @NonNull
    UserProduct getUserProduct(final Context context) {
        return new UserProduct(context);
    }

    public void setLocale(final String language, final String countryCode) {
        this.locale = language + "_" + countryCode;
    }

    public String getLocale() {
        return locale;
    }

    public void setProductRegistrationListener(final ProdRegListener listener) {
        this.listener = listener;
    }
}
