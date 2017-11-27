/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.sdkerrors;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class THSAnalyticTechnicalErrorTest {
    THSAnalyticTechnicalError mThsAnalyticTechnicalError;

    @Before
    public void setUp() throws Exception {
        mThsAnalyticTechnicalError = new THSAnalyticTechnicalError();
    }

    @Test
    public void assertError(){
        assertNotNull(mThsAnalyticTechnicalError);
    }
}