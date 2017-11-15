/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.mapper;

import com.philips.platform.catk.dto.GetConsentsModel;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DtoToConsentMapperTest {
    private Consent result;
    private DtoToConsentMapper givenMapper;
    private GetConsentsModel givenGetDto;
    private GetConsentsModel getActiveFromIndiaTypeMomentLocaleEnUsDto;

    @Before
    public void setUp() throws Exception {
        givenMapper = new DtoToConsentMapper();
        getActiveFromIndiaTypeMomentLocaleEnUsDto = new GetConsentsModel("2017-10-02", "en-US", "urn:com.philips.consent:moment/IN/1/someProposition/someApplication", "Consent", ConsentStatus.active, "someSubjectId");
    }

    @Test
    public void map_mapsCorrectly() {
        givenDto(getActiveFromIndiaTypeMomentLocaleEnUsDto);
        whenCallingMapWith();
        thenConsentIs(new Consent("en-US", ConsentStatus.active, "moment", 1));
    }

    private void givenDto(GetConsentsModel getDto) {
        this.givenGetDto = getDto;
    }

    private void whenCallingMapWith() {
        result = givenMapper.map(givenGetDto);
    }

    private void thenConsentIs(Consent expectedConsent) {
        assertEquals(expectedConsent, result);
    }
}