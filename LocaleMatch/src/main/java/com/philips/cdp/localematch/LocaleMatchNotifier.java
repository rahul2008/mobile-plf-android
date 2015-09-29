package com.philips.cdp.localematch;

import com.philips.cdp.localematch.enums.LocaleMatchError;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public final class LocaleMatchNotifier {

    private Set<LocaleMatchListener> mLocaleMatchListeners = Collections.newSetFromMap(new ConcurrentHashMap<LocaleMatchListener, Boolean>());

    private static LocaleMatchNotifier mNotifier = null;

    private LocaleMatchNotifier() {

    }

    public synchronized static LocaleMatchNotifier getIntance() {
        if (mNotifier == null) {
            mNotifier = new LocaleMatchNotifier();
        }
        return mNotifier;
    }

    public void registerForLocaleMatchChange(LocaleMatchListener listener) {
        synchronized (mLocaleMatchListeners) {
            mLocaleMatchListeners.add(listener);
        }
    }

    public void unRegisterLocaleMatchChange(LocaleMatchListener listener) {
        synchronized (mLocaleMatchListeners) {
            if (mLocaleMatchListeners.contains(listener)) {
                mLocaleMatchListeners.remove(listener);
            }
        }
    }

    public void notifyLocaleMatchChange(String locale) {
        synchronized (mLocaleMatchListeners) {
            for (Iterator<LocaleMatchListener> iterator = mLocaleMatchListeners.iterator(); iterator.hasNext(); ) {
                Object iteratorObj = iterator.next();
                if (iteratorObj != null) {
                    LocaleMatchListener listener = (LocaleMatchListener) iteratorObj;
                    listener.onLocaleMatchRefreshed(locale);
                    iterator.remove();
                }

            }
        }
    }

    public void notifyLocaleMatchChangeForSingleListener(LocaleMatchListener listener, String locale) {
        listener.onLocaleMatchRefreshed(locale);
    }

    public void notifyLocaleMatchError(LocaleMatchError error) {
        synchronized (mLocaleMatchListeners) {
            for (Iterator<LocaleMatchListener> iterator = mLocaleMatchListeners.iterator(); iterator.hasNext(); ) {
                Object iteratorObj = iterator.next();
                if (iteratorObj != null) {
                    LocaleMatchListener listener = (LocaleMatchListener) iteratorObj;
                    listener.onErrorOccurredForLocaleMatch(error);
                    iterator.remove();
                }

            }
        }
    }

}
