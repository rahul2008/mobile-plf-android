/**
 * @author 310202701 on 9/16/2015.
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.localematch;

import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Locale change/countryfall back/ language fallback observer class.
 */
public class LocaleMatchHandlerObserver extends Observable {

    private static final String TAG = LocaleMatchHandlerObserver.class.getSimpleName();
    private static ArrayList<Observer> mObservers = null;

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);
        if (mObservers == null) mObservers = new ArrayList<Observer>();
        mObservers.add(o);
        DigiCareLogger.d(TAG, "addObserver");
    }

    public void notificationReceived() {
        if (mObservers != null) {
            setChanged();
            notifyObservers();

            synchronized (mObservers) {
                mObservers.notifyAll();
            }
        }
       /* DigiCareLogger.i(TAG, "******** Locale notified ****");*/
    }
}