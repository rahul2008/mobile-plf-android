package com.philips.platform.modularui.cocointerface;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;

/**
 * Created by 310240027 on 6/22/2016.
 */
public interface UICoCoInterface {
    void loadPlugIn(Context context);

    void runCoCo(Context context);

    void unloadCoCo();

    void setActionbar(ActionbarUpdateListener actionBarClickListener);
    void setFragActivity(FragmentActivity fa);
}
