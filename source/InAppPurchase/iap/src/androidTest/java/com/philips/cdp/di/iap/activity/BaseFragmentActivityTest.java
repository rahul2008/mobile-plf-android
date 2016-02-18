package com.philips.cdp.di.iap.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.test.InstrumentationTestCase;

import com.philips.cdp.di.iap.Fragments.BaseAnimationSupportFragment;
import com.philips.cdp.di.iap.R;

import org.mockito.Mockito;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class BaseFragmentActivityTest extends InstrumentationTestCase {

    public void testGetTopFragment() throws Exception {
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        BaseFragmentActivity spyBaseFragmentActivity = Mockito.spy(baseFragmentActivity);
        baseFragmentActivity.getTopFragment();
        Mockito.verify(spyBaseFragmentActivity, Mockito.atLeast(1)).getSupportFragmentManager().findFragmentById(R.id.fl_mainFragmentContainer);
    }

    public void testTrueIsFragmentAlreadyOnTop() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = Mockito.mock(BaseAnimationSupportFragment.class);
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        Fragment fragment = Mockito.mock(Fragment.class);
        baseFragmentActivity.isFragmentAlreadyOnTop(baseAnimationSupportFragment, fragment);
        assertNotNull("Top Fragment is Not Null", fragment);
        assertNotNull("Top Fragment is Not Null", baseAnimationSupportFragment);
        assertEquals(true, baseAnimationSupportFragment.getClass().equals(fragment.getClass()));
    }

    public void testIsFragmentAlreadyOnTopFalseWhenBaseAnimationSupportFragmentIsNull() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = null;
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        Fragment fragment = Mockito.mock(Fragment.class);
        baseFragmentActivity.isFragmentAlreadyOnTop(baseAnimationSupportFragment, fragment);
        assertNotNull("Top Fragment is Not Null", fragment);
        assertNull("Top Fragment is Null", baseAnimationSupportFragment);
        assertEquals(false, baseAnimationSupportFragment.getClass().equals(fragment.getClass()));
    }

    public void testIsFragmentAlreadyOnTopFalseWhenFragmentIsNull() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = Mockito.mock(BaseAnimationSupportFragment.class);
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        Fragment fragment = null;
        baseFragmentActivity.isFragmentAlreadyOnTop(baseAnimationSupportFragment, fragment);
        assertNull("Top Fragment is Null", fragment);
        assertNull("Top Fragment is Not Null", baseAnimationSupportFragment);
        assertEquals(false, baseAnimationSupportFragment.getClass().equals(fragment.getClass()));
    }

    public void testIsFragmentAlreadyOnTopFalseWhenBothBaseSupportFragmentAndFragmentIsNull() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = null;
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        Fragment fragment = null;
        baseFragmentActivity.isFragmentAlreadyOnTop(baseAnimationSupportFragment, fragment);
        assertNull("Top Fragment is Null", fragment);
        assertNull("Top Fragment is Null", baseAnimationSupportFragment);
        assertEquals(false, baseAnimationSupportFragment.getClass().equals(fragment.getClass()));
    }

    public void testGenerateFragmentTagWithHashCode() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = Mockito.mock(BaseAnimationSupportFragment.class);
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        baseFragmentActivity.generateFragmentTag(baseAnimationSupportFragment);
        assertNotNull(baseAnimationSupportFragment);
        assertEquals("Fragment tag generated with HashCode", baseAnimationSupportFragment.getClass().getPackage() + "." + baseAnimationSupportFragment.getClass().getSimpleName()
                + baseAnimationSupportFragment.hashCode());
        Mockito.verify(baseAnimationSupportFragment, Mockito.atLeast(1)).hashCode();
    }

    public void testNotGenerateFragmentTagWithHashCodeBecauseOfNull() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = null;
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        baseFragmentActivity.generateFragmentTag(baseAnimationSupportFragment);
        assertNull(baseAnimationSupportFragment);
        assertEquals("Fragment tag not generated with HashCode", baseAnimationSupportFragment.getClass().getPackage() + "." + baseAnimationSupportFragment.getClass().getSimpleName()
                + baseAnimationSupportFragment.hashCode());
        Mockito.verify(baseAnimationSupportFragment, Mockito.atLeast(1)).hashCode();
    }

    public void testGetTopFragmentTag() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = Mockito.mock(BaseAnimationSupportFragment.class);
        Fragment fragment = Mockito.mock(Fragment.class);
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        baseFragmentActivity.getTopFragmentTag();
        Mockito.verify(fragment, Mockito.atLeast(1)).getTag();
    }

    public void testRemoveFragment() {
        Fragment fragment = Mockito.mock(Fragment.class);
        FragmentTransaction transaction = Mockito.mock(FragmentTransaction.class);
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        Mockito.when(fragment).thenReturn(baseFragmentActivity.getSupportFragmentManager().findFragmentByTag("Fragment"));
        Mockito.when(transaction).thenReturn(baseFragmentActivity.getSupportFragmentManager().beginTransaction());

        baseFragmentActivity.removeFragment("Fragment");
        Mockito.verify(transaction, Mockito.atLeast(1)).remove(fragment);
        Mockito.verify(transaction, Mockito.atLeast(1)).commitAllowingStateLoss();
    }

    public void testRemoveFragmentIfFragmentIsNull() {
        Fragment fragment = null;
        FragmentTransaction transaction = Mockito.mock(FragmentTransaction.class);
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        Mockito.when(fragment).thenReturn(baseFragmentActivity.getSupportFragmentManager().findFragmentByTag("Fragment"));
        Mockito.when(transaction).thenReturn(baseFragmentActivity.getSupportFragmentManager().beginTransaction());

        baseFragmentActivity.removeFragment("Fragment");
        assertNull("No fragment found to remove (\" + tag + \")\"");
    }

    public void testaddFragmentAndRemoveUnderneathWhenNotSameFragment() {
        BaseAnimationSupportFragment baseAnimationSupportFragment = Mockito.mock(BaseAnimationSupportFragment.class);
        FragmentTransaction transaction = Mockito.mock(FragmentTransaction.class);
        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
        boolean addOnTopOfSameFragment = false;
        String topFragmentTag = "Fragment";
        baseAnimationSupportFragment.setUnderlyingFragmentTag(topFragmentTag);
        String newFragmentTag = baseFragmentActivity.generateFragmentTag(baseAnimationSupportFragment);
        baseFragmentActivity.addFragmentAndRemoveUnderneath(baseAnimationSupportFragment, addOnTopOfSameFragment);
        Mockito.when(transaction).thenReturn(baseFragmentActivity.getSupportFragmentManager().beginTransaction());
        Mockito.verify(transaction, Mockito.atLeast(1)).add(R.id.fl_mainFragmentContainer, baseAnimationSupportFragment, newFragmentTag);
        Mockito.verify(transaction, Mockito.atLeast(1)).commitAllowingStateLoss();
    }

//    public void testaddFragmentAndRemoveUnderneathWhenSameFragment() {
//        BaseAnimationSupportFragment baseAnimationSupportFragment = Mockito.mock(BaseAnimationSupportFragment.class);
//        FragmentTransaction transaction = Mockito.mock(FragmentTransaction.class);
//        BaseFragmentActivity baseFragmentActivity = new BaseFragmentActivity();
//        boolean addOnTopOfSameFragment = true;
//        String topFragmentTag = "Fragment";
//        baseAnimationSupportFragment.setUnderlyingFragmentTag(topFragmentTag);
//        String newFragmentTag = baseFragmentActivity.generateFragmentTag(baseAnimationSupportFragment);
//        baseFragmentActivity.addFragmentAndRemoveUnderneath(baseAnimationSupportFragment, addOnTopOfSameFragment);
//    }
}