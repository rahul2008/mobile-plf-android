package com.philips.cdp.digitalcare.localematch;

import com.philips.cdp.digitalcare.util.DigiCareLogger;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * Created by 310202701 on 9/16/2015.
 */
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
        DigiCareLogger.d(TAG, "setValue called");
    }
}