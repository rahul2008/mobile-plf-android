/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.core;

import android.content.Context;

import com.philips.cdp.di.iap.controller.ControllerFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class ControllerFactoryTest {
    private Context mContext;

    @Before
    public void setUp() throws Exception {
        mContext = getInstrumentation().getContext();
    }

    @Test
    public void shouldDisplayCartIconReturnFalse() throws Exception {
        boolean shouldDisplay = !ControllerFactory.getInstance().isPlanB();
        Assert.assertFalse(shouldDisplay);
    }

    @Test
    public void createObjectForLocalShoppingCartPresenter() throws Exception {
        ControllerFactory.getInstance().init(true);
        Assert.assertNotNull(ControllerFactory.getInstance().getShoppingCartPresenter(mContext, null));
    }
    @Test
    public void createObjectForShoppingCartPresenter() throws Exception {
        ControllerFactory.getInstance().init(false);
        Assert.assertNotNull(ControllerFactory.getInstance().getShoppingCartPresenter(mContext, null));
    }

    @Test
    public void createObjectForLocalProductCatalog() throws Exception {
        ControllerFactory.getInstance().init(true);
        Assert.assertNotNull(ControllerFactory.getInstance().getProductCatalogPresenter(mContext, null));
    }

    @Test
    public void createObjectForProductCatalogPresenter() throws Exception {
        ControllerFactory.getInstance().init(false);
        Assert.assertNotNull(ControllerFactory.getInstance().getProductCatalogPresenter(mContext, null));
    }
}