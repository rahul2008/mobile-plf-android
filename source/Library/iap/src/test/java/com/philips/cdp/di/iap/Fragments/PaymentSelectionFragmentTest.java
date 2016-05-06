package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;

import com.philips.cdp.di.iap.activity.IAPActivity;

import junit.framework.TestCase;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

public class PaymentSelectionFragmentTest extends TestCase {
    @Test
    public void testDoNotCallSetTitleOrLeftMenuInOnCreate() {
        Bundle mockBundle = Mockito.mock(Bundle.class);
        PaymentSelectionFragment fragment = PaymentSelectionFragment
                .createInstance(mockBundle, BaseAnimationSupportFragment.AnimationType.NONE);

        PaymentSelectionFragment spyFragment = Mockito.spy(fragment);
        IAPActivity mockActivity = Mockito.mock(IAPActivity.class);

        spyFragment.onCreate(null);

        Mockito.verify(mockActivity, Mockito.never()).setHeaderTitle(Matchers.anyInt());
    }
}