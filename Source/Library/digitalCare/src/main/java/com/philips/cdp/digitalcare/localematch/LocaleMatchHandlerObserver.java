/**
 * @author  310202701 on 9/16/2015.
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.localematch;

import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class LocaleMatchHandlerObserver extends Observable {

    private static final String TAG = LocaleMatchHandlerObserver.class.getSimpleName();
    private static ArrayList<Observer> mObservers = new ArrayList<Observer>();

    @Override
    public void addObserver(Observer o) {
        super.addObserver(o);

        mObservers.add(o);
        DigiCareLogger.d(TAG, "addObserver");
    }

    public void notificationReceived() {
        setChanged();
        notifyObservers();
        synchronized(mObservers){
            mObservers.notify();
        }
       /* DigiCareLogger.i(TAG, "******** Locale notified ****");*/
    }
}