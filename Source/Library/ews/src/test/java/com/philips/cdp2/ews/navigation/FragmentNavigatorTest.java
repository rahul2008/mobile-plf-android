package com.philips.cdp2.ews.navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FragmentNavigatorTest {

    @Mock
    private FragmentManager mockFragmentManager;
    @Mock
    private FragmentTransaction mockFragmentTransaction;

    private FragmentNavigator subject;
    Fragment mockFragment;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new FragmentNavigator(mockFragmentManager, 12345);
        mockFragment = new Fragment();
    }

    @Test
    public void itShouldReplaceFragmentAndAddToBackStackWhenPushed() throws Exception {
        int containerId = 50;


        when(mockFragmentManager.beginTransaction()).thenReturn(mockFragmentTransaction);
        when(mockFragmentTransaction.replace(anyInt(), any(Fragment.class))).thenReturn(mockFragmentTransaction);
        when(mockFragmentTransaction.addToBackStack(anyString())).thenReturn(mockFragmentTransaction);

        subject.push(mockFragment, containerId,false,false);

        InOrder inOrder = inOrder(mockFragmentManager, mockFragmentTransaction);

        inOrder.verify(mockFragmentManager).beginTransaction();
        inOrder.verify(mockFragmentTransaction).replace(containerId, mockFragment);
        inOrder.verify(mockFragmentTransaction).addToBackStack("android.support.v4.app.Fragment");
        inOrder.verify(mockFragmentTransaction).commit();
    }

    @Test
    public void itShouldBeCommitAllowingStateLoss() throws Exception {
        int containerId = 50;


        when(mockFragmentManager.beginTransaction()).thenReturn(mockFragmentTransaction);
        when(mockFragmentTransaction.replace(anyInt(), any(Fragment.class))).thenReturn(mockFragmentTransaction);
        when(mockFragmentTransaction.addToBackStack(anyString())).thenReturn(mockFragmentTransaction);

        subject.push(mockFragment, containerId,true,false);

        InOrder inOrder = inOrder(mockFragmentManager, mockFragmentTransaction);

        inOrder.verify(mockFragmentManager).beginTransaction();
        inOrder.verify(mockFragmentTransaction).replace(containerId, mockFragment);
        inOrder.verify(mockFragmentTransaction).addToBackStack("android.support.v4.app.Fragment");
        inOrder.verify(mockFragmentTransaction).commitAllowingStateLoss();
    }

    @Test
    public void itShouldCallPopBackStack() throws Exception {
        int containerId = 50;

        when(mockFragmentManager.popBackStackImmediate(anyString(), eq(subject.POP_BACK_STACK_EXCLUSIVE))).thenReturn(true);

        subject.push(mockFragment, containerId,true,true);

        verify(mockFragmentManager).popBackStackImmediate(anyString(), eq(subject.POP_BACK_STACK_EXCLUSIVE));

        InOrder inOrder = inOrder(mockFragmentManager, mockFragmentTransaction);

        inOrder.verify(mockFragmentManager,times(0)).beginTransaction();
        inOrder.verify(mockFragmentTransaction,times(0)).replace(containerId, mockFragment);
        inOrder.verify(mockFragmentTransaction,times(0)).addToBackStack("android.support.v4.app.Fragment");
        inOrder.verify(mockFragmentTransaction,times(0)).commitAllowingStateLoss();
    }

    @Test
    public void itShouldCallPopBackStackAndReplaceFragmentAndAddToBackStack() throws Exception {
        int containerId = 50;

        when(mockFragmentManager.beginTransaction()).thenReturn(mockFragmentTransaction);
        when(mockFragmentTransaction.replace(anyInt(), any(Fragment.class))).thenReturn(mockFragmentTransaction);
        when(mockFragmentTransaction.addToBackStack(anyString())).thenReturn(mockFragmentTransaction);

        subject.push(mockFragment, containerId,false,true);

        verify(mockFragmentManager).popBackStackImmediate(anyString(), eq(subject.POP_BACK_STACK_EXCLUSIVE));

        InOrder inOrder = inOrder(mockFragmentManager, mockFragmentTransaction);
        inOrder.verify(mockFragmentManager).beginTransaction();
        inOrder.verify(mockFragmentTransaction).replace(containerId, mockFragment);
        inOrder.verify(mockFragmentTransaction).addToBackStack("android.support.v4.app.Fragment");
        inOrder.verify(mockFragmentTransaction).commit();
    }

    @Test
    public void itShouldVerifyPopBackStackExclusiveWhenCalled() throws Exception {
        String anyString = "anyString";
        subject.popToFragment(anyString);
        verify(mockFragmentManager).popBackStackImmediate(anyString, subject.POP_BACK_STACK_EXCLUSIVE);
    }

    @Test
    public void itShouldVerifyPopBackStackImmediateWhenCalled() throws Exception {
        subject.pop();

        verify(mockFragmentManager).popBackStackImmediate();
    }

    @Test
    public void itShouldVerifyCorrectContainerIdForFragmentNavigator() throws Exception {
        assertEquals(12345, subject.getContainerId());
    }

    @Test
    public void itShouldFinishWhenCalled() throws Exception {
        when(mockFragmentManager.getBackStackEntryCount()).thenReturn(1);
        assertEquals(true, subject.shouldFinish());
    }
}