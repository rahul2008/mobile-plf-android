package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.TestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MockVerticalAppConfig extends VerticalAppConfig {
    MockVerticalAppConfig(final Context context) {
        super(context);
    }

    @Override
    public InputStream readJSONInputStream(final Context context) throws IOException {
        String config = TestUtils.readFile(this.getClass(),"PhilipsInAppPurchaseConfiguration.json");
        InputStream is = new ByteArrayInputStream(config.getBytes());
        return is;
    }
}
