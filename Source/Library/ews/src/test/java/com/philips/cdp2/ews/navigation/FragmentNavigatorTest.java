package com.philips.cdp2.ews.navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FragmentNavigatorTest {

    @InjectMocks
    private FragmentNavigator subject;

    @Mock
    private FragmentManager mockFragmentManager;
    @Mock
    private FragmentTransaction mockFragmentTransaction;

    Fragment mockFragment;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockFragment = new Fragment();
    }

    @Test
    public void itShouldReplaceFragmentAndAddToBackStackWhenPushed() throws Exception {
        int containerId = 50;

        when(mockFragmentManager.beginTransaction()).thenReturn(mockFragmentTransaction);
        when(mockFragmentTransaction.replace(anyInt(), any(Fragment.class))).thenReturn(mockFragmentTransaction);
        when(mockFragmentTransaction.addToBackStack(anyString())).thenReturn(mockFragmentTransaction);

        subject.push(mockFragment, containerId);

        InOrder inOrder = inOrder(mockFragmentManager, mockFragmentTransaction);

        inOrder.verify(mockFragmentManager).beginTransaction();
        inOrder.verify(mockFragmentTransaction).replace(containerId, mockFragment);
        inOrder.verify(mockFragmentTransaction).addToBackStack("android.support.v4.app.Fragment");
        inOrder.verify(mockFragmentTransaction).commit();
    }

    @Test
    public void itShouldPopFragmentImmediate() throws Exception {
        String anyString = "anyString";
        when(mockFragmentManager.findFragmentByTag(anyString)).thenReturn(mockFragment);
        subject.popToFragment(anyString);
        verify(mockFragmentManager).findFragmentByTag(anyString);
        verify(mockFragmentManager).popBackStackImmediate(anyString, 0);
    }

    @Test
    public void itShouldPopImmediate() throws Exception {
        subject.pop();

        verify(mockFragmentManager).popBackStackImmediate();
    }
}