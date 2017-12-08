/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.mocks;

import com.philips.cdp.di.iap.session.IAPHurlStack;
import com.philips.cdp.di.iap.session.OAuthListener;

public class MockIAPHurlStack extends IAPHurlStack {
    public MockIAPHurlStack(final OAuthListener oAuthHandler) {
        super(oAuthHandler);
    }
}
