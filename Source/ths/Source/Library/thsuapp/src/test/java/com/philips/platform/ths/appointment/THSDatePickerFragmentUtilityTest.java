/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;

import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSDateEnum;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class THSDatePickerFragmentUtilityTest {

    THSDatePickerFragmentUtility mThsDatePickerFragmentUtility;

    @Mock
    THSBaseFragment thsBaseFragmentMock;

    @Mock
    DatePickerDialog.OnDateSetListener onDateSetListenerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(thsBaseFragmentMock);
        mThsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(thsBaseFragmentMock, THSDateEnum.DEFAULT);
    }

    @Test
    public void showDatePicker() throws Exception {
        assertNotNull(mThsDatePickerFragmentUtility);
    }

    @Test
    public void setCalendar() throws Exception {
        mThsDatePickerFragmentUtility.setCalendar(2015,2,3);
        assertNotNull(mThsDatePickerFragmentUtility.date);
    }

}