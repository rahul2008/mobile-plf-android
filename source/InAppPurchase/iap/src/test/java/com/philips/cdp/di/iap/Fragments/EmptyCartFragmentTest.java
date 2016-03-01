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
public class EmptyCartFragmentTest extends TestCase {
    @Test
    public void testDoNotCallSetTitleOrLeftMenuInOnCreate() {
        Bundle mockBundle = Mockito.mock(Bundle.class);
        EmptyCartFragment fragment = EmptyCartFragment
                .createInstance(mockBundle, BaseAnimationSupportFragment.AnimationType.NONE);

        EmptyCartFragment spyFragment = Mockito.spy(fragment);
        IAPActivity mockActivity = Mockito.mock(IAPActivity.class);

        spyFragment.onCreate(null);

        Mockito.verify(mockActivity, Mockito.never()).setHeaderTitle(Matchers.anyInt());
    }

    @Test
    public void testCallSetTitleAndLeftMenuInOnResume() {
        Bundle mockBundle = Mockito.mock(Bundle.class);
        EmptyCartFragment fragment = EmptyCartFragment
                .createInstance(mockBundle, BaseAnimationSupportFragment.AnimationType.NONE);

        IAPActivity mockActivity = Mockito.mock(IAPActivity.class);

        fragment.onResume();

        Mockito.verify(mockActivity, Mockito.never()).setHeaderTitle(Matchers.anyInt());
    }
}