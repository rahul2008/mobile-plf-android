/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.onboardingtour;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class OnBoardingTourPagerAdapterTest {

    OnBoardingTourPagerAdapter mOnBoardingTourPagerAdapter;

    @Mock
    FragmentManager fragmentManagerMock;

    @Mock
    OnBoardingTourContentModel onBoardingTourContentModel;

    @Mock
    Context contextMock;

    ArrayList list;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        list = new ArrayList();
        list.add(onBoardingTourContentModel);
        mOnBoardingTourPagerAdapter = new OnBoardingTourPagerAdapter(fragmentManagerMock,list,contextMock);
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
        assertNull(pageTitle);
    }

    @Test
    public void isValidPosition(){
        final boolean validPosition = mOnBoardingTourPagerAdapter.isValidPosition(0);
        assert validPosition == true;
    }

}