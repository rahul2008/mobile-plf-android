package com.philips.multiproduct.listeners;


import android.content.Context;

import java.util.Observable;


/**
 * This listener is responsible to update selected details on the screen.
 *
 * @author ritesh.jha@philips.com
 * @Date 16 Feb 2016
 */
public class ProductListDetailsTabletListener extends Observable {
    private Context mContext = null;

    public ProductListDetailsTabletListener(Context context){
        mContext = context;
    }
    public void notifyAllProductScreens(){
        setChanged();
        notifyObservers();
    }
}
