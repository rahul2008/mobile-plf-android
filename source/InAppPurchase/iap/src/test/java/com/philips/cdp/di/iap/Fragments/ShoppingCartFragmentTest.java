package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;

import com.philips.cdp.di.iap.activity.MainActivity;

import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShoppingCartFragmentTest extends TestCase {

    @Test
    public void testDoNotCallSetTitleOrLeftMenuInOnCreate() {
        Bundle mockBundle = Mockito.mock(Bundle.class);
        ShoppingCartFragment fragment = ShoppingCartFragment
                .createInstance(mockBundle, BaseAnimationSupportFragment.AnimationType.NONE);

        ShoppingCartFragment spyFragment = Mockito.spy(fragment);
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        Mockito.when(spyFragment.getMainActivity()).thenReturn(mockActivity);

        spyFragment.onCreate(null);

        Mockito.verify(mockActivity, Mockito.never()).setHeaderTitle(Matchers.anyString());
        Mockito.verify(mockActivity, Mockito.never()).setHeaderTitle(Matchers.anyInt());
    }

    @Test
    public void testCallSetTitleAndLeftMenuInOnResume() {
        Bundle mockBundle = Mockito.mock(Bundle.class);
        ShoppingCartFragment fragment = ShoppingCartFragment
                .createInstance(mockBundle, BaseAnimationSupportFragment.AnimationType.NONE);

        ShoppingCartFragment spyFragment = Mockito.spy(fragment);
        MainActivity mockActivity = Mockito.mock(MainActivity.class);
        Mockito.when(spyFragment.getMainActivity()).thenReturn(mockActivity);

        spyFragment.updateTitle();

        Mockito.verify(mockActivity, Mockito.never()).setHeaderTitle(Matchers.anyString());
        Mockito.verify(mockActivity, Mockito.times(1)).setHeaderTitle(Matchers.anyInt());
    }
}