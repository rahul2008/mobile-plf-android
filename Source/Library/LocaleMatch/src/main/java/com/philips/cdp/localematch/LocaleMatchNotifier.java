package com.philips.cdp.localematch;

import com.philips.cdp.localematch.enums.LocaleMatchError;

public final class LocaleMatchNotifier {

    private LocaleMatchListener mLocaleMatchListener = null;

    public LocaleMatchNotifier(LocaleMatchListener localeMatchListener) {
        mLocaleMatchListener = localeMatchListener;
    }

    public void notifyLocaleMatchSuccess(String locale) {
        mLocaleMatchListener.onLocaleMatchRefreshed(locale);
    }

    public void notifyLocaleMatchError(LocaleMatchError error) {
        mLocaleMatchListener.onErrorOccurredForLocaleMatch(error);
    }

}
