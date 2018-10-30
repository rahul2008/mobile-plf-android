/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.onboardingtour;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class OnBoardingTourPagerAdapterTest {

    OnBoardingTourPagerAdapter mOnBoardingTourPagerAdapter;

    @Mock
    FragmentManager fragmentManagerMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    OnBoardingTourContentModel onBoardingTourContentModel;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    Context contextMock;

    ArrayList list;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        list = new ArrayList();
        list.add(onBoardingTourContentModel);
        mOnBoardingTourPagerAdapter = new OnBoardingTourPagerAdapter(fragmentManagerMock,list, fragmentLauncherMock, actionBarListenerMock);
        when(onBoardingTourContentModel.getPageTitle()).thenReturn("ssss");
    }

    @Test
    public void getItem() throws Exception {
        final Fragment item = mOnBoardingTourPagerAdapter.getItem(0);
        assertNotNull(item);
    }

    @Test
    public void getCount() throws Exception {
        final int count = mOnBoardingTourPagerAdapter.getCount();
        assert count == 1;
    }

    @Test
    public void getPageTitle() throws Exception {
        final String pageTitle = mOnBoardingTourPagerAdapter.getPageTitle(0);
        assertNotNull(pageTitle);
    }
}