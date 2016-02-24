package com.philips.cdp.productselection.listeners;


import android.content.Context;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;


/**
 * This listener is responsible to update selected details on the screen.
 *
 * @author ritesh.jha@philips.com
 * @Date 16 Feb 2016
 */
public class ProductListDetailsTabletListener extends Observable {
    private Context mContext = null;
    protected Set<Observer> mObservers;

    public ProductListDetailsTabletListener(Context context) {
        mContext = context;
        mObservers = new HashSet<Observer>();
    }

    @Override
    public void addObserver(Observer o) {
        mObservers.add(o);
        super.addObserver(o);
    }

    @Override
    public synchronized void deleteObserver(Observer o) {
        mObservers.remove(o);
        super.deleteObserver(o);
    }

    @Override
    public void notifyObservers() {
        setChanged();

        Iterator iterator = mObservers.iterator();

        // check values
        while (iterator.hasNext()) {
            synchronized (iterator.next()) {
                super.notifyObservers();
            }
        }
    }
}
