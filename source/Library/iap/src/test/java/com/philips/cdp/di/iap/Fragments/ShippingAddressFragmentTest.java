package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;

import com.philips.cdp.di.iap.activity.IAPActivity;

import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShippingAddressFragmentTest extends TestCase {
    @Test
    public void testDoNotCallSetTitleOrLeftMenuInOnCreate() {
        Bundle mockBundle = Mockito.mock(Bundle.class);
        ShippingAddressFragment fragment = ShippingAddressFragment
                .createInstance(mockBundle, BaseAnimationSupportFragment.AnimationType.NONE);

        ShippingAddressFragment spyFragment = Mockito.spy(fragment);
        IAPActivity mockActivity = Mockito.mock(IAPActivity.class);

        spyFragment.onCreate(null);

        Mockito.verify(mockActivity, Mockito.never()).setHeaderTitle(Matchers.anyInt());
    }
}