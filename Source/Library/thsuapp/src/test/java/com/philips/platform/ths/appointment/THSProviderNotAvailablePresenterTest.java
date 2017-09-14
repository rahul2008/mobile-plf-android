/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import com.philips.platform.ths.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class THSProviderNotAvailablePresenterTest {
    THSProviderNotAvailablePresenter mTHSProviderNotAvailablePresenter;

    @Mock
    THSProviderNotAvailableFragment thsProviderNotAvailableFragmentMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mTHSProviderNotAvailablePresenter = new THSProviderNotAvailablePresenter(thsProviderNotAvailableFragmentMock);
    }

    @Test(expected = NullPointerException.class)
    public void onEvent() throws Exception {
        mTHSProviderNotAvailablePresenter.onEvent(R.id.calendar_view);
    }

}