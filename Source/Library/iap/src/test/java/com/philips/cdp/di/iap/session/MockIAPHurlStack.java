/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.philips.cdp.di.iap.TestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MockIAPHurlStack extends IAPHurlStack {
    public MockIAPHurlStack(final OAuthHandler oAuthHandler) {
        super(oAuthHandler);
    }

    @Override
    InputStream readTestCertificate(final Context context) throws IOException {
        String config = TestUtils.readFile(this.getClass(),"test.crt");
        InputStream is = new ByteArrayInputStream(config.getBytes());
        return is;
    }
}
