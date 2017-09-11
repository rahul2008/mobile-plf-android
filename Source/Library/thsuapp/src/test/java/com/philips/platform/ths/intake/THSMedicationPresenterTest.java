/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import com.philips.platform.ths.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

public class THSMedicationPresenterTest {

    THSMedicationPresenter mTHSMedicationPresenter;

    @Mock
    THSMedicationFragment thsMedicationFragmentMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mTHSMedicationPresenter = new THSMedicationPresenter(thsMedicationFragmentMock);
    }

    @Test
    public void onEvent_ths_intake_medication_continue_button() throws Exception {
        mTHSMedicationPresenter.onEvent(R.id.ths_intake_medication_continue_button);
    }

    @Test
    public void fetchMedication() throws Exception {

    }

    @Test
    public void updateMedication() throws Exception {

    }

    @Test
    public void addSearchedMedication() throws Exception {

    }

    @Test
    public void onGetMedicationReceived() throws Exception {

    }

    @Test
    public void onUpdateMedicationSent() throws Exception {

    }

}