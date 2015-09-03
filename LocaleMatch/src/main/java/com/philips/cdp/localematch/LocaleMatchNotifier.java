package com.philips.cdp.localematch;

import com.philips.cdp.localematch.enums.LocaleMatchError;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class LocaleMatchNotifier {

    private Set<LocaleMatchListener> mLocaleMatchListeners = Collections.synchronizedSet(new HashSet<LocaleMatchListener>());

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
            Iterator localeMatchListenerIterator = mLocaleMatchListeners.iterator();
            while (localeMatchListenerIterator.hasNext()) {
                Object iteratorObj = localeMatchListenerIterator.next();
                if (iteratorObj != null) {
                    LocaleMatchListener listener = (LocaleMatchListener) iteratorObj;
                    listener.onLocaleMatchRefreshed(locale);
                    localeMatchListenerIterator.remove();
                }
            }
        }
    }

    public void notifyLocaleMatchChangeForSingleListener(LocaleMatchListener listener, String locale) {
        listener.onLocaleMatchRefreshed(locale);
    }

    public void notifyLocaleMatchError(LocaleMatchError error) {
        synchronized (mLocaleMatchListeners) {
            Iterator localeMatchListenerIterator = mLocaleMatchListeners.iterator();
            while (localeMatchListenerIterator.hasNext()) {
                Object iteratorObj = localeMatchListenerIterator.next();
                if (iteratorObj != null) {
                    LocaleMatchListener listener = (LocaleMatchListener) iteratorObj;
                    listener.onErrorOccurredForLocaleMatch(error);
                    localeMatchListenerIterator.remove();
                }
            }
        }
    }

}
