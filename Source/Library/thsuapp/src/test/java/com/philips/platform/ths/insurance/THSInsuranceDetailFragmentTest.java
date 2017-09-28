package com.philips.platform.ths.insurance;

import com.americanwell.sdk.AWSDK;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
public class THSInsuranceDetailFragmentTest {
    THSInsuranceDetailFragmentMock thsInsuranceDetailFragment;


    @Mock
    AWSDK awsdkMock;


    @Before
    public void setUp() throws Exception {
        THSManager.getInstance().setAwsdk(awsdkMock);


    }

    @Test
    public void onCreateView() throws Exception {

    }

    @Test
    public void onActivityCreated() throws Exception {

    }

    @Test
    public void showProgressbar() throws Exception {

    }

    @Test
    public void onResume() throws Exception {

    }

    @Test
    public void onClick() throws Exception {

    }

    @Test
    public void updateInsuranceUI() throws Exception {

    }

    @Test
    public void showDatePicker() throws Exception {

    }

}