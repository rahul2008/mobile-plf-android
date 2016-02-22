package com.philips.cdp.di.iap.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.philips.cdp.di.iap.Fragments.BaseAnimationSupportFragment;
import com.philips.cdp.di.iap.Fragments.ShippingAddressFragment;
import com.philips.cdp.di.iap.Fragments.ShoppingCartFragment;

import junit.framework.TestCase;

import org.mockito.Mockito;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BaseFragmentActivityTest extends TestCase {

    public void testSomething() {
        assertEquals(true, true);
    }

    public void testTrueIsFragmentAlreadyOnTop() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = Mockito.mock(BaseAnimationSupportFragment.class);
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        Fragment fragment = Mockito.mock(Fragment.class);
        baseFragmentActivity.isFragmentAlreadyOnTop(baseAnimationSupportFragment, fragment);
        assertNotNull("Top Fragm ent is Not Null", fragment);
        assertNotNull("Top Fragment is Not Null", baseAnimationSupportFragment);
        assertEquals(false, fragment.getClass().equals(baseAnimationSupportFragment.getClass()));
    }

    public void testTrueIfTopFragmentIsFromBaseAnimationSupportFragment() {
        ShoppingCartFragment baseAnimationSupportFragment = Mockito.mock(ShoppingCartFragment.class);
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        ShoppingCartFragment fragment = Mockito.mock(ShoppingCartFragment.class);
        baseFragmentActivity.isFragmentAlreadyOnTop(baseAnimationSupportFragment, fragment);
        assertEquals(true, fragment.getClass().equals(baseAnimationSupportFragment.getClass()));
    }

    public void testIsFragmentAlreadyOnTopFalseWhenBaseAnimationSupportFragmentIsNull() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = null;
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        Fragment fragment = Mockito.mock(Fragment.class);
        baseFragmentActivity.isFragmentAlreadyOnTop(baseAnimationSupportFragment, fragment);
        assertNotNull("Top Fragment is Not Null", fragment);
        assertNull("Top Fragment is Null", baseAnimationSupportFragment);
    }

    public void testIsFragmentAlreadyOnTopFalseWhenFragmentIsNull() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = Mockito.mock(BaseAnimationSupportFragment.class);
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        Fragment fragment = null;
        baseFragmentActivity.isFragmentAlreadyOnTop(baseAnimationSupportFragment, fragment);
        assertNull("Top Fragment is Null", fragment);
    }

    public void testIsFragmentAlreadyOnTopFalseWhenBothBaseSupportFragmentAndFragmentIsNull() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = null;
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        Fragment fragment = null;
        baseFragmentActivity.isFragmentAlreadyOnTop(baseAnimationSupportFragment, fragment);
        assertNull("Top Fragment is Null", fragment);
        assertNull("Top Fragment is Null", baseAnimationSupportFragment);
    }

    public void testGenerateFragmentTagWithHashCode() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = Mockito.mock(BaseAnimationSupportFragment.class);
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        baseFragmentActivity.generateFragmentTag(baseAnimationSupportFragment);
        assertNotNull(baseAnimationSupportFragment);
    }

    public void testNotGenerateFragmentTagWithHashCodeBecauseOfNull() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = null;
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        baseFragmentActivity.generateFragmentTag(baseAnimationSupportFragment);
        assertNull(baseAnimationSupportFragment);
    }

    public void testGetTopFragmentTag() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = Mockito.mock(BaseAnimationSupportFragment.class);
        Fragment fragment = Mockito.mock(Fragment.class);
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        baseFragmentActivity.getTopFragmentTag();
        assertEquals(fragment.getTag(), baseFragmentActivity.getTopFragmentTag());
    }

    public void test_generateFragmentTagMustStartWithFragmentPackageAndClassName() {
        BaseFragmentActivity activity = new BaseFragmentActivity();
        Bundle mockBundle = Mockito.mock(Bundle.class);
        BaseAnimationSupportFragment fragment = ShoppingCartFragment
                .createInstance(BaseAnimationSupportFragment.AnimationType.NONE);

        fragment.setArguments(mockBundle);
        String tag = activity.generateFragmentTag(fragment);

        assertTrue(tag.startsWith(fragment.getClass().getPackage() + "."
                + fragment.getClass().getSimpleName()));
    }

    public void test_generateFragmentTagMustBeUnique() {
        BaseFragmentActivity activity = new BaseFragmentActivity();
        Bundle mockBundle = Mockito.mock(Bundle.class);
        BaseAnimationSupportFragment fragment = ShoppingCartFragment
                .createInstance(BaseAnimationSupportFragment.AnimationType.NONE);
        BaseAnimationSupportFragment fragment2 = ShoppingCartFragment
                .createInstance(BaseAnimationSupportFragment.AnimationType.NONE);
        String tag = activity.generateFragmentTag(fragment);
        String tag2 = activity.generateFragmentTag(fragment2);

        assertFalse(tag.equals(tag2));
    }

    public void test_isFragmentAlreadyOnTopSameFragment() {
        BaseFragmentActivity activity = new BaseFragmentActivity();
//        Bundle mockBundle = Mockito.mock(Bundle.class);
        BaseAnimationSupportFragment newFragment = new ShoppingCartFragment();
        Fragment topFragment = ShoppingCartFragment.createInstance(BaseAnimationSupportFragment.AnimationType.NONE);

        boolean isFragmentOnTop = activity.isFragmentAlreadyOnTop(newFragment, topFragment);

        assertTrue(isFragmentOnTop);
    }

    public void test_isFragmentAlreadyOnTopDifferentFragment() {
        BaseFragmentActivity activity = new BaseFragmentActivity();
        BaseAnimationSupportFragment newFragment = new ShoppingCartFragment();
        Bundle mockBundle = Mockito.mock(Bundle.class);
        Fragment topFragment = ShippingAddressFragment.createInstance(BaseAnimationSupportFragment.AnimationType.NONE);

        boolean isFragmentOnTop = activity.isFragmentAlreadyOnTop(newFragment, topFragment);

        assertFalse(isFragmentOnTop);
    }

    public void test_isFragmentAlreadyOnTopNullTag() {
        BaseFragmentActivity activity = new BaseFragmentActivity();
        BaseAnimationSupportFragment newFragment = new ShoppingCartFragment();
        Fragment topFragment = null;

        boolean isFragmentOnTop = activity.isFragmentAlreadyOnTop(newFragment, topFragment);

        assertFalse(isFragmentOnTop);
    }

    public void test_isFragmentAlreadyOnTopNullFragment() {
        BaseFragmentActivity activity = new BaseFragmentActivity();
        BaseAnimationSupportFragment newFragment = null;
        //Bundle mockBundle = Mockito.mock(Bundle.class);
        Fragment topFragment = ShoppingCartFragment.createInstance(BaseAnimationSupportFragment.AnimationType.NONE);

        boolean isFragmentOnTop = activity.isFragmentAlreadyOnTop(newFragment, topFragment);

        assertFalse(isFragmentOnTop);
    }
}