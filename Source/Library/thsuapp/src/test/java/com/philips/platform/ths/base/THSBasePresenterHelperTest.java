/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.base;

import android.os.Bundle;

import com.americanwell.sdk.entity.practice.Practice;
import com.philips.platform.ths.providerdetails.THSProviderEntity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

public class THSBasePresenterHelperTest {
    THSBasePresenterHelper mTHSBasePresenterHelper;

    @Mock
    THSBaseFragment thsBaseFragmentMock;

    @Mock
    THSProviderEntity thsProviderEntityMock;

    @Mock
    Practice practiceMock;

    @Mock
    Date dateMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mTHSBasePresenterHelper = new THSBasePresenterHelper();
    }

    @Test
    public void launchAvailableProviderDetailFragment() throws Exception {
        mTHSBasePresenterHelper.launchAvailableProviderDetailFragment(thsBaseFragmentMock,thsProviderEntityMock,dateMock,practiceMock);
        verify(thsBaseFragmentMock, atLeast(1)).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class), anyBoolean());
    }

}