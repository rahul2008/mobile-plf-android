package com.philips.cdp.di.iap.Fragments;

import android.app.Activity;

import com.philips.cdp.di.iap.activity.MainActivity;

import junit.framework.TestCase;

import org.mockito.Mockito;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BaseAnimationSupportFragmentTest extends TestCase {
    public static final String FRAGMENT_TAG = "testTag";

    public void test_removeUnderlyingFragmentReturnFalseWhenNotMainActivity() {
        BaseAnimationSupportFragment fragment = new TestBaseAnimationSupportFragment();
        Activity parent = new Activity();

        boolean success = fragment.removeUnderlyingFragment(parent, FRAGMENT_TAG);
        assertFalse(success);
    }

    public void test_removeUnderlyingFragmentReturnTrueWhenMainActivity() {
        BaseAnimationSupportFragment fragment = new TestBaseAnimationSupportFragment();
        MainActivity parent = Mockito.mock(MainActivity.class);

        boolean success = fragment.removeUnderlyingFragment(parent, FRAGMENT_TAG);
        assertTrue(success);
    }

    public void test_removeUnderlyingFragmentReturnFalseWhenNullActivity() {
        BaseAnimationSupportFragment fragment = new TestBaseAnimationSupportFragment();
        MainActivity parent = null;

        boolean success = fragment.removeUnderlyingFragment(parent, FRAGMENT_TAG);
        assertFalse(success);
    }

    public void test_removeUnderlyingFragmentCallRemoveFragmentWhenMainActivity() {
        BaseAnimationSupportFragment fragment = new TestBaseAnimationSupportFragment();
        MainActivity parent = Mockito.mock(MainActivity.class);

        fragment.removeUnderlyingFragment(parent, FRAGMENT_TAG);
        Mockito.verify(parent, Mockito.times(1)).removeFragment(FRAGMENT_TAG);
    }

    public void test_removeUnderlyingFragmentDontCallRemoveFragmentWhenTagNull() {
        BaseAnimationSupportFragment fragment = new TestBaseAnimationSupportFragment();
        MainActivity parent = Mockito.mock(MainActivity.class);

        fragment.removeUnderlyingFragment(parent, null);
        Mockito.verify(parent, Mockito.never()).removeFragment(null);
    }

    public void test_removeUnderlyingFragmentDontCallRemoveFragmentWhenTagEmpty() {
        BaseAnimationSupportFragment fragment = new TestBaseAnimationSupportFragment();
        MainActivity parent = Mockito.mock(MainActivity.class);

        fragment.removeUnderlyingFragment(parent, "");
        Mockito.verify(parent, Mockito.never()).removeFragment("");
    }

    private class TestBaseAnimationSupportFragment extends BaseAnimationSupportFragment {

        @Override
        protected AnimationType getDefaultAnimationType() {
            return AnimationType.NONE;
        }

        @Override
        protected void updateTitle() {

        }
    }
}